// buscar-imagem.js - ENHANCED
export async function handler(event) {
  const API_URL = process.env.VITE_API_URL;
  const id = event.queryStringParameters.id;
  const catId = event.queryStringParameters.catId; // Add support for catId

  console.log("=== BUSCAR IMAGEM DEBUG ===");
  console.log("Requested:", { id, catId });

  if (!id && !catId) {
    return {
      statusCode: 400,
      body: JSON.stringify({ error: "Missing id or catId parameter" })
    };
  }

  // Get token from cookies
  const cookies = event.headers.cookie || "";
  const token = cookies
    .split(";")
    .find(c => c.trim().startsWith("auth_token="))
    ?.split("=")[1];

  if (!token) {
    return {
      statusCode: 401,
      body: JSON.stringify({ error: "Usuário não autenticado" })
    };
  }

  try {
    // Get API_KEY
    const API_KEY = process.env.VITE_API_KEY;
    
    // Build headers
    const headers = {
      'Authorization': `Bearer ${token}`
    };
    
    if (API_KEY) {
      headers['x-api-key'] = API_KEY;
    }

    // Determine which endpoint to use
    let endpoint;
    if (catId) {
      // Use cat ID endpoint: /images/{catId}
      endpoint = `${API_URL}/images/${catId}`;
    } else {
      // Use image ID endpoint (try multiple)
      const endpoints = [
        `${API_URL}/images/${id}`,
        `${API_URL}/api/images/${id}`,
        `${API_URL}/api/cats/${id}/image`,
        `${API_URL}/api/animal/${id}/image`
      ];
      
      // Try each endpoint
      for (const url of endpoints) {
        console.log("Trying endpoint:", url);
        const response = await fetch(url, { headers });
        if (response.ok) {
          endpoint = url;
          break;
        }
      }
      
      if (!endpoint) {
        return {
          statusCode: 404,
          body: JSON.stringify({ error: "Imagem não encontrada" })
        };
      }
    }

    console.log("Fetching from:", endpoint);
    
    const response = await fetch(endpoint, { headers });
    
    if (!response.ok) {
      return {
        statusCode: response.status,
        body: JSON.stringify({ error: "Imagem não encontrada" })
      };
    }

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

  } catch (err) {
    console.error("Erro buscar-imagem:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
}