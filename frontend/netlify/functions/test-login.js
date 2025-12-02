// test-login.js Netlify function
export const handler = async (event) => {
  const API_URL = process.env.VITE_API_URL;
  
  // Test with known credentials (remove after testing)
  const testData = {
    login: "Emma",
    password: "emma123" // Use actual password
  };
  
  try {
    const resp = await fetch(`${API_URL}/api/auth/signin`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(testData),
    });
    
    const data = await resp.json();
    
    return {
      statusCode: 200,
      body: JSON.stringify({
        status: resp.status,
        response: data,
        fields: Object.keys(data),
        hasAccessToken: !!data.accessToken,
        hasToken: !!data.token,
        hasJwt: !!data.jwt
      })
    };
  } catch (err) {
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
};