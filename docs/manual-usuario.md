---
título: Manual do Usuário — Sistema de Gerenciamento de Biblioteca
versão: 1.0
data: 2026
---

# SISTEMA DE GERENCIAMENTO DE BIBLIOTECA (SGBP)
## Manual do Usuário

**Versão:** 1.0  
**Data:** Abril de 2026  
**Repositório GitHub:** [INSERIR LINK DO GITHUB AQUI]

---

## SUMÁRIO

1. [Introdução](#1-introdução)
2. [Requisitos do Sistema](#2-requisitos-do-sistema)
3. [Acesso ao Sistema](#3-acesso-ao-sistema)
4. [Cadastro de Livro](#4-cadastro-de-livro)
5. [Cadastro de Usuário/Leitor](#5-cadastro-de-usuárioleitor)
6. [Registro de Reserva](#6-registro-de-reserva)
7. [Registro de Empréstimo](#7-registro-de-empréstimo)
8. [Registro de Devolução](#8-registro-de-devolução)
9. [Considerações Finais](#9-considerações-finais)

---

## 1. Introdução

O **Sistema de Gerenciamento de Biblioteca (SGBP)** é uma aplicação web desenvolvida em Java com Spring Boot e Thymeleaf, com persistência em MongoDB Atlas. O sistema permite o controle do acervo bibliográfico, cadastro de leitores, registro de reservas, empréstimos e devoluções de livros.

O presente manual tem como objetivo orientar o usuário administrador no uso das funcionalidades disponíveis na interface web.

---

## 2. Requisitos do Sistema

### Para execução local

| Componente | Requisito |
|------------|-----------|
| Sistema Operacional | Windows 10/11, macOS 12+ ou Linux |
| JDK | Java 24 ou superior |
| Conexão à Internet | Necessária (MongoDB Atlas) |
| Navegador | Chrome, Firefox, Edge ou Safari (versões recentes) |

### Variáveis de ambiente necessárias

| Variável | Descrição |
|----------|-----------|
| `MONGODB_URI` | String de conexão do MongoDB Atlas |
| `MONGODB_DB`  | Nome do banco de dados (ex.: `biblioteca`) |

---

## 3. Acesso ao Sistema

Após iniciar a aplicação (veja o README para instruções de execução), acesse:

```
http://localhost:8080
```

O sistema redireciona automaticamente para a tela de login.

### Credenciais iniciais

| Campo | Valor |
|-------|-------|
| E-mail | admin@admin.com |
| Senha  | Admin@123 |

> **Atenção:** troque a senha após a primeira utilização em ambiente de produção.

**(INSERIR PRINT DA TELA DE LOGIN AQUI)**

---

## 4. Cadastro de Livro

O módulo de livros permite gerenciar o acervo bibliográfico da biblioteca.

### 4.1 Acessar a lista de livros

No menu lateral, clique em **Livros**. A tela exibe todos os livros cadastrados, com informações de quantidade total e quantidade disponível.

**(INSERIR PRINT DA TELA DE LISTA DE LIVROS AQUI)**

### 4.2 Cadastrar um novo livro

1. Clique no botão **+ Novo Livro** (canto superior direito)
2. Preencha os campos do formulário:
   - **Título** *(obrigatório)*
   - **Autor** *(obrigatório)*
   - **ISBN** *(opcional)*
   - **Gênero** *(opcional)*
   - **Ano de Publicação** *(opcional)*
   - **Quantidade Total** *(obrigatório)* — define o estoque inicial; a quantidade disponível é preenchida automaticamente com o mesmo valor
3. Clique em **Salvar**

**(INSERIR PRINT DO FORMULÁRIO DE CADASTRO DE LIVRO AQUI)**

### 4.3 Editar ou remover um livro

Na lista de livros, clique em **Editar** para alterar os dados ou em **Remover** para excluir o registro. A exclusão solicita confirmação.

---

## 5. Cadastro de Usuário/Leitor

O módulo de leitores gerencia os usuários que podem realizar reservas e empréstimos.

### 5.1 Acessar a lista de leitores

No menu lateral, clique em **Usuários/Leitores**.

**(INSERIR PRINT DA TELA DE LISTA DE LEITORES AQUI)**

### 5.2 Cadastrar um novo leitor

1. Clique em **+ Novo Leitor**
2. Preencha os campos:
   - **Nome** *(obrigatório)*
   - **E-mail** *(obrigatório)*
   - **CPF** *(opcional)*
   - **Telefone** *(opcional)*
   - **Status** — Ativo ou Inativo
3. Clique em **Salvar**

**(INSERIR PRINT DO FORMULÁRIO DE CADASTRO DE LEITOR AQUI)**

---

## 6. Registro de Reserva

A reserva permite que um leitor reserve um livro com disponibilidade. A reserva expira em 3 dias e **não reduz o estoque** — apenas o empréstimo efetivo reduz.

### 6.1 Criar uma reserva

1. No menu lateral, clique em **Reservas**
2. Clique em **+ Nova Reserva**
3. Selecione o **Livro** (somente livros com disponibilidade aparecem habilitados)
4. Selecione o **Leitor**
5. Clique em **Criar Reserva**

**(INSERIR PRINT DO FORMULÁRIO DE RESERVA AQUI)**

### 6.2 Cancelar uma reserva

Na lista de reservas, clique em **Cancelar** na linha da reserva com status **Pendente**.

**(INSERIR PRINT DA LISTA DE RESERVAS COM OPÇÃO DE CANCELAR AQUI)**

---

## 7. Registro de Empréstimo

O empréstimo registra a retirada física do livro pelo leitor. Ao confirmar, a **quantidade disponível do livro é reduzida em 1** e o prazo padrão de devolução é definido para **14 dias**.

### 7.1 Criar um empréstimo

1. No menu lateral, clique em **Empréstimos**
2. Clique em **+ Novo Empréstimo**
3. Selecione o **Livro** (apenas livros disponíveis aparecem habilitados)
4. Selecione o **Leitor**
5. Clique em **Registrar Empréstimo**

**(INSERIR PRINT DO FORMULÁRIO DE EMPRÉSTIMO AQUI)**

### 7.2 Visualizar empréstimos

A tela de listagem exibe todos os empréstimos com status **Ativo**, **Devolvido** ou **Em Atraso**.

**(INSERIR PRINT DA LISTA DE EMPRÉSTIMOS AQUI)**

---

## 8. Registro de Devolução

A devolução registra o retorno do livro pelo leitor. Ao confirmar, a **quantidade disponível do livro é incrementada em 1**.

### 8.1 Registrar uma devolução

1. No menu lateral, clique em **Devoluções**
2. A tela lista todos os empréstimos **ativos**, indicando se estão no prazo ou em atraso
3. Clique em **Registrar Devolução** na linha do empréstimo correspondente
4. Confirme a operação na caixa de diálogo

**(INSERIR PRINT DA TELA DE DEVOLUÇÕES AQUI)**

---

## 9. Considerações Finais

- O sistema foi desenvolvido como parte do projeto acadêmico da disciplina de Desenvolvimento de Sistemas.
- Para dúvidas técnicas, consulte o [README](../README.md) do projeto.
- O código-fonte está disponível em: [INSERIR LINK DO GITHUB AQUI]

---

*Documento elaborado seguindo diretrizes do padrão ABNT para manuais técnicos.*
