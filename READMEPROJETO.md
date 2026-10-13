# 🐇 Publisher — Estudos com RabbitMQ

Aplicação de estudos desenvolvida com **Spring Boot** para praticar o envio de mensagens ao **RabbitMQ**.
Este projeto é o lado **produtor (publisher)**: ele expõe um endpoint REST que recebe um texto e o publica em uma fila do RabbitMQ.

## 📚 Objetivo

- Entender os conceitos básicos de mensageria (produtor, fila, consumidor).
- Integrar uma aplicação Spring Boot com o RabbitMQ usando o **Spring AMQP**.
- Publicar mensagens através de uma API REST.

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot 4**
  - Spring Web MVC
  - Spring AMQP (`RabbitTemplate`)
- **RabbitMQ**
- **Maven** (via Maven Wrapper)
- **Lombok**

## 🏗️ Arquitetura

```
┌──────────┐   POST /publisher/Text   ┌────────────────┐   convertAndSend   ┌──────────────┐
│ Cliente  │ ───────────────────────▶ │   Publisher    │ ─────────────────▶ │   RabbitMQ   │
│ (HTTP)   │        "mensagem"        │ (Spring Boot)  │                    │ MY_TEST_NAME │
└──────────┘                          └────────────────┘                    └──────────────┘
```

### Estrutura do projeto

```
src/main/java/com/rmq/example/publisher
├── PublisherApplication.java      # Classe principal do Spring Boot
├── controller
│   └── Publisher.java             # Endpoint REST que recebe as mensagens
└── service
    └── PubliserService.java       # Envia as mensagens ao RabbitMQ via RabbitTemplate
```

## ⚙️ Configuração

As configurações ficam em `src/main/resources/application.properties`:

| Propriedade                 | Descrição                              | Valor padrão   |
|-----------------------------|----------------------------------------|----------------|
| `server.port`               | Porta da aplicação                     | `8081`         |
| `rabbitmq.qeuename`         | Nome da fila de destino                | `MY_TEST_NAME` |
| `spring.rabbitmq.host`      | Host do RabbitMQ                       | `localhost`    |
| `spring.rabbitmq.port`      | Porta AMQP do RabbitMQ                 | `8090`         |
| `spring.rabbitmq.username`  | Usuário do RabbitMQ                    | —              |
| `spring.rabbitmq.password`  | Senha do RabbitMQ                      | —              |

> ⚠️ A mensagem é enviada para a *default exchange* usando o nome da fila como *routing key*.
> Por isso, a fila `MY_TEST_NAME` precisa **existir** no RabbitMQ antes do envio. Você pode criá-la pelo painel de administração.

## 🚀 Como executar

### 1. Subir o RabbitMQ com Docker

```bash
docker run -d --name rabbitmq \
  -p 8090:5672 -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=seu_usuario \
  -e RABBITMQ_DEFAULT_PASS=sua_senha \
  rabbitmq:management
```

- Porta `8090` → AMQP (usada pela aplicação)
- Porta `15672` → painel de administração: http://localhost:15672

Depois, acesse o painel e crie a fila **`MY_TEST_NAME`** (aba *Queues and Streams* → *Add a new queue*).

### 2. Configurar as credenciais

Ajuste `spring.rabbitmq.username` e `spring.rabbitmq.password` no `application.properties` com os mesmos valores usados no Docker.

### 3. Rodar a aplicação

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8081`.

## 📨 Enviando uma mensagem

**Endpoint**

```
POST /publisher/Text
Content-Type: text/plain
```

**Exemplo com cURL**

```bash
curl -X POST http://localhost:8081/publisher/Text \
  -H "Content-Type: text/plain" \
  -d "Olá, RabbitMQ!"
```

Depois, abra o painel do RabbitMQ, entre na fila `MY_TEST_NAME` e use **Get messages** para ver a mensagem publicada. 🎉

## 🗺️ Próximos passos

- [ ] Criar a aplicação **consumer** para ler as mensagens da fila
- [ ] Declarar fila, exchange e binding via código (`@Bean Queue`, `DirectExchange`, `Binding`)
- [ ] Enviar mensagens em formato JSON (objetos Java)
- [ ] Testar diferentes tipos de exchange (direct, topic, fanout)
- [ ] Adicionar `docker-compose` para subir tudo com um comando

## 👨‍💻 Autor

Feito por **[Kaio Candido](https://github.com/kaiocandido)** para fins de estudo.
