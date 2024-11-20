package ru.yandex.masterskaya.service;

import org.springframework.data.domain.PageRequest;
import ru.yandex.masterskaya.model.dto.EventRequestDto;
import ru.yandex.masterskaya.model.dto.EventResponseDto;
import ru.yandex.masterskaya.model.dto.EventShortResponseDto;

import java.util.List;

public interface EventService {

    EventResponseDto saveEvent(EventRequestDto eventRequestDto, Long userId);

    EventResponseDto patchEvent(EventRequestDto eventRequestDto, Long userId, Long id);

    EventShortResponseDto getEvent(Long userId, Long id);

    List<EventShortResponseDto> getEventsByOwner(Long userId, PageRequest pageRequest);

    void deleteEvent(Long userId, Long id);
}
