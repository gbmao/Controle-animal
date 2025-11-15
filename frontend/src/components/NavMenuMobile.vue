<template>
    <nav class="nav--mobile">
        <ul class="menu menu--mobile">
        <li class="menu__item">
            <RouterLink class="menu__link" to="/">
            <i class="menu__icon bi bi-house-door"></i>
            <span class="menu__text">Home</span>
            </RouterLink>
        </li>

        <li class="menu__item">
            <RouterLink class="menu__link" to="/projetos">
            <i class="menu__icon bi bi-window-stack"></i>
            <span class="menu__text">Projetos</span>
            </RouterLink>
        </li>

        <li class="menu__item">
        <RouterLink class="menu__link" to="/sobre">
            <i class="menu__icon bi bi-person"></i>
            <span class="menu__text">Sobre</span>
            </RouterLink>
        </li>

        <li class="menu__item">
            <RouterLink class="menu__link" to="/contato">
            <i class="menu__icon bi bi-envelope"></i>
            <span class="menu__text">Contato</span>
            </RouterLink>
        </li>
        </ul>
    </nav>
</template>

<script setup>
import { RouterLink } from 'vue-router'
</script>

