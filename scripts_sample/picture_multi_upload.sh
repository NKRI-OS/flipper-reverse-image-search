#!/bin/bash

if [ $# -ne 3 ];
then
	echo "usage: $0 token path_images title"
	exit -1;
fi

TOKEN=$1
SAVEIFS=$IFS
IFS=$(echo -en "\n\b")
FILES=$2/*.jpg
title=$3


touch /tmp/run.sh;

for i in $FILES;
do
           fname=`basename $i`
	   fbname=${fname%.jpg}
	   echo "curl -i -v -X POST -F \"pictureFile=@$i\" -F \"title=$title\" -F \"description=description from $title\" http://localhost:8080/api/mypictures/upload -H \"x-auth-token:$TOKEN\" -H \"Accept:application/json\";" >> /tmp/run.sh;  
done

bash /tmp/run.sh > /dev/null 2>&1;
rm /tmp/run.sh;

