package ru.yandex.masterskaya.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(min = 1, max = 500)
    private String name;

    @Column(name = "description")
    @Size(min = 1, max = 1000)
    private String description;

    @Column(name = "created")
    private LocalDateTime createdDateTime;

    @Column(name = "start_datetime")
    @Future
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    @Future
    private LocalDateTime endDateTime;

    @Column(name = "location")
    @Size(min = 1, max = 100)
    private String location;

    @Column(name = "owner_id")
    @Positive
    private Long ownerId;
}
