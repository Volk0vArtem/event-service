package ru.yandex.masterskaya.model.mapper;

import org.mapstruct.Mapper;
import ru.yandex.masterskaya.model.Event;
import ru.yandex.masterskaya.model.dto.EventRequestDto;
import ru.yandex.masterskaya.model.dto.EventResponseDto;
import ru.yandex.masterskaya.model.dto.EventShortResponseDto;

@Mapper(componentModel = "spring")
public interface EventMapper {


    Event toEvent(EventRequestDto eventRequestDto);

    EventResponseDto toEventResponseDto(Event event);

    EventShortResponseDto toEventShortResponseDto(Event event);
}
