package ru.yandex.masterskaya.model.event;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.masterskaya.model.manager.Manager;

@Entity
@Table(name = "events")
@Data
@RequiredArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private LocalDateTime createdDateTime;

    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

    @Column(name = "location")
    private String location;

    @Column(name = "owner_id")
    private Long ownerId;

    @OneToMany
    @JoinTable(
        name = "event_team_managers",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "manager_id")
    )
    private Set<Manager> personnel = new HashSet<>();
}
