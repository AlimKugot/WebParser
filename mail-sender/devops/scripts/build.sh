mvn clean install -DskipTests -f ../../pom.xml spring-boot:repackage
docker build ../../ -t alimkugot/mail-sender:1.0.0
docker push alimkugot/mail-sender:1.0.0