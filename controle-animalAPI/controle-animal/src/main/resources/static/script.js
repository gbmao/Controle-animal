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