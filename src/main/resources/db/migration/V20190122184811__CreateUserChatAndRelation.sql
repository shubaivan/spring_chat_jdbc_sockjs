CREATE TABLE db_users(
  avatar BIGINT,
  date_of_birth DATE NOT NULL,
  date_of_registration TIMESTAMP NOT NULL,
  first_name VARCHAR(80) NOT NULL,
  last_name VARCHAR(80) NOT NULL,
  user_name VARCHAR(80) NOT NULL UNIQUE,
  password VARCHAR(15) NOT NULL,
  email VARCHAR(40) NOT NULL UNIQUE,
  url_facebook VARCHAR (80),
  url_git VARCHAR (80),
  url_linkedin VARCHAR (80),
  user_role INT NOT NULL
);

ALTER TABLE db_users ADD COLUMN id SERIAL PRIMARY KEY NOT NULL;

CREATE TABLE chats (
  chat_type INT NOT NULL,
  date_of_created TIMESTAMP NOT NULL,
  description VARCHAR (255),
  name VARCHAR(100),
  tags VARCHAR(255),
  owner_id BIGINT NOT NULL
);

ALTER TABLE chats ADD COLUMN id SERIAL PRIMARY KEY NOT NULL;

ALTER TABLE chats
ADD CONSTRAINT FK_db_users
FOREIGN KEY (owner_id)
REFERENCES db_users(id);

CREATE TABLE chats_users (
  chat_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  date_of_joined TIMESTAMP
);

ALTER TABLE chats_users ADD COLUMN id SERIAL PRIMARY KEY NOT NULL;

ALTER TABLE chats_users
ADD CONSTRAINT FK_db_users
FOREIGN KEY (user_id)
REFERENCES db_users(id);

ALTER TABLE chats_users
ADD CONSTRAINT FK_chats
FOREIGN KEY (chat_id)
REFERENCES chats(id);