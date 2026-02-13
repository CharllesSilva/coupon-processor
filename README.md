# Coupon Processor API

API para gerenciamento de cupons de desconto.

## O que a API faz

- Criar cupons de desconto com código, valor e data de validade
- Buscar cupons por ID
- Deleta cupons (soft delete)

## Tecnologias

- Java 17
- Spring Boot 4.0.2
- H2 Database
- Maven
- Docker

## Como rodar a aplicação

### Usando Docker Compose 
```bash
docker-compose up --build
```

A API estará disponível em: `http://localhost:8080`

### Documentação Swagger

Acesse a documentação interativa em:
```
http://localhost:8080/swagger-ui.html
```

## Endpoints disponíveis

- `POST /coupon` - Cria um novo cupom
- `GET /coupon/{id}` - Busca um cupom por ID
- `DELETE /coupon/{id}` - Deleta um cupom (soft delete)

## Regras de validação

- Código deve ter exatamente 6 caracteres alfanuméricos
- Desconto mínimo é 0.5
- Data de expiração não pode ser no passado
- Cupons deletados não podem ser deletados novamente

## Cobertura de testes

Para gerar o relatório de cobertura de código:
```bash
mvn clean test
```

O relatório será gerado em:
```
target/site/jacoco/index.html
```

Abra o arquivo `index.html` no navegador para visualizar a cobertura detalhada.

### Requisitos 

- Docker

### Rodar localmente sem Docker
```bash
mvn spring-boot:run
```

### Rodar testes
```bash
mvn test
```
---
