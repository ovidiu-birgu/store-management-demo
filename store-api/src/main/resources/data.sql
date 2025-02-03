-- Sample Products
INSERT INTO STORE_PRODUCT (name, description, price, stock_quantity, created_date)
VALUES ('Laptop', '15-inch 8GB RAM 512GB SSD', 999.99, 50, CURRENT_TIMESTAMP()),
       ('Smartphone', '6.5-inch 128GB Storage', 699.99, 100, CURRENT_TIMESTAMP()),
       ('Headphones', 'Wireless Noise Cancelling', 249.99, 200, CURRENT_TIMESTAMP());
/*
INSERT INTO STORE_ORDER (customer_username, shipping_address, quantity, price_at_purchase, product_id, created_date)
VALUES ('customer1', 'customer1 address', 10, 999.99, 1, CURRENT_TIMESTAMP()),
       ('customer1', 'customer1 address', 2, 699.99, 2, CURRENT_TIMESTAMP()),
       ('customer2', 'customer2 address', 3, 699.99, 2, CURRENT_TIMESTAMP());
*/
