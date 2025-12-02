export async function handler(event) {
  const API_URL = process.env.VITE_API_URL;
  const id = event.queryStringParameters.id;

  console.log("=== BUSCAR IMAGEM DEBUG ===");
  console.log("Requested image ID:", id);

  if (!id) {
    return {
      statusCode: 400,
      body: JSON.stringify({ error: "Missing id parameter" })
    };
  }

  try {
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

    // Try multiple possible endpoints
    const endpoints = [
      `${API_URL}/images/${id}`,
      `${API_URL}/api/images/${id}`,
      `${API_URL}/api/cats/${id}/image`,
      `${API_URL}/api/animal/${id}/image`,
      `${API_URL}/api/animals/${id}/image`,
      `${API_URL}/api/cat/${id}/image`
    ];

    let response;
    let successfulEndpoint;
    
    for (const endpoint of endpoints) {
      console.log("Trying endpoint:", endpoint);
      response = await fetch(endpoint, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (response.ok) {
        successfulEndpoint = endpoint;
        break;
      }
      console.log("Endpoint failed with status:", response.status);
    }

    if (!response || !response.ok) {
      console.log("All image endpoints failed");
      return {
        statusCode: 404,
        body: JSON.stringify({ 
          error: "Imagem não encontrada",
          triedEndpoints: endpoints
        })
      };
    }

    console.log("Successfully fetched from:", successfulEndpoint);
    
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