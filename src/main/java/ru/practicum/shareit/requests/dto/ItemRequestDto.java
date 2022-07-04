package ru.practicum.shareit.requests.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ItemRequestDto {

    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;

    public ItemRequestDto(String description, Long requestorId, LocalDateTime created) {
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
    }
}
