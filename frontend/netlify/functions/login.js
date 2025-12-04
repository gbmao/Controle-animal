export const handler = async (event) => {
  try {
    const API_URL = process.env.VITE_API_URL;

    const { login, password } = JSON.parse(event.body);

    // chama backend real
    const resp = await fetch(`${API_URL}/api/auth/signin`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ login, password }),
    });

    const backendData = await resp.json();
    
    console.log("=== LOGIN DEBUG ===");
    console.log("Backend response status:", resp.status);
    console.log("Backend response data:", backendData);
    console.log("Available fields:", Object.keys(backendData));

    if (!resp.ok) {
      return {
        statusCode: resp.status,
        headers: {
          "Access-Control-Allow-Origin": event.headers.origin || "*",
          "Access-Control-Allow-Credentials": "true",
        },
        body: JSON.stringify(backendData),
      };
    }

    // pega token do backend - CORRECT FIELD NAME IS accessToken
    const token = backendData.accessToken; // Changed from .token to .accessToken

    if (!token) {
      console.error("Token not found in backend response!");
      console.error("Full backend response:", backendData);
      return {
        statusCode: 500,
        body: JSON.stringify({
          error: "Backend não retornou token",
          respostaRecebida: backendData,
          nota: "O campo esperado é 'accessToken' não 'token'"
        }),
      };
    }

    console.log("Token successfully extracted:", token.substring(0, 50) + "...");

    // SETA O COOKIE SEGURO AQUI!! ESSENCIAL
    return {
      statusCode: 200,
      headers: {
        "Access-Control-Allow-Origin": event.headers.origin || "*",
        "Access-Control-Allow-Credentials": "true",
        "Set-Cookie": `auth_token=${token}; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=86400`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        // Return everything from backend
        ...backendData,
        // Make sure user data is available
        user: {
          id: backendData.id,
          login: backendData.login,
          email: backendData.email
        },
        // Also include token in response for debugging
        token: token
      }),
    };

  } catch (err) {
    console.error("Login error:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message }),
    };
  }
};