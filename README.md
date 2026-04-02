## CinemaBh

CinemaApp is a web-based ticketing application that allows online movie ticket purchases for a specific movie company
with multiple subsidiaries across the country/world. The application provides the users with different easy-to-use
services like purchasing tickets, seat reservations, projection schedule overview, etc.

## Java version

21.0.10.

## Postgres version

16.11

## Dependencies

* spring-boot-starter-web
* spring-boot-starter-data-jpa
* spring-boot-starter-security
* postgresql
* lombok
* flyway-core
* spring-boot-starter-flyway
* flyway-database-postgresql
* springdoc-openapi-starter-webmvc-ui

## Testing dependencies

* spring-boot-starter-data-jpa-test
* spring-boot-starter-security-test
* spring-boot-starter-test
* spring-boot-starter-webmvc-test
* junit-jupiter

## Environment variables

In the IntelliJ run configuration add these variables:

* DB_URL=jdbc:postgresql://...
* DB_PASSWORD=...
* DB_USER=...
* FRONTENT_URL=http://... (If not provided http://localhost:5173 is the default)

## Folder structure

Currently the project has separate folders/packages for controllers, services, DTOs, entities, repositories, utils,
mappers, entities and exceptions.
More might be added int the future if needed.