-- Create auth table
CREATE TABLE auth (
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    uid SERIAL PRIMARY KEY
);

-- Add index on email for faster lookups
CREATE INDEX idx_auth_email ON auth(email); 