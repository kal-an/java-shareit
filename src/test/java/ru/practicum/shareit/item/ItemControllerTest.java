package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto;
    private static final String X_HEADER_USER = "X-Sharer-User-Id";
    private CommentDto commentDto;
    private CommentCreationDto creationDto;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        itemDto = new ItemDto(
                null,
                "table",
                "white table",
                true,
                1L,
                null);

        commentDto = new CommentDto(null,
                "comment 1", "John", LocalDateTime.now());
        creationDto = new CommentCreationDto();
    }


    @Test
    @DisplayName("Save new item")
    void saveNewItem() throws Exception {
        when(itemService.addItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_HEADER_USER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    @DisplayName("Save new item without header")
    void saveNewItemWithoutHeader() throws Exception {
        when(itemService.addItem(itemDto, 1L))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Save new item empty available")
    void saveNewItemEmptyAvailable() throws Exception {
        itemDto = new ItemDto(
                null,
                "table",
                "white table",
                null,
                1L,
                null);
        when(itemService.addItem(itemDto, 1L))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(X_HEADER_USER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Save new item empty name")
    void saveNewItemEmptyName() throws Exception {
        itemDto = new ItemDto(
                null,
                null,
                "white table",
                true,
                1L,
                null);
        when(itemService.addItem(itemDto, 1L))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(X_HEADER_USER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Save new item empty description")
    void saveNewItemEmptyDescription() throws Exception {
        itemDto = new ItemDto(
                null,
                "table",
                null,
                true,
                1L,
                null);
        when(itemService.addItem(itemDto, 1L))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(X_HEADER_USER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Update item")
    void updateItem() throws Exception {
        when(itemService.editItem(any(ItemDto.class), anyLong(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/" + 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .header(X_HEADER_USER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    @DisplayName("Update item without header")
    void updateItemWithoutXHeader() throws Exception {
        when(itemService.editItem(any(ItemDto.class), anyLong(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/" + 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Get item")
    void getItem() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(new ItemDtoExtended(itemDto));

        mvc.perform(get("/items/1")
                        .header(X_HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    @DisplayName("Get all items")
    void getAllOwnerItems() throws Exception {
        when(itemService.getAllOwnerItems(anyInt(),anyInt(), anyLong()))
                .thenReturn(List.of(new ItemDtoExtended(itemDto)));

        mvc.perform(get("/items")
                        .header(X_HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId())))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    @DisplayName("Get all items without header")
    void getAllOwnerItemsWithoutXHeader() throws Exception {
        when(itemService.getAllOwnerItems(anyInt(),anyInt(), anyLong()))
                .thenReturn(List.of(new ItemDtoExtended(itemDto)));

        mvc.perform(get("/items"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Search items")
    void getSearchItemsForBooking() throws Exception {
        when(itemService.searchItemsForBooking(anyInt(), anyInt(), anyString()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "super item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId())))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    @DisplayName("Search items empty text")
    void getSearchItemsForBookingEmptyText() throws Exception {
        when(itemService.searchItemsForBooking(anyInt(), anyInt(), anyString()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Save new comment")
    void saveNewComment() throws Exception {
        creationDto.setText("comment 1");
        when(itemService.addComment(anyLong(), anyLong(), any(CommentCreationDto.class)))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(creationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_HEADER_USER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }

    @Test
    @DisplayName("Save new comment empty text")
    void saveNewCommentEmptyText() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any(CommentCreationDto.class)))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(creationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_HEADER_USER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Save new comment without header")
    void saveNewCommentWithoutXHeader() throws Exception {
        creationDto.setText("comment 1");
        when(itemService.addComment(anyLong(), anyLong(), any(CommentCreationDto.class)))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(creationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Search comments for item")
    void getSearchCommentsForItem() throws Exception {
        when(itemService.searchCommentsForItem(anyLong(), anyString()))
                .thenReturn(List.of(commentDto));

        mvc.perform(get("/items/1/comment")
                        .param("text", "super item"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(commentDto.getId())))
                .andExpect(jsonPath("$[0].text", is(commentDto.getText())))
                .andExpect(jsonPath("$[0].authorName", is(commentDto.getAuthorName())));
    }

    @Test
    @DisplayName("Search comments for item empty text")
    void getSearchCommentsForItemEmptyText() throws Exception {
        when(itemService.searchCommentsForItem(anyInt(), anyString()))
                .thenReturn(List.of(commentDto));

        mvc.perform(get("/items/1/comment"))
                .andExpect(status().is4xxClientError());
    }
}