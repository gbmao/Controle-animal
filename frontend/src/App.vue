<template>
  <header class="header">
    <NavMenu />

    <div class="header__logo">
      <img class="logo--projeto" src="./assets/controle-animal.png" alt="logo do projeto">
      <h1 class="header--text">Controle Animal</h1>

    </div>

    <div class="header__toggle">
      <i
        :class="['bi', theme === 'dark' ? 'bi-moon' : 'bi-sun']"
        @click="toggleTheme"
      ></i>
    </div>
  </header>

  <main>
    <RouterView />
  </main>
</template>

<script setup>
import NavMenu from './components/NavMenu.vue'

function toggleTheme() {
  const root = document.documentElement
  const current = root.getAttribute('data-theme')
  root.setAttribute('data-theme', current === 'dark' ? 'light' : 'dark')
}
</script>
