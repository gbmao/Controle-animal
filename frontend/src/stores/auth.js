import { defineStore } from "pinia";
import { login as loginService, removeToken, saveToken } from "@/services/auth";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    isAuthenticated: !!localStorage.getItem("auth_token"),
    loading: false,
    error: null
  }),

  actions: {
    async login(username, password) {
      this.loading = true;
      this.error = null;

      try {
        const data = await loginService(username, password);

        saveToken(data.token);
        this.isAuthenticated = true;

      } catch (err) {
        this.error = err.message;
        this.isAuthenticated = false;

      } finally {
        this.loading = false;
      }
    },

    logout() {
      removeToken();
      this.isAuthenticated = false;
    }
  }
});
