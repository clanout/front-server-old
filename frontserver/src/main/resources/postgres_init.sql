CREATE TABLE users
(
  user_id VARCHAR PRIMARY KEY NOT NULL
);

CREATE TABLE sessions
(
  session_id VARCHAR PRIMARY KEY NOT NULL,
  user_id    VARCHAR REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE user_feedback
(
  user_id VARCHAR REFERENCES users (user_id) ON UPDATE NO ACTION ON DELETE NO ACTION,
  type    INTEGER NOT NULL,
  comment TEXT
);

CREATE TABLE subscriptions
(
  email                  VARCHAR(25) PRIMARY KEY                               NOT NULL,
  subscription_timestamp TIMESTAMP WITH TIME ZONE                              NOT NULL
);