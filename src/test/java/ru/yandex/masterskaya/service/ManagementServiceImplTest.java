package ru.yandex.masterskaya.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.masterskaya.exception.ForbiddenException;
import ru.yandex.masterskaya.exception.NotFoundException;
import ru.yandex.masterskaya.model.event.dto.EventRequestDto;
import ru.yandex.masterskaya.model.event.dto.EventResponseDto;
import ru.yandex.masterskaya.model.manager.ManagerRole;
import ru.yandex.masterskaya.model.manager.dto.CreateManagerDto;
import ru.yandex.masterskaya.model.manager.dto.EventTeamDto;
import ru.yandex.masterskaya.model.manager.dto.ManagerDto;
import ru.yandex.masterskaya.service.event.EventService;
import ru.yandex.masterskaya.service.management.ManagementService;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "/managers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ManagementServiceImplTest {

    private final ManagementService service;

    private final EventService eventService;

    private final EventRequestDto createEvent = EventRequestDto.builder()
        .name("name")
        .description("description")
        .location("location")
        .startDateTime(LocalDateTime.now().plusDays(1))
        .endDateTime(LocalDateTime.now().plusDays(5))
        .build();

    private final ManagerDto manager = new ManagerDto(2L, ManagerRole.MANAGER);

    private EventResponseDto event;

    private CreateManagerDto createManager;

    @BeforeEach
    void saveEvent() {
        event = eventService.saveEvent(createEvent, 1L);
        createManager = new CreateManagerDto(event.getId(), manager);
    }

    @Test
    void shouldCreateTeamSuccessfully() {
        EventTeamDto answer = service.createTeam(event.getOwnerId(), createManager);
        assertThat(answer.getPersonnel().get(0).getUserId(), equalTo(manager.getUserId()));
    }

    @Test
    void shouldFailCreateTeam() {
        Long notOwner = 999L;
        ForbiddenException thrown = Assertions.assertThrows(ForbiddenException.class,
            () -> service.createTeam(notOwner, createManager));

        assertThat(thrown.getMessage(),
            equalTo("Only managers and owner can have access!"));
    }

    @Test
    void shouldUpdateRoleSuccessfully() {
        service.createTeam(event.getOwnerId(), createManager);

        manager.setRole(ManagerRole.SECURITY);
        ManagerDto result = service.updateRole(event.getOwnerId(), event.getId(), manager);
        assertThat(result.getRole(), equalTo(manager.getRole()));
    }

    @Test
    void shouldFailUpdateRole() {
        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class,
            () -> service.updateRole(99L, event.getId(), manager));

        assertThat(thrown.getMessage(),
            equalTo("Manager not found!"));
    }

    @Test
    void shouldGetPersonnelSuccessfully() {
        service.createTeam(event.getOwnerId(), createManager);
        createManager.getManager().setUserId(10L);
        service.createTeam(event.getOwnerId(), createManager);
        createManager.getManager().setUserId(20L);
        service.createTeam(event.getOwnerId(), createManager);

        EventTeamDto result = service.getPersonnel(event.getId());
        assertThat(result.getPersonnel().size(), equalTo(3));
    }

    @Test
    void shouldDeleteSuccessfully() {
        EventTeamDto answer = service.createTeam(event.getOwnerId(), createManager);

        service.delete(event.getOwnerId(), event.getId(), answer.getPersonnel().get(0).getUserId());

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class,
            () -> service.delete(event.getOwnerId(), event.getId(), answer.getPersonnel().get(0).getUserId()));

        assertThat(thrown.getMessage(),
            equalTo("Manager not found!"));
    }
}