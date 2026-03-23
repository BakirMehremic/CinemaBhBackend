INSERT INTO "movies" (
  "name",
  "pg_rating",
  "language",
  "duration",
  "trailer_link",
  "synopsis",
  "status",
  "imdb_rating",
  "rotten_tomatoes_rating",
  "start_showing_date",
  "end_showing_date",
  "draft_step"
) VALUES
(
  'Inception',
  'PG-13',
  'English',
  148,
  'https://youtube.com/watch?v=YoHD9XEInc0',
  'A thief who steals corporate secrets through dream-sharing technology.',
  'PUBLISHED',
  8.8,
  8.7,
  '2026-03-01',
  '2026-04-15',
  NULL
),
(
  'The Dark Knight',
  'PG-13',
  'English',
  152,
  'https://youtube.com/watch?v=EXeTwQWrcwY',
  'Batman faces the Joker, a criminal mastermind causing chaos in Gotham.',
  'DRAFT',
  9.0,
  9.4,
  '2026-03-10',
  '2026-05-01',
  NULL
),
(
  'Interstellar',
  'PG-13',
  'English',
  169,
  'https://youtube.com/watch?v=zSWdZVtXT7E',
  'A team travels through a wormhole in space to ensure humanity survival.',
  'DRAFT',
  8.6,
  8.3,
  '2026-04-01',
  '2026-05-20',
  'STEP_2'
),
(
  'Parasite',
  'R',
  'Korean',
  132,
  'https://youtube.com/watch?v=5xH0HfJHsaY',
  'A poor family schemes to become employed by a wealthy household.',
  'ARCHIVED',
  8.5,
  9.1,
  '2026-02-01',
  '2026-03-01',
  NULL
),
(
  'Avengers: Endgame',
  'PG-13',
  'English',
  181,
  'https://youtube.com/watch?v=TcMBFSGVi1c',
  'The Avengers assemble once more to reverse Thanos actions.',
  'DRAFT',
  8.4,
  9.0,
  '2026-03-15',
  '2026-06-01',
  NULL
);

INSERT INTO genres (name) VALUES ('Action');
INSERT INTO genres (name) VALUES ('Sci-Fi');
INSERT INTO genres (name) VALUES ('Thriller');
INSERT INTO genres (name) VALUES ('Drama');

INSERT INTO movies_genres (movie_id, genre_id) VALUES (1, 2);
INSERT INTO movies_genres (movie_id, genre_id) VALUES (1, 3);

INSERT INTO movies_genres (movie_id, genre_id) VALUES (2, 1);
INSERT INTO movies_genres (movie_id, genre_id) VALUES (2, 3);

INSERT INTO movies_genres (movie_id, genre_id) VALUES (3, 2);
INSERT INTO movies_genres (movie_id, genre_id) VALUES (3, 4);

INSERT INTO photos (image_url, is_cover_photo, movie_id) VALUES ('inception_cover.jpg', true, 1);
INSERT INTO photos (image_url, is_cover_photo, movie_id) VALUES ('inception_scene1.jpg', false, 1);

INSERT INTO photos (image_url, is_cover_photo, movie_id) VALUES ('dark_knight_cover.jpg', true, 2);
INSERT INTO photos (image_url, is_cover_photo, movie_id) VALUES ('dark_knight_scene1.jpg', false, 2);

INSERT INTO photos (image_url, is_cover_photo, movie_id) VALUES ('interstellar_cover.jpg', true, 3);
INSERT INTO photos (image_url, is_cover_photo, movie_id) VALUES ('interstellar_scene1.jpg', false, 3);