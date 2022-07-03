package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

@Setter
@Getter
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequestDto request;

    public ItemDto(String name, String description, boolean available, Long requestId) {
    }
}
