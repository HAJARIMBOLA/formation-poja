CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
                                     id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name  VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    user_name   VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    CONSTRAINT uq_users_user_name UNIQUE (user_name),
    CONSTRAINT uq_users_email UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS courses (
                                       id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title       VARCHAR(255) NOT NULL,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL,
    CONSTRAINT ck_courses_dates CHECK (end_date >= start_date)
    );

CREATE TABLE IF NOT EXISTS user_course (
                                           user_id     UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    course_id   UUID NOT NULL REFERENCES courses (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, course_id)
    );

CREATE INDEX IF NOT EXISTS idx_user_course_user_id ON user_course (user_id);
CREATE INDEX IF NOT EXISTS idx_user_course_course_id ON user_course (course_id);