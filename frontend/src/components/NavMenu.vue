<template>
  <nav class="header__nav">
    <ul class="menu menu--principal">
      <li class="menu__item">
        <RouterLink to="/listar" class="menu__link" active-class="active">
          <span class="menu__text">Listar</span>
        </RouterLink>
      </li>
      <li class="menu__item">
        <RouterLink to="/buscar" class="menu__link" active-class="active">
          <span class="menu__text">Buscar</span>
        </RouterLink>
      </li>
      <li class="menu__item">
        <RouterLink to="/adicionar" class="menu__link" active-class="active">
          <span class="menu__text">Adicionar</span>
        </RouterLink>
      </li>
    </ul>
  </nav>
</template>

<style scoped>
.menu__link.active {
  font-weight: bold;
  color: var(--color-primary);
}
</style>
