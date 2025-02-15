package ru.yandex.masterskaya.model.event.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class EventResponseDto extends EventShortResponseDto {

    private LocalDateTime createdDateTime;
}
