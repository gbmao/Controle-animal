<template>
  <section>
    <h2>Gatos registrados:</h2>
    
    <ul class="cat--list" v-if="gatos.length">
      
      <li class="cat--list--item" v-for="gato in gatos" :key="gato.id">
        <baseCard>

          <!-- Cabeçalho do gato (clique para expandir/fechar) -->
          <div class="nome--gato--info" @click="toggleInfo(gato.id)">
            {{ gato.name }}
            <SetaIcon :class="{ rotacionado: aberto[gato.id] }" />
          </div>

          <!-- Conteúdo expandido -->
          <div 
            class="gato-detalhes"
            v-show="aberto[gato.id]"
          >
            <p>Idade: {{ gato.age }}</p>
            <button @click="deletarGato(gato.id)">🗑️ Deletar</button>
          </div>

        </baseCard>
      </li>
      
    </ul>
    <p v-else>Nenhum gato encontrado</p>
  </section>
</template>

<script setup>
import BaseCard from '@/components/BaseCard.vue'
import SetaIcon from '@/components/SetaIcon.vue'
import { ref, onMounted } from 'vue'

const API_URL = import.meta.env.VITE_API_URL
const API_KEY = import.meta.env.VITE_API_KEY
const gatos = ref([])

const aberto = ref({}) // controla quais IDs estão abertos

function toggleInfo(id) {
  aberto.value[id] = !aberto.value[id]
}

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
