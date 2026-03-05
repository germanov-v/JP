
CREATE INDEX IF NOT EXISTS ix_order_item_order_id ON market.order_item(order_id);
CREATE INDEX IF NOT EXISTS ix_order_item_product_id ON market.order_item(product_id);
CREATE INDEX IF NOT EXISTS ix_order_item_guid_id ON market.order_item(guid_id);