package ru.yandex.masterskaya.service.management;

import ru.yandex.masterskaya.model.manager.dto.CreateManagerDto;
import ru.yandex.masterskaya.model.manager.dto.EventTeamDto;
import ru.yandex.masterskaya.model.manager.dto.ManagerDto;

public interface ManagementService {

    EventTeamDto createTeam(Long managerId, CreateManagerDto dto);

    ManagerDto updateRole(Long managerId, Long eventId, ManagerDto dto);

    EventTeamDto getPersonnel(Long eventId);

    void delete(Long managerId, Long eventId, Long userId);
}
