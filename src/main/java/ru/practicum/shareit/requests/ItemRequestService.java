package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoExtended;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(ItemRequestDto requestDto, long userId);

    List<ItemRequestDtoExtended> getAllOwnRequests(long userId);

    List<ItemRequestDtoExtended> getAllOtherRequests(int from, int size, long userId);

    ItemRequestDtoExtended getRequestById(long requestId, long userId);
}
