<template>
  <section>
    <h2>Adicionar novo gato:</h2>
    <div class="adicionar--gato">
      <div class="div--label-input">
        <label>Nome:</label>
        <input 
          v-model="animal.name" 
          placeholder="Nome do gato" 
          maxlength="20"
          :class="{ 'error': isNameTaken }"
          @input="clearNameError"
        />
        <div v-if="nameValidation" class="validation-message">
          {{ nameValidation }}
        </div>
      </div>
      
      <div class="div--label-input">
        <label>Data de nascimento:</label>
        <input 
          v-model="animal.birthDate" 
          type="date" 
          class="input--date" 
          placeholder="Data de nascimento do gato"
          :max="today"
        />
        <div v-if="birthDateValidation" class="validation-message">
          {{ birthDateValidation }}
        </div>
      </div>
      
      <div class="file-upload">
        <label for="">Foto: </label>
        <label class="file-label"> 
          <span>Selecionar foto </span>
          <i class="bi bi-image"></i>
          <input 
            type="file" 
            @change="handleFile" 
            accept="image/*" 
            hidden 
            ref="fileInput"
          />
        </label>
        <span v-if="imagem" class="file-name">
          {{ imagem.name }} ({{ formatFileSize(imagem.size) }})
        </span>

        <div v-if="preview" class="preview">
          <img :src="preview" alt="Preview" />
          <button @click="removeImage" class="remove-image-btn" title="Remover imagem">
            ×
          </button>
        </div>
      </div>
      
      <div class="adicionar--button">
        <button 
          @click="salvar" 
          :disabled="isSaving || isNameTaken || !isFormValid"
          :class="{ 'loading': isSaving }"
        >
          <span v-if="isSaving">
            <i class="bi bi-arrow-clockwise spinning"></i> Salvando...
          </span>
          <span v-else>
            <BaseButton
              title="Adicionar novo gato"
              icon="bi bi-plus-lg"
            />
          </span>
        </button>
      </div>
      
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </div>
  </section>
</template>

<script setup>
import BaseButton from '@/components/BaseButton.vue'
import { ref, computed, onMounted } from 'vue'

const animal = ref({
  name: "",
  birthDate: "",
  type: "Cat"
});

const imagem = ref(null);
const preview = ref(null);
const fileInput = ref(null);
const isSaving = ref(false);
const errorMessage = ref("");
const existingCats = ref([]);
const today = ref(new Date().toISOString().split('T')[0]);

// Get existing cats for name validation
async function loadExistingCats() {
  try {
    const response = await fetch("/.netlify/functions/listar-gatos", {
      credentials: "include"
    });
    if (response.ok) {
      const data = await response.json();
      existingCats.value = Array.isArray(data) ? data : [];
    }
  } catch (error) {
    console.error("Erro ao carregar gatos:", error);
  }
}

onMounted(() => {
  loadExistingCats();
});

// Computed properties for validation
const isNameTaken = computed(() => {
  if (!animal.value.name.trim()) return false;
  return existingCats.value.some(cat => 
    cat.name.toLowerCase() === animal.value.name.toLowerCase()
  );
});

const nameValidation = computed(() => {
  if (!animal.value.name.trim()) return "Digite um nome";
  if (isNameTaken.value) return "Este nome já está em uso";
  if (animal.value.name.length > 20) return "Nome muito longo (máx. 20 caracteres)";
  return "";
});

const birthDateValidation = computed(() => {
  if (!animal.value.birthDate) return "Selecione uma data";
  const birthDate = new Date(animal.value.birthDate);
  const todayDate = new Date();
  if (birthDate > todayDate) return "Data não pode ser futura";
  return "";
});

const isFormValid = computed(() => {
  return animal.value.name.trim() && 
         animal.value.birthDate && 
         !isNameTaken.value &&
         !birthDateValidation.value;
});

function handleFile(event) {
  const file = event.target.files[0];
  if (!file) return;
  
  // Validate file type
  if (!file.type.startsWith('image/')) {
    alert("Por favor, selecione um arquivo de imagem válido");
    resetFileInput();
    return;
  }
  
  // Validate file size (max 5MB)
  const maxSize = 5 * 1024 * 1024; // 5MB
  if (file.size > maxSize) {
    alert("A imagem é muito grande. Tamanho máximo: 5MB");
    resetFileInput();
    return;
  }
  
  imagem.value = file;
  preview.value = URL.createObjectURL(file);
  errorMessage.value = "";
}

function removeImage() {
  imagem.value = null;
  if (preview.value) {
    URL.revokeObjectURL(preview.value);
    preview.value = null;
  }
  resetFileInput();
}

function resetFileInput() {
  if (fileInput.value) {
    fileInput.value.value = "";
  }
}

function formatFileSize(bytes) {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

function clearNameError() {
  if (errorMessage.value.includes("nome")) {
    errorMessage.value = "";
  }
}

function calcularIdade(dateString) {
  const hoje = new Date();
  const nasc = new Date(dateString);
  let idade = hoje.getFullYear() - nasc.getFullYear();
  const m = hoje.getMonth() - nasc.getMonth();
  if (m < 0 || (m === 0 && hoje.getDate() < nasc.getDate())) idade--;
  return idade;
}

async function adicionarAnimal(animal, imagem) {
  const formData = new FormData();

  formData.append("data", new Blob(
    [JSON.stringify(animal)],
    { type: "application/json" }
  ));

  if (imagem) {
    console.log("📤 Enviando imagem:", {
      nome: imagem.name,
      tipo: imagem.type,
      tamanho: formatFileSize(imagem.size)
    });
    formData.append("multipartImage", imagem);
  } else {
    console.log("⚠️ Nenhuma imagem selecionada");
  }

  const response = await fetch("/.netlify/functions/adicionar-gato", {
    method: "POST",
    body: formData,
    credentials: "include"
  });

  const responseText = await response.text();
  console.log("📨 Resposta do backend:", responseText);
  
  if (!response.ok) {
    console.error("❌ Backend retornou erro:", responseText);
    
    let errorMsg = "Erro ao enviar animal";
    try {
      const errorData = JSON.parse(responseText);
      errorMsg = errorData.message || errorData.error || errorMsg;
      
      // Handle specific error messages
      if (errorMsg.includes("Já existe esse nome")) {
        errorMsg = "Já existe um gato com esse nome. Por favor, escolha outro nome.";
      } else if (errorMsg.includes("não autenticado") || errorMsg.includes("Unauthorized")) {
        errorMsg = "Sessão expirada. Por favor, faça login novamente.";
      }
    } catch {
      errorMsg = responseText || errorMsg;
    }
    
    throw new Error(errorMsg);
  }

  const responseData = JSON.parse(responseText);
  console.log("✅ Gato adicionado com sucesso:", responseData);
  
  // Debug: Check image-related fields
  console.log("🖼️ Campos relacionados à imagem:", {
    imgID: responseData.imgID,
    imagemUrl: responseData.imagemUrl,
    imageId: responseData.imageId,
    imageUrl: responseData.imageUrl,
    pictureId: responseData.pictureId,
    imagePath: responseData.imagePath
  });

  return responseData;
}

async function salvar() {
  if (isSaving.value) return;
  
  try {
    isSaving.value = true;
    errorMessage.value = "";
    
    // Validation
    if (!animal.value.name.trim()) {
      errorMessage.value = "Por favor, digite um nome para o gato";
      return;
    }
    
    if (isNameTaken.value) {
      errorMessage.value = "Este nome já está em uso. Por favor, escolha outro nome.";
      return;
    }
    
    if (!animal.value.birthDate) {
      errorMessage.value = "Por favor, selecione a data de nascimento";
      return;
    }
    
    animal.value.age = calcularIdade(animal.value.birthDate);
    console.log("💾 Salvando gato:", animal.value);

    const res = await adicionarAnimal(animal.value, imagem.value);

    alert("✅ Gato adicionado com sucesso!");
    
    // Reset form
    animal.value = { name: "", birthDate: "", type: "Cat" };
    removeImage();
    
    // Reload existing cats list
    loadExistingCats();
    
    // Emit event if parent component needs to refresh
    // emit('cat-added', res);

  } catch (e) {
    console.error("❌ Erro ao salvar:", e);
    errorMessage.value = e.message || "Não foi possível adicionar o gato";
    alert(errorMessage.value);
  } finally {
    isSaving.value = false;
  }
}
</script>

<style scoped>
.validation-message {
  color: #dc3545;
  font-size: 0.875rem;
  margin-top: 0.25rem;
  min-height: 1.25rem;
}

input.error {
  border-color: #dc3545;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

button.loading {
  position: relative;
}

.spinning {
  animation: spin 1s linear infinite;
  display: inline-block;
  margin-right: 0.5rem;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 0.75rem;
  border-radius: 0.375rem;
  margin-top: 1rem;
  border: 1px solid #f5c6cb;
}

.file-name {
  display: block;
  margin-top: 0.5rem;
  font-size: 0.875rem;
  color: #6c757d;
}

.preview {
  position: relative;
  margin-top: 1rem;
  max-width: 200px;
}

.preview img {
  max-width: 100%;
  max-height: 200px;
  border-radius: 0.375rem;
  border: 2px solid #dee2e6;
}

.remove-image-btn {
  position: absolute;
  top: -10px;
  right: -10px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  font-size: 1rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
}

.remove-image-btn:hover {
  background: #c82333;
}
</style>