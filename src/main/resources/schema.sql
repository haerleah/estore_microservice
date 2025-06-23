CREATE TABLE IF NOT EXISTS store_position_type
(
    id_  int8         NOT NULL,
    name varchar(150) NOT NULL,
    CONSTRAINT store_position_type_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS store_electro_type
(
    id_  int8         NOT NULL,
    name varchar(150) NOT NULL,
    CONSTRAINT store_electro_type_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS store_purchase_type
(
    id_  int8         NOT NULL,
    name varchar(150) NOT NULL,
    CONSTRAINT store_purchase_type_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS store_shop
(
    id_     int8         NOT NULL,
    address text         NOT NULL,
    name    varchar(150) NOT NULL,
    CONSTRAINT store_shop_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS store_employee
(
    id_         int8         NOT NULL,
    lastname    varchar(100) NOT NULL,
    firstname   varchar(100) NOT NULL,
    patronymic  varchar(100) NOT NULL,
    birth_date  timestamp    NOT NULL,
    position_id int8         NOT NULL,
    shop_id     int8         NOT NULL,
    gender      bool         NOT NULL,
    CONSTRAINT store_employee_pkey PRIMARY KEY (id_),
    CONSTRAINT fk_employee_position_type FOREIGN KEY (position_id) REFERENCES store_position_type (id_),
    CONSTRAINT fk_employee_shop FOREIGN KEY (shop_id) REFERENCES store_shop (id_)
);

CREATE TABLE IF NOT EXISTS store_electro_item
(
    id_         int8         NOT NULL,
    archive     bool         NOT NULL,
    count       bigint       NOT NULL,
    description text         NOT NULL,
    name        varchar(150) NOT NULL,
    price       bigint       NOT NULL,
    etype_id    int8         NOT NULL,
    CONSTRAINT store_electro_item_pkey PRIMARY KEY (id_),
    CONSTRAINT fk_electro_item_electro_type FOREIGN KEY (etype_id) REFERENCES store_electro_type (id_)
);

CREATE TABLE IF NOT EXISTS store_purchase
(
    id_           int8      NOT NULL,
    electro_id    int8      NOT NULL,
    employee_id   int8      NOT NULL,
    purchase_date timestamp NOT NULL,
    shop_id       int8      NOT NULL,
    type_id       int8      NOT NULL,
    CONSTRAINT store_purchase_pkey PRIMARY KEY (id_),
    CONSTRAINT fk_purchase_electro_item FOREIGN KEY (electro_id) REFERENCES store_electro_item (id_),
    CONSTRAINT fk_purchase_employee FOREIGN KEY (employee_id) REFERENCES store_employee (id_),
    CONSTRAINT fk_purchase_shop FOREIGN KEY (shop_id) REFERENCES store_shop (id_),
    CONSTRAINT fk_purchase_purchase_type FOREIGN KEY (type_id) REFERENCES store_purchase_type (id_)
);

CREATE TABLE IF NOT EXISTS store_electro_item_shop
(
    electro_item_id int8    NOT NULL,
    shop_id         int8    NOT NULL,
    count           integer NOT NULL,
    CONSTRAINT store_store_electro_item_shop_pkey PRIMARY KEY (electro_item_id, shop_id),
    CONSTRAINT fk_store_electro_item_shop_shop FOREIGN KEY (shop_id) REFERENCES store_shop (id_),
    CONSTRAINT fk_store_electro_item_shop_electro_item FOREIGN KEY (electro_item_id) REFERENCES store_electro_item (id_)
);

CREATE TABLE IF NOT EXISTS store_electro_type_employee
(
    electro_type_id int8 NOT NULL,
    employee_id     int8 NOT NULL,
    CONSTRAINT store_eshop_pkey PRIMARY KEY (electro_type_id, employee_id),
    CONSTRAINT fk_store_electro_type_employee_electro_type FOREIGN KEY (electro_type_id) REFERENCES store_electro_type (id_),
    CONSTRAINT fk_store_electro_type_employee_employee FOREIGN KEY (employee_id) REFERENCES store_employee (id_)
);

CREATE TABLE IF NOT EXISTS counter
(
    "name"    varchar(75) NOT NULL,
    currentid int8        NULL,
    CONSTRAINT counter_pkey PRIMARY KEY (name)
);