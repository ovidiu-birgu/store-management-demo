-- Products Table
CREATE TABLE IF NOT EXISTS STORE_PRODUCT
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name               VARCHAR(100)   NOT NULL,
    description        VARCHAR(500)   NOT NULL,
    price              DECIMAL(19, 2) NOT NULL,
    stock_quantity     INT            NOT NULL,
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP
);

-- Orders Table
CREATE TABLE IF NOT EXISTS STORE_ORDER
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    customer_username VARCHAR(100)   NOT NULL,
    shipping_address  VARCHAR(200)   NOT NULL,
    quantity          INT            NOT NULL,
    price_at_purchase DECIMAL(19, 2) NOT NULL,
    product_id        BIGINT,
    created_date      TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES STORE_PRODUCT (id)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_product_id ON STORE_PRODUCT (id);
CREATE INDEX IF NOT EXISTS idx_product_name ON STORE_PRODUCT (name);
CREATE INDEX IF NOT EXISTS idx_order_username ON STORE_ORDER (customer_username);
