CREATE TABLE users
(
  user_id VARCHAR PRIMARY KEY NOT NULL,
  facebook_id VARCHAR UNIQUE NOT NULL
);

CREATE TABLE sessions
(
  session_id VARCHAR PRIMARY KEY NOT NULL,
  user_id VARCHAR REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE
);