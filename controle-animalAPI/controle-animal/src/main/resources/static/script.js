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

    
    resultado.innerHTML = `
        <h2>Resultados:</h2>
        <ul>
        ${dados.map(item => `<li>${item.name}</li>`).join("")}
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