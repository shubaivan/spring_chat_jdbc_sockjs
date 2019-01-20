CREATE TABLE db_users(
  id BIGINT NOT NULL,
  avatar BIGINT,
  date_of_birth TIMESTAMP NOT NULL,
  date_of_registration TIMESTAMP NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  user_name VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  url_facebook VARCHAR (255),
  url_git VARCHAR (255),
  url_linkedin VARCHAR (255),
  user_role INT NOT NULL,
PRIMARY KEY (id)
);