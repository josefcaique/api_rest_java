# 📚 API REST Java com Spring Boot

Este projeto é uma API RESTful desenvolvida com **Java 17**, **Spring Boot**, **JPA**, **Docker**, e outras ferramentas modernas. Ele inclui funcionalidades como autenticação, envio de e-mails, upload/download de arquivos, integração com Swagger para documentação e testes automatizados. 

---

## 🚀 Funcionalidades

- 🔐 Autenticação via token (JWT)
- 👤 CRUD de pessoas
- 📚 CRUD de livros
- 📂 Upload e download de arquivos (CSV/XLSX)
- 📧 Envio de e-mails com template HTML
- 📄 Documentação Swagger/OpenAPI
- 🧪 Testes de integração com CORS
- 🐳 Docker + Docker Compose
- 🔄 CI/CD com GitHub Actions

---

## 🧰 Tecnologias e ferramentas

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 / MySQL
- Swagger/OpenAPI
- Docker e Docker Compose
- GitHub Actions
- Lombok
- Apache POI (manipulação Excel)
- JavaMailSender
- Maven

---

## 📁 Estrutura do projeto

```
api_rest/
├── controllers/
├── services/
├── models/
├── repositories/
├── config/
├── exceptions/
├── util/
├── UploadDir/
├── resources/
│   └── application.yml
├── Dockerfile
├── pom.xml
```

---

## 🧪 Testes

- Os testes de integração estão localizados em:
  ```
  integrationtests/controllers/cors/withjson/PersonControllerTest.java
  ```
- Testes utilizam JUnit e Spring Boot Test.

---

## 📦 Como executar o projeto localmente

### Requisitos
- Java 17+
- Maven 3.8+
- Docker (opcional)

### Executando via terminal

```bash
# Acessar o diretório da aplicação
cd api_rest

# Compilar o projeto
./mvnw clean install

# Executar a aplicação
./mvnw spring-boot:run
```

A aplicação ficará disponível em: `http://localhost:8080`

---

### 🐳 Executando com Docker

```bash
docker-compose up --build
```

Isso irá subir o banco de dados e a aplicação.

---

## 📂 Upload de Arquivos

- A pasta `UploadDir/` contém exemplos de arquivos `.csv` e `.xlsx` para upload.
- Endpoints permitem salvar e recuperar arquivos localmente.

---

## 🧪 Testando os endpoints

- Acesse a documentação Swagger:
```
http://localhost:8080/swagger-ui.html
```

---

## 🔒 Autenticação

- Autenticação JWT configurada via `AuthController`.
- Exemplo de login:
```json
POST /auth/signin
{
  "username": "admin",
  "password": "123456"
}
```

O token deve ser usado no header `Authorization: Bearer {token}` para chamadas autenticadas.

---

## 📬 Envio de E-mails

- Endpoint: `POST /email/send`
- Envia e-mail usando SMTP com suporte a templates HTML.

---

## 🔧 Configurações

- Configurações no `application.yml` para banco de dados, e-mail, CORS etc.

---

## 🛠️ CI/CD

- Configurado com GitHub Actions para build e deploy contínuo:
  - `.github/workflows/continuous-deployment.yml`

---

## 👤 Autor

**Josef Caique**  
💼 [LinkedIn](https://www.linkedin.com/in/seu-perfil)  
💻 [GitHub](https://github.com/josefcaique)

---
