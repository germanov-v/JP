
1 Проверка

```
mvn validate
```

2 Установка в локальный Maven-репозитарий

```
mvn install
```

3 Рецепт

```
mvn validate
mvn clean
mvn install
mvn dependency:tree
```


4 Тесты

Через консоль:
mvn -pl adapters/persistence test

Через IDE дебаг - чтобы тесты запускались в том jvm что и ide дебажит
mvn -pl adapters/persistence test -DforkCount=0

Конфиг для iDEa:
-pl bootstrap -am -Dtest=ItemV2ControllerTests,OrdersControllerTests -Dsurefire.failIfNoSpecifiedTests=false test -DforkCount=0
-pl bootstrap -am -Dtest=ItemV2ControllerTests,ProductControllerTests,OrdersControllerTests -Dsurefire.failIfNoSpecifiedTests=false test -DforkCount=0

5. Сбор

mvn -pl bootstrap -am clean install -DskipTests

6. Запуск
   mvn -f bootstrap/pom.xml spring-boot:run -DskipTests -Dspring-boot.run.profiles=dev