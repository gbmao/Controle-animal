// test-image-endpoint.js
export const handler = async (event) => {
  const API_URL = process.env.VITE_API_URL;
  const API_KEY = process.env.VITE_API_KEY;
  
  // Get token from cookies
  const cookies = event.headers.cookie || "";
  const token = cookies
    .split(";")
    .find(c => c.trim().startsWith("auth_token="))
    ?.split("=")[1];

  if (!token) {
    return {
      statusCode: 401,
      body: JSON.stringify({ error: "Não autenticado" })
    };
  }

  try {
    // First get cats to see their imgIDs
    const catsResponse = await fetch(`${API_URL}/api`, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'x-api-key': API_KEY,
        'Content-Type': 'application/json'
      }
    });
    
    const cats = await catsResponse.json();
    
    // Get a cat with an image (imgID not -1)
    const catWithImage = cats.find(cat => cat.imgID && cat.imgID !== -1);
    
    if (!catWithImage) {
      return {
        statusCode: 200,
        body: JSON.stringify({ 
          message: "No cats with images found",
          cats: cats.map(c => ({ id: c.id, name: c.name, imgID: c.imgID }))
        })
      };
    }
    
    console.log("Testing image for cat:", catWithImage.name, "imgID:", catWithImage.imgID);
    
    // Test different image endpoint patterns
    const testPatterns = [
      `/api/images/${catWithImage.imgID}`,
      `/images/${catWithImage.imgID}`,
      `/api/files/${catWithImage.imgID}`,
      `/api/image/${catWithImage.imgID}`,
      `/api/pictures/${catWithImage.imgID}`,
      `/api/upload/${catWithImage.imgID}`,
      `/api/cats/${catWithImage.id}/image`,
      `/api/animal/${catWithImage.id}/image`
    ];
    
    const results = [];
    
    for (const pattern of testPatterns) {
      const url = `${API_URL}${pattern}`;
      console.log("Testing:", url);
      
      try {
        const response = await fetch(url, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'x-api-key': API_KEY
          }
        });
        
        results.push({
          pattern,
          url,
          status: response.status,
          ok: response.ok,
          contentType: response.headers.get('content-type'),
          contentLength: response.headers.get('content-length')
        });
        
        // If successful, check if it's actually an image
        if (response.ok) {
          const clone = response.clone();
          const buffer = await clone.arrayBuffer();
          const firstBytes = Array.from(new Uint8Array(buffer.slice(0, 4)))
            .map(b => b.toString(16).padStart(2, '0'))
            .join('');
          
          results[results.length - 1].firstBytes = firstBytes;
          results[results.length - 1].isImage = 
            firstBytes.startsWith('ffd8') || // JPEG
            firstBytes.startsWith('89504e47') || // PNG
            firstBytes.startsWith('47494638'); // GIF
        }
      } catch (err) {
        results.push({
          pattern,
          url,
          error: err.message
        });
      }
    }
    
    // Find working endpoints
    const workingEndpoints = results.filter(r => r.ok && r.isImage);
    
    return {
      statusCode: 200,
      body: JSON.stringify({
        testCat: {
          id: catWithImage.id,
          name: catWithImage.name,
          imgID: catWithImage.imgID
        },
        results,
        workingEndpoints: workingEndpoints.map(w => w.pattern),
        recommendation: workingEndpoints.length > 0 
          ? `Use pattern: ${workingEndpoints[0].pattern}`
          : "No working image endpoints found. Check with backend developer."
      })
    };
    
  } catch (err) {
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
};