## Flyway Migration

### Install Flyway

```shell
brew install flyway 
```

then

```shell
cd src/main/resources

flyway -url="jdbc:postgresql://localhost:5432/postgres" -user="username" -password="pass" -locations="filesystem:db/migration/" migrate 
```

## Bootrun

```shell
DB_USERNAME=username DB_PASSWORD=pass gradle bootrun
```
