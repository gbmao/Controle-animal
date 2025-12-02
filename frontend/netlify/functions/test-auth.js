// Create a test-auth.js Netlify function
export const handler = async (event) => {
  const cookies = event.headers.cookie || '';
  console.log("Cookies:", cookies);
  
  // Try to call a simple authenticated endpoint on your backend
  const API_URL = process.env.VITE_API_URL;
  const token = cookies.split(';').find(c => c.trim().startsWith('auth_token='))?.split('=')[1];
  
  if (!token) {
    return { statusCode: 401, body: 'No token' };
  }
  
  const resp = await fetch(`${API_URL}/api/auth/test`, {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  
  return {
    statusCode: resp.status,
    body: await resp.text()
  };
};