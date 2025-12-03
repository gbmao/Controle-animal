// netlify/functions/test-no-auth.js
export const handler = async (event) => {
  const API_URL = "https://controle-animal.onrender.com";
  
  const endpoints = [
    { path: '/api', method: 'GET' },
    { path: '/api/auth/signin', method: 'GET' },
    { path: '/api/auth/signin', method: 'POST', body: { login: "test", password: "test" } },
    { path: '/health', method: 'GET' },
    { path: '/actuator/health', method: 'GET' }
  ];
  
  const results = [];
  
  for (const endpoint of endpoints) {
    try {
      const options = {
        method: endpoint.method,
        headers: { 'Content-Type': 'application/json' }
      };
      
      if (endpoint.body) {
        options.body = JSON.stringify(endpoint.body);
      }
      
      const response = await fetch(`${API_URL}${endpoint.path}`, options);
      
      results.push({
        endpoint: endpoint.path,
        method: endpoint.method,
        status: response.status,
        ok: response.ok,
        note: response.status === 401 ? "Requires auth (normal)" : 
              response.ok ? "✅ No auth needed!" : "Other error"
      });
      
    } catch (err) {
      results.push({
        endpoint: endpoint.path,
        method: endpoint.method,
        error: err.message
      });
    }
  }
  
  return {
    statusCode: 200,
    body: JSON.stringify({
      backend: API_URL,
      tests: results,
      analysis: results.some(r => r.ok) 
        ? "Some endpoints don't require authentication"
        : "All endpoints require authentication"
    })
  };
};