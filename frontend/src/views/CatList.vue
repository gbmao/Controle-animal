<template>
  <section>
    <h2>Gatos registrados:</h2>
    
    <ul class="cat--list" v-if="gatos.length">
      
      <li v-for="gato in gatos" :key="gato.id">
        <BaseCard>

          <!-- Cabeçalho do gato (clique para expandir/fechar) -->
          <div class="ficha--gato">
            <img 
              v-if="gato.imgID !== -1"
              class="cat--pics"
              :src="getImagemUrl(gato)"
              alt="Foto do gato"
            />
            <div 
              v-else
              class="cat--pics placeholder"
            ></div>
          
          <div class="nome--gato--info" @click="toggleInfo(gato.id)">
            <h3>{{ gato.name }}</h3>
            <SetaIcon :class="{ rotacionado: aberto[gato.id] }" />
            </div>
            </div>
            <!-- Conteúdo expandido -->
          <div 
            class="gato-detalhes"
            v-show="aberto[gato.id]"
          >
            <p>Idade: {{ gato.age }}</p>
            <hr></hr>
            <h3>Registro veterinário</h3>
            <p>Veterinário:</p>
            <p>Vacinas:</p>
            <p>Data da última vacina:</p>
            <div class="delete--buton">
              <button @click="deletarGato(gato.id)">
                <BaseButton
                  title="Deletar gato"
                  icon="bi bi-trash3"
                  variant="default"
                />
              </button>
            </div>
          

          
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

function getImagemUrl(gato) {
  if (gato.imgID === -1) {
    return "/controle-animal.png" 
  }

  return `${API_URL}/images/${gato.imgID}`
}

async function deletarGato(id) {
  console.log(`${API_URL}/${id}`)
  if (!confirm('Deseja realmente deletar este gato?')) return
  try {
    const resposta = await fetch(`${API_URL}/api/${id}`, {
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
