<template>
  <button 
    :class="['base-button', variant]"
    :title="title"
    @click="$emit('click')"
  >
    <!-- Ícone obrigatório -->
    <i v-if="icon" :class="icon"></i>

  </button>
</template>

<script setup>
defineProps({
  title: String,        // texto do tooltip
  icon: String,         // classe do ícone
  variant: {            // estilo: add, delete, search...
    type: String,
    default: 'default'
  }
});
</script>

<style scoped>
.base-button {
  display: flex;
  align-items: center;
  gap: .5rem;
  padding: 0.8rem 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-family: inherit;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.base-button.default { background: #7A9590; color: #f5f5f5; }

.base-button:hover {
  background-color: #557a76;
}

.icon svg {
  width: 16px;
  height: 16px;
}
</style>