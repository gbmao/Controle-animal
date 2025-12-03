// netlify/functions/test-render-api.js
export const handler = async (event) => {
  const API_URL = process.env.VITE_API_URL;
  const API_KEY = process.env.VITE_API_KEY;
  
  console.log("Testing API with authentication...");
  
  try {
    // Test 1: Root endpoint (should be 401)
    console.log("Test 1: Root endpoint (should require auth)");
    const rootResponse = await fetch(API_URL);
    
    // Test 2: API endpoint WITH API key
    console.log("Test 2: /api endpoint with API key");
    const apiResponse = await fetch(`${API_URL}/api`, {
      headers: { 
        'x-api-key': API_KEY,
        'Content-Type': 'application/json'
      }
    });
    
    // Test 3: Auth endpoint
    console.log("Test 3: /api/auth/signin endpoint");
    const authResponse = await fetch(`${API_URL}/api/auth/signin`, {
      method: 'GET', // Just checking if endpoint exists
      headers: { 
        'x-api-key': API_KEY,
        'Content-Type': 'application/json'
      }
    });
    
    let apiBody = null;
    let authBody = null;
    
    if (apiResponse.ok) {
      try {
        apiBody = await apiResponse.text();
      } catch (e) {
        apiBody = "Could not parse response";
      }
    }
    
    if (authResponse.ok) {
      try {
        authBody = await authResponse.text();
      } catch (e) {
        authBody = "Could not parse response";
      }
    }
    
    return {
      statusCode: 200,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        backend: API_URL,
        apiKeyUsed: API_KEY ? "***SET***" : "NOT SET",
        tests: {
          rootEndpoint: {
            url: API_URL,
            status: rootResponse.status,
            ok: rootResponse.ok,
            expected: "401 (requires auth)"
          },
          apiEndpoint: {
            url: `${API_URL}/api`,
            status: apiResponse.status,
            ok: apiResponse.ok,
            bodyPreview: apiBody ? apiBody.substring(0, 200) + "..." : null,
            headersUsed: ['x-api-key', 'Content-Type']
          },
          authEndpoint: {
            url: `${API_URL}/api/auth/signin`,
            status: authResponse.status,
            ok: authResponse.ok,
            note: authResponse.status === 405 ? "GET not allowed (needs POST)" : "Check endpoint",
            bodyPreview: authBody ? authBody.substring(0, 200) + "..." : null
          }
        },
        analysis: {
          ifRootIs401: "✅ Good! Root endpoint is protected",
          ifApiIs200: "✅ API endpoint works with authentication",
          ifApiIs401: "❌ API key might be wrong or missing 'x-api-key' header",
          ifAuthIs404: "❓ Auth endpoint might have different path",
          ifAuthIs405: "✅ Auth endpoint exists (needs POST, not GET)"
        }
      })
    };
    
  } catch (err) {
    return {
      statusCode: 500,
      body: JSON.stringify({
        error: err.message,
        backend: API_URL
      })
    };
  }
};