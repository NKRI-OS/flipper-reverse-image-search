DROP TABLE user_counter;
create table user_counter(
  user text,
  picture_counter counter,
  picturesearch_counter counter,
  metadata_counter counter,
  PRIMARY KEY(user)
);
