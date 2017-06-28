DELETE FROM votes;
DELETE FROM meals;
DELETE FROM lunches;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (registered, type, name, email, password) VALUES ('2017-06-15 12:30:00', 'ADMIN', 'Admin', 'admin@gmail.com', 'adminpass');
INSERT INTO users (registered, type, name, email, password) VALUES ('2017-06-16 12:30:00', 'USER', 'User', 'user@gmail.com', 'password');
INSERT INTO users (registered, type, name, email, password) VALUES ('2017-06-17 12:30:00', 'USER', 'User2', 'user2@gmail.com', 'password2');

INSERT INTO lunches (created, restaurantName) VALUES ('2017-06-19 10:15:00', 'restaurant1');
INSERT INTO lunches (created, restaurantName) VALUES ('2017-06-20 10:15:00', 'restaurant2');
INSERT INTO lunches (created, restaurantName) VALUES ('2017-06-21 10:15:00', 'restaurant3');

INSERT INTO meals (created, dishName, price, lunchId) VALUES
  ('2017-06-19 10:00:00', 'dish1', 100, 100003),
  ('2017-06-19 10:00:00', 'dish2', 100, 100003),
  ('2017-06-20 10:00:00', 'dish3', 100, 100004),
  ('2017-06-20 10:00:00', 'dish4', 50, 100004),
  ('2017-06-20 10:00:00', 'dish5', 150, 100004),
  ('2017-06-21 10:00:00', 'dish6', 155, 100005),
  ('2017-06-21 10:00:00', 'dish7', 155, 100005);

INSERT INTO VOTES (votingDate, userId, lunchId) VALUES ('2017-06-20', 100002, 100004);
INSERT INTO VOTES (votingDate, userId, lunchId) VALUES ('2017-06-21', 100002, 100005);
