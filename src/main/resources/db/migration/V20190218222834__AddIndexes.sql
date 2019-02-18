CREATE INDEX ON chats (owner_id);

CREATE INDEX ON chats_users (chat_id);
CREATE INDEX ON chats_users (user_id);

CREATE INDEX ON confirmation_token (confirmation_token);
CREATE INDEX ON confirmation_token (user_id);

CREATE INDEX ON messages (author_id);
CREATE INDEX ON messages (chat_id);

CREATE INDEX ON user_roles (user_id);
CREATE INDEX ON user_roles (role_id);

