ALTER TABLE transaction RENAME COLUMN datetime TO date;
ALTER TABLE transaction ALTER COLUMN date TYPE date;
