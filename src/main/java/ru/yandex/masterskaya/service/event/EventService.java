package ru.yandex.masterskaya.service.event;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import ru.yandex.masterskaya.model.event.dto.EventRequestDto;
import ru.yandex.masterskaya.model.event.dto.EventResponseDto;
import ru.yandex.masterskaya.model.event.dto.EventShortResponseDto;

public interface EventService {

    EventResponseDto saveEvent(EventRequestDto eventRequestDto, Long userId);

    EventResponseDto patchEvent(EventRequestDto eventRequestDto, Long userId, Long id);

    EventShortResponseDto getEvent(Long userId, Long id);

    List<EventShortResponseDto> getEventsByOwner(Long userId, PageRequest pageRequest);

    void deleteEvent(Long userId, Long id);
}
