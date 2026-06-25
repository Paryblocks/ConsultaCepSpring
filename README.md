# ViaCep 2.0

Aplicação web para consulta de CEPs e endereços brasileiros, com sistema de usuários, histórico de pesquisas e favoritos. Desenvolvida como trabalho para a disciplina de Laboratório de Desenvolvimento de Software do IFRS.

---

## Do que se trata o projeto?

O ViaCep 2.0 é basicamente um buscador de endereços. Você digita um CEP e ele te retorna todas as informações daquele endereço (cidade, estado, bairro, logradouro, etc). Também funciona ao contrário: dá pra pesquisar pelo endereço e descobrir o CEP.

Além da busca simples, a aplicação oferece:

- **Cadastro e login de usuários** — com senha protegida por hash SHA-256
- **Histórico de consultas** — tudo que o usuário logado pesquisa fica salvo no banco
- **Favoritos** — dá pra salvar CEPs com apelidos personalizados (tipo "Casa da vó", "Trabalho")
- **Exportação de relatório em CSV** — o histórico pode ser baixado em formato de planilha
- **Gerenciamento de conta** — o usuário pode alterar nome, e-mail, senha ou excluir a conta

---

## Tecnologias utilizadas

| Camada | Tecnologia |
|---|---|
| Backend | Java 21 + Spring Boot 4.1 |
| Banco de dados | MySQL (via JPA/Hibernate) |
| Frontend | HTML, CSS (vanilla) e JavaScript puro |
| API externa | [ViaCEP](https://viacep.com.br/) |
| Build | Maven |

---

## Explicação da arquitetura

O projeto segue a arquitetura em camadas (layered architecture), que é o padrão mais comum quando se trabalha com Spring Boot. A ideia é separar as responsabilidades do código em grupos bem definidos, onde cada camada tem sua função e só conversa com a camada vizinha.

Visualmente, o fluxo funciona assim:

```
Frontend (HTML/JS)  →  Controller  →  Service  →  Repository  →  Banco de dados (MySQL)
```

### Backend — como as camadas funcionam

#### Model (`model/`)

São as entidades do sistema — as classes que representam as tabelas do banco de dados. Cada atributo vira uma coluna. O JPA/Hibernate cuida de traduzir isso automaticamente.

As entidades são:

- **Usuario** — guarda nome, e-mail, senha (em hash) e data de criação. Tem relacionamento de "um para muitos" com Cep, Favorito e Categoria.
- **Cep** — representa uma consulta feita. Armazena todos os dados retornados pela API do ViaCEP (cidade, estado, bairro, UF, região, DDD, etc) junto com a data/hora da consulta. Pertence a um usuário.
- **Favorito** — um CEP que o usuário escolheu salvar com um apelido. Tem relação "muitos para muitos" com Categoria através de uma tabela intermediária (`favorito_categoria`).
- **Categoria** — permite organizar os favoritos em grupos. Pertence a um usuário e pode estar associada a vários favoritos.

#### Controller (`controller/`)

É a porta de entrada da aplicação. Os controllers recebem as requisições HTTP vindas do frontend e devolvem as respostas.

Existem três controllers:

- **CepController** — expõe os endpoints de busca por CEP (`GET /cep/{cep}`), busca por endereço (`GET /cep/{uf}/{cidade}/{logradouro}`), histórico do usuário, limpeza de histórico, exclusão individual de itens e geração de relatório CSV.
- **FavoritoController** — cuida de favoritar um CEP, listar os favoritos do usuário e remover um favorito.
- **UsuarioController** — gerencia cadastro, login, atualização de dados e exclusão de conta.

#### Service (`service/`)

Aqui mora a lógica de negócio, a parte "pensante" da aplicação. É onde as regras são aplicadas antes de salvar ou buscar dados.

Por exemplo:
- O `CepService` é quem faz a chamada HTTP para a API do ViaCEP, faz o parse manual do JSON retornado, verifica se o CEP já existe no histórico do usuário (pra não duplicar, apenas atualiza a data) e também monta o arquivo CSV do relatório.
- O `UsuarioService` aplica o hash SHA-256 com salt na senha antes de salvar, impede cadastro com e-mail duplicado e protege a atualização parcial dos dados.
- O `FavoritoService` valida se o CEP já foi favoritado antes de salvar um novo.

#### Repository (`repository/`)

São interfaces que estendem `JpaRepository` do Spring Data. Basicamente, é aqui que as consultas ao banco de dados são definidas. O Spring gera a implementação automaticamente a partir do nome dos métodos (como `findByUsuarioIdOrderByDataConsultaDesc` ou `findByCepAndUsuarioId`).

Quando a query precisa de algo mais específico, é usada a anotação `@Query` com JPQL, como no caso da exclusão de um CEP do histórico verificando o dono.

#### Config (`config/`)

Contém a configuração de CORS, que libera o frontend (que roda em outra origem) para fazer requisições ao backend. Sem isso, o navegador bloquearia as chamadas.

#### Util (`util/`)

Classe utilitária com o método de geração de hash SHA-256, usado para proteger as senhas dos usuários.

### Frontend — como funciona

O frontend é composto por páginas HTML estáticas que se comunicam com o backend via `fetch` (JavaScript vanilla). Não usa nenhum framework — é HTML, CSS e JS puro.

As páginas são:

| Arquivo | O que faz |
|---|---|
| `Index.html` / `Index.js` | Página principal. Tem os dois formulários de busca (por CEP e por endereço), exibe a tabela de resultados/histórico, e permite favoritar, deletar itens, limpar histórico e gerar relatório. |
| `Login.html` / `Login.js` | Tela de login. Autentica o usuário e salva os dados na `sessionStorage`. |
| `Cadastro.html` / `Cadastro.js` | Tela de cadastro de novo usuário. |
| `Conta.html` / `Conta.js` | Painel da conta. Exibe nome e e-mail do usuário logado, permite alterar cada campo individualmente e também excluir a conta. |
| `Favorito.html` / `favorito.js` | Lista os CEPs favoritados e permite removê-los. |
| `api.js` | Módulo centralizado com funções de chamada ao backend (POST de cadastro, login e favoritar). |
| `layout.css` | Estilização global usando fonte Inter do Google Fonts, com paleta azul. |

A sessão do usuário é controlada via `sessionStorage` do navegador — quando o usuário faz login, o ID, nome e e-mail são salvos ali e consultados em cada página pra saber se ele está logado.

---

## Estrutura de pastas

```
ConsultaCepSpring/
├── pom.xml
├── src/
│   ├── frontend/                          # Interface do usuário
│   │   ├── Imagens/
│   │   ├── Index.html / Index.js
│   │   ├── Login.html / Login.js
│   │   ├── Cadastro.html / Cadastro.js
│   │   ├── Conta.html / Conta.js
│   │   ├── Favorito.html / favorito.js
│   │   ├── api.js
│   │   └── layout.css
│   ├── main/
│   │   ├── java/com/trabalho/viacep/
│   │   │   ├── ViacepApplication.java     # Classe principal (ponto de entrada)
│   │   │   ├── config/
│   │   │   │   └── CorsConfiguration.java
│   │   │   ├── controller/
│   │   │   │   ├── CepController.java
│   │   │   │   ├── FavoritoController.java
│   │   │   │   └── UsuarioController.java
│   │   │   ├── model/
│   │   │   │   ├── Cep.java
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Favorito.java
│   │   │   │   └── Categoria.java
│   │   │   ├── repository/
│   │   │   │   ├── CepRepository.java
│   │   │   │   ├── FavoritoRepository.java
│   │   │   │   └── UsuarioRepository.java
│   │   │   ├── service/
│   │   │   │   ├── CepService.java
│   │   │   │   ├── FavoritoService.java
│   │   │   │   └── UsuarioService.java
│   │   │   └── util/
│   │   │       └── Util.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── target/
```

---

## Como rodar o projeto

### Pré-requisitos

- Java 21
- Maven
- MySQL rodando localmente na porta 3306

### Banco de dados

O banco é criado automaticamente (por causa do `createDatabaseIfNotExist=true` na URL de conexão). Basta ter o MySQL rodando com o usuário `root` e sem senha, ou ajustar o `application.properties` com as credenciais corretas.

### Subindo o backend

```bash
./mvnw spring-boot:run
```

O servidor sobe na porta 8080.

### Abrindo o frontend

Como o frontend é composto por arquivos estáticos, basta abrir o `Index.html` direto no navegador (ou usar um servidor local como o Live Server do VS Code). Os arquivos ficam em `src/frontend/`.

---

## Endpoints da API

### CEP

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/cep/{cep}` | Busca um CEP na API do ViaCEP |
| `GET` | `/cep/{uf}/{cidade}/{logradouro}` | Busca por endereço |
| `GET` | `/cep/historico/{usuarioId}` | Retorna o histórico de consultas do usuário |
| `DELETE` | `/cep/historico/delete/{usuarioId}` | Limpa todo o histórico |
| `DELETE` | `/cep/historico/{id}?usuarioId=X` | Remove um item específico do histórico |
| `GET` | `/cep/relatorio/{usuarioId}` | Gera e baixa um relatório CSV |

### Favoritos

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/favoritos?cep=X&nome=Y&usuarioId=Z` | Favorita um CEP com apelido |
| `GET` | `/favoritos/usuario/{usuarioId}` | Lista os favoritos do usuário |
| `DELETE` | `/favoritos?cep=X&usuarioId=Y` | Remove um favorito |

### Usuário

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/usuario/cadastro` | Cadastra um novo usuário |
| `POST` | `/usuario/login` | Realiza login |
| `PATCH` | `/usuario/atualizar/{id}` | Atualiza nome, e-mail ou senha |
| `DELETE` | `/usuario/deletar/{id}` | Exclui a conta |

---

## Modelo de dados (relacionamentos)

```
Usuario (1) ──── (N) Cep           → Um usuário tem vários CEPs no histórico
Usuario (1) ──── (N) Favorito      → Um usuário tem vários favoritos
Usuario (1) ──── (N) Categoria     → Um usuário tem várias categorias
Favorito  (N) ──── (N) Categoria   → Favoritos e categorias se relacionam via tabela intermediária
```
