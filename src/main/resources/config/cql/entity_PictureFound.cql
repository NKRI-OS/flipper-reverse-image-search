DROP TABLE pictureFound;
CREATE TABLE IF NOT EXISTS pictureFound (
    pictureSearch_id uuid,
    picture_id uuid,
    title text,
    owner text,
    created timestamp,
    littlePictureFile blob,
    mediumPictureFile blob,
    bigPictureFile blob,
    totalScore float,
    autocolorCorrelogramScore float,
    ceddScore float,
    colorHistogramScore float,
    colorLayoutScore float,
    edgeHistogramScore float,
    phogScore float,
    PRIMARY KEY((pictureSearch_id), totalscore)
)
WITH CLUSTERING ORDER BY (totalScore ASC);
CREATE INDEX ON pictureFound (picture_id);
CREATE INDEX ON pictureFound ( owner );
