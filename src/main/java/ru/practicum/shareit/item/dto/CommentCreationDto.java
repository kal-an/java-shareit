package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
public class CommentCreationDto {

    @NotEmpty(message = "Text should not be empty")
    @Size(max = 4000, message = "Text should less 4000 characters")
    private String text;
}
