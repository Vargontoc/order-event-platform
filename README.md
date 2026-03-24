# Order Event Platform

Sistema de procesamiento de pedidos basado en eventos.

## Objetivo del proyecto
Plataforma de aprendizaje para practicar diseño de sistemas,
arquitectura orientada a eventos y documentación de decisiones técnicas.

## Stack
- Spring Boot 3.x
- Apache Kafka
- PostgreSQL
- Prometheus + Grafana
- Docker Compose

## Documentación
- [Decisiones arquitectónicas](docs/adr/)
- [Diagramas](docs/diagrams/)
- [Visión general](ARCHITECTURE.md)

## Cómo levantar el entorno
\`\`\`bash
docker-compose -f infrastructure/docker-compose.yml up -d
\`\`\`