DROP DATABASE IF EXISTS onlineshop;
CREATE DATABASE onlineshop;
USE onlineshop;

    CREATE TABLE categories (
	id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    id_category INT(11),
    PRIMARY KEY (id),
    UNIQUE KEY (name)
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
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    login VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(50)  NOT NULL,
    postal_address VARCHAR(50)  NOT NULL,
    phone_number VARCHAR(50)  NOT NULL,
    cash INT(11) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY (login),
    UNIQUE KEY (email),
    UNIQUE KEY (phone_number)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

	CREATE TABLE baskets (
    id_client INT(11) NOT NULL,
    id_product INT(11) NOT NULL,
    amount  INT(11) NOT NULL,
    PRIMARY KEY (id_client, id_product),
    FOREIGN KEY (id_client) REFERENCES clients (id) ON DELETE CASCADE,
    FOREIGN KEY (id_product) REFERENCES products (id) ON DELETE CASCADE
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

    CREATE TABLE administrators (
    id INT(11) NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    post VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (login)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

    CREATE TABLE products_categories (
    id INT(11) NOT NULL AUTO_INCREMENT,
    id_product INT(11) NOT NULL,
    id_category INT(11) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (id_product, id_category),
    FOREIGN KEY (id_product) REFERENCES products (id) ON DELETE CASCADE,
    FOREIGN KEY (id_category) REFERENCES categories (id) ON DELETE CASCADE
    ) ENGINE=INNODB DEFAULT CHARSET=utf8;

    <---CREATE TABLE baskets_products (
    <---id INT(11) NOT NULL AUTO_INCREMENT,
    <---id_basket INT(11) NOT NULL,
    <---id_product INT(11) NOT NULL,
    <---amount  INT(11) NOT NULL,
    <---PRIMARY KEY (id),
    <---UNIQUE KEY (id_basket, id_product),
    <---FOREIGN KEY (id_basket) REFERENCES baskets (id_client) ON DELETE CASCADE,
    <---FOREIGN KEY (id_product) REFERENCES products (id) ON DELETE CASCADE
    <---) ENGINE=INNODB DEFAULT CHARSET=utf8;

  <---CREATE TABLE categories_subcategories (
    <---id INT(11) NOT NULL AUTO_INCREMENT,
    <---id_category INT(11) NOT NULL,
    <---id_subcategory INT(11) NOT NULL,
    <---PRIMARY KEY (id),
    <---UNIQUE KEY (id_category, id_subcategory),
    <---FOREIGN KEY (id_category) REFERENCES categories (id) ON DELETE CASCADE,
    <---FOREIGN KEY (id_subcategory) REFERENCES categories (id) ON DELETE CASCADE
    <---) ENGINE=INNODB DEFAULT CHARSET=utf8;

    	<---CREATE TABLE users (
        <---id INT(11) NOT NULL AUTO_INCREMENT,
        <---firstName VARCHAR(50) NOT NULL,
       <--- lastName VARCHAR(50) NOT NULL,
        <---patronymic VARCHAR(50),
       <--- post VARCHAR(50),
        <---login VARCHAR(50) NOT NULL,
        <---password VARCHAR(50) NOT NULL,
       <--- email VARCHAR(50),
        <---postal_address VARCHAR(50),
     <---   phone_number VARCHAR(50),
   <---     id_role INT(11) NOT NULL,
    <---    id_card INT(11),
     <---   id_basket  INT(11),
    <---    PRIMARY KEY (id),
    <---    UNIQUE KEY (login),
     <---   UNIQUE KEY (email),
     <---   UNIQUE KEY (phone_number),
      <---  UNIQUE KEY (id_card),
      <---  UNIQUE KEY (id_basket),
       <--- FOREIGN KEY (id_role) REFERENCES roles (id) ON DELETE CASCADE,
       <--- FOREIGN KEY (id_card) REFERENCES cards (id) ON DELETE CASCADE,
      <---  FOREIGN KEY (id_basket) REFERENCES baskets (id) ON DELETE CASCADE
      <---  ) ENGINE=INNODB DEFAULT CHARSET=utf8;

          <---CREATE TABLE cards (
          <---id INT(11) NOT NULL AUTO_INCREMENT,
          <---cash INT(11) NOT NULL DEFAULT 0,
         <--- PRIMARY KEY (id)
          <---) ENGINE=INNODB DEFAULT CHARSET=utf8;

              <---CREATE TABLE roles (
              <---id INT(11) NOT NULL AUTO_INCREMENT,
              <---name VARCHAR(50) NOT NULL,
              <---PRIMARY KEY (id),
              <---UNIQUE KEY (name)
              <---) ENGINE=INNODB DEFAULT CHARSET=utf8;