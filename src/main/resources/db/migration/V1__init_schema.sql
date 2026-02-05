-- ======================
-- SEQUENCES
-- ======================

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS genre_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS book_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS password_reset_token_seq START WITH 1 INCREMENT BY 1;

-- ======================
-- USERS
-- ======================

CREATE TABLE users (
    id BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    phone VARCHAR(50),
    auth_provider VARCHAR(50),
    google_id VARCHAR(255),
    profile_image VARCHAR(255),
    last_login TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- ======================
-- GENRE
-- ======================

CREATE TABLE genre (
    id BIGINT PRIMARY KEY DEFAULT nextval('genre_seq'),
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    display_order INTEGER DEFAULT 0,
    active BOOLEAN NOT NULL,
    parent_genre_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_genre_parent
        FOREIGN KEY (parent_genre_id) REFERENCES genre(id)
);

-- ======================
-- GENRE SUB GENRES (OneToMany için JOIN TABLE)
-- ======================

CREATE TABLE genre_sub_genres (
    genre_id BIGINT NOT NULL,
    sub_genres_id BIGINT NOT NULL,
    CONSTRAINT pk_genre_sub_genres PRIMARY KEY (genre_id, sub_genres_id),
    CONSTRAINT fk_gsg_genre FOREIGN KEY (genre_id) REFERENCES genre(id),
    CONSTRAINT fk_gsg_sub FOREIGN KEY (sub_genres_id) REFERENCES genre(id)
);

-- ======================
-- BOOK
-- ======================

CREATE TABLE book (
    id BIGINT PRIMARY KEY DEFAULT nextval('book_seq'),
    isbn VARCHAR(255) UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre_id BIGINT NOT NULL,
    publisher VARCHAR(255),
    published_date DATE,
    language VARCHAR(255),
    pages INTEGER,
    description TEXT,
    total_copies INTEGER NOT NULL,
    price NUMERIC(10,2),
    available_copies INTEGER NOT NULL,
    cover_image_url VARCHAR(255),
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_book_genre
        FOREIGN KEY (genre_id) REFERENCES genre(id)
);

-- ======================
-- PASSWORD RESET TOKEN
-- ======================

CREATE TABLE password_reset_token (
    id BIGINT PRIMARY KEY DEFAULT nextval('password_reset_token_seq'),
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_prt_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);
