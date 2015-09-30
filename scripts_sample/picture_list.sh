if [ $# -ne 1 ];
then
	echo "usage: $0 token_id"
fi

TOKEN=$1

#curl -i http://localhost:8080/api/pictures -H "x-auth-token:$TOKEN" -H "Accept:application/json"
curl -i http://localhost:8080/api/pictures?page=0&per_page=1 -H "x-auth-token:$TOKEN" -H "Accept:application/json"
