CREATE TABLE client (
    id_client BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    person_tipe VARCHAR(15) NOT NULL,
    cpf_cnpj VARCHAR(30),
    phone VARCHAR(20),
    email VARCHAR(50) NOT NULL,
    adress VARCHAR(50),
    number VARCHAR(15),
    address_2 VARCHAR(20),
    zip_code VARCHAR(15),
    id_city BIGINT(20),
    FOREIGN KEY (id_city) REFERENCES city(id_city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;