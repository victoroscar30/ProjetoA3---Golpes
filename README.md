# Projeto A3 - Sistema de Detecção de URLs Maliciosas

**Projeto A3 - Golpes** é uma aplicação **Java** desenvolvida para o programa Dual Bradesco, com o objetivo de combater golpes financeiros online através da verificação de URLs suspeitas. O sistema permite que usuários verifiquem a segurança de sites tendo como objetivo de auxiliar usuários a verificarem se determinados sites são confiáveis, maliciosos ou phishing. Oferecendo também um painel administrativo completo para monitoramento. A plataforma conta com funcionalidades de cadastro, login, consulta de sites e um painel administrativo para acompanhar as pesquisas feitas pelos usuários comuns.

---

## 📋 Índice

- [🛠 Tecnologias e Estruturas](#-tecnologias-e-estruturas)
- [✨ Funcionalidades](#-funcionalidades)
- [🎯 Objetivo](#-objetivo)
- [🏗️ Arquitetura](#️-arquitetura)
- [📊 Estruturas de Dados](#-estruturas-de-dados)
- [👥 Equipe](#-equipe)
- [🚀 Instalação e Configuração](#-instalação-e-configuração)
- [🎥 Demonstração em Vídeo](#-demonstração-em-vídeo)

---

## 🛠 Tecnologias e Estruturas

Este projeto foi desenvolvido com as seguintes tecnologias e estruturas:

* **Linguagem:** Java
* **Banco de Dados:** MySQL
* **Interface Gráfica:** Swing com FlatLaf (para tema dark)
* **Estruturas de Dados Customizadas:**
    * Trie para sugestão de URLs
    * Lista Duplamente Ligada para gerenciamento de avisos
* **Bibliotecas:**
    * MigLayout (para organização de componentes)
    * JDBC para conexão com banco de dados

---

## ✨ Funcionalidades

### 👤 Painel do Usuário

* **Autenticação segura** com registro de acessos suspeitos
* **Verificação de URLs** em tempo real
* **Classificação de riscos:** phishing, desconhecido, suspeito e seguro.
* **Histórico de acessos** com datas e horários
* **Dashboard visual** com métricas importantes
* **Sistema de avisos dinâmicos** com imagens educativas
* **Atualização de senha** segura

### 👮 Painel Administrativo

* **CRUD completo** para todas as tabelas (usuários, URLs, acessos, alertas)
* **Visualização em gráficos:**
    * Acessos por URL
    * Cadastros de usuários
    * Classificações de URLs
    * Top acessos
* **Alternância entre modos** (admin/comum)
* **Gerenciamento de alertas** e classificações

---

## 🎯 Objetivo

O projeto visa combater os golpes financeiros listados pelo Banco Central, especialmente:

* Phishing
* Sites falsos de bancos
* Fraudes em investimentos
* Golpes de falsos suportes técnicos

Através de uma plataforma que:

* **Educa** usuários sobre ameaças
* **Previne** acessos a sites maliciosos
* **Monitora** padrões de acesso suspeitos

---

## 🏗️ Arquitetura
```
├── Frontend (Swing)
│   ├── UsuarioDashboardUI - Interface do usuário
│   ├── AdminPanel - Painel administrativo
│   └── Componentes customizados
│
├── Backend
│   ├── DAOs (AcessoDAO, UrlDAO, UsuarioDAO)
│   ├── Modelos (Acesso, Url, Usuario)
│   └── Utilitários (PermissaoUtils)
│
├── Estruturas de Dados
│   ├── TrieUrls - Para sugestão de URLs
│   └── ListaDuplamenteLigadaAvisos - Para carrossel de avisos
│
└── Banco de Dados MySQL
```
---

## 📊 Estruturas de Dados

1.  **Trie (Árvore de Prefixos)**
    * **Finalidade:** Sugestão automática de URLs durante a digitação.
    * **Vantagem:** Busca eficiente O(m) onde m é o tamanho da string.
    * **Implementação:** Customizada (não utiliza bibliotecas Java).

2.  **Lista Duplamente Ligada**
    * **Finalidade:** Gerenciamento do carrossel de avisos visuais.
    * **Vantagem:** Navegação bidirecional eficiente entre avisos.
    * **Implementação:** Totalmente customizada.

---

## 👥 Equipe

| Integrante                            | RA            | LinkedIn                                                                 |
|--------------------------------------|---------------|--------------------------------------------------------------------------|
| **Victor Macartney Oscar Monteiro**  | 12522164519   | [linkedin.com/in/victor-monteiro-339291221](https://www.linkedin.com/in/victor-monteiro-339291221/) |
| **Gustavo Maglio Campos**            | 12522162221   | [linkedin.com/in/gustavo-campos-a2a974225](https://www.linkedin.com/in/gustavo-campos-a2a974225)     |
| **Luanna Correia da Silva**          | 12522219759   | [linkedin.com/in/luanna-correia-5a0a2a203](https://www.linkedin.com/in/luanna-correia-5a0a2a203/)    |
| **Maria Fernanda Kazi de Menezes**   | 12522213975   | [linkedin.com/in/maria-fernanda-menezes-762a05233](https://www.linkedin.com/in/maria-fernanda-menezes-762a05233/) |

---

## 🚀 Instalação e Configuração

### Pré-requisitos

* Java JDK 11+
* MySQL 8.0+
* MySQL Connector/J

### Passos para execução

1.  Clone o repositório:

    ```bash
    git clone https://github.com/victoroscar30/ProjetoA3---Golpes.git
    cd ProjetoA3---Golpes
    ```

2.  Configure o banco de dados:
    * Importe o arquivo SQL fornecido.
    * Atualize as credenciais no arquivo `Conexao.java`.
    
    **É necessária a instalação do JConnector** para a conexão com o MySQL.
    - Baixe-o em: https://dev.mysql.com/downloads/connector/j/

### Diferenciais Implementados

* Interface moderna com tema dark
* Sistema de avisos visuais com imagens educativas
* Registro de acessos suspeitos
* Sugestão inteligente de URLs
* Gráficos administrativos completos

---
## 🎥 Demonstração em Vídeo

[Assista à Demonstração do Projeto A3](https://www.youtube.com/watch?v=hfgwvU6UzvI)
