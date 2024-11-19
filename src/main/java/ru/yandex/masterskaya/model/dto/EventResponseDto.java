package ru.yandex.masterskaya.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventResponseDto extends EventShortResponseDto {

    private LocalDateTime createdDateTime;
}
