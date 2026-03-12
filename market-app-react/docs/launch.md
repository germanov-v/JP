mvn -pl bootstrap -am spring-boot:run -DskipTests -Dspring-boot.run.profiles=dev

mvn -pl bootstrap spring-boot:run -DskipTests


mvn -pl bootstrap -am spring-boot:run 

Совсем конкретный случай:

mvn -pl bootstrap -am spring-boot:run -Dspring-boot.run.main-class=ru.yp.marketapp.StartApplication