# Backend Biblioteca - Spring Boot

API REST para gestionar usuarios, libros, ejemplares y prestamos.

## Tecnologias

- Java 17
- Spring Boot 3.5.4
- Spring Data JPA
- Maven
- PostgreSQL
- Docker y Docker Compose

## Requisitos

- Docker Desktop en ejecucion
- Docker Engine activo (docker version debe mostrar Client y Server)

## Variables de entorno

El proyecto usa un archivo .env en la raiz del backend.

Ejemplo recomendado para Docker:

DB_URI=jdbc:postgresql://db:5432/library
DB_USER=postgres
DB_PASSWORD=postgres
DB_DRIVER=org.postgresql.Driver
SERVER_PORT=8080
POSTGRES_DB=library
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

Notas:

- En Docker, el host de PostgreSQL debe ser db (nombre del servicio en docker-compose).
- DB_* configuran Spring Boot.
- POSTGRES_* configuran el contenedor PostgreSQL.

## Levantar el backend con Docker

1) Ir a la carpeta del backend

cd demo

2) Crear el archivo .env local

Git Bash:

cp .env.example .env

PowerShell:

Copy-Item .env.example .env

3) Ajustar .env con valores de tu entorno (especialmente DB_URI y passwords)

4) Levantar los contenedores

docker compose up --build -d

5) Verificar estado

docker compose ps

6) Ver logs del backend

docker compose logs -f backend

## Restaurar datos de prueba

El backup se encuentra en:

database/dump/backup.dump

### Opcion Git Bash

docker exec -i library-db pg_restore --clean --if-exists -U postgres -d library < database/dump/backup.dump

### Opcion PowerShell

Get-Content .\database\dump\backup.dump -AsByteStream | docker exec -i library-db pg_restore -U postgres -d library

## Verificacion rapida de API

Con el backend arriba:

Git Bash:

curl http://localhost:8080/api/usuarios

PowerShell:

Invoke-RestMethod http://localhost:8080/api/usuarios

Si responde JSON (aunque sea []), la API esta accesible.

## Flujo sugerido de evaluacion (maximo 8 comandos)

### Git Bash

1. cd demo
2. cp .env.example .env
3. docker compose down -v
4. docker compose up --build -d
5. docker compose ps
6. docker exec -i library-db pg_restore -U postgres -d library < database/dump/backup.dump
7. curl http://localhost:8080/api/usuarios
8. docker compose logs -f backend

### PowerShell

1. cd demo
2. Copy-Item .env.example .env
3. docker compose down -v
4. docker compose up --build -d
5. docker compose ps
6. Get-Content .\database\dump\backup.dump -AsByteStream | docker exec -i library-db pg_restore -U postgres -d library
7. Invoke-RestMethod http://localhost:8080/api/usuarios
8. docker compose logs -f backend

## Nota para entorno del evaluador

Todos los comandos del README se ejecutan desde la raiz del backend (carpeta demo) una vez clonado el repositorio.
No se usan rutas absolutas de una maquina especifica.
