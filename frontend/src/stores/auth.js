// /src/stores/auth.js
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    token: null, // Store token in memory too if needed
  }),

  getters: {
    isAuthenticated: (state) => !!state.user,
  },

  actions: {
    setUser(user) {
      this.user = user;
    },

    setToken(token) {
      this.token = token;
    },

    logout() {
      this.user = null;
      this.token = null;
      // Also clear the cookie
      document.cookie = 'auth_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    }
  }
});