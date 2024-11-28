package ru.yandex.masterskaya.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.masterskaya.exception.BadRequestException;
import ru.yandex.masterskaya.model.event.dto.EventRequestDto;
import ru.yandex.masterskaya.model.event.dto.EventResponseDto;
import ru.yandex.masterskaya.model.event.dto.EventShortResponseDto;
import ru.yandex.masterskaya.service.event.EventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @PostMapping
    public ResponseEntity<EventResponseDto> saveEvent(@RequestBody @Valid EventRequestDto eventRequestDto,
                                                      @RequestHeader("X-User-Id") @Positive Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveEvent(eventRequestDto, userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EventResponseDto> patchEvent(@RequestBody @Valid EventRequestDto eventRequestDto,
                                                       @RequestHeader("X-User-Id") @Positive Long userId,
                                                       @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.patchEvent(eventRequestDto, userId, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventShortResponseDto> getEvent(@RequestHeader("X-User-Id") @Positive @NotNull Long userId,
                                                          @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.getEvent(userId, id));
    }

    @GetMapping
    public ResponseEntity<List<EventShortResponseDto>> getEventsByOwner(
        @RequestParam(name = "userId", required = false) Long userId,
        @RequestParam("page") Integer page,
        @RequestParam("size") Integer size) {
        if (page < 0 || size < 1) {
            throw new BadRequestException("Incorrect pagination parameters");
        }
        return ResponseEntity.ok().body(service.getEventsByOwner(userId, PageRequest.of(page / size, size)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@RequestHeader("X-User-Id") @NotNull Long userId,
                            @PathVariable("id") Long id) {
        service.deleteEvent(userId, id);
    }

}