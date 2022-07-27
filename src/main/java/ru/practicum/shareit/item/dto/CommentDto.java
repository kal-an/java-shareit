package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotEmpty(message = "Text should not be empty")
    @Size(max = 4000, message = "Text should less 4000 characters")
    private String text;

    @NotNull(message = "Author should not be empty")
    private String authorName;

    @NotNull(message = "Created date should not be null")
    private LocalDateTime created;
}