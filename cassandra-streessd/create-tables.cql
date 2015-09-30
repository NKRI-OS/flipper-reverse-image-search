  CREATE KEYSPACE flippertest WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1 };

  CREATE TABLE picture (
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
      PRIMARY KEY(id, created)
  ) WITH CLUSTERING ORDER BY (created DESC)
    AND compaction = { 'class':'LeveledCompactionStrategy' }
    AND comment='A table to hold picture';

CREATE INDEX ON picture (owner);

 CREATE TABLE metadata (
      picture_id uuid,
      id uuid,
      directoryName text,
      tagType int,
      tagName text,
      description text,
      title text,
      pictureFile BLOB,
      PRIMARY KEY((picture_id), id)
  ) WITH CLUSTERING ORDER BY (id DESC)
    AND compaction = { 'class':'LeveledCompactionStrategy' }
    AND comment='A table to hold metadata from pictures';

  CREATE TABLE user (
      id text,
      login text,
      password text,
      firstname text,
      lastname text,
      email text,
      activated boolean,
      lang_key text,
      activation_key text,
      reset_key text,
      reset_date timestamp,
      authorities set<text>,
      PRIMARY KEY(id)
  );

CREATE INDEX ON user (login); CREATE INDEX ON user (email);

 CREATE TABLE picturesearch (
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
  ) WITH CLUSTERING ORDER BY (created DESC)
    AND compaction = { 'class':'LeveledCompactionStrategy' }
    AND comment='A table to hold picture searchs';

CREATE INDEX ON picturesearch (userLogin);

  CREATE TABLE picturefound (
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
  ) WITH CLUSTERING ORDER BY (totalscore ASC)
    AND compaction = { 'class':'LeveledCompactionStrategy' }
    AND comment='A table to hold found pictures from searchs';

CREATE INDEX ON picturefound (owner);
