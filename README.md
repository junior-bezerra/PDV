# Sistema PDV (Ponto de Venda)

Projeto completo de sistema de ponto de venda (PDV) com Spring Boot, segurança com Spring Security, autenticação via formulário, integração com banco de dados PostgreSQL e testes de endpoints com Postman.

## 📦 Funcionalidades Implementadas

- Cadastro de produtos
- Cadastro de usuários com senha criptografada
- Autenticação via formulário (Spring Security)
- Validação de login
- Proteção de rotas com autenticação
- Integração com banco de dados PostgreSQL
- Testes de endpoints com Postman

---

## 🛠️ Tecnologias e Ferramentas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- Lombok
- Postman

---

## 🔐 Segurança (Spring Security)

### Configurações em `SecurityConfig.java`

```java
http.csrf().disable()
    .authorizeHttpRequests()
    .requestMatchers("/auth/**").permitAll()
    .anyRequest().authenticated()
    .and()
    .formLogin();
```

- As rotas `/auth/**` são públicas (cadastro e login)
- Todas as outras rotas exigem autenticação
- Senhas são criptografadas com `BCryptPasswordEncoder`

---

## 👤 Cadastro de Usuário

Endpoint para registrar um novo usuário:

**POST** `/auth/register`

```json
{
  "nome": "Admin",
  "email": "admin@email.com",
  "password": "123456"
}
```

O password é automaticamente criptografado no backend.

---

## 🔑 Login

Autenticação feita via **formulário padrão do Spring Security**.

Se quiser testar com Postman, utilize:

- Aba **Authorization**
- Tipo: `Basic Auth`
- `username`: email do usuário
- `password`: senha cadastrada

---

## 🛒 Cadastro de Produtos

Endpoint para adicionar produto ao banco:

**POST** `/produtos`

```json
{
  "nome": "Coca-Cola",
  "descricao": "Refrigerante lata 350ml",
  "preco": 4.99,
  "quantidade": 10
}
```

Obs: Requer autenticação via formulário. Para testes, é possível liberar a rota no `SecurityConfig` usando `.requestMatchers("/produtos/**").permitAll()` temporariamente.

---

## 🗃️ Banco de Dados (PostgreSQL)

### application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pdv
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

## 🔁 Estrutura de Pacotes

```
com.pdv.sistemapdv
├── controller
│   ├── AuthController.java
│   └── ProdutoController.java
├── model
│   ├── Usuario.java
│   └── Produto.java
├── repository
│   ├── UsuarioRepository.java
│   └── ProdutoRepository.java
├── security
│   ├── SecurityConfig.java
│   ├── UsuarioDetails.java
│   └── UsuarioDetailsService.java
└── service
    └── ProdutoService.java
```

---

## ✅ Próximos Passos

- Criar CRUD completo de produtos
- Adicionar controle de estoque
- Implementar vendas e fechamento de caixa
- Adicionar papéis de usuário (ADMIN, OPERADOR, etc.)
- Publicar projeto (Deploy) com banco de dados em nuvem

---

## 📬 Contato

Este projeto foi criado com fins didáticos e acadêmicos. Dúvidas ou melhorias, fique à vontade para contribuir!