export async function handler(event) {
    const API_URL = process.env.VITE_API_URL;
    const API_KEY = process.env.VITE_API_KEY;

    const body = JSON.parse(event.body);

    try {
        const resp = await fetch(`${API_URL}/api`, {
            method: "POST",
            headers: { 
                "Content-Type": "application/json",
                "x-api-key": API_KEY
            },
            body: JSON.stringify(body)
        });

        const data = await resp.json();

        return {
            statusCode: 200,
            body: JSON.stringify(data)
        };

    } catch (err) {
        console.error("Erro na Netlify Function:", err);

        return {
            statusCode: 500,
            body: JSON.stringify({ error: err.message })
        };
    }
}
