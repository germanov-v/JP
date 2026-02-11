
## Launch with tests

```
mvn clean package

```

## requirements

Java 21

```
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

or

```
export PATH="$JAVA_HOME/bin:$PATH"
source ~/.zshrc
java -version
mvn -v
```
