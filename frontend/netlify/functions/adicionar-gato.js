export const handler = async (event) => {
  try {
    const API_URL = process.env.VITE_API_URL;
    const API_KEY = process.env.VITE_API_KEY;

    // pegar cookie
    const cookies = event.headers.cookie || "";
    const token = cookies
      .split(";")
      .find(c => c.trim().startsWith("auth_token="))
      ?.split("=")[1];

    if (!token) {
      return {
        statusCode: 401,
        body: JSON.stringify({ error: "Usuário não autenticado" })
      };
    }

    // conteúdo original do multipart
    const contentType = event.headers["content-type"];

    // raw do jeito que veio
    const body = event.isBase64Encoded
      ? Buffer.from(event.body, "base64")
      : event.body;

    const resp = await fetch(`${API_URL}/api/animal`, {
      method: "POST",
      headers: {
        "x-api-key": API_KEY,
        "Authorization": "Bearer " + token,
        "Content-Type": contentType      // 🔥 ESSENCIAL
      },
      body
    });

    const text = await resp.text();

    return {
      statusCode: resp.status,
      headers: {
        "Access-Control-Allow-Origin": "http://localhost:5173",
        "Access-Control-Allow-Credentials": "true",
        "Content-Type": resp.headers.get("content-type") || "application/json"
      },
      body: text
    };

  } catch (err) {
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
};
