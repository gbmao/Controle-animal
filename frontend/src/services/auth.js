import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL;

export async function login(login, password) {
  const resp = await axios.post(`${API_URL}/api/auth/signin`, {
    login,        // correto
    password
  });

  localStorage.setItem("token", resp.data.token);

  return resp.data;
}

export function saveToken(token) {
  localStorage.setItem("auth_token", token);
}

export function removeToken() {
  localStorage.removeItem("auth_token");
}
