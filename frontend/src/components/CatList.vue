<template>
  <div class="container">
    <button @click="listarGatos">Listar gatos</button>

    <ul v-if="gatos.length">
      <li v-for="(gato, index) in gatos" :key="index">
        {{ gato.name }}
        <button @click="deletarGato(gato.id)">🗑️</button>
      </li>
    </ul>

    <div class="form-adicionar">
      <input v-model="novoGato.name" placeholder="Nome do gato" />
      <input v-model.number="novoGato.age" type="number" placeholder="Idade" />
      <button @click="adicionarGato">Adicionar</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const API_URL = import.meta.env.VITE_API_URL
const API_KEY = import.meta.env.VITE_API_KEY

const gatos = ref([])
const novoGato = ref({ name: '', age: 0 })

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

async function adicionarGato() {
  if (!novoGato.value.name) {
    alert('Digite o nome do gato!')
    return
  }

  try {
    const resposta = await fetch(`${API_URL}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'x-api-key': API_KEY,
      },
      body: JSON.stringify(novoGato.value),
    })

    if (!resposta.ok) throw new Error('Erro ao adicionar gato')
    listarGatos()
    novoGato.value = { name: '', age: 0 }
  } catch (err) {
    alert(err.message)
  }
}

async function deletarGato(id) {
  if (!confirm('Deseja realmente deletar este gato?')) return

  try {
    const resposta = await fetch(`https://controle-animal-production.up.railway.app/api/${id}`, {
      method: 'DELETE',
      headers: { 'x-api-key': API_KEY },
    })

    if (!resposta.ok) throw new Error('Erro ao deletar gato')
    listarGatos()
  } catch (err) {
    alert(err.message)
  }
}
</script>
