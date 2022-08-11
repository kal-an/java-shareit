package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private final static String X_HEADER_USER = "X-Sharer-User-Id";
    private BookingDto bookingDto;
    private BookingCreationDto bookingCreationDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        mapper.registerModule(new JavaTimeModule());

        bookingCreationDto = new BookingCreationDto(
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1),
                1L);

        bookingDto = new BookingDto(1L,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1),
                BookingDto.Item.builder()
                        .id(1L)
                        .name("item 1")
                        .build(),
                BookingDto.User.builder()
                        .id(1L)
                        .build(),
                "WAITING");
    }


    @Test
    @DisplayName("Save new booking")
    void saveNewBooking() throws Exception {
        when(bookingService.addBooking(any(BookingCreationDto.class), anyLong()))
                .thenReturn(bookingDto);
        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_HEADER_USER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }

    @Test
    @DisplayName("Save new booking without header")
    void saveNewBookingWithoutHeader() throws Exception {
        when(bookingService.addBooking(any(BookingCreationDto.class), anyLong()))
                .thenReturn(bookingDto);
        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Save new booking empty start")
    void saveNewBookingEmptyStart() throws Exception {
        bookingCreationDto.setStart(null);
        when(bookingService.addBooking(bookingCreationDto, 1L))
                .thenReturn(bookingDto);
        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_HEADER_USER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Save new booking empty end")
    void saveNewBookingEmptyEnd() throws Exception {
        bookingCreationDto.setEnd(null);
        when(bookingService.addBooking(bookingCreationDto, 1L))
                .thenReturn(bookingDto);
        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_HEADER_USER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Save new booking empty itemId")
    void saveNewBookingEmptyItemId() throws Exception {
        bookingCreationDto.setItemId(null);
        when(bookingService.addBooking(bookingCreationDto, 1L))
                .thenReturn(bookingDto);


        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingCreationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(X_HEADER_USER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Update booking")
    void updateBooking() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/" + 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(X_HEADER_USER, 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())));
    }

    @Test
    @DisplayName("Update booking without header")
    void updateBookingWithoutXHeader() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/" + 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Update booking without approved")
    void updateBookingWithoutApproved() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/" + 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(X_HEADER_USER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Get booking")
    void getBooking() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .header(X_HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())));
    }

    @Test
    @DisplayName("Get booking without header")
    void getBookingWithoutXHeader() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Get all user bookings")
    void getAllUserBookings() throws Exception {
        when(bookingService.getAllUserBookings(anyInt(),anyInt(), anyString(), anyLong()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header(X_HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())));
    }

    @Test
    @DisplayName("Get all user bookings without header")
    void getAllUserBookingsWithoutXHeader() throws Exception {
        when(bookingService.getAllUserBookings(anyInt(),anyInt(), anyString(), anyLong()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Get all owner bookings")
    void getAllOwnerBookings() throws Exception {
        when(bookingService.getAllOwnerBookings(anyInt(),anyInt(), anyString(), anyLong()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header(X_HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())));
    }

    @Test
    @DisplayName("Get all owner bookings without header")
    void getAllOwnerBookingsWithoutXHeader() throws Exception {
        when(bookingService.getAllOwnerBookings(anyInt(),anyInt(), anyString(), anyLong()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings"))
                .andExpect(status().is4xxClientError());
    }
}