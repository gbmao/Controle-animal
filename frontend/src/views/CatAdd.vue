<template>
  <section>
    <h2>Adicionar novo gato:</h2>
    <div class="adicionar--gato">
      <input v-model="novo.name" placeholder="Nome do gato" />
      <input v-model.number="novo.age" type="number" placeholder="Idade do gato" />
    
      <div class="adicionar--button">
        <button @click="adicionarGato">
          <BaseButton
            title="Adicionar novo gato"
            icon="bi bi-plus-lg"
          />
        </button>
      </div>
    </div>
  </section>
</template>

<script setup>
import BaseButton from '@/components/BaseButton.vue'
import { ref } from 'vue'

const API_URL = import.meta.env.VITE_API_URL
const API_KEY = import.meta.env.VITE_API_KEY
const novo = ref({ name: '', age: '', type: 'Cat' })

async function adicionarGato() {
  if (!novo.value.name) {
    alert('Digite o nome!')
    return
  }

  try {
    const resposta = await fetch(`${API_URL}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'x-api-key': API_KEY,
      },
      body: JSON.stringify(novo.value),
    })

    if (!resposta.ok) throw new Error('Erro ao adicionar gato')

    alert('Gato adicionado!')
    novo.value = { name: '', age: 0, type: 'Cat'}
  } catch (err) {
    alert(err.message)
  }
}
</script>
