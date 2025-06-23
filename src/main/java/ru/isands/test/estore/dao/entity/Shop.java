package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "store_shop")
public class Shop implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Идентификатор магазина
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "shop_counter"
    )
    @TableGenerator(
            name = "shop_counter",
            pkColumnName = "name",
            pkColumnValue = "ru.isands.test.estore.dao.entity.Shop",
            table = "counter",
            valueColumnName = "currentid",
            allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;

    /**
     * Наименование магазина
     */
    @Column(name = "name", length = 150)
    String name;

    /**
     * Адрес магазина
     */
    @Column(name = "address")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String address;
}
