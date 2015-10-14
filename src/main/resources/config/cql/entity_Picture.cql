DROP TABLE picture;
CREATE TABLE IF NOT EXISTS picture (
    id uuid,
    title text,
    description text,
    pictureFile blob,
    littlePictureFile blob,
    mediumPictureFile blob,
    bigPictureFile blob,
    created timestamp,
    modified timestamp,
    owner text,
    modifiedBy text,
    autocolorCorrelogramAsBase64 text,
    autocolorCorrelogram list<double>,
    ceddAsBase64 text,
    cedd list<double>,
    colorHistogramAsBase64 text,
    colorHistogram list<double>,
    colorLayoutAsBase64 text,
    colorLayout list<double>,
    edgeHistogramAsBase64 text,
    edgeHistogram list<double>,
    phogAsBase64 text,
    phog list<double>,
    PRIMARY KEY(id)
);
CREATE INDEX ON picture ( owner );
CREATE INDEX ON picture ( title );
