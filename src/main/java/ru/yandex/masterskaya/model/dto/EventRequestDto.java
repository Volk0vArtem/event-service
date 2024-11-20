package ru.yandex.masterskaya.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventRequestDto {

    @Size(min = 1, max = 500)
    private String name;

    @Size(min = 10, max = 1000)
    private String description;

    @Future
    private LocalDateTime startDateTime;

    @Future
    private LocalDateTime endDateTime;

    @Size(min = 5, max = 500)
    private String location;
}
