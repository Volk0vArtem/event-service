package ru.yandex.masterskaya.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.masterskaya.model.event.dto.EventRequestDto;
import ru.yandex.masterskaya.model.event.dto.EventResponseDto;
import ru.yandex.masterskaya.model.event.dto.EventShortResponseDto;
import ru.yandex.masterskaya.repository.EventRepository;
import ru.yandex.masterskaya.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EventServiceImplTest {

    private final EventService eventService;
    private final EventRepository eventRepository;

    private final EventRequestDto eventRequestDto = EventRequestDto.builder()
            .name("name")
            .description("description")
            .location("location")
            .startDateTime(LocalDateTime.now().plusDays(1))
            .endDateTime(LocalDateTime.now().plusDays(5))
            .build();


    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
    }

    @Test
    void saveEvent() {
        EventResponseDto savedEvent = eventService.saveEvent(eventRequestDto, 1L);
        assertEquals(eventRequestDto.getName(), savedEvent.getName());
        assertEquals(eventRequestDto.getDescription(), savedEvent.getDescription());
        assertEquals(eventRequestDto.getLocation(), savedEvent.getLocation());
        assertEquals(eventRequestDto.getStartDateTime(), savedEvent.getStartDateTime());
        assertEquals(eventRequestDto.getEndDateTime(), savedEvent.getEndDateTime());
        assertNotNull(savedEvent.getCreatedDateTime());
        assertNotNull(savedEvent.getId());
    }

    @Test
    void patchEvent() {
        EventResponseDto event = eventService.saveEvent(eventRequestDto, 1L);
        EventRequestDto patchedEventRequestDto = EventRequestDto.builder()
                .name("patchedName")
                .build();
        EventResponseDto patchedEvent = eventService.patchEvent(patchedEventRequestDto, event.getId(), event.getOwnerId());
        assertEquals(patchedEventRequestDto.getName(), patchedEvent.getName());
    }

    @Test
    void getEvent() {
        eventService.saveEvent(eventRequestDto, 1L);
        EventShortResponseDto savedEvent = eventService.getEvent(2L, 1L);
        assertEquals(eventRequestDto.getName(), savedEvent.getName());
        assertEquals(eventRequestDto.getDescription(), savedEvent.getDescription());
        assertEquals(eventRequestDto.getLocation(), savedEvent.getLocation());
        assertEquals(eventRequestDto.getStartDateTime(), savedEvent.getStartDateTime());
        assertEquals(eventRequestDto.getEndDateTime(), savedEvent.getEndDateTime());
        assertNotNull(savedEvent.getId());
    }

    @Test
    void getEventUserOwner() {
        EventResponseDto responseDto = eventService.saveEvent(eventRequestDto, 1L);
        EventShortResponseDto event = eventService.getEvent(1L, responseDto.getId());
        assertEquals(eventRequestDto.getName(), event.getName());
        assertEquals(eventRequestDto.getDescription(), event.getDescription());
        assertEquals(eventRequestDto.getLocation(), event.getLocation());
        assertEquals(eventRequestDto.getStartDateTime(), event.getStartDateTime());
        assertEquals(eventRequestDto.getEndDateTime(), event.getEndDateTime());
        assertNotNull(event.getId());
        EventResponseDto response = (EventResponseDto) event;
        assertNotNull(response.getCreatedDateTime());
    }

    @Test
    void getEventsByOwner() {
        eventService.saveEvent(eventRequestDto, 1L);
        EventRequestDto event2 = EventRequestDto.builder()
                .name("name2")
                .description("description2")
                .location("location2")
                .startDateTime(LocalDateTime.now().plusDays(2))
                .endDateTime(LocalDateTime.now().plusDays(3))
                .build();
        eventService.saveEvent(event2, 2L);

        List<EventShortResponseDto> events = eventService.getEventsByOwner(2L, PageRequest.of(0, 10));

        assertEquals(1, events.size());
        assertEquals(event2.getName(), events.getFirst().getName());
        assertEquals(event2.getDescription(), events.getFirst().getDescription());
        assertEquals(event2.getLocation(), events.getFirst().getLocation());
        assertEquals(event2.getStartDateTime(), events.getFirst().getStartDateTime());
        assertEquals(event2.getEndDateTime(), events.getFirst().getEndDateTime());
    }

    @Test
    void deleteEvent() {
        EventResponseDto eventResponseDto = eventService.saveEvent(eventRequestDto, 1L);
        eventService.deleteEvent(1L, eventResponseDto.getId());
        List<EventShortResponseDto> events = eventService.getEventsByOwner(null, PageRequest.of(1, 10));
        assertEquals(0, events.size());
    }


}