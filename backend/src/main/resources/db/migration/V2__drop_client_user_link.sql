-- Single-operator tool: there is no client login, so clients no longer link to a user.
ALTER TABLE clients DROP COLUMN user_id;
