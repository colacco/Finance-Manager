ALTER TABLE users ADD COLUMN active boolean NOT NULL DEFAULT true;
UPDATE users set active = true WHERE active IS NULL;