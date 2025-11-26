export default async (req) => {
    const API_URL = process.env.VITE_API_URL;
    const API_KEY = process.env.VITE_API_KEY;
    
    const body = JSON.parse(req.body);
    const id = body.id;

    try {
        const resp = await fetch(`${API_URL}/api/${id}`, {
        method: "PUT",
        headers: { 
            "Content-Type": "application/json",
            "x-api-key": API_KEY
        },
        body: JSON.stringify({ name: body.name })
        });

        return {
        statusCode: 200,
        body: JSON.stringify({ ok: true })
        };

    } catch (err) {
        return {
        statusCode: 500,
        body: JSON.stringify({ error: err.message })
        };
    }
};
