    <template>
    <div>
        <input type="file" @change="handleFile" />
        <button @click="uploadImage">Enviar Imagem</button>
    </div>
    </template>

    <script setup>
    import { ref } from "vue"

    const API_URL = import.meta.env.VITE_API_URL
    const API_KEY = import.meta.env.VITE_API_KEY

    const selectedFile = ref(null)

    function handleFile(event) {
    selectedFile.value = event.target.files[0]
    }

    async function uploadImage() {
    if (!selectedFile.value) {
        alert("Selecione uma imagem!")
        return
    }

    const formData = new FormData()
    formData.append("multipartImage", selectedFile.value) // ajuste o nome se necessário

    try {
        const resp = await fetch(
        `${API_URL}/images/${animalId}`,
        {
            method: "POST",
            headers: {
            "x-api-key": API_KEY,
            },
            body: formData,
        }
        )
        const data = await resp.json().catch(() => ({}))
        if (resp.ok) {
        console.log("Upload OK! Imagem ID:", data)
        } else {
        console.error("Erro no upload", data)
        alert(data?.message || "Erro no upload")
        }
    } catch (e) {
        console.error("Erro de rede:", e)
        alert("Erro de rede")
    }
    }
    </script>