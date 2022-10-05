package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

}
