if [ $# -ne 1 ];
then
	echo "usage: $0 token_id"
fi

TOKEN=$1

curl -i -X POST -F "file=@twitter.png" http://localhost:8080/api/_search/pictureSearch/byFile/  -H "x-auth-token:$TOKEN" -H "Accept:application/json"

