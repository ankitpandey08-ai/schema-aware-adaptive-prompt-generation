CREATE TABLE IF NOT EXISTS customers (
  id BIGINT PRIMARY KEY,
  customer_name VARCHAR(160) NOT NULL,
  region VARCHAR(80),
  created_at TIMESTAMP
);
CREATE TABLE IF NOT EXISTS orders (
  id BIGINT PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  order_status VARCHAR(40),
  total_amount DECIMAL(12,2),
  created_at TIMESTAMP
);
