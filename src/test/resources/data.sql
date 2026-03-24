INSERT INTO "movies" ("name",
                      "pg_rating",
                      "language",
                      "duration_minutes",
                      "trailer_link",
                      "synopsis",
                      "status",
                      "imdb_rating",
                      "rotten_tomatoes_rating",
                      "start_showing_date",
                      "end_showing_date",
                      "draft_step")
VALUES ('TEST_Movie_Inception',
        'PG-13',
        'English',
        100,
        'https://example.com/test-inception',
        'TEST synopsis for Inception',
        'PUBLISHED',
        1.1,
        1.2,
        '2026-01-01',
        '2026-02-01',
        NULL),

       ('TEST_Movie_DarkKnight',
        'PG-13',
        'English',
        110,
        'https://example.com/test-darkknight',
        'TEST synopsis for Dark Knight',
        'DRAFT',
        2.1,
        2.2,
        '2026-01-05',
        '2026-02-05',
        NULL),

       ('TEST_Movie_Interstellar',
        'PG-13',
        'English',
        120,
        'https://example.com/test-interstellar',
        'TEST synopsis for Interstellar',
        'DRAFT',
        3.1,
        3.2,
        '2026-01-10',
        '2026-02-10',
        'TEST_STEP'),

       ('TEST_Movie_Parasite',
        'R',
        'Korean',
        90,
        'https://example.com/test-parasite',
        'TEST synopsis for Parasite',
        'ARCHIVED',
        4.1,
        4.2,
        '2026-01-15',
        '2026-02-15',
        NULL),

       ('TEST_Movie_Endgame',
        'PG-13',
        'English',
        130,
        'https://example.com/test-endgame',
        'TEST synopsis for Endgame',
        'DRAFT',
        5.1,
        5.2,
        '2026-01-20',
        '2026-02-20',
        NULL);

INSERT INTO genres (name)
VALUES ('TEST_Action'),
       ('TEST_SciFi'),
       ('TEST_Thriller'),
       ('TEST_Drama');

INSERT INTO movies_genres (movie_id, genre_id)
VALUES (1, 2),
       (1, 3),
       (2, 1),
       (2, 3),
       (3, 2),
       (3, 4);

INSERT INTO photos (image_url, is_cover_photo, movie_id)
VALUES ('test_inception_cover.jpg', true, 1),
       ('test_inception_scene.jpg', false, 1),

       ('test_darkknight_cover.jpg', true, 2),
       ('test_darkknight_scene.jpg', false, 2),

       ('test_interstellar_cover.jpg', true, 3),
       ('test_interstellar_scene.jpg', false, 3);

INSERT INTO "countries" ("name")
VALUES ('TEST_USA'),
       ('TEST_France'),
       ('TEST_Germany');

INSERT INTO "cities" ("name", "country_id")
VALUES ('TEST_NewYork', 1),
       ('TEST_LosAngeles', 1),
       ('TEST_Paris', 2),
       ('TEST_Berlin', 3);

INSERT INTO "venues" ("name", "street", "street_number", "phone", "image_url", "city_id")
VALUES ('TEST_Venue_1', 'TEST_Street_1', '1', '000-000-0001', 'https://test.com/v1.jpg', 1),
       ('TEST_Venue_2', 'TEST_Street_2', '2', '000-000-0002', 'https://test.com/v2.jpg', 2),
       ('TEST_Venue_3', 'TEST_Street_3', '3', '000-000-0003', 'https://test.com/v3.jpg', 3),
       ('TEST_Venue_4', 'TEST_Street_4', '4', '000-000-0004', 'https://test.com/v4.jpg', 4);