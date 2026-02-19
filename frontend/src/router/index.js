import { createRouter, createWebHistory } from "vue-router";
import CatList from "../views/CatList.vue";
import CatSearch from "../views/CatSearch.vue";
import CatAdd from "../views/CatAdd.vue";
import Login from "@/views/Login.vue";
import Signup from "@/views/Signup.vue";
import { useAuthStore } from "@/stores/auth";

const routes = [
  { path: "/", redirect: "/login" },

  // ROTAS PÚBLICAS
  { path: "/login", component: Login },
  { path: "/signup", component: Signup },

  // ROTAS PRIVADAS
  { path: "/listar", component: CatList, meta: { requiresAuth: true } },
  { path: "/buscar", component: CatSearch, meta: { requiresAuth: true } },
  { path: "/adicionar", component: CatAdd, meta: { requiresAuth: true } },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// PROTEÇÃO DE ROTAS
router.beforeEach((to) => {
  const auth = useAuthStore();

  // Rota protegida → não logado
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return "/login";
  }

  // Usuário logado tentando acessar login → enviar para listar
  if (to.path === "/login" && auth.isAuthenticated) {
    return "/listar";
  }
});

export default router;
