package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ItemRequestDto {

    private Long id;

    @NotEmpty(message = "Description should not be empty")
    @Size(max = 4000, message = "Description should less 4000 characters")
    private String description;

    private LocalDateTime created;

}
