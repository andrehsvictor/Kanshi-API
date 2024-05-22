CREATE TABLE IF NOT EXISTS animes (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    episodes INT NOT NULL,
    seasons INT NOT NULL,
    finished BOOLEAN NOT NULL,
    release_date DATE NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);