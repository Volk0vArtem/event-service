package ru.yandex.masterskaya.model.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.masterskaya.model.event.Event;
import ru.yandex.masterskaya.model.event.dto.EventRequestDto;
import ru.yandex.masterskaya.model.event.dto.EventResponseDto;
import ru.yandex.masterskaya.model.event.dto.EventShortResponseDto;
import ru.yandex.masterskaya.model.manager.dto.EventTeamDto;
import ru.yandex.masterskaya.model.manager.mapper.ManagerMapper;

@Mapper(componentModel = "spring", uses = {ManagerMapper.class})
public interface EventMapper {

    @Mapping(target = "eventId", source = "id")
    EventTeamDto toEventTeamDto(Event event);

    Event toEvent(EventRequestDto eventRequestDto);

    EventResponseDto toEventResponseDto(Event event);

    EventShortResponseDto toEventShortResponseDto(Event event);
}
