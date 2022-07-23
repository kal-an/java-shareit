package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class ItemDtoExtended extends ItemDto {

    @NotNull(message = "Last booking should not be empty")
    private DateBooking lastBooking;

    @NotNull(message = "Next booking should not be empty")
    private DateBooking nextBooking;

    private List<CommentDto> comments;

    public ItemDtoExtended(ItemDto itemDto) {
        super(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), itemDto.getOwnerId(), itemDto.getRequestId());
    }
}
