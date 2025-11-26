export async function handler(event) {
    const API_URL = process.env.VITE_API_URL;

    const nome = event.queryStringParameters.nome;

    if (!nome) {
        return {
            statusCode: 400,
            body: JSON.stringify({ error: "Missing nome parameter" })
        };
    }

    try {
        const resp = await fetch(`${API_URL}/api/search/${encodeURIComponent(nome)}`);

        if (!resp.ok) {
            return {
                statusCode: resp.status,
                body: JSON.stringify({ error: "Gato não encontrado" })
            };
        }

        const gato = await resp.json();

        // Sempre retornar array
        return {
            statusCode: 200,
            body: JSON.stringify([gato])
        };

    } catch (err) {
        return {
            statusCode: 500,
            body: JSON.stringify({ error: err.message })
        };
    }
}
