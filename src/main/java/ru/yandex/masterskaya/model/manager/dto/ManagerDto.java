package ru.yandex.masterskaya.model.manager.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.masterskaya.model.manager.ManagerRole;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerDto {

    @Min(1)
    private Long userId;
    private ManagerRole role;
}
