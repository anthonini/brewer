CREATE TABLE user (
    id_user BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(120) NOT NULL,
    active BOOLEAN DEFAULT true,
    birth_date DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_group (
    id_user_group BIGINT(20) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permission (
    id_permission BIGINT(20) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_group_user (
    id_user_group BIGINT(20) NOT NULL,
    id_user BIGINT(20) NOT NULL,
    PRIMARY KEY (id_user_group, id_user),
    FOREIGN KEY (id_user_group) REFERENCES user_group(id_user_group),
    FOREIGN KEY (id_user) REFERENCES user(id_user)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_group_permission (
    id_user_group BIGINT(20) NOT NULL,
    id_permission BIGINT(20) NOT NULL,
    PRIMARY KEY (id_user_group, id_permission),
    FOREIGN KEY (id_user_group) REFERENCES user_group(id_user_group),
    FOREIGN KEY (id_permission) REFERENCES permission(id_permission)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;