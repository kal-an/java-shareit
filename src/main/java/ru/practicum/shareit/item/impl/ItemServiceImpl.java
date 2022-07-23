package ru.practicum.shareit.item.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.DateBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository,
                           CommentRepository commentRepository,
                           UserService userService) {
        this.repository = repository;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        final Item item = ItemMapper.toItem(itemDto);
        userService.getUserById(userId);
        item.setOwner(userId);
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
        if (!itemInDb.getOwner().equals(userId)) {
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
        final Item item = repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", id)));
        final ItemDtoExtended dtoExtended = new ItemDtoExtended(ItemMapper.toItemDto(item));
        if (item.getOwner().equals(userId)) {
            List<Booking> bookings = bookingRepository.findByItemId(id);
            for (Booking booking : bookings) {
                setBookingDates(booking, dtoExtended);
            }
        }
        return dtoExtended;
    }

    @Override
    public List<ItemDtoExtended> getAllOwnerItems(long ownerId) {
        userService.getUserById(ownerId);
        final List<ItemDtoExtended> itemDtoExtendedList = new ArrayList<>();
        final List<Item> items = repository.findAllItemsByOwner(ownerId);
        for (Item item : items) {
            final List<Booking> bookings = bookingRepository.findByItemId(item.getId());
            for (Booking booking : bookings) {
                final ItemDtoExtended dtoExtended = new ItemDtoExtended(ItemMapper.toItemDto(item));
                setBookingDates(booking, dtoExtended);
                itemDtoExtendedList.add(dtoExtended);
                log.info(String.valueOf(dtoExtended));
            }
        }
        return itemDtoExtendedList;
//        return repository.findAllItemsByOwner(ownerId).stream()
//                .map(ItemMapper::toItemDto)
//                .map(ItemDtoExtended::new)
//                .collect(Collectors.toList());
    }

    private void setBookingDates(Booking booking, ItemDtoExtended dtoExtended) {
        if (LocalDateTime.now().isAfter(booking.getEnd())) {
            dtoExtended.setLastBooking(new DateBooking(
                    booking.getId(),
                    booking.getBookerId(),
                    booking.getStart(),
                    booking.getEnd()));
        }
        if (LocalDateTime.now().isBefore(booking.getStart())) {
            dtoExtended.setNextBooking(new DateBooking(
                    booking.getId(),
                    booking.getBookerId(),
                    booking.getStart(),
                    booking.getEnd()));
        }
    }

    @Override
    public List<ItemDto> searchItemsForBooking(String text) {
        if (text.isEmpty()) {
            log.info("Text is empty");
            return Collections.emptyList();
        }
        return repository.searchItemsForBooking(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long userId, long itemId,
                                 CommentCreationDto text) {
        return null;
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
