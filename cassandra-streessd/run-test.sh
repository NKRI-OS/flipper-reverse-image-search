#!/bin/bash

# Running Mixed picture test on flippertest keyspace
cassandra-stress user profile=./flipper-picture.yaml ops\(singlepicture=1,wherein=1,owner=1,insert=1\)

# Running other tables
cassandra-stress user profile=./flipper-metadata.yaml ops\(singlepost=1,timeline=1,insert=1\)
cassandra-stress user profile=./flipper-pictureFound.yaml ops\(singlepost=1,timeline=1,insert=1\)
cassandra-stress user profile=./flipper-pictureSearch.yaml ops\(singlepost=1,timeline=1,insert=1\)
cassandra-stress user profile=./flipper-user.yaml ops\(singlepost=1,timeline=1,insert=1\)
