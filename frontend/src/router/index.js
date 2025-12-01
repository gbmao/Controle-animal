import { createRouter, createWebHistory } from "vue-router";
import CatList from "../views/CatList.vue";
import CatSearch from "../views/CatSearch.vue";
import CatAdd from "../views/CatAdd.vue";
import Login from "@/views/Login.vue";
import Signup from "@/views/Signup.vue";   // <-- ADICIONAR
import { useAuthStore } from "@/stores/auth";

const routes = [
  { path: "/", redirect: "/login" },

  // TELAS PÚBLICAS
  { path: "/login", component: Login },
  { path: "/signup", component: Signup }, // <-- ADICIONAR

  // TELAS PROTEGIDAS
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

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return "/login";
  }
});

export default router;
