# GUV-loader
Microservicio para cargar y obtener ,cheques a corregir, de la base de datos a una MongoDB.

### Cómo funciona?
La aplicación posee un job diferente por cada tipo de corrección (importe, CUIT, CMC7, fecha), que se ejecutan cada minuto con una diferencia de 15 segundos entre los mismos.<br>
Cada vez que un job se ejecuta, verifica si debe iniciar la carga de cheques evaluando la capacidad de la colección en la MongoDB, a partir de la siguiente regla:<br>
```
total >= 0L && (100 * total / pageSize) <= percentage
```

Ambas variables configurables:`loader.page.size` y `loader.mongo.percentage`.
En caso de que se necesite carga, consulta en la BD si hay cheques que falten cargar a la MongoDB y los inserta.

Para la obtención de cheques, existe un endpoint por tipo de correción. Este retorna los primeros 20 cheques ,del tipo de corrección solicitado, de la MongoDB que no se encuentren reservados y se configura un TTL de 2 minutos para realizar la corrección de los mismos. En caso de que no se corrijan, expiran y vuelven a ser cargados.


### Dependencias utilizadas
* Project Reactor
* Oracle
* MongoDB