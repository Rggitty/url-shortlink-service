CREATE TABLE IF NOT EXISTS short_links (
  id BIGSERIAL PRIMARY KEY,
  short_code VARCHAR(32) UNIQUE,
  original_url VARCHAR(2048) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_short_links_code ON short_links(short_code);
