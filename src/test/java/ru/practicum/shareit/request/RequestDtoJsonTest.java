package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoExtended;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> jsonRequest;
    @Autowired
    private JacksonTester<ItemRequestDtoExtended> jsonRequestExtended;

    @Test
    void testRequestDto() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto(
                1L,
                "want white table",
                LocalDateTime.of(2022, 1, 1, 16, 0, 0));

        JsonContent<ItemRequestDto> result = jsonRequest.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("want white table");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-01-01T16:00");
    }

    @Test
    void testRequestExtendedDto() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto(
                1L,
                "want white table",
                LocalDateTime.of(2022, 1, 1, 16, 0, 0));
        ItemRequestDtoExtended requestDtoExtended = new ItemRequestDtoExtended(requestDto);

        requestDtoExtended.setItems(List.of(
                ItemRequestDtoExtended.Item.builder()
                        .id(1L)
                        .name("table")
                        .description("white table")
                        .available(true)
                        .requestId(1L)
                        .build()
        ));
        JsonContent<ItemRequestDtoExtended> result = jsonRequestExtended.write(requestDtoExtended);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("want white table");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-01-01T16:00");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("table");
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo("white table");
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(1);
    }

}
