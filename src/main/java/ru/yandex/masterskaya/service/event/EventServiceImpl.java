package ru.yandex.masterskaya.service.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.masterskaya.exception.BadRequestException;
import ru.yandex.masterskaya.exception.ForbiddenException;
import ru.yandex.masterskaya.exception.NotFoundException;
import ru.yandex.masterskaya.model.event.Event;
import ru.yandex.masterskaya.model.event.dto.EventRequestDto;
import ru.yandex.masterskaya.model.event.dto.EventResponseDto;
import ru.yandex.masterskaya.model.event.dto.EventShortResponseDto;
import ru.yandex.masterskaya.model.event.mapper.EventMapper;
import ru.yandex.masterskaya.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper mapper;

    public EventResponseDto saveEvent(EventRequestDto eventRequestDto, Long userId) {
        Event event = mapper.toEvent(eventRequestDto);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setOwnerId(userId);
        if (event.getStartDateTime().isAfter(event.getEndDateTime())) {
            throw new BadRequestException("Start time can't be after end time");
        }
        if (event.getStartDateTime().isBefore(event.getCreatedDateTime())) {
            throw new BadRequestException("Event can't start before it is created");
        }
        return mapper.toEventResponseDto(eventRepository.save(event));
    }

    public EventResponseDto patchEvent(EventRequestDto eventRequestDto, Long userId, Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getOwnerId().equals(userId)) {
            throw new ForbiddenException("User is not event owner");
        }

        Event patchedEvent = mapper.toEvent(eventRequestDto);

        if (patchedEvent.getName() != null) {
            event.setName(patchedEvent.getName());
        }
        if (patchedEvent.getDescription() != null) {
            event.setDescription(patchedEvent.getDescription());
        }
        if (patchedEvent.getLocation() != null) {
            event.setLocation(patchedEvent.getLocation());
        }
        if (patchedEvent.getStartDateTime() != null) {
            event.setStartDateTime(patchedEvent.getStartDateTime());
            if (event.getStartDateTime().isAfter(event.getEndDateTime())) {
                throw new BadRequestException("Start time can't be after end time");
            }
        }
        if (patchedEvent.getEndDateTime() != null) {
            event.setEndDateTime(patchedEvent.getEndDateTime());
            if (event.getStartDateTime().isAfter(event.getEndDateTime())) {
                throw new BadRequestException("Start time can't be after end time");
            }
        }

        return mapper.toEventResponseDto(eventRepository.save(event));
    }

    public EventShortResponseDto getEvent(Long userId, Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Event not found"));

        if (event.getOwnerId().equals(userId)) {
            return mapper.toEventResponseDto(event);
        } else {
            return mapper.toEventShortResponseDto(event);
        }
    }

    public List<EventShortResponseDto> getEventsByOwner(Long userId, PageRequest pageRequest) {
        if (userId != null) {
            return eventRepository.findAllByOwnerId(userId, pageRequest).stream()
                .map(mapper::toEventShortResponseDto)
                .collect(Collectors.toList());
        } else {
            return eventRepository.findAll().stream()
                .map(mapper::toEventShortResponseDto)
                .collect(Collectors.toList());
        }
    }

    public void deleteEvent(Long userId, Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getOwnerId().equals(userId)) {
            throw new ForbiddenException("User is not event owner");
        }
        eventRepository.deleteById(id);
    }
}