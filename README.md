# 🐱 Controle Animal - Pet Management System  

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![React](https://img.shields.io/badge/React-18-61DAFB.svg)](https://reactjs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Um sistema completo para gerenciamento de animais de estimação, desenvolvido para aprender e praticar desenvolvimento fullstack com tecnologias modernas.

## 🚀 Demonstração
  
**Frontend:** `https://patrulha-felina.netlify.app`

## ✨ Funcionalidades

### 🐾 Gerenciamento de Animais
- ✅ Cadastro de animais (gatos, cachorros, etc.)
- ✅ Upload de fotos
- ✅ Histórico de saúde
- ✅ Lembretes de vacinas e consultas

### 👤 Gestão de Usuários  
- ✅ Autenticação JWT segura
- ✅ Perfis de usuário
- ✅ Dashboard personalizado
- ✅ Multiplos animais por usuário

### 🔒 Segurança
- ✅ Autenticação com JWT
- ✅ Validação de dados robusta
- ✅ Proteção contra ataques comuns
- ✅ Criptografia de senhas

## 🛠️ Tecnologias

### Backend
- **Java 17** - Linguagem principal
- **Spring Boot 3** - Framework backend
- **Spring Security** - Autenticação e autorização
- **JWT** - Tokens de autenticação
- **PostgreSQL** - Banco de dados relacional
- **Flyway** - Migrations de banco
- **JUnit 5 & Mockito** - Testes unitários

### Frontend
- **Vue 3** - Framework JavaScript progressivo
- **Vue Router 4** - Roteamento e navegação
- **Pinia** - Gerenciamento de estado (stores)
- **Vite** - Build tool e bundler
- **Axios** - Cliente HTTP
- **CSS Custom Properties** - Variáveis CSS para temas (light/dark mode)
- **Font Awesome / Bootstrap Icons** - Ícones via classes CSS (bi-*)
- **Netlify Functions** - Serverless functions para proxy de API

## 📦 Instalação

### Pré-requisitos
- Java 17 ou superior
- Node.js 18+ 
- PostgreSQL 14+
- Maven 3.8+

### Backend
```bash
# Clone o repositório
git clone https://github.com/gbmao/controle-animal.git
cd controle-animal/backend

# Configure o banco de dados
cp src/main/resources/application.example.properties src/main/resources/application.properties
# Edite as configurações do banco

# Execute a aplicação
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
```

## 🔧 Configuração

### Variáveis de Ambiente (Backend)
```properties
# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/controle_animal
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# JWT
jwt.secret=seu_secret_aqui
jwt.expiration=86400000
```

### API Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/signup` | Registrar novo usuário |
| POST | `/api/auth/login` | Login e obtenção de token |
| GET | `/api/users/{id}/animals` | Listar animais do usuário |
| POST | `/api/animals` | Cadastrar novo animal |
| PUT | `/api/animals/{id}` | Atualizar animal |
| DELETE | `/api/animals/{id}` | Remover animal |

## 📚 Aprendizados

### Backend (Gabriel)
- Arquitetura REST com Spring Boot
- Autenticação JWT com Spring Security
- Validação de dados e tratamento de erros
- Upload e gestão de arquivos
- Testes unitários e de integração
- Migrations com Flyway
- Deploy em ambiente cloud

### Frontend (Débora)
- Componentização com React
- Gerenciamento de estado
- Formulários complexos com validação
- Consumo de APIs REST
- Rotas protegidas
- UI responsiva com Tailwind
- Deploy de aplicações React

## 🧪 Testes

```bash
# Backend tests
cd backend
mvn test

# Frontend tests  
cd frontend
npm test
```

## 📁 Estrutura do Projeto

```
controle-animal/
├── backend/                 # API Spring Boot
│   ├── src/main/java/com/projeto/controleanimal/
│   │   ├── controller/     # Controladores REST
│   │   ├── service/        # Lógica de negócio
│   │   ├── repository/     # Camada de dados
│   │   ├── model/          # Entidades JPA
│   │   ├── security/       # Configurações de segurança
│   │   └── dto/            # Objetos de transferência
│   └── src/main/resources/
│       └── application.properties
│
├── frontend/               # Aplicação React
│   ├── src/
│   │   ├── components/     # Componentes React
│   │   ├── pages/         # Páginas da aplicação
│   │   ├── services/      # Chamadas à API
│   │   ├── hooks/         # Custom hooks
│   │   └── utils/         # Funções utilitárias
│   └── public/
│
└── docs/                  # Documentação
```

## 🤝 Contribuição

Este é um projeto de aprendizado, mas contribuições são bem-vindas!

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Add nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 👥 Autores

- **Gabriel** ([@gbmao](https://github.com/gbmao)) - Backend Developer
- **Débora**([@deboradevsouza](https://github.com/deboradevsouza))  - Frontend Developer

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 🙏 Agradecimentos

- Comunidade Spring
- Documentação do React
- Stack Overflow pela paciência infinita
- Café ☕️ por manter a sanidade

---

**Desenvolvido com ❤️ para aprendizado e portfólio**

> "O único modo de fazer um excelente trabalho é amar o que você faz." - Steve Jobs
