// test-token.js Netlify function
export const handler = async (event) => {
  const API_URL = process.env.VITE_API_URL;
  const cookies = event.headers.cookie || '';
  
  const token = cookies.split(';').find(c => c.trim().startsWith('auth_token='))?.split('=')[1];
  
  if (!token) {
    return {
      statusCode: 401,
      body: JSON.stringify({ error: 'No token in cookies' })
    };
  }
  
  // Test if token is valid by calling a simple authenticated endpoint
  try {
    const resp = await fetch(`${API_URL}/api/auth/verify`, { // Check if you have this endpoint
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    
    return {
      statusCode: resp.status,
      body: await resp.text()
    };
  } catch (err) {
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
};