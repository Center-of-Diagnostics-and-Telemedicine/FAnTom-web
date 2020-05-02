# Fantom WEB

* Set of projects:
  * [Api Models Library](api-models) &mdash; MPP library with constants and serializable data classes for client-server interaction
  * [Common Library](shared) &mdash; MPP library with frontend logic. Based on MVI. Depends on [Api Models Library](api-models).
  * [KotlinJs React](js-frontend) &mdash; Frontend Js app built on KotlinJs. Depends on [Common Library](shared).
  * [Main Backend Server](main-server) &mdash; Main Server with MySQL. Depends on [Api Models Library](api-models). 
  * [Fantom-Library Backend Server](library-server) Depends on [Api Models Library](api-models).

## Run application

- `./gradlew :main-server:run` will run development [Main Backend Server](main-server)
- `./gradlew :livrary-server:run` will run development [Fantom-Library Backend Server](library-server)
- `./gradlew :js-frontend:run` will build optimized [KotlinJs React](js-frontend) bundle and run it on https://localhost:8080
- `./gradlew :client:run -t` will run development [KotlinJs React](js-frontend) at https://localhost:8080 with live reload
    
## Distribution

- `./gradlew :main-server:build` will create `main-server/build/libs/main-server.jar`. You can deploy it on the server and run `java -jar main-server.jar` to start the server. Client is included.
- `./gradlew :library-server:build` will create `library-server/build/libs/library-server.jar`. You can deploy it on the server and run `java -jar library-server.jar` to start the server.
- `./gradlew :js-frontend:build` will create `js-frontend/build/distributions/client.js`.

## Description

It is a [Kotlin Multiplatform](https://kotlinlang.org/docs/reference/multiplatform.html) project.

It uses:
- `kotlin-multiplatform`, `kotlin-js` and `kotlin-jvm` plugins for Kotlin compilation;
- [Ktor framework](https://ktor.io) as a web server;
- [Exposed](https://github.com/JetBrains/Exposed) and MySQL;
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) for client/server (de-)serialization;
- [React](https://reactjs.org), [Styled components](https://www.styled-components.com) and [kotlin-wrappers](https://github.com/JetBrains/kotlin-wrappers) for rendering. 