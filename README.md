# 🐾 Petshop API

API REST de um petshop, construída para estudar **arquitetura de software backend** com Java e Spring Boot: banco distribuído, cache, mensageria e containers.

Escolhi o domínio de petshop de propósito: é simples de entender, mas tem regras de negócio reais, como pets vinculados a tutores, catálogo de serviços e agendamentos.

## Tecnologias

- **Java 21** e **Spring Boot 4**
- **Apache Cassandra**: banco principal, com modelagem orientada à consulta
- **Redis**: cache do catálogo de serviços, com expiração (TTL) e invalidação
- **Apache Kafka**: eventos entre partes do sistema *(em andamento)*
- **Spring Security**: login com JWT e senhas com hash BCrypt *(em andamento)*
- **Docker Compose**: todo o ambiente sobe com um comando
- **Bean Validation**: validação dos dados de entrada

## Arquitetura

```
Cliente (Postman / front-end)
        │  HTTP + JSON
        ▼
   Controller  ──►  Service  ──►  Repository  ──►  Cassandra
                       │
                       └──► Redis (cache do catálogo)
```

- **Controller**: recebe as requisições HTTP e valida a entrada.
- **Service**: regras de negócio, como "usar o cache e limpá-lo quando o catálogo mudar".
- **Repository**: acesso ao banco com Spring Data.

O código é organizado **por funcionalidade** (`pet/`, `servico/`, `config/`), e não por tipo de classe.

## Decisões de modelagem

- **Tabela `pets_por_tutor`**: `tutor_id` é a chave de partição e `pet_id` é a chave de clustering. A consulta "pets do tutor X" lê uma única partição, o que é rápido mesmo com muitos dados. No Cassandra, a tabela é desenhada a partir da consulta, e não o contrário.
- **Catálogo de serviços em cache**: `@Cacheable` guarda a lista no Redis por 10 minutos, e `@CacheEvict` apaga o cache sempre que um serviço é cadastrado, para nunca servir dado desatualizado.
- **Preços com `BigDecimal`**: nunca `double` para dinheiro.
- **DTOs separados das entidades**: o cliente envia só os dados do formulário, e os ids são gerados pelo servidor.

## Como rodar

**Pré-requisitos:** JDK 21+ e Docker.

```bash
# 1. Subir Cassandra, Redis e Kafka
docker compose up -d

# 2. Aguardar ~1 minuto e criar o keyspace
docker compose exec cassandra cqlsh -e "CREATE KEYSPACE IF NOT EXISTS petshop WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"

# 3. Rodar a aplicação
./mvnw spring-boot:run        # Linux/Mac
mvnw.cmd spring-boot:run      # Windows
```

Verifique se está no ar em `http://localhost:8080/actuator/health`.

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/tutores/{tutorId}/pets` | Cadastra um pet para o tutor |
| `GET` | `/tutores/{tutorId}/pets` | Lista os pets do tutor |
| `POST` | `/servicos` | Cadastra um serviço e limpa o cache do catálogo |
| `GET` | `/servicos` | Lista o catálogo (servido do Redis quando em cache) |

Exemplo de cadastro de pet:

```json
{
  "nome": "Rex",
  "especie": "Cachorro",
  "raca": "Vira-lata",
  "nascimento": "2021-05-10"
}
```

> ⚠️ A segurança ainda está aberta de forma provisória (`permitAll`) para facilitar os testes. A próxima etapa substitui isso por autenticação com JWT.

## Roadmap

- [x] Ambiente com Docker Compose (Cassandra, Redis e Kafka)
- [x] Cadastro e listagem de pets no Cassandra
- [x] Catálogo de serviços com cache no Redis
- [ ] Segurança: login com JWT, perfis CLIENTE/ADMIN, senhas com BCrypt e logout com tokens revogados no Redis
- [ ] Agendamentos publicando eventos no Kafka
- [ ] Fotos dos pets no S3 (via LocalStack)
- [ ] Testes de integração com Testcontainers
- [ ] Front-end com TypeScript, React e Next.js

## Aprendizados

- Modelagem orientada à consulta no Cassandra e o custo de consultas fora da chave de partição.
- Estratégias de cache: TTL como rede de segurança e invalidação quando o dado muda.
- Depuração de ambiente: imagens Docker corrompidas por downloads interrompidos (`exec format error`), incompatibilidade entre versões do Maven e da IDE, e um BOM invisível impedindo a compilação.
- Uso da IA como professora: perguntando o porquê de cada decisão, em vez de pedir o código pronto.

## Autor

**Júlio César**, estudante de Análise e Desenvolvimento de Sistemas, com foco em backend e Inteligência Artificial.

[GitHub](https://github.com/Juliodev001)
