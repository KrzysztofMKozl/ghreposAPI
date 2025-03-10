# ghrepos

This project fetches github repositories for a given user if said user exists. Otherwise the status return is 404
If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.

The application is now runnable using 
```
java -jar build/quarkus-app/quarkus-run.jar
```

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```


## Provided Code

### REST

Quarkus is by default listening on http://0.0.0.0:8080

The endpoint `\users\{name}` is used to obtain informations on the `user` repositories.
Only non-fork repositories are provided. Every repository has it's branches listed.
The data is provided as an array of JSONs.