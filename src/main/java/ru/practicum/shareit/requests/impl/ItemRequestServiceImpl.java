package ru.practicum.shareit.requests.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.RequestNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoExtended;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository requestRepository,
                                  ItemRepository itemRepository,
                                  UserService userService) {
        this.requestRepository = requestRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    public ItemRequestDto addRequest(ItemRequestDto requestDto, long userId) {
        final User user = UserMapper.toUser(userService.getUserById(userId));
        final ItemRequest saveInDb = requestRepository.save(new ItemRequest(
                requestDto.getDescription(), user));
        log.info("Item request {} saved", saveInDb);
        return ItemRequestMapper.toItemRequestDto(saveInDb);
    }

    @Override
    public List<ItemRequestDtoExtended> getAllOwnRequests(long userId) {
        userService.getUserById(userId);
        Sort sortBy = Sort.by(Sort.Direction.DESC, "created");
        final List<ItemRequest> requests = requestRepository.findByRequesterId(userId, sortBy);
        return getItemsForRequest(requests);
    }

    @Override
    public List<ItemRequestDtoExtended> getAllOtherRequests(int fromPage, int size, long userId) {
        if (fromPage < 0 || size < 1) {
            throw new InvalidEntityException("Invalid request parameter");
        }
        userService.getUserById(userId);
        Sort sortBy = Sort.by(Sort.Direction.DESC, "created");
        int page = fromPage * size;
        Pageable pageable = PageRequest.of(page, size, sortBy);
        final List<ItemRequest> requests = requestRepository
                .findByRequesterIdNot(userId, pageable);
        return getItemsForRequest(requests);
    }

    @Override
    public ItemRequestDtoExtended getRequestById(long requestId, long userId) {
        userService.getUserById(userId);
        final ItemRequest requestInDb = requestRepository.findById(requestId).orElseThrow(() ->
                new RequestNotFoundException(String
                        .format("Request with ID %d not found", requestId)));
        final List<ItemRequestDtoExtended.Item> items = getItemsForRequest(requestInDb);
        final ItemRequestDtoExtended convertToDto = new ItemRequestDtoExtended(ItemRequestMapper
                .toItemRequestDto(requestInDb));

        convertToDto.setItems(items);

        return convertToDto;
    }

    private List<ItemRequestDtoExtended.Item> getItemsForRequest(ItemRequest request) {
        return itemRepository.findByRequestId(request.getId()).stream()
                .map(ir -> ItemRequestDtoExtended.Item.builder()
                        .id(ir.getId())
                        .name(ir.getName())
                        .description(ir.getDescription())
                        .available(ir.getAvailable())
                        .requestId(ir.getRequest().getId())
                        .build())
                .collect(Collectors.toList());
    }

    private List<ItemRequestDtoExtended> getItemsForRequest(List<ItemRequest> requests) {
        final List<ItemRequestDtoExtended> requestsDto = new ArrayList<>();
        for (ItemRequest request : requests) {
            final ItemRequestDtoExtended requestDto = new ItemRequestDtoExtended(ItemRequestMapper
                    .toItemRequestDto(request));
            final List<ItemRequestDtoExtended.Item> items = getItemsForRequest(request);
            requestDto.setItems(items);
            requestsDto.add(requestDto);
        }
        return requestsDto;
    }
}
