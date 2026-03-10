

CREATE EXTENSION IF NOT EXISTS pgcrypto; --  gen_random_uuid()

CREATE SCHEMA  IF NOT EXISTS market;

CREATE TABLE IF NOT EXISTS market.product (
    id          BIGSERIAL PRIMARY KEY,
    guid_id     UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),

    title       TEXT        NOT NULL,
    description TEXT        NULL,
    img_path    TEXT        NULL,
    price       BIGINT      NOT NULL,
    count       INTEGER     NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS ix_product_guid_id ON market.product(guid_id);

CREATE TABLE IF NOT EXISTS market.cart (
    id          BIGSERIAL PRIMARY KEY,
    guid_id     UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS ix_cart_guid_id ON market.cart(guid_id);

CREATE TABLE IF NOT EXISTS market.cart_item (
    id          BIGSERIAL PRIMARY KEY,
    guid_id     UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),

    cart_id     BIGINT      NOT NULL REFERENCES market.cart(id) ON DELETE CASCADE,
    product_id  BIGINT      NOT NULL REFERENCES market.product(id),
    quantity    INTEGER     NOT NULL CHECK (quantity > 0),

    CONSTRAINT uq_cart_item_cart_product UNIQUE (cart_id, product_id)
);

CREATE INDEX IF NOT EXISTS ix_cart_item_cart_id ON market.cart_item(cart_id);
CREATE INDEX IF NOT EXISTS ix_cart_item_product_id ON market.cart_item(product_id);
CREATE INDEX IF NOT EXISTS ix_cart_item_guid_id ON market.cart_item(guid_id);

CREATE TABLE IF NOT EXISTS market."order" (
    id          BIGSERIAL PRIMARY KEY,
    guid_id     UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS ix_order_guid_id ON market."order"(guid_id);

CREATE TABLE IF NOT EXISTS market.order_item (
    id          BIGSERIAL PRIMARY KEY,
    guid_id     UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),

    order_id    BIGINT      NOT NULL REFERENCES market."order"(id) ON DELETE CASCADE,
    product_id  BIGINT      NOT NULL REFERENCES market.product(id),
    quantity    INTEGER     NOT NULL CHECK (quantity > 0)
);

CREATE INDEX IF NOT EXISTS ix_order_item_order_id ON market.order_item(order_id);
CREATE INDEX IF NOT EXISTS ix_order_item_product_id ON market.order_item(product_id);
CREATE INDEX IF NOT EXISTS ix_order_item_guid_id ON market.order_item(guid_id);
