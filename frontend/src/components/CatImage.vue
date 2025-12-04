<!-- CatImage.vue - UPDATED -->
<template>
  <div class="cat-image-container">
    <!-- Show image if cat has imgID (not -1) -->
    <img 
      v-if="shouldShowImage && imageUrl && !isLoading && !imageError"
      :src="imageUrl"
      :alt="`Foto do gato ${gato.name}`"
      class="cat--pics"
      @error="handleImageError"
      @load="handleImageLoad"
    />
    
    <!-- Loading state -->
    <div v-else-if="shouldShowImage && isLoading" class="cat--pics placeholder loading">
      <i class="bi bi-arrow-clockwise spinning"></i>
      <span>Carregando imagem...</span>
    </div>
    
    <!-- Error state -->
    <div v-else-if="shouldShowImage && imageError" class="cat--pics placeholder error">
      <i class="bi bi-exclamation-triangle"></i>
      <span>Erro ao carregar</span>
      <small>ID: {{ gato.id }}</small>
    </div>
    
    <!-- No image (imgID === -1) -->
    <div v-else class="cat--pics placeholder no-image">
      <i class="bi bi-image"></i>
      <span>Sem imagem</span>
    </div>
    
    <!-- Debug info (optional) -->
    <div v-if="showDebug" class="debug-info">
      <small>
        Cat ID: {{ gato.id }}<br>
        imgID: {{ gato.imgID }}<br>
        Has image: {{ shouldShowImage }}
      </small>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';

const props = defineProps({
  gato: {
    type: Object,
    required: true
  },
  showDebug: {
    type: Boolean,
    default: false
  }
});

const isLoading = ref(false);
const imageError = ref(false);
const imageLoaded = ref(false);

// Determine if we should try to load an image
const shouldShowImage = computed(() => {
  return props.gato.imgID && props.gato.imgID !== -1;
});

// Generate the correct image URL using CAT ID, not imgID
const imageUrl = computed(() => {
  if (!shouldShowImage.value) {
    return null;
  }
  
  // Use the cat's ID (animalId), NOT imgID!
  return `/.netlify/functions/buscar-imagem-universal?catId=${props.gato.id}`;
});

function handleImageError() {
  console.error(`Image failed to load for cat ${props.gato.name} (ID: ${props.gato.id})`);
  imageError.value = true;
  isLoading.value = false;
}

function handleImageLoad() {
  console.log(`Image loaded successfully for cat ${props.gato.name}`);
  imageLoaded.value = true;
  imageError.value = false;
  isLoading.value = false;
}

// Load/Reset when cat changes
watch(() => props.gato, () => {
  imageError.value = false;
  imageLoaded.value = false;
  isLoading.value = false;
}, { deep: true });

onMounted(() => {
  if (shouldShowImage.value) {
    isLoading.value = true;
  }
});
</script>

