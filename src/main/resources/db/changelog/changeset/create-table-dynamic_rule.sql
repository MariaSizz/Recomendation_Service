CREATE TABLE dynamic_rules (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(255),
    product_id VARCHAR(255) UNIQUE,
    product_text TEXT,
    rule JSONB
);