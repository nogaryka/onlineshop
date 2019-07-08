DROP DATABASE IF EXISTS onlineshop;
CREATE DATABASE onlineshop;
USE onlineshop;

    CREATE TABLE users (
    id INT(11) NOT NULL AUTO_INCREMENT,
    login VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(30) NOT NULL,
    patronymic VARCHAR(30),
    PRIMARY KEY (id),
    UNIQUE KEY (login)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

    CREATE TABLE categories (
	id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    idParentCategory INT(11),
    PRIMARY KEY (id),
    UNIQUE KEY (name),
    FOREIGN KEY (idParentCategory) REFERENCES categories (id) ON DELETE CASCADE
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

    CREATE TABLE products (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    cost INT(11) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (name)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

    CREATE TABLE clients (
    id INT(11) NOT NULL AUTO_INCREMENT,
    email VARCHAR(50)  NOT NULL,
    postal_address VARCHAR(50)  NOT NULL,
    phone_number VARCHAR(50)  NOT NULL,
    cash INT(11) NOT NULL DEFAULT 0,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY (email),
    UNIQUE KEY (phone_number)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

	CREATE TABLE baskets (
    idClient INT(11) NOT NULL,
    idProduct INT(11) NOT NULL,
    amount  INT(11) NOT NULL,
    PRIMARY KEY (idClient,  idProduct),
    FOREIGN KEY (idClient) REFERENCES clients (id) ON DELETE CASCADE,
    FOREIGN KEY (idProduct) REFERENCES products (id) ON DELETE CASCADE
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

    CREATE TABLE administrators (
    id INT(11) NOT NULL AUTO_INCREMENT,
    post VARCHAR(50) NOT NULL,
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

    CREATE TABLE products_categories (
    id INT(11) NOT NULL AUTO_INCREMENT,
    idProduct INT(11) NOT NULL,
    idCategory INT(11) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (idProduct, idCategory),
    FOREIGN KEY (idProduct) REFERENCES products (id) ON DELETE CASCADE,
    FOREIGN KEY (idCategory) REFERENCES categories (id) ON DELETE CASCADE
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;
