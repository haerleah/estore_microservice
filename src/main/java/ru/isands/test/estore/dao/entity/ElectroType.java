package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "store_electro_type")
public class ElectroType {
    /**
     * Идентификатор типа электроники
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "electro_type_counter"
    )
    @TableGenerator(
            name = "electro_type_counter",
            pkColumnName = "name",
            pkColumnValue = "ru.isands.test.estore.dao.entity.ElectroType",
            table = "counter",
            valueColumnName = "currentid",
            allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;

    /**
     * Наименование типа электроники
     */
    @Column(name = "name", length = 150)
    String name;
}
