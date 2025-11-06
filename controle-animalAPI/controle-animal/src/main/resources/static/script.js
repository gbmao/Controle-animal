//Relacionado aos temas (light and dark)
const toggleTheme = document.getElementById("toggleTheme");
const rootHtml = document.documentElement;
function changeTheme() {
    const currentTheme = rootHtml.getAttribute("data-theme");

    currentTheme === "dark"
        ? rootHtml.setAttribute("data-theme", "light")
        : rootHtml.setAttribute("data-theme", "dark");

    toggleTheme.classList.toggle("bi-sun");
    toggleTheme.classList.toggle("bi-moon");
}

toggleTheme.addEventListener("click", changeTheme);

const linkListar = document.getElementById("listarLink");
const resultado = document.getElementById("resultado");

//Relacionado ao Listar
// Define a URL da sua API (nova URL com /api/all)
const API_URL = "https://controle-animal-production.up.railway.app/api/all";

// Função que busca a API
async function buscarApi() {
    try {
        const resposta = await fetch(API_URL);
        if (!resposta.ok) {
            throw new Error("Erro ao buscar a API");
        }

        const dados = await resposta.json();

        const arrowSvg = `
<svg width="13" height="8" viewBox="0 0 13 8" fill="none" xmlns="http://www.w3.org/2000/svg">
<path d="M1.01599 1.01599L6.09599 6.09599L11.176 1.01599" stroke="#7A9590" stroke-width="2.032" stroke-linecap="round" stroke-linejoin="round"/>
</svg>
`;

        resultado.innerHTML = `
    <h2>Gatos registrados:</h2>
    <ul class="lista--registrados">
    ${dados.map(item => `<li class="item--lista--registrados">${item.name} ${arrowSvg}</li>`).join("")}
    </ul>
`;
    } catch (erro) {
        resultado.innerHTML = `<p style="color:red;">${erro.message}</p>`;
    }
}

// Quando clicar no link, cancela o comportamento padrão e chama a função
linkListar.addEventListener("click", (evento) => {
    evento.preventDefault(); // impede o salto da âncora
    buscarApi();
});

async function adicionarGato() {
    // pega os valores dos inputs
    const name = document.getElementById("name").value.trim(); // trim() remove espaços vazios
    const age = parseInt(document.getElementById("age").value);

    if (!name) {
        alert("Por favor, digite o nome do gato antes de cadastrar!");
        return;
    }

    // monta o objeto com os dados do usuário
    const novoGato = { name, age };

    // envia para o backend (agora também com /api/all)
    const resposta = await fetch("https://controle-animal-production.up.railway.app/api", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(novoGato)
    });

    if (!resposta.ok) {
        throw new Error("Erro ao cadastrar o gato");
    }

    const data = await resposta.json();
    console.log("Gato cadastrado:", data);

    alert(`Gato ${data.name} cadastrado com sucesso!`);
    adicionarSection.style.display = "none";
}

const linkAdicionar = document.getElementById("adicionarLink");
const adicionarSection = document.getElementById("adicionarSection");

linkAdicionar.addEventListener("click", function(event) {
    event.preventDefault();
    // Esconde outras seções se necessário
    resultado.innerHTML = ""; // esconde a lista, por exemplo
    adicionarSection.style.display = "block";
});

//Funções para esconder as outras seções que não estou usando
function esconderTodasSecoes() {
    resultado.innerHTML = "";
    adicionarSection.style.display = "none";
    // Adicione outras seções aqui se necessário
}

linkListar.addEventListener("click", (evento) => {
    evento.preventDefault();
    esconderTodasSecoes();
    buscarApi();
});

linkAdicionar.addEventListener("click", function(event) {
    event.preventDefault();
    esconderTodasSecoes();
    adicionarSection.style.display = "block";
});
