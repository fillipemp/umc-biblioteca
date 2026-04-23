# Sistema de Gerenciamento de Biblioteca

Aplicação web para gerenciamento de acervo, leitores, reservas, empréstimos e devoluções de uma biblioteca.

Desenvolvido com Java 24, Spring Boot, Thymeleaf e MongoDB Atlas.

---

## Estrutura do projeto

```
umc-biblioteca/
└── biblioteca/      → aplicação completa (back-end + telas HTML no mesmo projeto)
```

> O Spring Boot já serve as telas HTML via Thymeleaf — não existe uma aplicação
> front-end separada para instalar ou rodar.

### Como o código está organizado dentro de `biblioteca/`

```
biblioteca/src/main/
│
├── java/                        ← BACK-END (lógica em Java)
│   └── br/com/biblioteca/
│       ├── controller/          ← recebe as requisições HTTP e chama o serviço
│       ├── service/             ← regras de negócio
│       ├── model/               ← estrutura dos dados (entidades)
│       └── repository/          ← acesso ao banco de dados (MongoDB)
│
└── resources/                   ← FRONT-END (telas e estilos)
    ├── templates/               ← HTML de cada tela (renderizado pelo Thymeleaf)
    │   ├── books/
    │   ├── readers/
    │   ├── loans/
    │   ├── reservations/
    │   ├── users/
    │   └── fragments/           ← menu lateral e layout base
    └── static/
        └── css/                 ← estilo visual das telas
```

---

## Requisito

**Java 24** — [Download aqui](https://www.oracle.com/java/technologies/downloads/#java24)

Maven e todas as dependências são baixados automaticamente. Não precisa instalar mais nada.

---

## Como executar

### Terminal

```bash
# 1. Clone o repositório
git clone https://github.com/fillipemp/umc-biblioteca.git

# 2. Entre na pasta da aplicação
cd umc-biblioteca/biblioteca

# 3. Execute
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows
```

> **Se você baixou o ZIP pelo GitHub** (botão "Download ZIP"), a pasta extraída se chama
> `umc-biblioteca-main`. Nesse caso, no passo 2 use:
> ```bash
> cd umc-biblioteca-main/biblioteca
> ```

---

### IntelliJ IDEA (Community ou Ultimate)

1. Abra o IntelliJ
2. **File → Open** → selecione a pasta **`biblioteca`** → **OK**
3. Se aparecer a janela **"Trust and Open Project?"**, clique em **Trust Project**
4. Aguarde o IntelliJ terminar de indexar o projeto
5. No canto superior direito, clique em **"Current File"** → **Edit Configurations...**
6. Clique em **+** → **Maven**
7. No campo **Command line** digite: `spring-boot:run`
8. Em **Working directory** confirme que está apontando para a pasta `biblioteca`
9. Clique em **Run**

> Confirme que o IntelliJ está usando o JDK 24:
> **File → Project Structure → SDK → JDK 24**

---

### NetBeans

1. Abra o NetBeans
2. **File → Open Project** → selecione a pasta **`biblioteca`**
3. Marque a opção **Trust Project Build Script** antes de clicar em **Open Project**
4. Botão direito no projeto → **Run** (ou **F6**)

> Confirme que o NetBeans está usando o JDK 24:
> **Tools → Java Platforms → JDK 24**
>
> Se aparecer erro de conexão com o banco ao rodar, é porque o NetBeans está
> ignorando o arquivo `.env`. Não se preocupe, a URI do banco já está configurada
> diretamente no `application.properties` e a aplicação vai conectar normalmente.

---

Após iniciar, acesse no navegador:

## [http://localhost:8080](http://localhost:8080)

---

## Credenciais iniciais

Criadas automaticamente na primeira execução:

| Campo  | Valor           |
|--------|-----------------|
| E-mail | admin@admin.com |
| Senha  | Admin@123       |

---

## Funcionalidades

| Módulo | Descrição |
|--------|-----------|
| **Livros** | Cadastro, edição, exclusão e controle de estoque |
| **Leitores** | Cadastro com CPF e telefone, status ativo/inativo |
| **Reservas** | Reserva de livros com validade de 3 dias |
| **Empréstimos** | Prazo de 14 dias com desconto automático de estoque |
| **Devoluções** | Devolução com reposição automática de estoque |
| **Operadores** | Gerenciamento de usuários do sistema (somente administrador) |
