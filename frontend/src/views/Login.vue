<script setup>
import { ref } from "vue";
import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

const login = ref("");     // nome da propriedade CERTA
const password = ref("");
const msg = ref("");

async function loginUser() {
  try {
    const resp = await axios.post(`${API_URL}/api/auth/signin`, {
      login: login.value,      // CORRETO
      password: password.value
    });

    localStorage.setItem("token", resp.data.token);

    msg.value = "Login realizado com sucesso!";
    // window.location.href = "/listar";
  } catch (err) {
    msg.value = "Usuário ou senha incorretos";
  }
}
</script>

<template>
  <section class="login">
    <h2>Login</h2>

    <input v-model="login" placeholder="Usuário (login)" />
    <input v-model="password" type="password" placeholder="Senha" />

    <button @click="loginUser">Entrar</button>

    <p v-if="msg">{{ msg }}</p>
  </section>
</template>
