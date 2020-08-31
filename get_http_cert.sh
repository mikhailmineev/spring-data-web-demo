#!/bin/sh

# show client certs
openssl s_client -connect localhost:32768 -showcerts

# trust ssl
curl --cacert trust_root.crt https://localhost:32769 -vvv

# client ssl
curl -k --cert ~/localhost.crt --key ~/localhost.key -vvvv https://localhost:32773

# mockserver with ssl
docker run -d --rm \
-e MOCKSERVER_TLS_MUTUAL_AUTHENTICATION_REQUIRED=true \
-e MOCKSERVER_TLS_MUTUAL_AUTHENTICATION_CERTIFICATE_CHAIN=/opt/localhost.crt \
-v $(pwd)/src/main/resources/localhost.crt:/opt/localhost.crt \
-P mockserver/mockserver

# client ssl creation
https://www.baeldung.com/x-509-authentication-in-spring-security
