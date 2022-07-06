mvn clean install -DskipTests -f ../pom.xml spring-boot:repackage
docker build ../ -t alimkugot/webparser-client:1.0.0
docker push alimkugot/webparser-client:1.0.0