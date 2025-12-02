<template>
  <section class="login">
    <h2>Login</h2>

    <div class="form">
      <input
        v-model="login"
        placeholder="Login"
      />

      <input
        v-model="password"
        type="password"
        placeholder="Senha"
      />

      <button @click="loginUser">
        Entrar
      </button>

      <p>{{ msg }}</p>
    </div>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import router from "@/router";

const auth = useAuthStore();

const login = ref("");
const password = ref("");
const msg = ref("");

async function loginUser() {
  msg.value = "";

  try {
    const resp = await fetch("/.netlify/functions/login", {
      method: "POST",
      credentials: "include", // ESSENCIAL → recebe o cookie HttpOnly
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        login: login.value,
        password: password.value
      })
    });

    const data = await resp.json();

    console.log("Login response:", data); // DEBUG

    if (!resp.ok) {
      msg.value = data.message || data.error || "Usuário ou senha incorretos";
      return;
    }

    // Guarda o usuário (agora vem como data.user)
    if (data.user) {
      auth.setUser(data.user);
    } else if (data.login) {
      // Fallback to individual fields
      auth.setUser({
        id: data.id,
        login: data.login, // Changed from username to login
        email: data.email
      });
    }

    msg.value = "Login realizado com sucesso!";
    
    // Wait a moment to ensure cookie is set
    setTimeout(() => {
      router.push("/listar");
    }, 100);

  } catch (err) {
    console.error("Login error:", err);
    msg.value = "Erro ao conectar ao servidor";
  }
}
</script>