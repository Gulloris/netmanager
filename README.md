# 🌐 NetManager — Gerenciador de Provedor de Internet

Sistema web para gerenciar clientes, planos e faturas de internet.

**Stack:** Java 17 + Spring Boot 3 + Thymeleaf + PostgreSQL (Supabase)  
**Hospedagem gratuita:** Railway  
**Banco de dados gratuito:** Supabase

---

## 🚀 PASSO A PASSO — DEPLOY GRATUITO

### 1. Criar banco de dados no Supabase (GRÁTIS)

1. Acesse **https://supabase.com** e crie uma conta gratuita
2. Clique em **"New Project"**
3. Preencha o nome do projeto (ex: `netmanager`) e uma senha forte
4. Aguarde criar (1-2 minutos)
5. Vá em **Settings → Database**
6. Copie a **Connection string (URI)** no formato:
   ```
   postgresql://postgres:[SUA-SENHA]@db.xxxx.supabase.co:5432/postgres
   ```
7. Guarde também o **User** e **Password** separadamente

---

### 2. Subir o código no GitHub

1. Crie uma conta em **https://github.com** (se não tiver)
2. Crie um repositório novo (ex: `netmanager`)
3. No terminal, na pasta do projeto:
   ```bash
   git init
   git add .
   git commit -m "primeiro commit"
   git remote add origin https://github.com/SEU_USUARIO/netmanager.git
   git push -u origin main
   ```

---

### 3. Deploy no Railway (GRÁTIS)

1. Acesse **https://railway.app** e crie conta com GitHub
2. Clique em **"New Project" → "Deploy from GitHub repo"**
3. Selecione o repositório `netmanager`
4. Railway detecta automaticamente que é Spring Boot (Maven)
5. Vá em **Variables** e adicione:

   | Variável          | Valor                                              |
   |-------------------|----------------------------------------------------|
   | `DATABASE_URL`    | `jdbc:postgresql://db.xxx.supabase.co:5432/postgres` |
   | `DATABASE_USER`   | `postgres`                                         |
   | `DATABASE_PASSWORD` | `SUA_SENHA_DO_SUPABASE`                          |
   | `PORT`            | `8080`                                             |

   > ⚠️ No `DATABASE_URL`, prefixe com `jdbc:` antes da URL do Supabase!  
   > Exemplo: `jdbc:postgresql://db.abcdef.supabase.co:5432/postgres`

6. Railway faz o deploy automático. Em ~3 minutos seu sistema estará online!
7. Clique em **Settings → Domains** para pegar a URL pública (ex: `netmanager.up.railway.app`)

---

## 💻 Rodando Localmente

### Pré-requisitos
- Java 17+
- Maven 3.6+

### Configuração
1. Edite `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://db.xxx.supabase.co:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=SUA_SENHA
   ```

2. Compile e rode:
   ```bash
   mvn spring-boot:run
   ```

3. Acesse: **http://localhost:8080**

---

## 📦 Funcionalidades

### 👥 Clientes
- Cadastro com nome, CPF, telefone, e-mail, endereço
- Associação a plano de internet
- Dia de vencimento da fatura
- Inativação de clientes

### 📶 Planos
- Nome, velocidade (Mbps) e valor mensal
- Cards visuais com preço em destaque
- Ativação/inativação

### 💰 Faturas
- Geração manual ou automática pelo cliente
- Filtros: Todas / Pendentes / Vencidas / Pagas
- **Botão de dar baixa** com data de pagamento
- Atualização automática de status (vencidas)
- Cancelamento de faturas

### 📊 Dashboard
- Total de clientes ativos
- Total de planos
- Faturas pendentes e vencidas
- Valor recebido no mês atual
- Atalhos rápidos

---

## ⚠️ Limites dos Planos Gratuitos

| Serviço   | Limite Free                          |
|-----------|--------------------------------------|
| Railway   | 5 USD de crédito/mês (suficiente p/ uso normal) |
| Supabase  | 500 MB de banco, 50.000 req/mês      |

Para uso comercial com muitos clientes, considere os planos pagos (~$5-10/mês).
