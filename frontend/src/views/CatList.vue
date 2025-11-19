<template>
  <section>
    <h2>Gatos registrados:</h2>

    <CatPics/>
    
    <ul class="cat--list" v-if="gatos.length">
      
      <li v-for="gato in gatos" :key="gato.id">
        <BaseCard>

          <!-- Cabeçalho do gato (clique para expandir/fechar) -->
          <div class="nome--gato--info" @click="toggleInfo(gato.id)">
            <h3>{{ gato.name }}</h3>
            <SetaIcon :class="{ rotacionado: aberto[gato.id] }" />
          </div>

          <!-- Conteúdo expandido -->
          <div 
            class="gato-detalhes"
            v-show="aberto[gato.id]"
          >
            <p>Idade: {{ gato.age }}</p>
            <div class="delete--buton">
              <button @click="deletarGato(gato.id)">
                <BaseButton
                  title="Deletar gato"
                  icon="bi bi-trash3"
                  variant="default"
                  @click="buscar"
                />
              </button>
            </div>
          </div>

        </BaseCard>
      </li>
      
    </ul>
    <p v-else>Nenhum gato encontrado</p>
  </section>
</template>

<script setup>
import BaseButton from '@/components/BaseButton.vue'
import BaseCard from '@/components/BaseCard.vue'
import SetaIcon from '@/components/SetaIcon.vue'
import CatPics from '@/components/CatPics.vue'
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
    const resposta = await fetch(`${API_URL}/api/all`, {
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
