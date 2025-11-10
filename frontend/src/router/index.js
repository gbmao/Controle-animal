import { createRouter, createWebHistory } from 'vue-router'
import CatList from '../views/CatList.vue'
import CatSearch from '../views/CatSearch.vue'
import CatAdd from '../views/CatAdd.vue'

const routes = [
  { path: '/', redirect: '/listar' },
  { path: '/listar', component: CatList },
  { path: '/buscar', component: CatSearch },
  { path: '/adicionar', component: CatAdd },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
