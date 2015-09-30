if [ $# -ne 2 ];
then
	echo "usage: $0 token_id username"
	exit -1;
fi

TOKEN=$1

curl -i http://localhost:8080/api/pictures/byUser/$2 -H "x-auth-token:$TOKEN" -H "Accept:application/json"
