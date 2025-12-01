import { createRouter, createWebHistory } from "vue-router";
import CatList from "../views/CatList.vue";
import CatSearch from "../views/CatSearch.vue";
import CatAdd from "../views/CatAdd.vue";
import Login from "@/views/Login.vue";
import Signup from "@/views/Signup.vue";
import { useAuthStore } from "@/stores/auth";

const routes = [
  { path: "/", redirect: "/login" },

  // PÚBLICAS
  { path: "/login", component: Login },
  { path: "/signup", component: Signup },

  // PRIVADAS
  { path: "/listar", component: CatList, meta: { requiresAuth: true } },
  { path: "/buscar", component: CatSearch, meta: { requiresAuth: true } },
  { path: "/adicionar", component: CatAdd, meta: { requiresAuth: true } },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to) => {
  const auth = useAuthStore();

  // ROTA PROTEGIDA → usuário não está logado
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return "/login";
  }

  // Usuário logado tentando acessar /login → manda para /listar
  if (to.path === "/login" && auth.isAuthenticated) {
    return "/listar";
  }
});

export default router;
