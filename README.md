# LemonCash

## Demo API for LemonCash Excercise

Lemoncash

Lemoncash es una plataforma que permite a los usuarios administrar distintos tipos de
activos .

## Para ejecutar:
Ejecutar localemente desde ide o maven.
Por le momento para la BD SQL se utiliza H2, por lo cual no se periste la info.

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
Posibles movement_type: ```deposit``` - ```extract``` - ```transfer```

Posibles coin_type: ```ARS``` - ```USDT``` - ```BTC```

### Listar movimientos de un usuario

```bash
curl --location --request GET 'http://localhost:8080/movement/?user_id=1'
```
Otros posible parametros: ```coin_type``` - ```limit``` - ```offset```- ```movement_type```