@echo off
setlocal enabledelayedexpansion

del /q out

for %%i in (1 2 3) do (
    curl.exe -X POST "http://localhost:8080/realms/DAT152/protocol/openid-connect/token" -o "out/user%%i.json" --create-dirs --data "grant_type=password&client_id=dat152oblig2&username=user%%i&password=user%%i"

    rem for /f "tokens=*" %%g in ('type out\user%%i.json') do (set "access_tokens[%%i]=%%g")

    for /f "tokens=2 delims=:,{" %%a in ('findstr /i "access_token" out\user%%i.json') do (
        echo %%a
        set "access_tokens[%%i]=%%a"
    )

)

echo user.token.test=%access_tokens[1]% > out\tokens.txt
echo admin.token.test=%access_tokens[2]% >> out\tokens.txt
echo user3.token.test=%access_tokens[3]% >> out\tokens.txt

EXIT /B 0


