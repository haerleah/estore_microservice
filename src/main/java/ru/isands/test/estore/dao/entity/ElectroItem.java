package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "store_electro_item")
public class ElectroItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Идентификатор товара
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "electro_item_counter"
    )
    @TableGenerator(
            name = "electro_item_counter",
            table = "counter",
            pkColumnName = "name",
            pkColumnValue = "ru.isands.test.estore.dao.entity.ElectroGood",
            valueColumnName = "currentid",
            allocationSize = 2
    )
    @Column(name = "id_", unique = true, nullable = false)
    Long id;

    /**
     * Название товара
     */
    @Column(name = "name", nullable = false, length = 150)
    String name;

    /**
     * Ссылка на тип товара
     */
    @Column(name = "etype_id", nullable = false)
    Long typeId;

    /**
     * Цена товара
     */
    @Column(name = "price", nullable = false)
    Long price;

    /**
     * Количество товара в магазинах
     */
    @Column(name = "count", nullable = false)
    Long count;

    /**
     * Признак архивности товара (true - нет в наличии, false - в продаже)
     */
    @Column(name = "archive", nullable = false)
    boolean archive;

    /**
     * Описание товара
     */
    @Column(name = "description")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String description;
}
