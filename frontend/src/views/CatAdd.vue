<template>
  <section>
    <h2>Adicionar novo gato:</h2>
    <div class="adicionar--gato">
      <div class="div--label-input">
        <label>Nome:</label>
        <input v-model="animal.name" placeholder="Nome do gato" maxlength="20"/>
      </div>
      <div class="div--label-input">
        <label>Data de nascimento:</label>
        <input v-model="animal.birthDate" type="date" class="input--date" placeholder="Data de nascimento do gato" />
      </div>
      <div class="file-upload">
        <label for="">Foto: </label>
        <label class="file-label"> 
          <span>Selecionar foto </span>
          <i class="bi bi-image"></i>
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
  birthDate: "", 
  type: 'Cat'
});

function calcularIdade(dateString) {
  const hoje = new Date();
  const nasc = new Date(dateString);

  let idade = hoje.getFullYear() - nasc.getFullYear();
  const m = hoje.getMonth() - nasc.getMonth();

  if (m < 0 || (m === 0 && hoje.getDate() < nasc.getDate())){
    idade--;
  }

  return idade;
}

const imagem = ref(null);
const preview = ref(null);

function handleFile(event) {
  const file = event.target.files[0];
  imagem.value = file;

  preview.value = URL.createObjectURL(file);
}

async function salvar() {
  try {
    animal.value.age = calcularIdade(animal.value.birthDate);

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
