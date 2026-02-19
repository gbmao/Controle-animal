// buscar-imagem-universal.js - CORRECTED
export async function handler(event) {
  const API_URL = process.env.VITE_API_URL;
  const API_KEY = process.env.VITE_API_KEY;
  const { id, path, catId } = event.queryStringParameters;

  console.log("=== BUSCAR IMAGEM UNIVERSAL DEBUG ===");
  console.log("Parameters:", { id, path, catId });

  // Get token from cookies
  const cookies = event.headers.cookie || "";
  const token = cookies
    .split(";")
    .find(c => c.trim().startsWith("auth_token="))
    ?.split("=")[1];

  console.log("Token extracted:", token ? "YES" : "NO");

  if (!token) {
    return {
      statusCode: 401,
      body: JSON.stringify({ error: "Usuário não autenticado" })
    };
  }

  if (!API_KEY) {
    return {
      statusCode: 500,
      body: JSON.stringify({ error: "API_KEY não configurada" })
    };
  }

  try {
    // The CORRECT endpoint is: /images/{animalId} where animalId is the CAT ID
    // NOT imgID!
    
    let animalId;
    
    if (catId) {
      animalId = catId; // Direct cat ID provided
    } else if (id) {
      // If id parameter is provided, it should be the CAT ID, not imgID
      // But we need to verify - check if this is actually needed
      animalId = id;
    } else {
      return {
        statusCode: 400,
        body: JSON.stringify({ error: "Missing animalId parameter" })
      };
    }

    const imageUrl = `${API_URL}/images/${animalId}`;
    console.log("Fetching image from:", imageUrl);
    console.log("Using cat ID:", animalId);

    // Required headers based on ImageController:
    // 1. x-api-key header (required - from ApiKeyValidator.check(key))
    // 2. Authorization: Bearer token (for @AuthenticationPrincipal)
    const headers = {
      'x-api-key': API_KEY,
      'Authorization': `Bearer ${token}`
    };

    const response = await fetch(imageUrl, { headers });
    
    console.log("Image response status:", response.status);
    
    if (!response.ok) {
      const errorText = await response.text();
      console.error("Image fetch failed:", errorText);
      
      return {
        statusCode: response.status,
        body: JSON.stringify({ 
          error: `Failed to fetch image: ${response.status}`,
          message: errorText,
          url: imageUrl,
          animalId: animalId
        })
      };
    }

    // Success! Process the image
    const buffer = await response.arrayBuffer();
    const contentType = response.headers.get('content-type') || 'image/jpeg';
    const base64Image = Buffer.from(buffer).toString("base64");

    return {
      statusCode: 200,
      headers: {
        "Content-Type": contentType,
        "Cache-Control": "public, max-age=86400",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": "true"
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