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

    if (!resp.ok) {
      msg.value = data.message || "Usuário ou senha incorretos";
      return;
    }

    // Guarda apenas o usuário (não o token)
    auth.setUser(data.login);
    router.push("/listar");

    msg.value = "Login realizado com sucesso!";
    // window.location.href = "/listar";

  } catch (err) {
    msg.value = "Erro ao conectar ao servidor";
  }
}
</script>

<style scoped>
.login {
  max-width: 350px;
  margin: 0 auto;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
button {
  padding: 10px 20px;
}
</style>
