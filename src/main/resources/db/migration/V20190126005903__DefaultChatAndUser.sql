INSERT INTO db_users (date_of_birth,
                      date_of_registration,
                      first_name,
                      last_name,
                      user_name,
                      password,
                      email, is_enabled)
VALUES ('2019-01-25',
        '2019-01-25 12:27:53.848000',
        'Inna',
        'Bakum',
        'ibakum',
        '$2a$10$30ApHRliD81V5Sw1FVrU0OqxYEF3LOjsG2O2G25KDRnjJk8ccJ7SC',
        'ibakum95@gmail.com', true);

INSERT INTO user_roles(user_id, role_id)
VALUES (1, 2);

INSERT INTO chats (chat_type, date_of_created, description, name, tags, owner_id)
VALUES (2, '2019-01-25 12:27:53.848000', 'Default public chat', 'Default', 'public, default', 1);
