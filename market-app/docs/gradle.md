

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