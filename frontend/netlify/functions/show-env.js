// netlify/functions/show-env.js
export const handler = async (event) => {
  // Get ALL environment variables (filter out sensitive ones)
  const allEnvVars = {};
  
  for (const key in process.env) {
    if (key.includes('API') || key.includes('URL') || key.includes('KEY')) {
      allEnvVars[key] = process.env[key];
    }
  }
  
  return {
    statusCode: 200,
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    },
    body: JSON.stringify({
      timestamp: new Date().toISOString(),
      environment: allEnvVars,
      note: 'Check if VITE_API_URL is set to the new Render.com URL'
    })
  };
};