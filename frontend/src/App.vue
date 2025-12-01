<template>
  <header class="header">
    <NavMenu />

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
  <NavMenuMobile />

</template>

<script setup>
import { ref, onMounted } from 'vue'
import NavMenu from './components/NavMenu.vue'
import NavMenuMobile from './components/NavMenuMobile.vue'

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
