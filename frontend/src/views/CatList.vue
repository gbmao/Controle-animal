<template>
  <section>
    <h2>Gatos registrados:</h2>
    <ul class="cat--list" v-if="gatos.length">
      <li v-for="gato in gatos" :key="gato.id">
        <BaseCard>
          <div class="ficha--gato">
            <img 
              v-if="gato.imgID !== -1"
              class="cat--pics"
              :src="getImagemUrl(gato)"
              alt="Foto do gato"
            />
            <div v-else class="cat--pics placeholder"></div>
            <div class="nome--gato--info" @click="toggleInfo(gato.id)">
              <h3 v-if="editando !== gato.id">{{ gato.name }}</h3>
              <input 
                v-else 
                v-model="nomeEditado" 
                class="input-editar-nome"
                @keyup.enter="salvarNome(gato)"
                @click.stop
              />
              <SetaIcon :class="{ rotacionado: aberto[gato.id] }" />
            </div>
          </div>
          <!-- Aqui fecha ficha--gato -->
          <div class="gato-detalhes" v-show="aberto[gato.id]">
            <hr />

            <p>Idade: {{ gato.age }}</p>

            <div class="delete--buton">
              <button v-if="editando !== gato.id" @click.stop="editarNome(gato)">
                <BaseButton title="Editar nome do gato" icon="bi bi-pencil" variant="default" />
              </button>
              <button v-else @click.stop="salvarNome(gato)">
                <BaseButton title="Salvar nome" icon="bi bi-check-lg" variant="default" />
              </button>
              <button @click="deletarGato(gato.id)">
                <BaseButton title="Deletar gato" icon="bi bi-trash3" variant="default" />
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
    const resposta = await fetch("/.netlify/functions/listar-gatos")

    const data = await resposta.json()

    // Aqui é só usar o data.sort para organizar por ordem alfabética pelo nome dos gatos
    gatos.value = data.sort((a, b) => 
      a.name.localeCompare(b.name)
    )

  } catch (err) {
    alert('Erro ao listar gatos: ' + err.message)
  }
}

function getImagemUrl(gato) {
  if (gato.imgID === -1) {
    return "/controle-animal.png" 
  }

  return `/.netlify/functions/buscar-imagem?id=${gato.id}`;

}

async function deletarGato(id) {
  if (!confirm('Deseja realmente deletar este gato?')) return
  try {
    const resposta = await fetch(`/.netlify/functions/deletar-gato?id=${id}`)
    if (resposta.ok) {
      alert("Gato excluído")
    }
    if (!resposta.ok) throw new Error('Erro ao deletar gato')
    listarGatos()
  } catch (err) {
    alert(err.message)
  }
}

const editando = ref(null) // id do gato sendo editado
const nomeEditado = ref("")

function editarNome(gato) {
  editando.value = gato.id
  nomeEditado.value = gato.name
}

async function salvarNome(gato) {
  try {
    // Atualiza o nome temporariamente para enviar ao backend
    const gatoAtualizado = {
      ...gato,
      name: nomeEditado.value
    };

    const resposta = await fetch("/.netlify/functions/editar-gato", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(gatoAtualizado)
    });

    if (!resposta.ok) throw new Error("Erro ao alterar nome")

    if (resposta.ok) {
      alert("Nome alterado com sucesso!")
    }

    // Atualiza lista
    await listarGatos()

    // Sai do modo edição
    editando.value = null

  } catch(err) {
    alert(err.message)
  }
}

onMounted(listarGatos)
</script>
