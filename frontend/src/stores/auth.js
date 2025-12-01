import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null
  }),

  getters: {
    isAuthenticated: (state) => state.user !== null
  },

  actions: {
    setUser(login) {
      this.user = login;  // aqui pode ser "true" se preferir
    },

    logout() {
      this.user = null;
    }
  }
});
