

Проверка (dependencyInsight - диагностика):

```
./gradlew :adapters:persistence:dependencyInsight \
  --dependency spring-boot-starter-liquibase \
  --configuration compileClasspath
```

Перезапуск:

```
./gradlew --stop
./gradlew clean
./gradlew :adapters:persistence:dependencyInsight --dependency spring-boot-starter-liquibase --configuration compileClasspath
```



Проверить есть читает ли application:

./gradlew projects
или
./gradlew :application:dependencyInsight --dependency spring-boot-starter --configuration compileClasspath



Global clean

```
rm -rf ~/.gradle/caches
rm -rf ~/.gradle/wrapper/dists
```


./gradlew clean build --no-daemon


Очистка и запуск без тестов!!!!

./gradlew clean build -x test --no-daemon


./gradlew :bootstrap:bootRun
./gradlew :bootstrap:bootRun --args='--spring.profiles.active=dev --server.port=8085' 


./gradlew :bootstrap:tasks --all

