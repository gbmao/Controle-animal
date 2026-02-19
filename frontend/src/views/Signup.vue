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
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const auth = useAuthStore();

const login = ref("");
const email = ref("");
const password = ref("");
const msg = ref("");

async function signupUser() {
  msg.value = "";

  try {
    // 1) SIGNUP
    const respSignup = await fetch("/.netlify/functions/signup", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        login: login.value,
        email: email.value,
        password: password.value
      })
    });

    const dataSignup = await respSignup.json();

    if (!respSignup.ok) {
      msg.value = dataSignup.message || "Erro ao cadastrar";
      return;
    }

    // 2) LOGIN AUTOMÁTICO
    const respLogin = await fetch("/.netlify/functions/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include", // MUITO IMPORTANTE para receber cookie HttpOnly
      body: JSON.stringify({
        login: login.value,
        password: password.value
      })
    });

    const dataLogin = await respLogin.json();

    if (!respLogin.ok) {
      msg.value = "Conta criada, mas erro ao autenticar.";
      console.log("Erro login:", dataLogin);
      return;
    }

    // 3) ATUALIZA PINIA
    auth.login({
      login: dataLogin.login,
      email: dataLogin.email,
      id: dataLogin.id
    });

    // 4) REDIRECIONA PARA LISTAR
    router.push("/listar");

  } catch (err) {
    msg.value = "Erro inesperado ao cadastrar.";
  }
}
</script>
