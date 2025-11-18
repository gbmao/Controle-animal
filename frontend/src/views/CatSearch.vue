<template>
  <section>
    <h2>Buscar por gato:</h2>
    <div class="buscar--gato">
      <input v-model="busca" placeholder="Digite o nome do gato" />
      <button @click="buscarGato">
        <BaseButton
          title="Buscar gato"
          icon="bi bi-search"
        />
      </button>
    </div>

    <ul v-if="resultado.length">
      <li v-for="gato in resultado" :key="gato.id">
        <BaseCard>
          <div class="nome--gato--info">
            <h3>{{ gato.name }}</h3>
          </div>
          <div class="gato-detalhes">
            <p>Idade: {{ gato.age }}</p>
          </div>
        </BaseCard>
      </li>
    </ul>
    <p v-else-if="buscou">Nenhum gato encontrado </p>
  </section>
</template>

<script setup>
import BaseButton from '@/components/BaseButton.vue'
import BaseCard from '@/components/BaseCard.vue'
import { ref } from 'vue'

const API_URL = import.meta.env.VITE_API_URL
const API_KEY = import.meta.env.VITE_API_KEY

const busca = ref('')
const resultado = ref([])
const buscou = ref(false)

// util para tentar parsear JSON com fallback para texto
async function parseResponse(res) {
  const ct = res.headers.get('content-type') || ''
  const text = await res.text()
  // tenta parsear se for JSON ou aparentar JSON
  if (ct.includes('application/json') || text.trim().startsWith('{') || text.trim().startsWith('[')) {
    try {
      return JSON.parse(text)
    } catch (e) {
      // fallback: devolve o texto caso o JSON esteja malformado
      return { __rawText: text }
    }
  }
  return { __rawText: text }
}

async function buscarGato() {
  if (!busca.value.trim()) return
  try {
    const url = `${API_URL}/search/${encodeURIComponent(busca.value)}`
    const resposta = await fetch(url, {
      headers: { 'x-api-key': API_KEY },
    })

    const parsed = await parseResponse(resposta)

    if (!resposta.ok) {
      // se o backend devolveu mensagem em texto, parsed.__rawText terá o texto
      const msg = parsed && parsed.message ? parsed.message : parsed.__rawText || 'Erro desconhecido'
      throw new Error(msg)
    }

    // se parsed for { __rawText: '...' } significa que veio texto (não ideal)
    if (parsed.__rawText) {
      // tenta converter texto simples em array/objeto esperado (ou mostrarmos mensagem)
      // aqui assumimos que backend deveria retornar um array de gatos em JSON
      console.warn('Resposta não-JSON recebida:', parsed.__rawText)
      resultado.value = [] // ou interpretar conforme necessário
      throw new Error(parsed.__rawText)
    } else {
      resultado.value = Array.isArray(parsed) ? parsed : [parsed]
    }
  } catch (err) {
    alert('Erro ao buscar gato: ' + err.message)
    resultado.value = []
  } finally {
    buscou.value = true
  }
}

async function buscarSugestoes() {
  if (busca.value.length < 2) {
    sugestoes.value = []
    return
  }

  try {
    const url = `${API_URL}/search/${encodeURIComponent(busca.value)}`
    const resposta = await fetch(url, { headers: { 'x-api-key': API_KEY } })
    const parsed = await parseResponse(resposta)

    if (!resposta.ok) {
      const msg = parsed && parsed.message ? parsed.message : parsed.__rawText || 'Erro desconhecido'
      throw new Error(msg)
    }

    // espera um array de sugestões
    sugestoes.value = Array.isArray(parsed) ? parsed : []
  } catch (err) {
    console.warn('Erro ao buscar sugestões:', err.message)
    sugestoes.value = []
  }
}

</script>
