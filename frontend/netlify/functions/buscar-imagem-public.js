// buscar-imagem-public.js
export async function handler(event) {
  const API_URL = process.env.VITE_API_URL;
  const { id } = event.queryStringParameters;

  console.log("=== BUSCAR IMAGEM PUBLIC DEBUG ===");
  console.log("Image ID:", id);

  if (!id) {
    return {
      statusCode: 400,
      body: JSON.stringify({ error: "Missing id parameter" })
    };
  }

  try {
    // Try without any authentication headers
    const url = `${API_URL}/api/images/${id}`;
    console.log("Trying public access to:", url);
    
    const response = await fetch(url);
    
    console.log("Response status:", response.status);
    
    if (response.ok) {
      return await processImageResponse(response);
    }
    
    // If public access fails, try with simple auth
    console.log("Public access failed, trying with basic auth...");
    
    // Maybe it needs API key only (not Bearer token)
    const API_KEY = process.env.VITE_API_KEY;
    const headers = {};
    
    if (API_KEY) {
      headers['x-api-key'] = API_KEY;
    }
    
    const authResponse = await fetch(url, { headers });
    
    if (authResponse.ok) {
      return await processImageResponse(authResponse);
    }
    
    return {
      statusCode: authResponse.status,
      body: JSON.stringify({ 
        error: "Failed to fetch image",
        status: authResponse.status,
        url: url
      })
    };

  } catch (err) {
    console.error("Erro:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
}

async function processImageResponse(response) {
  const buffer = await response.arrayBuffer();
  const contentType = response.headers.get('content-type') || 'image/jpeg';
  const base64Image = Buffer.from(buffer).toString("base64");

  return {
    statusCode: 200,
    headers: {
      "Content-Type": contentType,
      "Cache-Control": "public, max-age=86400"
    },
    body: base64Image,
    isBase64Encoded: true
  };
}