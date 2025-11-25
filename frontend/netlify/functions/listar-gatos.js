export async function handler() {
    const response = await fetch(process.env.VITE_API_URL + "/api/all", {
        headers: {
        "x-api-key": process.env.VITE_API_KEY
        }
    });

    const data = await response.json();

    return {
        statusCode: 200,
        body: JSON.stringify(data)
    };
}
