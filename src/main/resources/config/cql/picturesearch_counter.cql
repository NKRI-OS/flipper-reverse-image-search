DROP TABLE picturesearch_counter;
create table picturesearch_counter(
  pictureSearch_id uuid,
  picturefound_counter counter,
  PRIMARY KEY(pictureSearch_id)
);
