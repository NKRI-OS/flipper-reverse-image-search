#!/bin/bash

sudo dsetool create_core flipper.picture generateResources=true reindex=true coreOptions=config-picture.yaml
sudo dsetool create_core flipper.metadata generateResources=true reindex=true coreOptions=config-metadata.yaml
sudo dsetool create_core flipper.picturesearch generateResources=true reindex=true coreOptions=config-pictureSearch.yaml
sudo dsetool create_core flipper.picturefound generateResources=true reindex=true coreOptions=config-pictureFound.yaml


# Reindexing
## In place
# $ dsetool reload_core keyspace.table reindex=true deleteAll=false
## Full
# dsetool reload_core keyspace.table reindex=true deleteAll=true
#
