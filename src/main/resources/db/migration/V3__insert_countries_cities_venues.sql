INSERT INTO "countries" ("name")
VALUES ('USA'),
       ('France'),
       ('Germany'),
       ('Bosnia and Herzegovina');

INSERT INTO "cities" ("name", "country_id")
VALUES ('New York', 1),
       ('Los Angeles', 1),
       ('Paris', 2),
       ('Berlin', 3),
       ('Sarajevo', 4),
       ('Banja Luka', 4),
       ('Mostar', 4),
       ('Tuzla', 4),
       ('Zenica', 4);

INSERT INTO "venues" ("name", "street", "street_number", "phone", "image_url", "city_id")
VALUES ('Cineplex', 'Ulica Street', '2', '987-654-3210', 'https://ik.imagekit.io/4oxyrugmfe/cineplex.jpeg', 6),
       ('Meeting Point', 'Cinema St', '121', '987-654-3210', 'https://ik.imagekit.io/4oxyrugmfe/meeting.jpg', 7),
       ('Cinema City', 'Kino', '17', '987-654-3210', 'https://ik.imagekit.io/4oxyrugmfe/cinema-city.jpg', 7),
       ('SFF', 'Figueroa St', '90', '987-654-3210', 'https://ik.imagekit.io/4oxyrugmfe/sff.jpg', 5),
       ('Kino Atlant', 'Ulica St', '11', '987-654-3210', 'https://ik.imagekit.io/4oxyrugmfe/kino.jpg', 5);