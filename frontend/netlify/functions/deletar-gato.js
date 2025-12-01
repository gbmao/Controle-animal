export async function handler(event) {
  const API_URL = process.env.VITE_API_URL;

  const id = event.queryStringParameters?.id;

  if (!id) {
    return {
      statusCode: 400,
      body: JSON.stringify({ error: "Missing id parameter" }),
    };
  }

  try {
    const resp = await fetch(`${API_URL}/api/${id}`, {
      method: "DELETE",
      headers: {
        "Authorization": event.headers.authorization || ""
      }
    });

    const text = await resp.text();

    return {
      statusCode: resp.status,
      headers: { "Content-Type": "application/json" },
      body: text
    };

  } catch (err) {
    console.error("Erro deletar-gato:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
}
