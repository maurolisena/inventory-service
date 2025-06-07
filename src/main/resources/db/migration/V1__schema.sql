-- Verifica la base de datos
SHOW DATABASES;

-- Verifica o crea el usuario
CREATE USER IF NOT EXISTS 'mlisena'@'%' IDENTIFIED BY 'A8424628';
GRANT ALL PRIVILEGES ON inventory_db.* TO 'mlisena'@'%';
FLUSH PRIVILEGES;


-- Verifica que tenga permisos sobre la base de datos
SHOW GRANTS FOR 'mlisena'@'%';

CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku_code VARCHAR(255) NOT NULL,
    quantity INT NOT NULL
);