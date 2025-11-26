    export async function handler(event) {
    const API_URL = process.env.VITE_API_URL;
    const API_KEY = process.env.VITE_API_KEY;

    const id = event.queryStringParameters.id;

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
            "x-api-key": API_KEY
        }
        });

        if (!resp.ok) {
        return {
            statusCode: resp.status,
            body: JSON.stringify({ error: "Erro ao deletar no backend" }),
        };
        }

        return {
        statusCode: 200,
        body: JSON.stringify({ ok: true }),
        };

    } catch (err) {
        return {
        statusCode: 500,
        body: JSON.stringify({ error: err.message }),
        };
    }
    }
