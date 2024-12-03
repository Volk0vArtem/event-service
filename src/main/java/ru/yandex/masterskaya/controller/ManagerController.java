package ru.yandex.masterskaya.controller;

import static ru.yandex.masterskaya.constants.Constants.X_EVENT_MANAGER;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.masterskaya.model.manager.dto.CreateManagerDto;
import ru.yandex.masterskaya.model.manager.dto.EventTeamDto;
import ru.yandex.masterskaya.model.manager.dto.ManagerDto;
import ru.yandex.masterskaya.service.management.ManagementService;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor
@Validated
public class ManagerController {

    private final ManagementService service;

    @PostMapping
    public EventTeamDto create(@RequestHeader(X_EVENT_MANAGER) @Min(1) Long authorId,
        @RequestBody @Valid CreateManagerDto dto) {
        return service.createTeam(authorId, dto);
    }

    @PatchMapping("/{eventId}")
    public ManagerDto changeStatus(@RequestHeader(X_EVENT_MANAGER) @Min(1) Long authorId,
        @PathVariable(name = "eventId") @Min(1) Long eventId,
        @RequestBody @Valid ManagerDto dto) {
        return service.updateRole(authorId, eventId, dto);
    }

    @GetMapping("/{eventId}")
    public EventTeamDto getStaff(@PathVariable(name = "eventId") @Min(1) Long eventId) {
        return service.getPersonnel(eventId);
    }

    @DeleteMapping("/{eventId}/{managerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader(X_EVENT_MANAGER) @Min(1) Long authorId,
        @PathVariable(name = "eventId") @Min(1) Long eventId,
        @PathVariable(name = "managerId") @Min(1) Long userId) {
        service.delete(authorId, eventId, userId);
    }
}
