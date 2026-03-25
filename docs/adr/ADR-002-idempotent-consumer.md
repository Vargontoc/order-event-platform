# ADR-002: Consumidor idempotente con ACK manual

**Fecha:** 2024-01-15  
**Estado:** Aceptado

## Contexto
Kafka puede entregar el mismo mensaje más de una vez
(at-least-once delivery). Si el consumidor falla después
de procesar pero antes de hacer ACK, recibirá el mensaje
de nuevo. Necesitamos que procesar el mismo evento dos
veces no produzca efectos duplicados.

## Decisión
Usamos ACK manual y guardamos en base de datos el orderId
procesado antes de ejecutar la lógica de negocio.
En cada mensaje comprobamos si ya existe ese orderId.

## Consecuencias

**Positivas:**
- Garantía de procesamiento exactamente una vez a nivel de negocio
- Control total sobre cuándo confirmamos el mensaje a Kafka
- Trazabilidad completa del estado de cada procesamiento

**Negativas / trade-offs:**
- Añade una query de lectura por cada mensaje recibido
- Requiere índice único en orderId para evitar race conditions

**Riesgos asumidos:**
- En volúmenes muy altos, la query de idempotencia puede
  ser un cuello de botella. Mitigable con Redis como caché.

## Alternativas consideradas
| Alternativa | Por qué se descartó |
|---|---|
| Auto-commit | Sin control sobre fallos parciales |
| Idempotencia en memoria | Se pierde al reiniciar el servicio |s