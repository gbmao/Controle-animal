export const handler = async (event) => {
  // Handle preflight OPTIONS request
  if (event.httpMethod === 'OPTIONS') {
    return {
      statusCode: 200,
      headers: {
        'Access-Control-Allow-Origin': event.headers.origin || 'http://localhost:8888',
        'Access-Control-Allow-Credentials': 'true',
        'Access-Control-Allow-Headers': 'Content-Type, Authorization',
        'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
      },
      body: ''
    };
  }

  try {
    const API_URL = process.env.VITE_API_URL;

    const body = JSON.parse(event.body);
    const { login, password } = body;

    console.log("Login attempt for:", login);

    // 🔥 LOGIN NO BACKEND JAVA
    const resp = await fetch(`${API_URL}/api/auth/signin`, {
      method: "POST",
      headers: { 
        "Content-Type": "application/json",
        "Accept": "application/json"
      },
      body: JSON.stringify({ login, password })
    });

    const data = await resp.json();

    console.log("====== RESPOSTA DO BACKEND (JAVA) ======");
    console.log(JSON.stringify(data, null, 2));

    if (!resp.ok) {
      return {
        statusCode: resp.status,
        headers: {
          'Access-Control-Allow-Origin': event.headers.origin || 'http://localhost:8888',
          'Access-Control-Allow-Credentials': 'true',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      };
    }

    // 🔥 Extrair token corretamente
    const token = data.accessToken || data.token || data.jwt || data.tokenJwt;

    console.log("====== TOKEN EXTRAÍDO ======");
    console.log(token ? "Token found" : "No token found");

    if (!token) {
      return {
        statusCode: 500,
        headers: {
          'Access-Control-Allow-Origin': event.headers.origin || 'http://localhost:8888',
          'Access-Control-Allow-Credentials': 'true',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          error: "Backend não retornou token!",
          camposDisponiveis: Object.keys(data)
        })
      };
    }

    // Criar headers de resposta
    const headers = {
      'Access-Control-Allow-Origin': event.headers.origin || 'http://localhost:8888',
      'Access-Control-Allow-Credentials': 'true',
      'Content-Type': 'application/json'
    };

    // SÓ adiciona Set-Cookie se não estiver em desenvolvimento local (opcional)
    // Ou sempre adiciona se for seguro
    if (token) {
      // Para desenvolvimento local (HTTP), use SameSite=Lax
      // Para produção (HTTPS), use SameSite=None; Secure
      const isLocalhost = event.headers.origin && event.headers.origin.includes('localhost');
      const cookieSettings = isLocalhost 
        ? `auth_token=${token}; Path=/; HttpOnly; SameSite=Lax; Max-Age=86400`
        : `auth_token=${token}; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=86400`;
      
      headers['Set-Cookie'] = cookieSettings;
    }

    return {
      statusCode: 200,
      headers: headers,
      body: JSON.stringify({
        success: true,
        login: data.login,
        email: data.email,
        id: data.id,
        message: "Login realizado com sucesso"
      })
    };

  } catch (err) {
    console.error("Error in login function:", err);
    return {
      statusCode: 500,
      headers: {
        'Access-Control-Allow-Origin': event.headers.origin || 'http://localhost:8888',
        'Access-Control-Allow-Credentials': 'true',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ 
        error: "Internal server error",
        message: err.message 
      })
    };
  }
};