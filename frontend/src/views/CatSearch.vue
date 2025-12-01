<template>
  <section>
    <h2>Buscar por gato:</h2>

    <div class="buscar--gato">
      <input v-model="busca" placeholder="Digite o nome do gato" />

      <BaseButton
        @click="buscarGato"
        title="Buscar gato"
        icon="bi bi-search"
      />
    </div>

    <ul v-if="resultado.length">
      <li v-for="gato in resultado" :key="gato.id">
        <BaseCard>
          <div class="info--search">
            <div class="cat--search">
              <img v-if="gato.imagemUrl" :src="gato.imagemUrl" class="cat--pics" />

              <!-- MOSTRA PLACEHOLDER SE NÃO EXISTIR -->
              <div v-else class="cat--pics placeholder"></div>

              <h3>{{ gato.name }}</h3>
              </div>
              <hr/>
              <div class="gato-detalhes-search">
                <p>Idade: {{ gato.age }}</p>
              </div>
            </div>
        </BaseCard>
      </li>
    </ul>

    <p v-else-if="buscou">Nenhum gato encontrado</p>
  </section>
</template>

<script setup>
import BaseButton from '@/components/BaseButton.vue'
import BaseCard from '@/components/BaseCard.vue'
import { ref } from 'vue'

const busca = ref('')
const resultado = ref([])
const buscou = ref(false)

async function buscarGato() {
  if (!busca.value.trim()) return

  try {
    const url = `/.netlify/functions/buscar-gato?nome=${encodeURIComponent(busca.value)}`
    const resposta = await fetch(url)

    if (!resposta.ok) {
      resultado.value = []
      return
    }

    const gatos = await resposta.json()

resultado.value = gatos.map(g => {
  console.log("🐱 Dados recebidos do backend:", g)
  console.log("📸 URL gerada pelo backend (imagemUrl):", g.imagemUrl)
  return {
    ...g,
    imagemUrl: g.imagemUrl || null
  }
})

  } catch (err) {
    alert("Erro ao buscar gato: " + err.message)
    resultado.value = []
  } finally {
    buscou.value = true
  }
}
</script>