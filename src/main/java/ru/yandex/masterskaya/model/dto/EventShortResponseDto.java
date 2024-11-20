package ru.yandex.masterskaya.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class EventShortResponseDto {
    private Long id;

    private String name;

    private String description;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String location;

    private Long ownerId;

}
