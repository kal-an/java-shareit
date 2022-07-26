package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

    @Setter
    @Getter
    @Builder
    public static class DateBooking {

        @NotNull(message = "ID should not be empty")
        private Long id;

        @NotNull(message = "Booker ID should not be empty")
        private Long bookerId;

        @NotNull(message = "Start date should not be empty")
        private LocalDateTime start;

        @NotNull(message = "End date should not be empty")
        private LocalDateTime end;
    }
}
