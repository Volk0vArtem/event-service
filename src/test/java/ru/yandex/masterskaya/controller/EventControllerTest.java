package ru.yandex.masterskaya.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ru.yandex.masterskaya.model.event.dto.EventRequestDto;
import ru.yandex.masterskaya.model.event.dto.EventResponseDto;
import ru.yandex.masterskaya.service.event.EventService;

@WebMvcTest(EventController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final LocalDateTime now = LocalDateTime.now();

    private final EventRequestDto eventRequestDto = EventRequestDto.builder()
        .name("name")
        .description("description")
        .startDateTime(now.plusDays(1))
        .endDateTime(now.plusDays(5))
        .location("location")
        .build();

    private final EventResponseDto eventResponseDto = EventResponseDto.builder()
        .name("name")
        .description("description")
        .startDateTime(now.plusDays(1))
        .endDateTime(now.plusDays(5))
        .location("location")
        .createdDateTime(now)
        .ownerId(1L)
        .id(1L)
        .build();

    @MockBean
    private EventService eventService;

    @Test
    void saveEvent() throws Exception {
        Mockito.when(eventService.saveEvent(Mockito.any(EventRequestDto.class), anyLong()))
            .thenReturn(eventResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1)
                .content(objectMapper.writeValueAsString(eventRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(eventResponseDto)));
    }

    @Test
    void saveInvalidEvent() throws Exception {
        EventRequestDto invalidEventRequestDto = EventRequestDto.builder()
            .name("")
            .description("description")
            .startDateTime(now.plusDays(1))
            .endDateTime(now.plusDays(5))
            .location("location")
            .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1)
                .content(objectMapper.writeValueAsString(invalidEventRequestDto)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void saveEventInvalidTime() throws Exception {
        EventRequestDto invalidEventRequestDto = EventRequestDto.builder()
            .name("name")
            .description("description")
            .startDateTime(now.minusDays(5))
            .endDateTime(now.plusDays(10))
            .location("location")
            .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1)
                .content(objectMapper.writeValueAsString(invalidEventRequestDto)))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void patchEvent() throws Exception {
        Mockito.when(eventService.patchEvent(Mockito.any(EventRequestDto.class), anyLong(), anyLong()))
            .thenReturn(eventResponseDto);
        EventRequestDto patchedEventRequestDto = EventRequestDto.builder()
            .description("description")
            .location("location")
            .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1)
                .content(objectMapper.writeValueAsString(patchedEventRequestDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(eventResponseDto)));
    }

    @Test
    void getEvent() throws Exception {
        Mockito.when(eventService.getEvent(anyLong(), anyLong()))
            .thenReturn(eventResponseDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(eventResponseDto)));
    }

    @Test
    void getEventsByOwner() throws Exception {
        Mockito.when(eventService.getEventsByOwner(anyLong(), any(PageRequest.class)))
            .thenReturn(List.of(eventResponseDto));
        mockMvc.perform(MockMvcRequestBuilders.get("/events?userId=1&page=1&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(List.of(eventResponseDto))));
    }

    @Test
    void getEventsByOwnerInvalidPageRequest() throws Exception {
        Mockito.when(eventService.getEventsByOwner(anyLong(), any(PageRequest.class)))
            .thenReturn(List.of(eventResponseDto));
        mockMvc.perform(MockMvcRequestBuilders.get("/events?userId=1&page=-1&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        verify(eventService).deleteEvent(anyLong(), anyLong());
    }

    @Test
    void deleteEventNotOwner() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }
}