
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

mvn -pl adapters/persistence test