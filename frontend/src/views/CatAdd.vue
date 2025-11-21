<template>
  <section>
    <h2>Adicionar novo gato:</h2>
    <div class="adicionar--gato">
      <input v-model="animal.name" placeholder="Nome do gato" />
      <input v-model.number="animal.age" type="number" placeholder="Idade do gato" />
      <div class="file-upload">
        <label class="file-label">
          Selecionar foto
          <input type="file" @change="handleFile" accept="image/*" hidden />
        </label>

        <div v-if="preview" class="preview">
          <img :src="preview" alt="Preview" />
        </div>
      </div>
      <div class="adicionar--button">
        <button @click="salvar">
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

const animal = ref({
  name: "",
  age: 0, 
  type: 'Cat'
});

const imagem = ref(null);
const preview = ref(null);

function handleFile(event) {
  const file = event.target.files[0];
  imagem.value = file;

  preview.value = URL.createObjectURL(file);
}

async function salvar() {
  try {
    const res = await adicionarAnimal(animal.value, imagem.value);
    console.log("Retorno:", res);
  } catch (e) {
    console.error(e);
  }
}

async function adicionarAnimal(animal, imagem) {
  const formData = new FormData();

  formData.append(
    "data",
    new Blob([JSON.stringify(animal)], { type: "application/json" })
  );

  // O backend espera "multipartImage"
  if (imagem) formData.append("multipartImage", imagem);

  const response = await fetch(`${API_URL}/api`, {
    method: "POST",
    headers: {
      "x-api-key": API_KEY,
    },
    body: formData
  });

  if (!response.ok) {
    throw new Error("Erro ao enviar animal");
  }

  return await response.json();
}
</script>
