package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "store_position_type")
public class PositionType {
    /**
     * Идентификатор должности
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "position_counter"
    )
    @TableGenerator(
            name = "position_counter",
            pkColumnName = "name",
            pkColumnValue = "ru.isands.test.estore.dao.entity.PositionType",
            table = "counter",
            valueColumnName = "currentid",
            allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;

    /**
     * Наименование должности
     */
    @Column(name = "name", length = 150)
    String name;
}
