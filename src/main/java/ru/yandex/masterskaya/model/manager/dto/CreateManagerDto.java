package ru.yandex.masterskaya.model.manager.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateManagerDto {
    @Min(1)
    private Long eventId;
    @Valid
    private ManagerDto manager;
}
