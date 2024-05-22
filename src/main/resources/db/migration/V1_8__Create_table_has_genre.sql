CREATE TABLE IF NOT EXISTS has_genre (
    id SERIAL PRIMARY KEY,
    anime_id INT NOT NULL,
    genre_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (anime_id) REFERENCES animes (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
);