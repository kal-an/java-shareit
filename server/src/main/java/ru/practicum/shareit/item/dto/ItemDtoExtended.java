package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ItemDtoExtended extends ItemDto {

    private DateBooking lastBooking;

    private DateBooking nextBooking;

    private List<CommentDto> comments;

    public ItemDtoExtended(ItemDto itemDto) {
        super(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), itemDto.getOwnerId(), itemDto.getRequestId());
    }

    @Setter
    @Getter
    @Builder
    public static class DateBooking {

        private Long id;

        private Long bookerId;

        private LocalDateTime start;

        private LocalDateTime end;
    }
}
