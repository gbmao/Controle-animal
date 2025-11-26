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

          <!-- IMAGEM OU PLACEHOLDER -->
        <img
        v-if="gato.imgID !== -1"
        :src="gato.imagemUrl"
        class="cat--pics"
        />

        <div v-else class="cat--pics placeholder"></div>

          <div class="gato-detalhes">
            <h3>{{ gato.name }}</h3>
            <p>Idade: {{ gato.age }}</p>
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

// Função que busca a imagem individualmente via Netlify
async function carregarImagem(gato) {
  try {
    const res = await fetch(`/.netlify/functions/buscar-imagem?id=${gato.id}`)

    if (!res.ok) {
      return {
        ...gato,
        imgID: -1,
        imagemUrl: "/controle-animal.png"
      }
    }

    const blob = await res.blob()

    // 🔥 CASO CRÍTICO → quando o backend devolve imagem null
    if (blob.size === 0) {
      return {
        ...gato,
        imgID: -1,
        imagemUrl: "/controle-animal.png"
      }
    }

    const url = URL.createObjectURL(blob)

    return {
      ...gato,
      imgID: gato.id,
      imagemUrl: url
    }

  } catch (e) {
    return {
      ...gato,
      imgID: -1,
      imagemUrl: "/controle-animal.png"
    }
  }
}

async function buscarGato() {
  if (!busca.value.trim()) return

  try {
    const url = `/.netlify/functions/buscar-gato?nome=${encodeURIComponent(busca.value)}`
    const resposta = await fetch(url)

    if (!resposta.ok) {
      resultado.value = []
      return
    }

    const dados = await resposta.json()

    // garante array
    const gatos = Array.isArray(dados) ? dados : []

    // 2️⃣ para cada gato → buscar imagem
    const gatosComImagem = await Promise.all(
      gatos.map(g => carregarImagem(g))
    )

    resultado.value = gatosComImagem

  } catch (err) {
    alert('Erro ao buscar gato: ' + err.message)
    resultado.value = []
  } finally {
    buscou.value = true
  }
}
</script>
