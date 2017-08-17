DELETE FROM votes;
DELETE FROM meals;
DELETE FROM lunches;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;
-- adminpass
INSERT INTO users (registered, type, name, email, password)
VALUES ('2017-06-15 12:30:00', 'ROLE_ADMIN', 'Admin', 'admin@gmail.com', '$2a$10$QmoxHeaZvyK40aHbE8XOOe1q9XYCdDDfu9Hoil.gBxfQDsgDuAOHW');
-- password
INSERT INTO users (registered, type, name, email, password)
VALUES ('2017-06-16 12:30:00', 'ROLE_USER', 'User', 'user@gmail.com', '$2a$10$j8lsh93dpNE/s5ywcRt0k.axNnIuDL9V33LXSTc.kKqj35qKK2Vui');
-- password2
INSERT INTO users (registered, type, name, email, password)
VALUES ('2017-06-17 12:30:00', 'ROLE_USER', 'User2', 'user2@gmail.com', '$2a$10$LnSMTXeJb/Q.2o.xDqbwX.7XzwMfIHvQLstE4tHGioTax5/0qWMeG');

INSERT INTO lunches (created, restaurantName) VALUES ('2017-06-19', 'restaurant1');
INSERT INTO lunches (created, restaurantName) VALUES ('2017-06-20', 'restaurant2');
INSERT INTO lunches (created, restaurantName) VALUES (now, 'restaurant3');

INSERT INTO meals (created, dishName, price, lunchId) VALUES
  ('2017-06-19', 'dish1', 1000, 100003),
  ('2017-06-19', 'dish2', 1000, 100003),
  ('2017-06-20', 'dish3', 1000, 100004),
  ('2017-06-20', 'dish4', 500, 100004),
  ('2017-06-20', 'dish5', 1500, 100004),
  (now, 'dish6', 1550, 100005),
  (now, 'dish7', 1550, 100005);

INSERT INTO VOTES (votingDate, userId, lunchId) VALUES ('2017-06-20', 100002, 100004);
INSERT INTO VOTES (votingDate, userId, lunchId) VALUES (now, 100002, 100005);
