package ru.yandex.masterskaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.masterskaya.model.manager.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

}
