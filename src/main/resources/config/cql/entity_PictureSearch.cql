DROP table pictureSearch;
CREATE TABLE IF NOT EXISTS pictureSearch (
    id uuid,
    pictureFile blob,
    littlePictureFile blob,
    created timestamp,
    userLogin text,
    pictureIdList list<uuid>,
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
    PRIMARY KEY((id), created)
)
WITH CLUSTERING ORDER BY (created DESC);
