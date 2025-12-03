// netlify/functions/debug-exact-request.js
export const handler = async (event) => {
  const API_URL = "https://controle-animal.onrender.com";
  
  // Get the ACTUAL API key from environment
  const envApiKey = process.env.VITE_API_KEY;
  
  console.log("=== DEBUG EXACT REQUEST ===");
  console.log("Environment VITE_API_KEY:", envApiKey ? `"${envApiKey}"` : "NOT SET");
  console.log("Length:", envApiKey?.length);
  console.log("Characters:", envApiKey?.split('').map(c => c.charCodeAt(0)));
  
  // Test with EXACT same request as Postman
  const postmanStyleRequest = {
    url: `${API_URL}/api`,
    method: 'GET',
    headers: {
      'x-api-key': envApiKey || '',
      'Content-Type': 'application/json',
      'User-Agent': 'Netlify-Function-Debug'
    }
  };
  
  console.log("Sending request:", JSON.stringify(postmanStyleRequest, null, 2));
  
  try {
    const response = await fetch(postmanStyleRequest.url, {
      method: postmanStyleRequest.method,
      headers: postmanStyleRequest.headers
    });
    
    console.log("Response status:", response.status);
    console.log("Response headers:", Object.fromEntries(response.headers.entries()));
    
    const responseText = await response.text();
    console.log("Response body (first 500 chars):", responseText.substring(0, 500));
    
    // Also try with curl-style raw output
    const rawRequest = `curl -X GET \\
  -H "x-api-key: ${envApiKey || ''}" \\
  -H "Content-Type: application/json" \\
  "${API_URL}/api"`;
    
    return {
      statusCode: 200,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        debug: {
          environmentKey: envApiKey ? `"${envApiKey}" (length: ${envApiKey.length})` : "NOT SET",
          keyCharacters: envApiKey ? envApiKey.split('').map((c, i) => ({
            position: i,
            char: c,
            code: c.charCodeAt(0),
            isWhitespace: c.trim() === ''
          })) : [],
          requestSent: postmanStyleRequest,
          response: {
            status: response.status,
            statusText: response.statusText,
            headers: Object.fromEntries(response.headers.entries()),
            body: responseText
          }
        },
        curlCommand: rawRequest,
        nextSteps: [
          "1. Copy the curl command and run it in terminal",
          "2. Compare with what works in Postman",
          "3. Check for hidden characters in the key",
          "4. Verify exact key match with backend dev"
        ]
      })
    };
    
  } catch (err) {
    return {
      statusCode: 500,
      body: JSON.stringify({
        error: err.message,
        stack: err.stack
      })
    };
  }
};