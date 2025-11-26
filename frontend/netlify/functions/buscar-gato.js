export default async (req) => {
    const API_URL = process.env.VITE_API_URL;
    const termo = req.queryStringParameters.q;

    try {
        const resp = await fetch(`${API_URL}/api/search/${encodeURIComponent(termo)}`);

        const data = await resp.json();

        return {
        statusCode: 200,
        body: JSON.stringify(data)
        };

    } catch (err) {
        return {
        statusCode: 500,
        body: JSON.stringify({ error: err.message })
        };
    }
};
