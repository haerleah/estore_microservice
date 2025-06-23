package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "store_purchase_type")
public class PurchaseType {

    /**
     * Идентификатор типа электроники
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "purchase_type_counter"
    )
    @TableGenerator(
            name = "purchase_type_counter",
            pkColumnName = "name",
            pkColumnValue = "ru.isands.test.estore.dao.entity.PurchaseType",
            table = "counter",
            valueColumnName = "currentid",
            allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;

    /**
     * Наименование типа покупки
     */
    @Column(name = "name", length = 150)
    String name;
}
