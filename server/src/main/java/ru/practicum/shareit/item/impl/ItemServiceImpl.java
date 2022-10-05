package ru.practicum.shareit.item.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.requests.RequestNotFoundException;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository,
                           UserService userService,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository,
                           ItemRequestRepository requestRepository) {
        this.repository = repository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        final Item item = ItemMapper.toItem(itemDto);
        final User user = UserMapper.toUser(userService.getUserById(userId));
        if (itemDto.getRequestId() != null) {
            final ItemRequest requestInDb = requestRepository
                    .findById(itemDto.getRequestId()).orElseThrow(() ->
                            new RequestNotFoundException(String
                                    .format("Request ID %d not found", itemDto.getRequestId())));
            item.setRequest(requestInDb);
        }
        item.setOwner(user);
        if (itemDto.getId() != null) {
            log.error("Item ID should be empty {}", item);
            throw new InvalidEntityException("Item ID should be empty");
        }
        final Item saveItemInDb = repository.save(item);
        log.info("Item {} saved", saveItemInDb);
        return ItemMapper.toItemDto(saveItemInDb);
    }

    @Override
    public ItemDto editItem(ItemDto itemDto, long itemId, long userId) {
        final Item itemInDb = repository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", itemId)));
        userService.getUserById(userId);
        if (!itemInDb.getOwner().getId().equals(userId)) {
            log.error("UserID not equal item owner");
            throw new UserNotFoundException("UserID should be item owner");
        }
        if (itemDto.getAvailable() != null) itemInDb.setAvailable(itemDto.getAvailable());
        if (itemDto.getName() != null) itemInDb.setName(itemDto.getName());
        if (itemDto.getDescription() != null) itemInDb.setDescription(itemDto.getDescription());
        final Item updateItemInDb = repository.save(itemInDb);
        log.info("Item {} updated", updateItemInDb);
        return ItemMapper.toItemDto(updateItemInDb);
    }

    @Override
    public ItemDtoExtended getItemById(long id, long userId) {
        userService.getUserById(userId);
        final Item item = repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", id)));
        final ItemDtoExtended dtoExtended = new ItemDtoExtended(ItemMapper.toItemDto(item));
        LocalDateTime now = LocalDateTime.now();
        if (item.getOwner().getId().equals(userId)) {
            List<Booking> bookings = bookingRepository.findByItemIdOrderByEndDesc(id);
            Booking lastBooking = bookings.stream()
                    .filter(booking -> booking.getEnd().isBefore(now))
                    .findFirst().orElse(null);
            Booking nextBooking = bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(now))
                    .findFirst().orElse(null);
            setBookingDates(lastBooking, nextBooking, dtoExtended);
        }
        List<Comment> comments = commentRepository.findAllByItemId(id);
        for (Comment comment : comments) {
            final UserDto userDto = userService.getUserById(comment.getAuthor().getId());
            comment.setAuthor(UserMapper.toUser(userDto));
        }
        dtoExtended.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
        return dtoExtended;
    }

    @Override
    public List<ItemDtoExtended> getAllOwnerItems(int fromPage,
                                                  int size, long ownerId) {
        userService.getUserById(ownerId);
        int page = fromPage / size;
        Sort sortByEnd = Sort.by(Sort.Direction.DESC, "end");
        Pageable pageable = PageRequest.of(page, size, sortByEnd);
        final List<ItemDtoExtended> itemDtoExtendedList = new ArrayList<>();
        final List<Item> items = repository.findItemsByOwnerId(ownerId,
                PageRequest.of(0, size, Sort.by(Sort.Direction.ASC, "id")));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> allBookings = bookingRepository.findByItemIdIn(items.stream()
                .map(Item::getId)
                .collect(Collectors.toList()), pageable);
        for (Item item : items) {
            Booking lastBooking = allBookings.stream()
                    .filter(booking -> booking.getEnd().isBefore(now)
                            && booking.getItem().getId().equals(item.getId()))
                    .findFirst().orElse(null);
            Booking nextBooking = allBookings.stream()
                    .filter(booking -> booking.getStart().isAfter(now)
                            && booking.getItem().getId().equals(item.getId()))
                    .findFirst().orElse(null);
            final ItemDtoExtended dtoExtended = new ItemDtoExtended(ItemMapper.toItemDto(item));

            setBookingDates(lastBooking, nextBooking, dtoExtended);

            List<Comment> comments = commentRepository.findAllByItemId(item.getId());
            for (Comment comment : comments) {
                final UserDto userDto = userService.getUserById(comment.getAuthor().getId());
                comment.setAuthor(UserMapper.toUser(userDto));
            }
            dtoExtended.setComments(comments.stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));
            itemDtoExtendedList.add(dtoExtended);
        }
        return itemDtoExtendedList;
    }

    private void setBookingDates(Booking lastBooking,
                                 Booking nextBooking,
                                 ItemDtoExtended dtoExtended) {
        if (lastBooking != null) {
            dtoExtended.setLastBooking(ItemDtoExtended.DateBooking.builder()
                    .id(lastBooking.getId())
                    .bookerId(lastBooking.getBooker().getId())
                    .start(lastBooking.getStart())
                    .end(lastBooking.getEnd())
                    .build());
        }
        if (nextBooking != null) {
            dtoExtended.setNextBooking(ItemDtoExtended.DateBooking.builder()
                    .id(nextBooking.getId())
                    .bookerId(nextBooking.getBooker().getId())
                    .start(nextBooking.getStart())
                    .end(nextBooking.getEnd())
                    .build());
        }
    }

    @Override
    public List<ItemDto> searchItemsForBooking(int fromPage,
                                               int size, String text) {
        if (text.isEmpty()) {
            log.info("Text is empty");
            return Collections.emptyList();
        }
        int page = fromPage / size;
        Pageable pageable = PageRequest.of(page, size);
        return repository.searchItemsForBooking(text, pageable).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long userId, long itemId,
                                 CommentCreationDto textDto) {
        final User user = UserMapper.toUser(userService.getUserById(userId));
        final Item item = repository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", itemId)));
        List<Booking> bookings = bookingRepository
                .findByItemIdAndBookerIdAndStatusAndEndIsBefore(itemId, userId,
                Status.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty()) {
            log.error("User {} can't leave comment to item {}", userId, itemId);
            throw new InvalidEntityException("User can't leave comment");
        }
        final Comment savedCommentInDb = commentRepository.save(
                new Comment(textDto.getText(), item, user));
        log.info("Comment {} saved", savedCommentInDb);
        return CommentMapper.toCommentDto(savedCommentInDb);
    }

    @Override
    public List<CommentDto> searchCommentsForItem(long itemId, String text) {
        if (text.isEmpty()) {
            log.info("Text is empty");
            return Collections.emptyList();
        }
        return commentRepository.findCommentsByItemIdAndTextContains(itemId, text).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
