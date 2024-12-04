package ru.yandex.masterskaya.model.manager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "managers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Manager {

    @Id
    @Column(name = "manager_id", unique = true)
    private Long userId;

    @Column(name = "manager_role")
    @Enumerated(EnumType.STRING)
    private ManagerRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Manager manager = (Manager) o;
        return userId.equals(manager.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
