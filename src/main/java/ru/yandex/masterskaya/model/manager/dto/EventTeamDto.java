package ru.yandex.masterskaya.model.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventTeamDto {
    private Long eventId;
    private List<ManagerDto> personnel;
}
