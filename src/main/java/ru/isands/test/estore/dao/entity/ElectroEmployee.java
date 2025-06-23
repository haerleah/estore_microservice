package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@IdClass(ElectroEmployeePK.class)
@Table(name = "store_electro_type_employee")
public class ElectroEmployee {
    /**
     * Идентификатор сотрудника
     */
    @Id
    @Column(name = "employeeId", nullable = false)
    Long employeeId;

    /**
     * Идентификатор типа электротовара
     */
    @Id
    @Column(name = "electroTypeId", nullable = false)
    Long electroTypeId;
}
