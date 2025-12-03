// netlify/functions/test-cors-fix.js
export const handler = async (event) => {
  const API_URL = "https://controle-animal.onrender.com";
  
  console.log("Testing CORS fix for:", API_URL);
  
  try {
    // Test 1: Check OPTIONS preflight request
    console.log("Test 1: OPTIONS preflight request");
    const optionsResponse = await fetch(`${API_URL}/api`, {
      method: 'OPTIONS',
      headers: {
        'Origin': 'http://localhost:8888',
        'Access-Control-Request-Method': 'GET',
        'Access-Control-Request-Headers': 'x-api-key, content-type'
      }
    });
    
    // Test 2: Check CORS headers in regular request
    console.log("Test 2: Check CORS headers");
    const getResponse = await fetch(`${API_URL}/api`, {
      method: 'GET',
      headers: {
        'Origin': 'http://localhost:8888'
      }
    });
    
    // Test 3: Check with API key (still expect 401, but with CORS headers)
    console.log("Test 3: Check with API key");
    const authResponse = await fetch(`${API_URL}/api`, {
      method: 'GET',
      headers: {
        'Origin': 'http://localhost:8888',
        'x-api-key': 'senhaanimal'
      }
    });
    
    return {
      statusCode: 200,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        backend: API_URL,
        tests: {
          optionsPreflight: {
            status: optionsResponse.status,
            headers: extractCorsHeaders(optionsResponse.headers),
            note: "Should return 200/204 with CORS headers"
          },
          getRequest: {
            status: getResponse.status,
            headers: extractCorsHeaders(getResponse.headers),
            note: "Should have CORS headers even on 401"
          },
          withApiKey: {
            status: authResponse.status,
            headers: extractCorsHeaders(authResponse.headers),
            note: authResponse.status === 401 ? "API key still wrong" : "✅ Working!"
          }
        },
        analysis: analyzeCorsResults(optionsResponse, getResponse, authResponse),
        nextSteps: getNextSteps(authResponse.status)
      })
    };
    
  } catch (err) {
    return {
      statusCode: 500,
      body: JSON.stringify({
        error: err.message,
        backend: API_URL,
        note: "Network error - check if backend is accessible"
      })
    };
  }
};

function extractCorsHeaders(headers) {
  const corsHeaders = {};
  const corsHeaderNames = [
    'access-control-allow-origin',
    'access-control-allow-methods',
    'access-control-allow-headers',
    'access-control-allow-credentials',
    'vary'
  ];
  
  corsHeaderNames.forEach(header => {
    const value = headers.get(header);
    if (value) {
      corsHeaders[header] = value;
    }
  });
  
  return corsHeaders;
}

function analyzeCorsResults(optionsRes, getRes, authRes) {
  const analysis = [];
  
  // Check OPTIONS response
  if (optionsRes.status === 200 || optionsRes.status === 204) {
    analysis.push("✅ OPTIONS preflight works");
  } else {
    analysis.push("❌ OPTIONS preflight failing - CORS not configured");
  }
  
  // Check CORS headers
  const hasOriginHeader = getRes.headers.get('access-control-allow-origin');
  if (hasOriginHeader) {
    analysis.push(`✅ CORS headers present: ${hasOriginHeader}`);
  } else {
    analysis.push("❌ Missing CORS headers");
  }
  
  // Check API key
  if (authRes.status === 401) {
    analysis.push("⚠️ Still getting 401 - API key issue remains");
  } else if (authRes.ok) {
    analysis.push("🎉 API key is now working!");
  }
  
  return analysis;
}

function getNextSteps(authStatus) {
  if (authStatus === 401) {
    return [
      "1. CORS is fixed but API key is still wrong",
      "2. Ask backend dev for correct Render.com API key",
      "3. Test with correct API key"
    ];
  } else if (authStatus === 200) {
    return [
      "1. ✅ Everything is working!",
      "2. Test your login function now",
      "3. Update all functions to use the new backend"
    ];
  }
  
  return ["Check CORS configuration with backend developer"];
}