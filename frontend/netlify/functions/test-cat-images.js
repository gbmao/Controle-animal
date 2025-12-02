// test-cat-images.js - UPDATED VERSION
export const handler = async (event) => {
  const API_URL = process.env.VITE_API_URL;
  
  console.log("=== TEST CAT IMAGES DEBUG ===");
  console.log("Headers received:", JSON.stringify(event.headers, null, 2));
  
  // Get token from cookies
  const cookies = event.headers.cookie || "";
  console.log("Raw cookies:", cookies);
  
  const token = cookies
    .split(";")
    .find(c => c.trim().startsWith("auth_token="))
    ?.split("=")[1];

  console.log("Token extracted:", token ? `YES (${token.substring(0, 30)}...)` : "NO");

  if (!token) {
    return {
      statusCode: 401,
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": "true"
      },
      body: JSON.stringify({ 
        error: "Não autenticado",
        message: "Token não encontrado nos cookies. Faça login primeiro.",
        debug: {
          cookiesReceived: cookies,
          cookieKeys: cookies.split(";").map(c => c.trim().split("=")[0])
        }
      })
    };
  }

  try {
    // First, get all cats
    console.log("Fetching cats from:", `${API_URL}/api/list`);
    const catsResponse = await fetch(`${API_URL}/api/list`, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    
    console.log("Cats response status:", catsResponse.status);
    
    if (!catsResponse.ok) {
      const errorText = await catsResponse.text();
      console.error("Failed to fetch cats:", errorText);
      return {
        statusCode: catsResponse.status,
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Credentials": "true"
        },
        body: errorText
      };
    }
    
    const cats = await catsResponse.json();
    console.log(`Found ${cats.length} cats`);
    
    // Test image endpoints for each cat (limit to first 3 for speed)
    const catsToTest = cats.slice(0, 3);
    console.log(`Testing images for ${catsToTest.length} cats`);
    
    const results = await Promise.all(
      catsToTest.map(async (cat) => {
        const endpoints = [
          { url: `${API_URL}/api/images/${cat.imgID || cat.id}`, name: 'API Images by ID' },
          { url: `${API_URL}/images/${cat.imgID || cat.id}`, name: 'Direct Images by ID' },
          { url: `${API_URL}/api/cats/${cat.id}/image`, name: 'Cat Image Endpoint' },
          { url: `${API_URL}/api/animal/${cat.id}/image`, name: 'Animal Image Endpoint' },
          { url: `${API_URL}/api/animals/${cat.id}/image`, name: 'Animals Image Endpoint' }
        ];
        
        const endpointTests = [];
        
        for (const endpoint of endpoints) {
          try {
            console.log(`Testing ${endpoint.name}: ${endpoint.url}`);
            const resp = await fetch(endpoint.url, {
              headers: { 'Authorization': `Bearer ${token}` }
            });
            
            endpointTests.push({
              name: endpoint.name,
              url: endpoint.url,
              status: resp.status,
              ok: resp.ok,
              contentType: resp.headers.get('content-type'),
              contentLength: resp.headers.get('content-length')
            });
            
            // Read a small part of the response to check if it's actually an image
            if (resp.ok) {
              const clone = resp.clone(); // Clone to read without consuming
              const buffer = await clone.arrayBuffer();
              const firstBytes = Array.from(new Uint8Array(buffer.slice(0, 4)))
                .map(b => b.toString(16).padStart(2, '0'))
                .join('');
              endpointTests[endpointTests.length - 1].firstBytes = firstBytes;
              endpointTests[endpointTests.length - 1].isImage = firstBytes.startsWith('ffd8') || // JPEG
                                                                firstBytes.startsWith('89504e47') || // PNG
                                                                firstBytes.startsWith('47494638'); // GIF
            }
          } catch (err) {
            endpointTests.push({
              name: endpoint.name,
              url: endpoint.url,
              error: err.message
            });
          }
        }
        
        return {
          cat: { 
            id: cat.id, 
            name: cat.name, 
            imgID: cat.imgID,
            imagemUrl: cat.imagemUrl,
            imageUrl: cat.imageUrl
          },
          endpoints: endpointTests
        };
      })
    );
    
    return {
      statusCode: 200,
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": "true"
      },
      body: JSON.stringify({
        totalCats: cats.length,
        catsTested: catsToTest.length,
        results,
        note: "Check which endpoints work for each cat. 'isImage' true means the response appears to be an image file.",
        debugInfo: {
          API_URL,
          tokenPresent: !!token
        }
      })
    };
    
  } catch (err) {
    console.error("Error in test-cat-images:", err);
    return {
      statusCode: 500,
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": "true"
      },
      body: JSON.stringify({ 
        error: err.message,
        stack: err.stack 
      })
    };
  }
};