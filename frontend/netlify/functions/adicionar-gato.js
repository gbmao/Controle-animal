export const handler = async (event) => {
    try {
        const API_URL = process.env.VITE_API_URL;
        const API_KEY = process.env.VITE_API_KEY;

        // AQUI: event.body é base64 quando multipart
        const body = Buffer.from(event.body, "base64");

        const resp = await fetch(`${API_URL}/api`, {
        method: "POST",
        headers: {
            "Content-Type": event.headers["content-type"], // repassa o tipo multipart
            "x-api-key": API_KEY
        },
        body
        });

        const text = await resp.text();

        return {
        statusCode: resp.status,
        body: text
        };

    } catch (err) {
        console.error("Erro na Netlify Function:", err);
        return {
        statusCode: 500,
        body: JSON.stringify({ error: err.message })
        };
    }
};
