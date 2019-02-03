CREATE TABLE roles(
id SERIAL PRIMARY KEY,
name VARCHAR (20)
);

INSERT INTO roles(name) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

CREATE TABLE user_roles(
id SERIAL PRIMARY KEY,
user_id BIGINT,
role_id BIGINT
);

ALTER TABLE user_roles
ADD CONSTRAINT FK_db_users
FOREIGN KEY (user_id)
REFERENCES db_users(id);

ALTER TABLE user_roles
ADD CONSTRAINT FK_db_roles
FOREIGN KEY (role_id)
REFERENCES roles(id);