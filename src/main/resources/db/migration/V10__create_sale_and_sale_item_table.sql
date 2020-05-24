CREATE TABLE sale (
    id_sale BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    creation_date DATETIME NOT NULL,
    shipping_value DECIMAL(10,2),
    discount_value DECIMAL(10,2),
    total_value DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    observation VARCHAR(200),
    delivery_date_time DATETIME,
    id_client BIGINT(20) NOT NULL,
    id_user BIGINT(20) NOT NULL,
    FOREIGN KEY (id_client) REFERENCES client(id_client),
    FOREIGN KEY (id_user) REFERENCES user(id_user)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sale_item (
    id_sale_item BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    quantity INTEGER NOT NULL,
    unity_value DECIMAL(10,2) NOT NULL,
    id_beer BIGINT(20) NOT NULL,
    id_sale BIGINT(20) NOT NULL,
    FOREIGN KEY (id_beer) REFERENCES beer(id_beer),
    FOREIGN KEY (id_sale) REFERENCES sale(id_sale)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;