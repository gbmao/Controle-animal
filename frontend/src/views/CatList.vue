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
import BaseButton from '@/components/BaseButton.vue';
import BaseCard from '@/components/BaseCard.vue';
import SetaIcon from '@/components/SetaIcon.vue';
import { ref, onMounted } from 'vue';
import api from "@/services/api";

const gatos = ref([]);
const loading = ref(false);
const error = ref(null);

const aberto = ref({});
const editando = ref(null);
const nomeEditado = ref("");

// ---------- ABRIR/FECHAR DETALHES ----------
function toggleInfo(id) {
  aberto.value[id] = !aberto.value[id];
}

// ---------- LISTAR GATOS ----------
async function listarGatos() {
  loading.value = true;

  try {
    const { data } = await api.get("/listar-gatos");

    console.log("RETORNO LISTAR-GATOS:", data);

    const lista = Array.isArray(data)
      ? data
      : Array.isArray(data?.data)
        ? data.data
        : Array.isArray(data?.gatos)
          ? data.gatos
          : [];

    gatos.value = lista.sort((a, b) =>
      a.name.localeCompare(b.name)
    );

  } catch (err) {
    error.value = "Erro ao listar gatos";
    console.error(err);

  } finally {
    loading.value = false;
  }
}

// ---------- IMAGENS ----------
function getImagemUrl(gato) {
  if (gato.imgID === -1) return "/controle-animal.png";

  return `/.netlify/functions/buscar-imagem?id=${gato.id}`;
}

// ---------- DELETAR GATO ----------
async function deletarGato(id) {
  if (!confirm("Deseja realmente deletar este gato?")) return;

  try {
    await api.delete(`/deletar-gato?id=${id}`);
    alert("Gato excluído!");
    listarGatos();
  } catch (err) {
    alert("Erro ao deletar gato");
    console.error(err);
  }
}

// ---------- EDITAR NOME ----------
function editarNome(gato) {
  editando.value = gato.id;
  nomeEditado.value = gato.name;
}

async function salvarNome(gato) {
  try {
    await api.put("/editar-gato", {
      ...gato,
      name: nomeEditado.value
    });

    alert("Nome alterado!");

    editando.value = null;
    listarGatos();

  } catch (err) {
    alert("Erro ao salvar nome");
    console.error(err);
  }
}

onMounted(listarGatos);
</script>
