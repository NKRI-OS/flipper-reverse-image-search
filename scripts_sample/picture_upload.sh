#!/bin/bash

if [ $# -ne 1 ];
then
	echo "usage: $0 token_id"
fi

TOKEN=$1

curl -i -v -X POST -F "pictureFile=@twitter.png" -F "title=twitter" -F "description=twitter+logo+png" http://localhost:8080/api/mypictures/upload -H "x-auth-token:$TOKEN" -H "Accept:application/json"

