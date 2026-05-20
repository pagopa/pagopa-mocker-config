# pagoPA Mocker Config

Spring Application that exposes a REST API to configure the mocked responses used by the [pagoPA Mocker](https://github.com/pagopa/pagopa-mocker) service.

- [pagoPA Mocker Config](#pagopa-mocker-config)
    * [Technology Stack](#technology-stack)
    * [Start Project Locally 🚀](#start-project-locally---)
        + [Prerequisites](#prerequisites)
        + [Run docker container](#run-docker-container)
    * [Develop Locally 💻](#develop-locally---)
        + [Prerequisites](#prerequisites-1)
        + [Run the project](#run-the-project)
        + [Spring Profiles](#spring-profiles)
        + [Testing 🧪](#testing---)
            - [Unit testing](#unit-testing)
    * [API Documentation](#api-documentation)
    * [Related Services](#related-services)
    * [Contributors 👥](#contributors---)
        + [Maintainers](#maintainers)

---

## Technology Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data MongoDB
- Spring Data Redis
- Lombok
- ModelMapper
- SpringDoc OpenAPI (Swagger UI)

---

## Start Project Locally 🚀

### Prerequisites

- docker
- [yq](https://github.com/mikefarah/yq) (`pip3 install yq`)

### Run docker container

From `./docker` directory:

```bash
sh ./run_docker.sh local
```

Available environments: `local`, `dev`, `uat`.

ℹ️ Note: for PagoPa ACR is required the login `az acr login -n <acr-name>`

ℹ️ Note: when `local` is selected, a new image is built from the current branch using `dev` dependencies.

---

## Develop Locally 💻

### Prerequisites

- git
- maven
- jdk-17

### Run the project

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Spring Profiles

- **local**: to develop locally, pointing to DEV database.
- _default (no profile set)_: The application gets the properties from the environment (for Azure).

### Testing 🧪

#### Unit testing

To run the **JUnit** tests:

```bash
mvn clean verify
```

---

## API Documentation

Once the application is running, the Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

The OpenAPI spec is also available as a static file at [`openapi/openapi.json`](openapi/openapi.json).

**Main API groups:**

| Group | Base path | Description |
|---|---|---|
| Mock Resources | `/resources` | CRUD of mock resources and their rules |
| Archetypes | `/archetypes` | Manage archetypes and import from OpenAPI specs |
| Scripting | `/scripting` | List available JavaScript scripts |

> 📖 For a complete operational guide with examples and workflows, see [GUIDA_OPERATIVA.md](GUIDA_OPERATIVA.md).

---

## Related Services

This service is the **configuration backend** for the Mocker ecosystem:

- **[pagopa-mocker](https://github.com/pagopa/pagopa-mocker)** — the runtime engine that reads configurations from MongoDB and serves mocked HTTP responses. See its [Guida Operativa](https://github.com/pagopa/pagopa-mocker/blob/docs/guida-operativa/GUIDA_OPERATIVA.md).

Both services share the same MongoDB database (`mocker` by default).

---

## Contributors 👥

Made with ❤️ by PagoPa S.p.A.

### Maintainers

See `CODEOWNERS` file
