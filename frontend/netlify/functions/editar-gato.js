    export async function handler(event) {
    const API_URL = process.env.VITE_API_URL;
    const API_KEY = process.env.VITE_API_KEY;

    try {
        // Agora funciona, pois o frontend enviará JSON
        const body = JSON.parse(event.body);

        const resp = await fetch(`${API_URL}/api/${body.id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "x-api-key": API_KEY
        },
        body: JSON.stringify(body)
        });

        const data = await resp.json();

        return {
        statusCode: resp.status,
        body: JSON.stringify(data)
        };

    } catch (err) {
        console.error("Erro na Netlify Function editar-gato:", err);
        return {
        statusCode: 500,
        body: JSON.stringify({ error: err.message })
        };
    }
    }
