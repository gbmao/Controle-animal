<template>
  <div class="app-grid">
  <header class="header">
    <NavMenu v-if="auth.isAuthenticated" />

    <div class="header__logo">
      <img class="logo--projeto" src="./assets/controle-animal.png" alt="logo do projeto">
      <h1 class="header--text">Controle Animal</h1>
    </div>

    <div class="header__toggle">
      <i id="toggleTheme"
        :class="['bi', theme === 'dark' ? 'bi-moon' : 'bi-sun']"
        @click="toggleTheme"
      ></i>
    </div>
  </header>

  <main>
    <RouterView />
  </main>
<hr/>
  <footer>
    <p>2025 | Desenvolvido por&nbsp;<a target="_blank" rel="noopener noreferrer" href="https://github.com/gbmao">Gabriel</a>&nbsp;e&nbsp;<a target="_blank" rel="noopener noreferrer" href="https://github.com/deboradevsouza">Débora</a></p>
  </footer>
  <NavMenuMobile v-if="auth.isAuthenticated" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import NavMenu from './components/NavMenu.vue'
import NavMenuMobile from './components/NavMenuMobile.vue'

const auth = useAuthStore()

// estado reativo para o tema
const theme = ref('light')

onMounted(() => {
  const root = document.documentElement
  theme.value = root.getAttribute('data-theme') || 'light'
})

function toggleTheme() {
  const root = document.documentElement
  theme.value = theme.value === 'dark' ? 'light' : 'dark'
  root.setAttribute('data-theme', theme.value)
}
</script>

<style scoped>

.app-grid {
  display: grid;
  grid-template-rows: auto 1fr auto auto; /* header, main, navmobile, footer */
  min-height: 100vh;
}

footer {
  display: flex;
  justify-content: center;
  padding: 1rem;
}

p {
  display: flex;
  align-items: center;
  padding: 1rem;
  color: #5e7c76;
}

a {
  display: flex;
  align-items: center;
  color: #5e7c76;
}

</style>
