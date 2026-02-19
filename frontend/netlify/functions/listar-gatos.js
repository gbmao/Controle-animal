// In your listar-gatos.js function
export const handler = async (event) => {
  try {
    console.log("=== LISTAR GATOS DEBUG ===");
    
    const API_URL = process.env.VITE_API_URL;
    const API_KEY = process.env.VITE_API_KEY;

    // Get token from cookies
    const cookies = event.headers.cookie || "";
    const token = cookies
      .split(";")
      .find(c => c.trim().startsWith("auth_token="))
      ?.split("=")[1];

    console.log("Token found:", token ? "YES" : "NO");

    const response = await fetch(`${API_URL}/api/all`, {
      method: "GET",
      headers: {
        "x-api-key": API_KEY,
        "Authorization": token ? `Bearer ${token}` : "",
        "Content-Type": "application/json"
      }
    });

    console.log("Backend response status:", response.status);
    
    if (!response.ok) {
      const errorText = await response.text();
      console.error("Backend error:", errorText);
      return {
        statusCode: response.status,
        body: errorText
      };
    }

    const data = await response.json();
    console.log("Number of cats received:", data.length || 0);
    
    // Log image-related info for each cat
    if (Array.isArray(data)) {
      data.forEach((cat, index) => {
        console.log(`Cat ${index + 1}:`, {
          id: cat.id,
          name: cat.name,
          imgID: cat.imgID,
          imageId: cat.imageId,
          imagemUrl: cat.imagemUrl,
          imageUrl: cat.imageUrl,
          pictureId: cat.pictureId
        });
      });
    }

    return {
      statusCode: 200,
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": "true"
      },
      body: JSON.stringify(data)
    };

  } catch (error) {
    console.error("Erro listar-gatos:", error);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: error.message })
    };
  }
};