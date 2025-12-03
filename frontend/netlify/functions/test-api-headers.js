// netlify/functions/test-api-headers.js
export const handler = async (event) => {
  const API_URL = "https://controle-animal.onrender.com";
  const API_KEY = "senhaanimal";
  
  console.log("Testing different authentication headers...");
  
  const headerTests = [
    { name: 'x-api-key', value: API_KEY },
    { name: 'X-API-Key', value: API_KEY },
    { name: 'X-Api-Key', value: API_KEY },
    { name: 'api-key', value: API_KEY },
    { name: 'Api-Key', value: API_KEY },
    { name: 'Authorization', value: `Bearer ${API_KEY}` },
    { name: 'Authorization', value: `Basic ${Buffer.from(API_KEY).toString('base64')}` },
    { name: 'Authorization', value: API_KEY },
    { name: 'X-API-TOKEN', value: API_KEY },
    { name: 'x-api-token', value: API_KEY }
  ];
  
  const results = [];
  
  for (const headerTest of headerTests) {
    try {
      console.log(`Testing header: ${headerTest.name}`);
      
      const response = await fetch(`${API_URL}/api`, {
        headers: {
          [headerTest.name]: headerTest.value,
          'Content-Type': 'application/json'
        }
      });
      
      results.push({
        header: headerTest.name,
        value: headerTest.value.substring(0, 5) + '...',
        status: response.status,
        ok: response.ok,
        statusText: response.statusText
      });
      
      // If successful, get response body
      if (response.ok) {
        try {
          const body = await response.text();
          results[results.length - 1].bodyPreview = body.substring(0, 100);
        } catch (e) {}
      }
      
    } catch (err) {
      results.push({
        header: headerTest.name,
        error: err.message
      });
    }
  }
  
  // Find working headers
  const workingHeaders = results.filter(r => r.ok);
  
  return {
    statusCode: 200,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      backend: API_URL,
      apiKey: API_KEY ? "***SET***" : "NOT SET",
      tests: results,
      workingHeaders,
      recommendation: workingHeaders.length > 0
        ? `Use header: ${workingHeaders[0].header}`
        : "No headers worked. Check API key or ask backend developer.",
      nextSteps: [
        "1. Verify API key with backend developer",
        "2. Check backend logs on Render.com",
        "3. Check Spring Boot security configuration"
      ]
    })
  };
};