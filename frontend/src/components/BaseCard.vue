<template>
  <div class="base-card">
    <slot></slot>
  </div>
</template>

<script setup>
</script>

<style scoped>
.base-card {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    background: var(--card-bg, #fff);
    padding: 1.2rem;
    border-radius: 12px;
    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    margin-bottom: 1rem;
}
</style>
