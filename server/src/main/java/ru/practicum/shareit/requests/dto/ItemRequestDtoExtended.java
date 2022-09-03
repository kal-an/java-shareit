package ru.practicum.shareit.requests.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ItemRequestDtoExtended extends ItemRequestDto {

    private List<Item> items;

    public ItemRequestDtoExtended(ItemRequestDto dto) {
        super(dto.getId(), dto.getDescription(), dto.getCreated());
    }

    @Getter
    @Setter
    @Builder
    public static class Item {

        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;

    }
}
