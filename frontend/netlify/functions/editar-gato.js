export async function handler(event) {
  const API_URL = process.env.VITE_API_URL;

  try {
    const body = JSON.parse(event.body);

    const resp = await fetch(`${API_URL}/api/${body.id}`, {
      method: "PUT",
      headers: {
        "Authorization": event.headers.authorization || "",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body)
    });

    const text = await resp.text();

    return {
      statusCode: resp.status,
      headers: { "Content-Type": "application/json" },
      body: text
    };

  } catch (err) {
    console.error("Erro editar-gato:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
}
