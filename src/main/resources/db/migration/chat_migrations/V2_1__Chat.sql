CREATE TABLE chats (
  id BIGINT NOT NULL,
  type INT NOT NULL,
  date_of_created TIMESTAMP NOT NULL,
  description VARCHAR (255),
  name VARCHAR(255),
  tags VARCHAR(255),
  owner_id BIGINT NOT NULL,
PRIMARY KEY (id)
);

ALTER TABLE chats
ADD CONSTRAINT FK_db_users
FOREIGN KEY (owner_id)
REFERENCES db_users(id);