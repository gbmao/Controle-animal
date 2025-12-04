export const handler = async (event) => {
  if (event.httpMethod !== "POST") {
    return {
      statusCode: 405,
      body: JSON.stringify({ message: "Método não permitido" })
    };
  }

  try {
    const body = JSON.parse(event.body);
    const API_URL = process.env.VITE_API_URL;

    const resp = await fetch(`${API_URL}/api/auth/signup`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        login: body.login,
        email: body.email,
        password: body.password
      }),
    });

    const data = await resp.json();

    if (!resp.ok) {
      return {
        statusCode: resp.status,
        headers: {
          "Access-Control-Allow-Origin": event.headers.origin || "http://localhost:5174",
          "Access-Control-Allow-Credentials": "true",
        },
        body: JSON.stringify(data)
      };
    }

    // ✔️ Signup não retorna token. Só devolvemos os dados.
    return {
      statusCode: 200,
      headers: {
        "Access-Control-Allow-Origin": event.headers.origin || "http://localhost:5174",
        "Access-Control-Allow-Credentials": "true",
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        message: "Usuário criado com sucesso",
        login: data.login,
        email: data.email,
        id: data.id
      }),
    };

  } catch (err) {
    return {
      statusCode: 500,
      headers: {
        "Access-Control-Allow-Origin": event.headers.origin || "http://localhost:5174"
      },
      body: JSON.stringify({
        message: "Erro ao criar conta",
        detalhe: err.message
      })
    };
  }
};
