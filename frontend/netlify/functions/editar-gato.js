// editar-gato.js - CORRECTED
export async function handler(event) {
  const API_URL = process.env.VITE_API_URL;
  const API_KEY = process.env.VITE_API_KEY;

  console.log("=== EDITAR GATO DEBUG ===");
  console.log("API_URL:", API_URL);
  console.log("API_KEY:", API_KEY ? "SET" : "NOT SET");

  try {
    const body = JSON.parse(event.body);
    
    // Get token from cookies
    const cookies = event.headers.cookie || "";
    const token = cookies
      .split(";")
      .find(c => c.trim().startsWith("auth_token="))
      ?.split("=")[1];

    console.log("Cat ID to update:", body.id);
    console.log("New name:", body.name);
    console.log("Token from cookies:", token ? "PRESENT" : "MISSING");

    // Build headers
    const headers = {
      'Content-Type': 'application/json'
    };

    // Add API key
    if (API_KEY) {
      headers['x-api-key'] = API_KEY;
    }

    // Add Bearer token
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    console.log("Headers:", headers);
    console.log("Calling:", `${API_URL}/api/${body.id}`);

    const resp = await fetch(`${API_URL}/api/${body.id}`, {
      method: "PUT",
      headers: headers,
      body: JSON.stringify(body)
    });

    console.log("Backend response status:", resp.status);
    
    const text = await resp.text();
    console.log("Backend response:", text);

    return {
      statusCode: resp.status,
      headers: { 
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": "true"
      },
      body: text
    };

  } catch (err) {
    console.error("Erro editar-gato:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ 
        error: err.message,
        note: "Check if PUT endpoint exists and accepts the request format"
      })
    };
  }
}