// adicionar-gato.js - CORRECTED VERSION
export const handler = async (event) => {
  try {
    console.log("=== ADICIONAR GATO DEBUG WITH IMAGE ===");
    
    const API_URL = process.env.VITE_API_URL;
    const API_KEY = process.env.VITE_API_KEY;

    // --- PEGAR TOKEN DO COOKIE ---
    const cookies = event.headers.cookie || "";
    console.log("Cookies received:", cookies);
    
    const token = cookies
      .split(";")
      .find(c => c.trim().startsWith("auth_token="))
      ?.split("=")[1];

    console.log("Token extracted:", token ? "YES" : "NO");

    if (!token) {
      console.log("Returning 401 - No token found");
      return {
        statusCode: 401,
        body: JSON.stringify({ error: "Usuário não autenticado" })
      };
    }

    // --- RAW BODY DO FRONTEND ---
    const rawBody = Buffer.from(
      event.body,
      event.isBase64Encoded ? "base64" : "utf8"
    );

    console.log("Request body size:", rawBody.length, "bytes");

    // --- CHAMADA AO BACKEND SPRING ---
    const resp = await fetch(`${API_URL}/api`, {
      method: "POST",
      headers: {
        "x-api-key": API_KEY,
        "Authorization": `Bearer ${token}`,
        "Content-Type": event.headers['content-type'],
      },
      body: rawBody
    });

    console.log("Backend response status:", resp.status);
    const text = await resp.text();
    console.log("Backend response body:", text);
    
    let jsonResponse;
    try {
      jsonResponse = JSON.parse(text);
      console.log("Parsed JSON response:", jsonResponse);
      console.log("Response keys:", Object.keys(jsonResponse));
      
      // Check for image-related fields
      if (jsonResponse) {
        console.log("Image ID field:", jsonResponse.imgID);
        console.log("Image URL field:", jsonResponse.imagemUrl);
        console.log("Image path field:", jsonResponse.imagePath);
        console.log("All fields:", Object.entries(jsonResponse));
      }
    } catch (e) {
      console.log("Response is not JSON or parsing failed");
    }

    return {
      statusCode: resp.status,
      headers: {
        "Content-Type": resp.headers.get("content-type") || "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": "true"
      },
      body: text
    };

  } catch (err) {
    console.error("Erro adicionar-gato:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
};