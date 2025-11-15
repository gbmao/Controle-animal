<template>
    <nav class="nav--mobile">
        <ul class="menu menu--mobile">
        <li class="menu__item">
            <RouterLink class="menu__link" to="/listar">
            <i class="menu__icon bi bi-list"></i>
            <span class="menu__text">LISTAR</span>
            </RouterLink>
        </li>

        <li class="menu__item">
            <RouterLink class="menu__link" to="/buscar">
            <i class="menu__icon bi bi-search"></i>
            <span class="menu__text">BUSCAR</span>
            </RouterLink>
        </li>

        <li class="menu__item">
        <RouterLink class="menu__link" to="/adicionar">
            <i class="menu__icon bi bi-plus-lg"></i>
            <span class="menu__text">ADICIONAR</span>
            </RouterLink>
        </li>
        </ul>
    </nav>
</template>

<script setup>
import { RouterLink } from 'vue-router'
</script>

