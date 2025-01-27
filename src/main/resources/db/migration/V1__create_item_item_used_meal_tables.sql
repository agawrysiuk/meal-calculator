CREATE TABLE item
(
    id               UUID           NOT NULL PRIMARY KEY,
    name             TEXT           NOT NULL UNIQUE,
    calories         NUMERIC(20, 2) NOT NULL,
    protein          NUMERIC(20, 2) NOT NULL,
    fat              NUMERIC(20, 2) NOT NULL,
    carbohydrates    NUMERIC(20, 2) NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE meal
(
    id               UUID NOT NULL PRIMARY KEY,
    name             TEXT NOT NULL,
    meal_day         DATE NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE recipe
(
    id               UUID NOT NULL PRIMARY KEY,
    name             TEXT NOT NULL UNIQUE,
    link             TEXT,
    description      TEXT,
    servings         NUMBER    DEFAULT 1,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE item_used
(
    id               UUID           NOT NULL PRIMARY KEY,
    name             TEXT           NOT NULL,
    grams            NUMBER         NOT NULL,
    calories         NUMERIC(20, 2) NOT NULL,
    protein          NUMERIC(20, 2) NOT NULL,
    fat              NUMERIC(20, 2) NOT NULL,
    carbohydrates    NUMERIC(20, 2) NOT NULL,
    meal_id          UUID,
    recipe_id        UUID,
    type             TEXT           NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
