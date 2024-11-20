package ru.yandex.masterskaya.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class EventResponseDto extends EventShortResponseDto {

    private LocalDateTime createdDateTime;
}
