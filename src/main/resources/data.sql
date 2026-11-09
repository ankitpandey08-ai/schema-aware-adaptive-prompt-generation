INSERT INTO customers(id, customer_name, region, created_at) VALUES
 (1, 'Acme Ltd', 'APAC', CURRENT_TIMESTAMP),
 (2, 'Northwind', 'EMEA', CURRENT_TIMESTAMP);
INSERT INTO orders(id, customer_id, order_status, total_amount, created_at) VALUES
 (1001, 1, 'PENDING', 1200.00, CURRENT_TIMESTAMP),
 (1002, 2, 'SHIPPED', 450.00, CURRENT_TIMESTAMP);
