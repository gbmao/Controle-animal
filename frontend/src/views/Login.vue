<template>
  <section class="login">

    <div class="login-info">
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
      <button>
        <BaseButton @click="loginUser"
          title="Entrar"
          icon="bi bi-box-arrow-in-right"
        />
      </button>
      <p>{{ msg }}</p>
    </div>
      <div>
        <h2>Ainda não tem uma conta?</h2>
        <p><span class="cadastro-link" @click="goToSignup">Cadastre-se</span></p>
      </div>
    
    </div>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import router from "@/router";
import BaseButton from "@/components/BaseButton.vue";

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

function goToSignup() {
  router.push("/signup");
}
</script>

<style scoped>

.login {
  display: flex;
  justify-content: space-between;
}

.login-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

button {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

</style>
