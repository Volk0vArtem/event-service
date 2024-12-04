package ru.yandex.masterskaya.controller;

import static ru.yandex.masterskaya.constants.Constants.X_USER_ID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.masterskaya.exception.BadRequestException;
import ru.yandex.masterskaya.model.event.dto.EventRequestDto;
import ru.yandex.masterskaya.model.event.dto.EventResponseDto;
import ru.yandex.masterskaya.model.event.dto.EventShortResponseDto;
import ru.yandex.masterskaya.service.event.EventService;

@Tag(name = "Events", description = "Контроллер для работы с событиями")
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService service;

    @Operation(
        summary = "Сохранить новое событие",
        description = "Создает новое событие и возвращает его детали",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Событие успешно создано",
                content = @Content(schema = @Schema(implementation = EventResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Некорректный запрос"
            )
        }
    )
    @PostMapping
    public ResponseEntity<EventResponseDto> saveEvent(@RequestBody @Valid EventRequestDto eventRequestDto,
                                                      @RequestHeader(X_USER_ID) @Positive Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveEvent(eventRequestDto, userId));
    }

    @Operation(
        summary = "Обновить событие",
        description = "Обновляет данные существующего события",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Событие успешно обновлено",
                content = @Content(schema = @Schema(implementation = EventResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Событие не найдено"
            )
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<EventResponseDto> patchEvent(@RequestBody @Valid EventRequestDto eventRequestDto,
                                                       @RequestHeader(X_USER_ID) @Positive Long userId,
                                                       @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.patchEvent(eventRequestDto, userId, id));
    }

    @Operation(
        summary = "Получить событие",
        description = "Возвращает данные события по ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Данные события",
                content = @Content(schema = @Schema(implementation = EventShortResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Событие не найдено"
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EventShortResponseDto> getEvent(@RequestHeader(X_USER_ID) @Positive @NotNull Long userId,
                                                          @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.getEvent(userId, id));
    }

    @Operation(
        summary = "Получить список событий пользователя",
        description = "Возвращает список событий, принадлежащих пользователю, с пагинацией",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Список событий",
                content = @Content(schema = @Schema(implementation = List.class))
            )
        }
    )
    @GetMapping
    public ResponseEntity<List<EventShortResponseDto>> getEventsByOwner(@RequestParam(name = "userId", required = false) Long userId,
                                                                        @RequestParam("page") Integer page,
                                                                        @RequestParam("size") Integer size) {
        if (page < 0 || size < 1) {
            throw new BadRequestException("Incorrect pagination parameters");
        }
        return ResponseEntity.ok().body(service.getEventsByOwner(userId, PageRequest.of(page / size, size)));
    }

    @Operation(
        summary = "Удалить событие",
        description = "Удаляет событие по ID",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Событие успешно удалено"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Событие не найдено"
            )
        }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@RequestHeader(X_USER_ID) @NotNull Long userId,
                            @PathVariable("id") Long id) {
        service.deleteEvent(userId, id);
    }

}