

Проверка (dependencyInsight - диагностика):

```
./gradlew :adapters:persistence:dependencyInsight \
  --dependency spring-boot-starter-liquibase \
  --configuration compileClasspath
```