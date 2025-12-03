// netlify/functions/check-site.js
export const handler = async (event) => {
  return {
    statusCode: 200,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      site: {
        name: process.env.SITE_NAME,
        url: process.env.URL,
        deployUrl: process.env.DEPLOY_PRIME_URL,
        deployId: process.env.DEPLOY_ID
      },
      backend: {
        apiUrl: process.env.VITE_API_URL,
        hasApiKey: !!process.env.VITE_API_KEY
      },
      allRelevantEnvVars: Object.keys(process.env)
        .filter(key => key.includes('API') || key.includes('URL') || key.includes('SITE'))
        .reduce((obj, key) => {
          obj[key] = key.includes('KEY') ? '***SET***' : process.env[key];
          return obj;
        }, {})
    })
  };
};