# ADR-001: Serialización de eventos con JSON plano

**Fecha:** 2026-03-24
**Estado:** Aceptado

## Contexto
El sistema necesita publicar eventos de pedido a través de Kafka.
Existen varias opciones de serialización con distintos niveles
de complejidad operacional.

## Decisión
Usamos JSON plano serializado con Jackson. El contrato del evento
queda definido por la clase Java `OrderCreatedEvent`.

## Consecuencias

**Positivas:**
- Sin infraestructura adicional (no necesita Schema Registry)
- Fácil de inspeccionar y debuggear en Kafka UI
- Suficiente para un equipo pequeño o proyecto individual

**Negativas / trade-offs:**
- Sin validación de esquema en tiempo de producción
- Evolución del evento requiere coordinación manual entre servicios
- No escalable si el número de consumidores crece mucho

**Riesgos asumidos:**
- Si el proyecto crece en equipo o servicios, migrar a Avro
  tendrá un coste no trivial

## Alternativas consideradas
| Alternativa | Por qué se descartó |
|---|---|
| Avro + Schema Registry | Complejidad operacional innecesaria en esta fase |
| JSON con validación manual | Duplicación de lógica entre servicios sin beneficio claro |