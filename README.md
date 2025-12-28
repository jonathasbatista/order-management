# 🛒 OrderHub - Sistema de Gestão de Pedidos

![Java](https://img.shields.io/badge/Java-21%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-green)
![SQLite](https://img.shields.io/badge/Database-SQLite-blue)
![Tailwind CSS](https://img.shields.io/badge/Frontend-Tailwind_CSS-38bdf8)
![JUnit 5](https://img.shields.io/badge/Tests-JUnit_5-2EA44F)

> Um sistema Full Stack para gestão de pedidos de e-commerce, focando em integridade de dados, performance e uma interface minimalista de alto contraste.

---

## 📸 Visão Geral

O **OrderHub** é uma aplicação completa que simula o fluxo de compras de uma loja virtual. O projeto foi desenvolvido para demonstrar a integração robusta entre um backend RESTful em **Java/Spring Boot** e um frontend moderno e leve utilizando **JavaScript Puro (Vanilla)** e **Tailwind CSS**.

O sistema gerencia todo o ciclo de vida do pedido: desde a seleção de produtos no carrinho, passando pela criação do pedido no banco de dados, até o processamento de pagamentos e atualização automática de status.

### ✨ Funcionalidades Principais

#### 🏠 Módulo Loja (Frontend)
* **Catálogo Dinâmico:** Listagem de produtos consumindo a API.
* **Filtros Inteligentes:** Busca por nome, filtragem por categoria e *toggle* para exibir apenas produtos ativos.
* **Carrinho Client-Side:** Gestão de estado do carrinho (adicionar/remover/atualizar) feita puramente em JavaScript.
* **Design Responsivo:** Interface arredondada que se adapta a dispositivos móveis e desktops.

#### 📦 Módulo de Pedidos (Backend & Frontend)
* **Criação Atômica:** Uso de `@Transactional` para garantir que pedidos só sejam salvos se todos os itens forem válidos e houver consistência nos dados.
* **Cálculo Seguro de Preços:** Todos os valores monetários são manipulados em **centavos (Long)** para evitar erros de arredondamento de ponto flutuante.
* **Histórico e Detalhes:** Visualização completa dos pedidos anteriores e seus itens.

#### 💳 Módulo de Pagamentos
* **Processamento de Pagamentos:** Endpoint dedicado para registrar pagamentos parciais ou totais.
* **Máquina de Estados:** O sistema calcula automaticamente se o valor pago cobre o total do pedido, atualizando o status de `NEW` para `PAID`.

#### 🛡️ Qualidade e Observabilidade
* **Logs Estruturados:** Implementação de logs (INFO, ERROR, WARN) em pontos chave do sistema para rastreamento de requisições, diagnósticos de erros e auditoria de transações.
* **Testes Unitários:** Suíte de testes automatizados cobrindo Services e regras de negócio críticas, garantindo estabilidade nas manutenções futuras.

---

## 🚀 Tecnologias Utilizadas

### Backend (API REST)
* **Java 21**: Linguagem core.
* **Spring Boot 4**: Framework para configuração e injeção de dependências.
* **Spring Data JPA**: Camada de persistência e abstração de repositórios.
* **SQLite**: Banco de dados relacional (configurado no `application.properties`) para facilidade de execução e portabilidade.
* **JUnit 5 & Mockito**: Frameworks para testes unitários e mocks.
* **SLF4J**: Abstração para logging.
* **Maven**: Gerenciamento de dependências e build.

### Frontend (Client)
* **HTML5 & CSS3**: Estrutura semântica e estilização.
* **Tailwind CSS**: Framework utilitário para design responsivo e tema "Preto e Branco".
* **Vanilla JavaScript (ES6+)**: Lógica de interação com a API (`fetch`), manipulação do DOM e gerenciamento de estado sem frameworks pesados (React/Angular).

---

## 🛠️ Como Rodar o Projeto

### Pré-requisitos
* JDK 21 ou superior instalado.
* Maven instalado (ou use o wrapper `mvnw` incluído).

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/order-management.git](https://github.com/seu-usuario/order-management.git)
    cd order-management
    ```

2.  **Execute a aplicação:**
    Utilize o Maven Wrapper para garantir a versão correta:
    ```bash
    ./mvnw spring-boot:run
    ```
    *No Windows:* `mvnw.cmd spring-boot:run`

3.  **Acesse a Aplicação:**
    Abra o navegador em: `http://localhost:8080/index.html`

### 🧪 Executando os Testes

Para garantir que todas as regras de negócio estão funcionando corretamente e verificar a integridade do código, execute a suíte de testes unitários:

```bash
./mvnw test
```

---

## 📡 Documentação da API

A API segue os padrões REST. Abaixo estão os principais endpoints identificados nos Controllers:

| Controlador | Método HTTP | Rota | Descrição |
| :--- | :--- | :--- | :--- |
| **Product** | `GET` | `/products` | Lista todos os produtos cadastrados. |
| **Product** | `POST` | `/products` | Cadastra um novo produto. |
| **Order** | `POST` | `/orders` | Cria um novo pedido (Recebe `CreateOrderDTO`). |
| **Order** | `GET` | `/orders` | Lista todos os pedidos. |
| **Order** | `GET` | `/orders/{id}` | Retorna detalhes de um pedido específico (DTO com itens e total). |
| **Payment** | `POST` | `/payments` | Registra um pagamento e atualiza status do pedido. |

---

## 🧠 Decisões de Arquitetura

1.  **Integração Frontend/Backend Simplificada:**
    Os arquivos estáticos (`index.html`, `js/`, `css/`) são servidos diretamente pelo Spring Boot através da pasta `src/main/resources/static`. Isso elimina a necessidade de configurar servidores de frontend separados ou lidar com problemas complexos de CORS (Cross-Origin Resource Sharing) durante o desenvolvimento.

2.  **Tratamento Global de Erros:**
    Implementação de um `GlobalExceptionHandler` para capturar exceções de negócio (ex: "Produto inativo") e retornar respostas JSON amigáveis com status HTTP adequados (Bad Request, Not Found), em vez de stack traces genéricos.

3.  **Integridade Referencial no Código:**
    Validações manuais no Service (como verificar se o produto está ativo antes da venda) garantem que as regras de negócio sejam respeitadas antes de qualquer persistência no banco.

4. **Observabilidade via Logs:**
   A aplicação não falha silenciosamente. Exceções tratadas e fluxos importantes de negócio geram logs no console, permitindo que o desenvolvedor entenda o comportamento do sistema em tempo real sem precisar de debug constante.