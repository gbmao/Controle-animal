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

🔹 PUT /{id}

Altera o nome e/ou idade do animal pelo ID.

Exemplo:

```bash
curl -X PUT -H "x-api-key: {senha}" https://controle-animal-production.up.railway.app/api/3
```
Body JSON:
```json
{
  "name": "Luna",
  "age": 4
}
```


Resposta:
✅200 OK 

Body JSON:
```json
{
  "id": 5,
  "name": "Luna",
  "age": 4,
  "type": "Animal" 
}
```


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



📘 Image API Documentation

Base URL:
```bash
/images
```

📄 GET /images/all

Retorna informações básicas de todas as imagens cadastradas.

✔️ Response 200 —ok
```json
[
  {
    "animalId": 1,
    "animalName": "Rex",
    "imageId": 10
  }
]
```


❌ Erros

Nenhum erro específico.

📤 POST /images/{animalId}

Faz upload de uma imagem para um animal.
Se o animal já possui uma imagem, ela é substituída automaticamente.

🔒 Authentication

Necessita header:

```css
x-api-key: {your-secret}
```

📥 Request

Path Param

Nome	Tipo	Descrição
animalId	Long	ID do animal

Body — multipart/form-data

Campo	Tipo	Obrigatório	Descrição
multipartImage	File	✔️	Arquivo de imagem
✔️ Response 200 — OK

Retorna o ID da imagem criada.

```json
10
```

❌ Possible Errors
Código	Motivo
401 Unauthorized	API key inválida
404 Not Found	Animal não encontrado
📥 GET /images/{id}

Faz o download da imagem pelo ID.

📥 Request

Path Param

Nome	Tipo	Descrição
id	Long	ID da imagem
✔️ Response 200 — OK

Retorna um binário JPEG.

Headers:
```arduino
Content-Type: image/jpeg
```

❌ Errors
Código	Motivo
404 Not Found	Imagem não encontrada
🗑️ DELETE /images/{animalId}

Remove a associação de imagem de um animal.

A imagem não é deletada da tabela, apenas removida do animal.

🔒 Authentication

Necessita:
```css
x-api-key: {your-secret}
```
📥 Request

Path Param

Nome	Tipo	Descrição
animalId	Long	ID do animal
✔️ Response 204 — No Content

Sem retorno.

❌ Errors
Código	Motivo
401 Unauthorized	API key inválida
404 Not Found	Animal não encontrado
