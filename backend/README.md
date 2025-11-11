🐾 API — Controle Animal

Base URL:
```bash
https://controle-animal-production.up.railway.app/api
```

Headers obrigatórios:
```h
x-api-key: {senha aqui}
Content-Type: application/json
```

📋 Endpoints
🔹 GET /all

Retorna todos os animais cadastrados.

Exemplo de requisição:
```bash
curl -H "x-api-key: {senha}" https://controle-animal-production.up.railway.app/api/all
```

Exemplo de resposta:
```json
[
  { "id": 1, "name": "Boris", "age": 3, "type": "Cat" },
  { "id": 2, "name": "Mimi", "age": 2, "type": "Cat" }
]
```
🔹 GET /{id}

Retorna um animal específico.

Exemplo:
```bash
curl -H "x-api-key: {senha}" https://controle-animal-production.up.railway.app/api/1
```

Resposta:
```json
{ "id": 1, "name": "Boris", "age": 3, "type": "Cat" }
```
🔹 POST /

Cadastra um novo animal.

Headers:
```h
x-api-key: {senha}
Content-Type: application/json
```

Body JSON:
```json
{
  "name": "Luna",
  "age": 4,
  "type": "Cat"
}
```

Resposta:
```json
{
  "id": 5,
  "name": "Luna",
  "age": 4,
  "type": "Cat"
}
```
🔹 DELETE /{id}

Remove um animal pelo ID.

Exemplo:
```bash
curl -X DELETE -H "x-api-key: {senha}" https://controle-animal-production.up.railway.app/api/3
```

Resposta:
✅ 204 No Content (sem corpo na resposta)

🔐 Autenticação

Todas as rotas que modificam dados (POST, DELETE) exigem o header:
```h
x-api-key: {senha}
```
⚠️ Códigos de erro
Código	Descrição
401	Chave de API inválida
404	Animal não encontrado
500	Erro interno do servidor
