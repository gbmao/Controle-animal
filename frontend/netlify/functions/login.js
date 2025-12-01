export const handler = async (event) => {
  try {
    const API_URL = process.env.VITE_API_URL;

    const body = JSON.parse(event.body);
    const { login, password } = body;

    // 🔥 LOGIN NO BACKEND JAVA
    const resp = await fetch(`${API_URL}/api/auth/signin`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ login, password })
    });

    const data = await resp.json();

    console.log("====== RESPOSTA DO BACKEND (JAVA) ======");
    console.log(data);   // <--- precisamos saber o nome DO TOKEN aqui

    if (!resp.ok) {
      return {
        statusCode: resp.status,
        body: JSON.stringify(data)
      };
    }

    // 🔥 Aqui é onde está errado. Antes estava: data.token (mas token NÃO EXISTE)
    const token = data.token || data.jwt || data.accessToken || data.tokenJwt;

    console.log("====== TOKEN EXTRAÍDO ======");
    console.log(token);

    if (!token) {
      return {
        statusCode: 500,
        body: JSON.stringify({
          error: "Backend não retornou token! Campos disponíveis:",
          campos: data
        })
      };
    }

    const cookie = `auth_token=${token}; Secure; SameSite=None; Path=/; Max-Age=86400`;

    return {
      statusCode: 200,
      headers: {
        "Access-Control-Allow-Origin": "http://localhost:5174",
        "Access-Control-Allow-Credentials": "true",
        "Set-Cookie": `auth_token=${token}; Path=/; HttpOnly; Secure; SameSite=None`
      },
      body: JSON.stringify(user)
};

  } catch (err) {
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
};
