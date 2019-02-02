INSERT INTO db_users (avatar,
                      date_of_birth,
                      date_of_registration,
                      first_name,
                      last_name,
                      user_name,
                      password,
                      email,
                      url_facebook,
                      url_git,
                      url_linkedin)
VALUES (null,
        '2019-01-25 12:27:53.848000',
        '2019-01-25 12:27:53.848000',
        'FirstName',
        'LastName',
        'UserName',
        '$2a$10$IaoBV/NCCZF2nfi8D/xXp.2K/PkninBKF8jGl6mtIVQb/PBiflg7a',
        'ivashchenko.dima@gmail.com',
        null,
        null,
        null);

INSERT INTO chats (chat_type, date_of_created, description, name, tags, owner_id)
VALUES (2, '2019-01-25 12:27:53.848000', 'Default public chat', 'Default', 'public, default', 1);
