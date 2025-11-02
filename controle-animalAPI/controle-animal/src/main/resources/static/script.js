function buscarBoris() {
    fetch('http://localhost:8080/api/boris')
        .then(response => response.json())
        .then(data => {
        document.getElementById('resultado').textContent = JSON.stringify(data, null, 2);
        })
        .catch(err => alert('Erro: ' + err));
    }

const toggleTheme = document.getElementById("toggleTheme");
const rootHtml = document.documentElement;
function changeTheme() {
    const currentTheme = rootHtml.getAttribute("data-theme");

    currentTheme === "dark" ? rootHtml.setAttribute("data-theme", "light") : rootHtml.setAttribute("data-theme", "dark")

    toggleTheme.classList.toggle("bi-sun")
    toggleTheme.classList.toggle("bi-moon")
}

toggleTheme.addEventListener("click", changeTheme);

const linkListar = document.getElementById("listarLink");
const resultado = document.getElementById("resultado");

// Define a URL da sua API
const API_URL = "http://localhost:8080/api/all";

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