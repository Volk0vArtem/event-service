package ru.yandex.masterskaya.model.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.masterskaya.model.manager.Manager;
import ru.yandex.masterskaya.model.manager.dto.CreateManagerDto;
import ru.yandex.masterskaya.model.manager.dto.ManagerDto;

@Mapper(componentModel = "spring")
public interface ManagerMapper {

    Manager toManager(ManagerDto dto);

    @Mapping(target = "userId", source = "dto.manager.userId")
    @Mapping(target = "role", source = "dto.manager.role")
    Manager toManager(CreateManagerDto dto);

    ManagerDto toManagerDto(Manager manager);
}
