<template>
  <section class="signup">
    <h2>Criar conta</h2>

    <input v-model="login" placeholder="Login" />
    <input v-model="email" placeholder="Email" />
    <input v-model="password" type="password" placeholder="Senha" />

    <button @click="signupUser">Cadastrar</button>

    <p v-if="msg">{{ msg }}</p>
  </section>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

const login = ref("");
const email = ref("");
const password = ref("");
const msg = ref("");

async function signupUser() {
  try {
    const resp = await axios.post(`${API_URL}/api/auth/signup`, {
      login: login.value,
      email: email.value,
      password: password.value
    });

    msg.value = resp.data.message || "Cadastro realizado com sucesso!";
  } catch (err) {
    msg.value = err.response?.data?.message || "Erro ao cadastrar";
  }
}
</script>
