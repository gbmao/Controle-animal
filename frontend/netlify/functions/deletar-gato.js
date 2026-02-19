// deletar-gato.js - CORRECTED
export async function handler(event) {
  const API_URL = process.env.VITE_API_URL;
  const API_KEY = process.env.VITE_API_KEY;

  console.log("=== DELETAR GATO DEBUG ===");
  console.log("API_URL:", API_URL);
  console.log("API_KEY:", API_KEY ? "SET" : "NOT SET");
  
  const id = event.queryStringParameters?.id;

  if (!id) {
    return {
      statusCode: 400,
      body: JSON.stringify({ error: "Missing id parameter" }),
    };
  }

  // Get token from cookies (for Bearer auth)
  const cookies = event.headers.cookie || "";
  const token = cookies
    .split(";")
    .find(c => c.trim().startsWith("auth_token="))
    ?.split("=")[1];

  console.log("Cat ID to delete:", id);
  console.log("Token from cookies:", token ? "PRESENT" : "MISSING");
  console.log("API Key:", API_KEY ? "PRESENT" : "MISSING");

  try {
    // Build headers
    const headers = {
      'Content-Type': 'application/json'
    };

    // Add API key if available
    if (API_KEY) {
      headers['x-api-key'] = API_KEY;
    }

    // Add Bearer token if available
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    console.log("Headers being sent:", headers);
    console.log("Calling endpoint:", `${API_URL}/api/${id}`);

    const resp = await fetch(`${API_URL}/api/${id}`, {
      method: "DELETE",
      headers: headers
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
    console.error("Erro deletar-gato:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ 
        error: err.message,
        note: "Check if DELETE endpoint exists and accepts authentication"
      })
    };
  }
}