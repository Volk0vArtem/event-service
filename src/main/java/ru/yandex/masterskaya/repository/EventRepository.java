package ru.yandex.masterskaya.repository;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.masterskaya.model.event.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOwnerId(Long ownerId, PageRequest pageRequest);
}
