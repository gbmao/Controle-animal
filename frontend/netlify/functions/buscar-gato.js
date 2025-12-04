// buscar-gato.js - UPDATED to match listar-gatos structure
export const handler = async (event) => {
  const API_URL = process.env.VITE_API_URL;
  const API_KEY = process.env.VITE_API_KEY;
  
  console.log("=== BUSCAR GATO DEBUG ===");

  const nome = event.queryStringParameters?.nome;

  if (!nome) {
    return {
      statusCode: 400,
      body: JSON.stringify({ error: "Missing nome parameter" })
    };
  }

  // Get token from cookies
  const cookies = event.headers.cookie || "";
  const token = cookies
    .split(";")
    .find(c => c.trim().startsWith("auth_token="))
    ?.split("=")[1];

  console.log("Searching for:", nome);
  console.log("Token found:", token ? "YES" : "NO");

  try {
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

    console.log("Using headers:", headers);
    
    // Try the search endpoint - using the SAME endpoint as listar-gatos
    // First try /api/busca
    const response = await fetch(`${API_URL}/api/busca/${encodeURIComponent(nome)}`, {
      headers: headers
    });

    console.log("Search response status:", response.status);
    
    if (!response.ok) {
      // Try alternative endpoints
      console.log("Trying alternative search endpoints...");
      const altEndpoints = [
        `${API_URL}/api/search/${encodeURIComponent(nome)}`,
        `${API_URL}/api/animal/search/${encodeURIComponent(nome)}`,
        `${API_URL}/api/animals/search/${encodeURIComponent(nome)}`
      ];
      
      let altResponse = null;
      for (const endpoint of altEndpoints) {
        console.log("Trying:", endpoint);
        const res = await fetch(endpoint, { headers });
        if (res.ok) {
          altResponse = res;
          break;
        }
      }
      
      if (!altResponse) {
        return {
          statusCode: response.status,
          body: JSON.stringify({ 
            error: "Gato não encontrado",
            details: `Status: ${response.status}`
          })
        };
      }
      
      const lista = await altResponse.json();
      return processAndReturnCats(lista);
    }

    const lista = await response.json();
    return processAndReturnCats(lista);

  } catch (err) {
    console.error("Erro buscar-gato:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
};

// Helper function to ensure consistent cat data structure
function processAndReturnCats(lista) {
  console.log(`Found ${lista.length} cats`);
  
  // Log each cat's structure for debugging
  lista.forEach((cat, index) => {
    console.log(`Cat ${index + 1} structure:`, {
      id: cat.id,
      name: cat.name,
      imgID: cat.imgID,
      imageId: cat.imageId,
      pictureId: cat.pictureId,
      imagemUrl: cat.imagemUrl,
      imageUrl: cat.imageUrl,
      pictureUrl: cat.pictureUrl,
      // Log all keys
      allKeys: Object.keys(cat)
    });
  });

  // Transform to match what CatImage component expects
  const listaComImgId = lista.map(g => {
    // Determine the imgID value
    let imgID = -1;
    
    // Check various possible image ID fields
    if (g.imgID !== undefined && g.imgID !== null && g.imgID !== -1) {
      imgID = g.imgID;
    } else if (g.imageId !== undefined && g.imageId !== null) {
      imgID = g.imageId;
    } else if (g.pictureId !== undefined && g.pictureId !== null) {
      imgID = g.pictureId;
    } else if (g.id !== undefined) {
      // If no imgID field, use cat ID (some APIs work this way)
      imgID = g.id;
    }
    
    // Return structured data
    return {
      id: g.id,
      name: g.name,
      age: g.age,
      // Ensure imgID is present for CatImage component
      imgID: imgID,
      // Preserve other fields
      ...g
    };
  });

  console.log("Processed cats for frontend:", listaComImgId);

  return {
    statusCode: 200,
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Credentials": "true"
    },
    body: JSON.stringify(listaComImgId)
  };
}