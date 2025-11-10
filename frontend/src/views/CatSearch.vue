<template>
  <section>
    <h2>Buscar Gato</h2>
    <input v-model="busca" placeholder="Digite o nome" />
    <button @click="buscarGato">Buscar</button>

    <ul v-if="resultado.length">
      <li v-for="gato in resultado" :key="gato.id">
        {{ gato.name }} ({{ gato.age }} anos)
      </li>
    </ul>
    <p v-else-if="buscou">Nenhum gato encontrado 😿</p>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const API_URL = import.meta.env.VITE_API_URL
const API_KEY = import.meta.env.VITE_API_KEY

const busca = ref('')
const resultado = ref([])
const buscou = ref(false)

async function buscarGato() {
  if (!busca.value.trim()) return
  try {
    const resposta = await fetch(`${API_URL}/search?name=${busca.value}`, {
      headers: { 'x-api-key': API_KEY },
    })
    resultado.value = await resposta.json()
  } catch (err) {
    alert('Erro ao buscar gato: ' + err.message)
  } finally {
    buscou.value = true
  }
}
</script>
