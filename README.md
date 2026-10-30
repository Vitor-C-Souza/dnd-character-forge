# D&D Character Forge

API REST para criação e gerenciamento de fichas de personagens de D&D 5e (2024), desenvolvida com Java 21 e Spring Boot. O projeto tem foco em Design Patterns, DDD tático, MVC tradicional, testes e qualidade de código.

> Projeto de estudo/portfólio. As regras de jogo serão implementadas gradualmente e devem ser conferidas em fontes oficiais compatíveis com a versão 2024. Esta base ainda não contém regras de negócio nem autenticação.

## Stack

- Java 21 e Maven
- Spring Boot 3.5.x: Spring Web, Bean Validation, Spring Data JPA e Actuator
- PostgreSQL 16 + Flyway para versionamento do schema
- Springdoc OpenAPI/Swagger UI
- JUnit 5, Spring Boot Test e H2 apenas para testes isolados
- Docker Compose
- GitHub Actions, JaCoCo e SonarCloud

## Estrutura inicial

```text
src/
├── main/
│   ├── java/com/vitorcsouza/app/characterforge/
│   │   └── CharacterForgeApplication.java
│   └── resources/
│       ├── application.yml
│       └── db/migration/
└── test/
    ├── java/com/vitorcsouza/app/characterforge/
    │   └── CharacterForgeApplicationTest.java
    └── resources/
        └── application-test.yml
```

Quando as funcionalidades forem implementadas, organize os pacotes por contexto/feature e mantenha as responsabilidades separadas: `controller`, `service`, `domain`, `repository` e `dto`. O domínio não deve depender de HTTP, persistência ou Spring Security.

## Requisitos locais

- JDK 21
- Maven 3.9+
- Docker Engine/Desktop com Docker Compose v2

## Executar localmente

1. Copie `.env.example` para `.env` e ajuste as credenciais locais.
2. Suba o PostgreSQL:

   ```bash
   docker compose up -d postgres
   ```

3. Execute os testes e gere o relatório de cobertura:

   ```bash
   mvn clean verify
   ```

4. Inicie a aplicação pelo IntelliJ ou via Maven:

   ```bash
   mvn spring-boot:run
   ```

A API fica disponível em `http://localhost:8080`. A interface Swagger UI estará em `http://localhost:8080/swagger-ui.html` e a documentação OpenAPI em `http://localhost:8080/api-docs`. O health check do Actuator fica em `http://localhost:8080/actuator/health`.

> O teste de contexto usa H2 em memória e não precisa que o PostgreSQL esteja ligado. O perfil padrão usa PostgreSQL. As credenciais de `.env.example` são exclusivamente para desenvolvimento local; não reutilize esses valores em ambientes compartilhados ou de produção.

## Docker Compose

Para construir a imagem e iniciar a aplicação com o banco:

```bash
docker compose up --build -d
docker compose ps
docker compose logs -f app
```

O serviço da API só inicia depois que o health check do PostgreSQL passar. Os dados do banco ficam no volume nomeado `postgres_data`.

Para encerrar:

```bash
docker compose down
```

Para apagar também os dados persistidos localmente (ação destrutiva):

```bash
docker compose down -v
```

## CI e análise estática

O workflow `.github/workflows/ci.yml` executa `mvn clean verify` em pushes e pull requests, publica os relatórios de teste/cobertura como artefato e executa a análise SonarCloud quando o projeto estiver configurado.

Configure no repositório GitHub:

- **Actions variable** `SONAR_ORGANIZATION`: chave da organização no SonarCloud.
- **Actions variable** `SONAR_PROJECT_KEY`: chave do projeto no SonarCloud.
- **Actions secret** `SONAR_TOKEN`: token de análise com permissão para o projeto.

A etapa SonarCloud fica ignorada até as duas variables estarem definidas. Configure as três entradas antes de esperar uma análise de qualidade no pipeline. O workflow aguarda o Quality Gate e falha se ele não passar. Relatório local JaCoCo: `target/site/jacoco/index.html`.

## Estratégia de branches

- `main`: versão estável; não desenvolver diretamente nela.
- `development`: integração do trabalho concluído.
- `feature/<nome>`: novas funcionalidades.
- `fix/<nome>`: correções.

Fluxo sugerido: criar uma branch de feature a partir de `development`, abrir Pull Request para `development`, aguardar CI/revisão e só então promover mudanças estáveis para `main`. Ative proteção de branch no GitHub para exigir Pull Request e checks antes do merge.

## Próximas etapas de desenvolvimento

1. Modelar os value objects e as regras de atributos.
2. Implementar o padrão Strategy para os métodos de geração de atributos.
3. Adicionar criação de personagem e validações de domínio com testes unitários.
4. Persistir os dados com migrations Flyway e endpoints MVC.
5. Introduzir Spring Security apenas depois das funcionalidades centrais e dos testes de domínio.

## Segurança e configuração

Não commite `.env`, tokens, senhas reais ou configurações específicas da máquina. Em CI, use GitHub Actions Secrets/Variables. Spring Security intencionalmente não faz parte do bootstrap inicial.
