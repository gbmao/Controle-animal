<template>
  <section>
    <h2>Lista de Gatos</h2>
    
    <ul v-if="gatos.length">
      
      <li v-for="gato in gatos" :key="gato.id">
        <baseCard>
        {{ gato.name }} ({{ gato.age }} anos)
        <button @click="deletarGato(gato.id)">🗑️</button>
        </baseCard>
      </li>
      
    </ul>
    <p v-else>Nenhum gato encontrado 😺</p>
  </section>
</template>

<script setup>
import BaseCard from '@/components/BaseCard.vue'
import { ref, onMounted } from 'vue'

const API_URL = import.meta.env.VITE_API_URL
const API_KEY = import.meta.env.VITE_API_KEY
const gatos = ref([])

async function listarGatos() {
  try {
    const resposta = await fetch(`${API_URL}/all`, {
      headers: { 'x-api-key': API_KEY },
    })
    gatos.value = await resposta.json()
  } catch (err) {
    alert('Erro ao listar gatos: ' + err.message)
  }
}

async function deletarGato(id) {
  console.log(`${API_URL}/${id}`)
  if (!confirm('Deseja realmente deletar este gato?')) return
  try {
    const resposta = await fetch(`${API_URL}/${id}`, {
      method: 'DELETE',
      headers: { 'x-api-key': API_KEY },
    })
    if (!resposta.ok) throw new Error('Erro ao deletar gato')
    listarGatos()
  } catch (err) {
    alert(err.message)
  }
}

onMounted(listarGatos)
</script>
