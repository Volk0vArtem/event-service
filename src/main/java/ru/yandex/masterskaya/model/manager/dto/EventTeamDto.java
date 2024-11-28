package ru.yandex.masterskaya.model.manager.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventTeamDto {

    private Long eventId;
    private List<ManagerDto> personnel;
}
