Base:

1. ./gradlew :bootstrap:bootRun --args='--spring.profiles.active=dev --server.port=8085' 

Tests:

1.  ./gradlew :bootstrap:test --tests "*Product*" --info
2.  ./gradlew :bootstrap:test --tests "*ItemV2*" --info
3. ./gradlew :adapters:persistence:test --tests "*CartJpaRepositoryH2*" --stacktrace