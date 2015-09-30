if [ $# -ne 1 ];
then
	echo "usage: $0 token_id"
	exit -1;
fi

TOKEN=$1

curl -i http://localhost:8080/api/_search/pictures/title -H "x-auth-token:$TOKEN" -H "Accept:application/json"
