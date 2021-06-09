# LemonCash

## Demo API for LemonCash Excercise

Lemoncash

Lemoncash es una plataforma que permite a los usuarios administrar distintos tipos de
activos.

## Setup:
Para crear las imagenes de las bases de datos referirse a:
[LemonCashSQL](https://github.com/PELI92/LemonCashSQL)

## Para ejecutar:
Ejecutar localemente desde IDE de preferencia con la siguiente VM option:
```bash
-Dspring.profiles.active=local 
```
o con maven:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
## Para probar:
### Alta de un usuario
```bash
curl --location --request POST 'http://localhost:8080/users/' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Juan",
"surname": "Perez",
"alias": "juan-perez",
"email": "juancito.perez@gmail.com"
}'
```

### Obtención de un usuario
```bash
curl --location --request GET 'http://localhost:8080/users/id/1'
```

### Registrar un nuevo movimiento
```bash
curl --location --request POST 'http://localhost:8080/movement' \
--header 'Content-Type: application/json' \
--data-raw '{
"movement_type": "deposit",
"coin_type": "ARS",
"amount": 1000,
"user_id_destination": 1
}'
```
Posibles movement_type: `deposit` - `extract` - `transfer`

Posibles coin_type: `ARS` - `USDT` - `BTC`

### Listar movimientos de un usuario

```bash
curl --location --request GET 'http://localhost:8080/movement/?user_id=1'
```
Otros posible parametros: `coin_type` - `limit` - `offset`- `movement_type`

### TODO:
* Pulir manejo de errores.
* Subir test coverage.
