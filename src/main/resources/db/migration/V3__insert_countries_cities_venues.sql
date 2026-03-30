INSERT INTO "countries" ("name") VALUES
('USA'),
('France'),
('Germany');

INSERT INTO "cities" ("name", "country_id") VALUES
('New York', 1),
('Los Angeles', 1),
('Paris', 2),
('Berlin', 3);

INSERT INTO "venues" ("name", "street", "street_number", "phone", "image_url", "city_id") VALUES
('Madison Square Garden', '7th Ave', '4', '123-456-7890', 'https://example.com/msg.jpg', 1),
('Staples Center', 'Figueroa St', '1111', '987-654-3210', 'https://example.com/staples.jpg', 2),
('Le Grand Rex', 'Boulevard Poissonnière', '1', '+33 1 42 80 44 00', 'https://example.com/grandrex.jpg', 3),
('Berliner Philharmonie', 'Herbert-von-Karajan-Str.', '1', '+49 30 254 88 0', 'https://example.com/philharmonie.jpg', 4);