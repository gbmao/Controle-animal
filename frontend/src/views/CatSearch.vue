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
              <!-- Use CatImage component -->
              <CatImage :gato="gato" />
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
    
    <!-- Optional: Show error message -->
    <p v-if="errorMsg" style="color: red;">{{ errorMsg }}</p>
  </section>
</template>

<script setup>
import BaseButton from '@/components/BaseButton.vue'
import BaseCard from '@/components/BaseCard.vue'
import CatImage from '@/components/CatImage.vue'
import { ref } from 'vue'

const busca = ref('')
const resultado = ref([])
const buscou = ref(false)
const errorMsg = ref('')

async function buscarGato() {
  if (!busca.value.trim()) {
    errorMsg.value = "Digite um nome para buscar"
    return
  }

  errorMsg.value = ''
  resultado.value = []
  buscou.value = false

  try {
    const url = `/.netlify/functions/buscar-gato?nome=${encodeURIComponent(busca.value)}`
    const resposta = await fetch(url, {
      credentials: "include" // Important: sends cookies with auth token
    })

    console.log("Search status:", resposta.status)

    if (resposta.status === 401) {
      errorMsg.value = "Erro de autenticação. Faça login novamente."
      return
    }

    if (!resposta.ok) {
      const errorData = await resposta.json().catch(() => ({}))
      errorMsg.value = errorData.error || "Erro na busca"
      return
    }

    const gatos = await resposta.json()

    resultado.value = gatos.map(g => {
      console.log("Cat found:", g)
      return {
        ...g,
        // Ensure imgID is set for CatImage component
        imgID: g.imgID || g.imageId || -1
      }
    })

  } catch (err) {
    console.error("Erro ao buscar gato:", err)
    errorMsg.value = "Erro de conexão: " + err.message
    resultado.value = []
  } finally {
    buscou.value = true
  }
}
</script>