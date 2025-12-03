// netlify/functions/quick-render-test.js
export const handler = async (event) => {
  const API_URL = "https://controle-animal.onrender.com";
  
  console.log("Quick test of:", API_URL);
  
  try {
    // Use AbortController for timeout
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), 10000); // 10 second timeout
    
    const response = await fetch(API_URL, {
      method: 'GET',
      signal: controller.signal
    }).finally(() => clearTimeout(timeout));
    
    return {
      statusCode: 200,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        success: true,
        backend: API_URL,
        status: response.status,
        statusText: response.statusText,
        note: response.ok 
          ? "✅ Backend is responding!"
          : `⚠️ Backend responded with ${response.status}`
      })
    };
    
  } catch (err) {
    console.error("Quick test error:", err.message);
    
    return {
      statusCode: 200, // Still return 200 to show the error
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        success: false,
        backend: API_URL,
        error: err.message,
        likelyCauses: [
          "Render.com service is sleeping (free tier)",
          "Service not deployed",
          "Wrong URL",
          "Network/firewall issue"
        ],
        actions: [
          "1. Open https://controle-animal.onrender.com in browser",
          "2. Check Render.com dashboard",
          "3. Wait 30-60 seconds for service to wake up",
          "4. Verify backend is actually deployed"
        ]
      })
    };
  }
};