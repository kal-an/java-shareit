package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoExtended;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RequestControllerTest {
    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private static final String X_HEADER_USER = "X-Sharer-User-Id";
    private ItemRequestDto requestDto;
    private ItemRequestDtoExtended requestDtoExtendedDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mapper.registerModule(new JavaTimeModule());

        requestDto = ItemRequestDto.builder()
                .id(null)
                .description("want a table")
                .build();
        requestDtoExtendedDto = new ItemRequestDtoExtended(requestDto);
        requestDtoExtendedDto.setItems(List.of(ItemRequestDtoExtended.Item.builder()
                        .id(1L)
                        .name("table")
                        .description("white table")
                        .available(true)
                        .requestId(1L)
                .build()));
    }

    @Test
    @DisplayName("Save new request")
    void saveNewRequest() throws Exception {
        when(requestService.addRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(requestDto);
        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_HEADER_USER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated())));
    }

    @Test
    @DisplayName("Save new request without header")
    void saveNewRequestWithoutXHeader() throws Exception {
        when(requestService.addRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(requestDto);
        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Save new request empty description")
    void saveNewRequestEmptyDescription() throws Exception {
        requestDto.setDescription(null);
        when(requestService.addRequest(requestDto, 1L))
                .thenReturn(requestDto);
        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_HEADER_USER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Get request")
    void getRequest() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(requestDtoExtendedDto);

        mvc.perform(get("/requests/1")
                        .header(X_HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDtoExtendedDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDtoExtendedDto.getDescription())))
                .andExpect(jsonPath("$.created", is(requestDtoExtendedDto.getCreated())))
                .andExpect(jsonPath("$.items[0].id", is(requestDtoExtendedDto.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.items[0].name", is(requestDtoExtendedDto.getItems().get(0).getName())))
                .andExpect(jsonPath("$.items[0].description", is(requestDtoExtendedDto.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$.items[0].available", is(requestDtoExtendedDto.getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$.items[0].requestId", is(requestDtoExtendedDto.getItems().get(0).getRequestId()), Long.class));
    }

    @Test
    @DisplayName("Get request without header")
    void getRequestWithoutXHeader() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(requestDtoExtendedDto);

        mvc.perform(get("/requests/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Get all own requests")
    void getAllOwnRequests() throws Exception {
        when(requestService.getAllOwnRequests(anyLong()))
                .thenReturn(List.of(requestDtoExtendedDto));

        mvc.perform(get("/requests")
                        .header(X_HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDtoExtendedDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDtoExtendedDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(requestDtoExtendedDto.getCreated())))
                .andExpect(jsonPath("$[0].items[0].id", is(requestDtoExtendedDto.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name", is(requestDtoExtendedDto.getItems().get(0).getName())))
                .andExpect(jsonPath("$[0].items[0].description", is(requestDtoExtendedDto.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[0].items[0].available", is(requestDtoExtendedDto.getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[0].items[0].requestId", is(requestDtoExtendedDto.getItems().get(0).getRequestId()), Long.class));
    }

    @Test
    @DisplayName("Get all own requests without header")
    void getAllOwnRequestsWithoutXHeader() throws Exception {
        when(requestService.getAllOwnRequests(anyLong()))
                .thenReturn(List.of(requestDtoExtendedDto));

        mvc.perform(get("/requests"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Get all requests")
    void getAllRequests() throws Exception {
        when(requestService.getAllOtherRequests(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(requestDtoExtendedDto));

        mvc.perform(get("/requests/all")
                        .header(X_HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDtoExtendedDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDtoExtendedDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(requestDtoExtendedDto.getCreated())))
                .andExpect(jsonPath("$[0].items[0].id", is(requestDtoExtendedDto.getItems().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name", is(requestDtoExtendedDto.getItems().get(0).getName())))
                .andExpect(jsonPath("$[0].items[0].description", is(requestDtoExtendedDto.getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[0].items[0].available", is(requestDtoExtendedDto.getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[0].items[0].requestId", is(requestDtoExtendedDto.getItems().get(0).getRequestId()), Long.class));
    }

    @Test
    @DisplayName("Get all requests without header")
    void getAllRequestsWithoutXHeader() throws Exception {
        when(requestService.getAllOtherRequests(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(requestDtoExtendedDto));

        mvc.perform(get("/requests/all"))
                .andExpect(status().is4xxClientError());
    }
}