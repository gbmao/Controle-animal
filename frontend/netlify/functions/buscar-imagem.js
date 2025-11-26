    export async function handler(event) {
    const API_URL = process.env.VITE_API_URL;
    const id = event.queryStringParameters.id;

    if (!id) {
        return {
        statusCode: 400,
        body: JSON.stringify({ error: "Missing id parameter" })
        };
    }

    try {
        const response = await fetch(`${API_URL}/images/${id}`);

        // se o backend retornar null → Netlify recebe response.ok = false
        if (!response.ok) {
        return {
            statusCode: 404,
            body: JSON.stringify({ error: "Imagem não encontrada" })
        };
        }

        const buffer = await response.arrayBuffer();
        const base64Image = Buffer.from(buffer).toString("base64");

        return {
        statusCode: 200,
        headers: {
            "Content-Type": "image/jpeg"
        },
        body: base64Image,
        isBase64Encoded: true
        };


    } catch (err) {
        return {
        statusCode: 500,
        body: JSON.stringify({ error: err.message })
        };
    }
    }
