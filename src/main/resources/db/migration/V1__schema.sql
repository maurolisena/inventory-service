-- Verifica la base de datos
SHOW DATABASES;

-- Verifica que el usuario exista
SELECT user, host FROM mysql.user WHERE user = 'mlisena';

GRANT ALL PRIVILEGES ON inventory_db.* TO 'mlisena'@'%' IDENTIFIED BY 'A8424628';
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