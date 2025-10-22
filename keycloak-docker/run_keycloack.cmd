@echo off
setlocal enabledelayedexpansion



docker run --name keycloak_unoptimized -p 8080:8080 ^
        -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin ^
        -v ./keycloak/imports:/opt/keycloak/data/import ^
        quay.io/keycloak/keycloak:latest ^
        start-dev --import-realm

EXIT /B 0
