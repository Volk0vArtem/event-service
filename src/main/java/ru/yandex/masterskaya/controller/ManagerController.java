package ru.yandex.masterskaya.controller;

import static ru.yandex.masterskaya.constants.Constants.X_EVENT_MANAGER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
        summary = "Создать новую команду для события",
        description = "Создает новую команду и возвращает её данные",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Команда успешно создана",
                content = @Content(schema = @Schema(implementation = EventTeamDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Некорректный запрос"
            )
        }
    )
    @PostMapping
    public EventTeamDto create(@RequestHeader(X_EVENT_MANAGER) @Min(1) Long authorId,
                               @RequestBody @Valid CreateManagerDto dto) {
        return service.createTeam(authorId, dto);
    }

    @Operation(
        summary = "Изменить статус менеджера",
        description = "Обновляет роль или статус менеджера в рамках события",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Роль менеджера успешно обновлена",
                content = @Content(schema = @Schema(implementation = ManagerDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Событие или менеджер не найдены"
            )
        }
    )
    @PatchMapping("/{eventId}")
    public ManagerDto changeStatus(@RequestHeader(X_EVENT_MANAGER) @Min(1) Long authorId,
                                   @PathVariable(name = "eventId") @Min(1) Long eventId,
                                   @RequestBody @Valid ManagerDto dto) {
        return service.updateRole(authorId, eventId, dto);
    }


    @Operation(
        summary = "Получить персонал события",
        description = "Возвращает список команды (менеджеров) для указанного события",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Список команды события",
                content = @Content(schema = @Schema(implementation = EventTeamDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Событие не найдено"
            )
        }
    )
    @GetMapping("/{eventId}")
    public EventTeamDto getStaff(@PathVariable(name = "eventId") @Min(1) Long eventId) {
        return service.getPersonnel(eventId);
    }

    @Operation(
        summary = "Удалить менеджера из команды события",
        description = "Удаляет указанного менеджера из команды события",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Менеджер успешно удалён"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Событие или менеджер не найдены"
            )
        }
    )
    @DeleteMapping("/{eventId}/{managerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader(X_EVENT_MANAGER) @Min(1) Long authorId,
                       @PathVariable(name = "eventId") @Min(1) Long eventId,
                       @PathVariable(name = "managerId") @Min(1) Long userId) {
        service.delete(authorId, eventId, userId);
    }
}
