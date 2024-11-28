package ru.yandex.masterskaya.service.management;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.masterskaya.exception.ForbiddenException;
import ru.yandex.masterskaya.exception.NotFoundException;
import ru.yandex.masterskaya.model.event.Event;
import ru.yandex.masterskaya.model.event.mapper.EventMapper;
import ru.yandex.masterskaya.model.manager.Manager;
import ru.yandex.masterskaya.model.manager.ManagerRole;
import ru.yandex.masterskaya.model.manager.dto.CreateManagerDto;
import ru.yandex.masterskaya.model.manager.dto.EventTeamDto;
import ru.yandex.masterskaya.model.manager.dto.ManagerDto;
import ru.yandex.masterskaya.model.manager.mapper.ManagerMapper;
import ru.yandex.masterskaya.repository.EventRepository;
import ru.yandex.masterskaya.repository.ManagerRepository;

@Service
@RequiredArgsConstructor
public class ManagementServiceImpl implements ManagementService {

    private final EventRepository eventRepository;
    private final ManagerRepository managerRepository;
    private final EventMapper eventTeamMapper;
    private final ManagerMapper managerMapper;

    @Override
    public EventTeamDto createTeam(Long managerId, CreateManagerDto dto) {
        Event event = getEvent(dto.getEventId());
        checkAccessRight(managerId, event);
        Manager manager = managerMapper.toManager(dto);
        managerRepository.save(manager);
        event.getPersonnel().add(manager);
        event = eventRepository.save(event);
        return eventTeamMapper.toEventTeamDto(event);
    }

    @Override
    public ManagerDto updateRole(Long managerId, Long eventId, ManagerDto dto) {
        Event event = getEvent(eventId);
        checkMangerExists(event, dto.getUserId());
        checkAccessRight(managerId, event);
        Manager man = managerMapper.toManager(dto);
        return managerMapper.toManagerDto(managerRepository.save(man));
    }

    @Override
    public EventTeamDto getPersonnel(Long eventId) {
        Event event = getEvent(eventId);
        return eventTeamMapper.toEventTeamDto(event);
    }

    @Override
    public void delete(Long managerId, Long eventId, Long userId) {
        Event event = getEvent(eventId);
        checkMangerExists(event, userId);
        checkAccessRight(managerId, event);
        event.getPersonnel().removeIf(man -> man.getUserId().equals(userId));
        eventRepository.save(event);
        managerRepository.deleteById(userId);
    }

    private void checkAccessRight(Long managerId, Event event) {
        boolean isManager = event.getPersonnel().stream()
            .anyMatch(manager -> manager.getUserId().equals(managerId)
                && manager.getRole().equals(ManagerRole.MANAGER));
        if (managerId.equals(event.getOwnerId())) {
            return;
        }
        if (isManager) {
            return;
        }
        throw new ForbiddenException("Only managers and owner can have access!");
    }

    private void checkMangerExists(Event event, Long managerId) {
        boolean notFound = event.getPersonnel().stream()
            .noneMatch(manager -> manager.getUserId().equals(managerId));
        if (notFound) {
            throw new NotFoundException("Manager not found!");
        }
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new NotFoundException("Event not found"));
    }
}
