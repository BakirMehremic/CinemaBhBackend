CREATE TYPE "movie_status" AS ENUM (
  'draft',
  'published',
  'archived'
);

CREATE TYPE "personnel_type" AS ENUM (
  'director',
  'writer',
  'cast'
);

CREATE TYPE "seat_type" AS ENUM (
  'regular',
  'vip',
  'love_seat'
);

CREATE TYPE "user_role" AS ENUM (
  'admin',
  'registered_user'
);

CREATE TYPE "draft_step_status" AS ENUM (
  'step_1',
  'step_2',
  'step_3'
);

CREATE TABLE "users" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "role" user_role NOT NULL,
  "password_hash" varchar(255) NOT NULL,
  "first_name" varchar(255) NOT NULL,
  "last_name" varchar(255),
  "phone_number" varchar(255) UNIQUE,
  "email" varchar(255) UNIQUE NOT NULL,
  "image_url" varchar(255),
  "created_at"  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  "city_id" integer
);

CREATE TABLE "countries" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" varchar(255) UNIQUE NOT NULL
);

CREATE TABLE "cities" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" varchar(255) NOT NULL,
  "country_id" integer NOT NULL,

  UNIQUE("name", "country_id")
);

CREATE TABLE "venues" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" varchar(255) NOT NULL,
  "street" varchar(255) NOT NULL,
  "street_number" varchar(255),
  "phone" varchar(255),
  "image_url" varchar(255),
  "city_id" integer NOT NULL
);

CREATE TABLE "halls" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "hall_name" varchar(255),
  "venue_id" integer NOT NULL
);

CREATE TABLE "movies" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" varchar(255) NOT NULL,
  "pg_rating" varchar(255) NOT NULL,
  "language" varchar(255) NOT NULL,
  "duration" integer NOT NULL,
  "trailer_link" varchar(255),
  "synopsis" varchar(255),
  "status" movie_status NOT NULL,
  "imdb_rating" decimal(2,1),
  "rotten_tomatoes_rating" decimal(2,1),
  "start_showing_date" date NOT NULL,
  "end_showing_date" date NOT NULL,
  "draft_step" draft_step_status
);

CREATE TABLE "photos" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "image_url" varchar(255) NOT NULL,
  "is_cover_photo" boolean,
  "movie_id" integer NOT NULL
);

CREATE TABLE "genres" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" varchar(255) NOT NULL
);

CREATE TABLE "movies_genres" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "genre_id" integer NOT NULL,
  "movie_id" integer NOT NULL
);

CREATE TABLE "personnel" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "name" varchar(255) NOT NULL,
  "type" personnel_type NOT NULL
);

CREATE TABLE "movies_personnel" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "movie_id" integer NOT NULL,
  "personnel_id" integer NOT NULL
);

CREATE TABLE "seats" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "row_letter" varchar(255) NOT NULL,
  "row_number" integer NOT NULL,
  "hall_id" integer NOT NULL,
  "seat_type" "seat_type" NOT NULL,

  UNIQUE("row_letter", "row_number", "hall_id")
);

CREATE TABLE "projections" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "movie_id" integer NOT NULL,
  "hall_id" integer NOT NULL,
  "start_time" timestamp NOT NULL,
  "end_time" timestamp NOT NULL
);

CREATE TABLE "users_projections_seats" (
  "id" INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  "projection_id" integer NOT NULL,
  "seat_id" integer NOT NULL,
  "user_id" integer NOT NULL,

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
