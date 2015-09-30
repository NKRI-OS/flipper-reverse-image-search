DROP TABLE metadata;
CREATE TABLE IF NOT EXISTS metadata (
    picture_id uuid,
    id uuid,
    directoryName text,
    tagType int,
    tagName text,
    description text,
    created timestamp,
    title text,
    pictureFile BLOB,
    PRIMARY KEY((picture_id), id)
);
CREATE INDEX ON metadata( id );
CREATE INDEX ON metadata( tagName );
CREATE INDEX ON metadata( description );
