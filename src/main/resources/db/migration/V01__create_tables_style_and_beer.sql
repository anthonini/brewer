CREATE TABLE style (
    id_style BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE beer (
    id_beer BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    sku VARCHAR(50) NOT NULL,
    name VARCHAR(80) NOT NULL,
    description TEXT NOT NULL,
    value DECIMAL(10, 2) NOT NULL,
    alcoholic_strength DECIMAL(10, 2) NOT NULL,
    comission DECIMAL(10, 2) NOT NULL,
    stock_quantity INTEGER,
    origin VARCHAR(50) NOT NULL,
    flavor VARCHAR(50) NOT NULL,
    id_style BIGINT(20) NOT NULL,
    FOREIGN KEY (id_style) REFERENCES style(id_style)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO style VALUES (0, 'Amber Lager');
INSERT INTO style VALUES (0, 'Dark Lager');
INSERT INTO style VALUES (0, 'Pale Lager');
INSERT INTO style VALUES (0, 'Pilsner');