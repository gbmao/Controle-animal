export async function handler(event) {
  const API_URL = process.env.VITE_API_URL;

  try {
    const resp = await fetch(`${API_URL}/api/all`, {
      method: "GET",
      headers: {
        "Authorization": event.headers.authorization || "", // repassa o token
        "Content-Type": "application/json"
      }
    });

    const text = await resp.text();

    return {
      statusCode: resp.status,
      headers: { "Content-Type": "application/json" },
      body: text
    };

  } catch (err) {
    console.error("Erro listar-gatos:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
}
