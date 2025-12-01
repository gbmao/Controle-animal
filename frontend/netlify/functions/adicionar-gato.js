export const handler = async (event) => {
  try {
    const API_URL = process.env.VITE_API_URL;
    const API_KEY = process.env.VITE_API_KEY;

    // Cookies do navegador
    const cookies = event.headers.cookie || "";

    console.log("===== COOKIES RECEBIDOS DO NAVEGADOR =====");
console.log(event.headers.cookie);

console.log("===== COOKIES ENVIADOS AO BACKEND =====");
console.log(cookies);


    // CONTENT TYPE verdadeiro do multipart enviado pelo navegador
    const contentType = event.headers["content-type"];

    // 🔥 MULTIPART + NETLIFY SEMPRE VEM BASE64 → precisa decodificar
    const bodyBuffer = event.isBase64Encoded
      ? Buffer.from(event.body, "base64")
      : Buffer.from(event.body);

    console.log("BODY BUFFER SIZE:", bodyBuffer.length);

    // 🔥 IMPORTANTE: NÃO DEFINIR Content-Type MANUALMENTE!
    // O boundary precisa ser preservado pelo fetch nativo.
    const resp = await fetch(`${API_URL}/api`, {
      method: "POST",
      headers: {
        "x-api-key": API_KEY,
        "Cookie": cookies,
        "Content-Type": contentType   // agora sim, com boundary original
      },
      body: bodyBuffer
    });

    const text = await resp.text();

    return {
      statusCode: resp.status,
      headers: {
        "Content-Type": resp.headers.get("content-type") || "application/json"
      },
      body: text
    };

  } catch (err) {
    console.error("Erro na Netlify Function:", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: err.message })
    };
  }
};
