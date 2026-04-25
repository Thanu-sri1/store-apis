CREATE TABLE IF NOT EXISTS categories (
    id TINYINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description LONGTEXT NOT NULL,
    category_id TINYINT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Seed initial data
INSERT IGNORE INTO categories (id, name) VALUES (1, 'Men'), (2, 'Women'), (3, 'Kids');

INSERT IGNORE INTO products (name, price, description, category_id) VALUES
('Classic White T-Shirt', 499.00, '100% cotton round neck t-shirt', 1),
('Blue Slim Fit Jeans', 1299.00, 'Slim fit denim jeans for men', 1),
('Women Floral Summer Dress', 1599.00, 'Lightweight floral printed dress', 2),
('Black Leather Handbag', 2499.00, 'Premium leather handbag for women', 2),
('Kids Cartoon Hoodie', 799.00, 'Warm hoodie with cartoon prints', 3);
