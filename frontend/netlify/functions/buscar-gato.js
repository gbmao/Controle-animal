exports.handler = async function(event) {
    const API_URL = process.env.VITE_API_URL;
    const nome = event.queryStringParameters?.nome;

    if (!nome) {
        return {
            statusCode: 400,
            body: JSON.stringify({ error: "Missing nome parameter" })
        };
    }

    try {
        const resp = await fetch(`${API_URL}/api/busca/${encodeURIComponent(nome)}`);

        if (!resp.ok) {
            return {
                statusCode: resp.status,
                body: JSON.stringify({ error: "Gato não encontrado" })
            };
        }

        const lista = await resp.json();

        const listaComUrl = lista.map(g => ({
            ...g,
            // ❗ NÃO concatena o domínio — já vem completo do backend
            imagemUrl: g.imgUrl || null
        }));

        return {
            statusCode: 200,
            body: JSON.stringify(listaComUrl)
        };

    } catch (err) {
        return {
            statusCode: 500,
            body: JSON.stringify({ error: err.message })
        };
    }
};
