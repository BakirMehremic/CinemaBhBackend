CREATE TYPE "movie_status" AS ENUM (
  'DRAFT',
  'PUBLISHED',
  'ARCHIVED'
);

CREATE TYPE "personnel_type" AS ENUM (
  'DIRECTOR',
  'WRITER',
  'CAST'
);

CREATE TYPE "seat_type" AS ENUM (
  'REGULAR',
  'VIP',
  'LOVE_SEAT'
);

CREATE TYPE "user_role" AS ENUM (
  'ADMIN',
  'REGISTERED USER'
);

CREATE TYPE "draft_step_status" AS ENUM (
  'BASIC_INFO',
  'PHOTOS_AND_CAST',
  'PROJECTIONS'
);

CREATE TABLE "users" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "role" user_role NOT NULL,
  "password_hash" VARCHAR(255) NOT NULL,
  "first_name" VARCHAR(100) NOT NULL,
  "last_name" VARCHAR(80),
  "phone_number" VARCHAR(25) UNIQUE,
  "email" VARCHAR(255) UNIQUE NOT NULL,
  "image_url" VARCHAR(255),
  "created_at"  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "city_id" BIGINT
);

CREATE TABLE "countries" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE "cities" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" VARCHAR(100) NOT NULL,
  "country_id" BIGINT NOT NULL,

  UNIQUE("name", "country_id")
);

CREATE TABLE "venues" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" VARCHAR(100) NOT NULL,
  "street" VARCHAR(100) NOT NULL,
  "street_number" VARCHAR(20),
  "phone" VARCHAR(255),
  "image_url" VARCHAR(255),
  "city_id" BIGINT NOT NULL
);

CREATE TABLE "halls" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "hall_name" VARCHAR(100),
  "venue_id" BIGINT NOT NULL
);

CREATE TABLE "movies" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" VARCHAR(200) NOT NULL,
  "pg_rating" VARCHAR(20) NOT NULL,
  "language" VARCHAR(50) NOT NULL,
  "duration_minutes" INTEGER NOT NULL,
  "trailer_link" VARCHAR(255),
  "synopsis" TEXT,
  "status" movie_status NOT NULL,
  "imdb_rating" DECIMAL(2,1),
  "rotten_tomatoes_rating" DECIMAL(2,1),
  "start_showing_date" DATE NOT NULL,
  "end_showing_date" DATE NOT NULL,
  "draft_step" draft_step_status
);

CREATE TABLE "photos" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "image_url" VARCHAR(255) NOT NULL,
  "is_cover_photo" BOOLEAN,
  "movie_id" BIGINT NOT NULL
);

CREATE TABLE "genres" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" VARCHAR(100) NOT NULL
);

CREATE TABLE "movies_genres" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "genre_id" BIGINT NOT NULL,
  "movie_id" BIGINT NOT NULL
);

CREATE TABLE "personnel" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" VARCHAR(100) NOT NULL,
  "type" personnel_type NOT NULL
);

CREATE TABLE "movies_personnel" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "movie_id" BIGINT NOT NULL,
  "personnel_id" BIGINT NOT NULL
);

CREATE TABLE "seats" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "row_letter" VARCHAR(5) NOT NULL,
  "row_number" BIGINT NOT NULL,
  "hall_id" BIGINT NOT NULL,
  "seat_type" seat_type NOT NULL,

  UNIQUE("row_letter", "row_number", "hall_id")
);

CREATE TABLE "projections" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "movie_id" BIGINT NOT NULL,
  "hall_id" BIGINT NOT NULL,
  "start_time" TIMESTAMP NOT NULL,
  "end_time" TIMESTAMP NOT NULL
);

CREATE TABLE "users_projections_seats" (
  "id" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "projection_id" BIGINT NOT NULL,
  "seat_id" BIGINT NOT NULL,
  "user_id" BIGINT NOT NULL,

  UNIQUE("seat_id", "user_id", "projection_id")
);

CREATE UNIQUE INDEX ON "cities" ("name", "country_id");

ALTER TABLE "cities" ADD FOREIGN KEY ("country_id") REFERENCES "countries" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "users" ADD FOREIGN KEY ("city_id") REFERENCES "cities" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "venues" ADD FOREIGN KEY ("city_id") REFERENCES "cities" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "movies_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "movies_genres" ADD FOREIGN KEY ("movie_id") REFERENCES "movies" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "photos" ADD FOREIGN KEY ("movie_id") REFERENCES "movies" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "movies_personnel" ADD FOREIGN KEY ("personnel_id") REFERENCES "personnel" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "movies_personnel" ADD FOREIGN KEY ("movie_id") REFERENCES "movies" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "seats" ADD FOREIGN KEY ("hall_id") REFERENCES "halls" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "projections" ADD FOREIGN KEY ("hall_id") REFERENCES "halls" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "projections" ADD FOREIGN KEY ("movie_id") REFERENCES "movies" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "users_projections_seats" ADD FOREIGN KEY ("projection_id") REFERENCES "projections" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "users_projections_seats" ADD FOREIGN KEY ("seat_id") REFERENCES "seats" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "users_projections_seats" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "halls" ADD FOREIGN KEY ("venue_id") REFERENCES "venues" ("id") DEFERRABLE INITIALLY IMMEDIATE;
