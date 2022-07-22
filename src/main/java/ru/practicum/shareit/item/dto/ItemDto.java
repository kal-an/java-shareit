package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class ItemDto {

    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(max = 255, message = "Name should less 255 characters")
    private String name;

    @NotEmpty(message = "Description should not be empty")
    @Size(max = 4000, message = "Description should less 4000 characters")
    private String description;

    @NotNull(message = "Available should not be empty")
    private Boolean available;
    private Long ownerId;
    private Long requestId;

    public ItemDto(Long id,
                   String name,
                   String description,
                   Boolean available,
                   Long ownerId,
                   Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.requestId = requestId;
    }
}
