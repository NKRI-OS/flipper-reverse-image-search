#!/bin/bash

#if [ $# -ne 2 ];
#then
#	echo "usage: $0 username password";
#	exit -1;
#fi;

USER=user
PASS=user

curl -X POST http://localhost:8080/api/authenticate -H "Accept: application/json" -d "username=$USER&password=$PASS"
echo "";
