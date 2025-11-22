    <template>
    <div>
        <input type="file" @change="handleFile" />
        <button @click="uploadImage">Enviar Imagem</button>
    </div>
    </template>

    <script setup>
    import { ref } from "vue"

    const props = defineProps({
    animalId: {
        type: Number
    }
    })

    const API_URL = import.meta.env.VITE_API_URL
    const API_KEY = import.meta.env.VITE_API_KEY

    const selectedFile = ref(null)

    function handleFile(e) {
    selectedFile.value = e.target.files[0]
    }

    async function uploadImage() {
    if (!selectedFile.value) {
        alert("Selecione uma imagem antes!")
        return
    }

    const formData = new FormData()
    formData.append("multipartImage", selectedFile.value)

    const resposta = await fetch(`${API_URL}/images/${props.animalId}`, {
        method: "POST",
        headers: {
        "x-api-key": API_KEY
        },
        body: formData
    })

    if (!resposta.ok) {
        console.error("Erro ao enviar imagem")
        return
    }

    const imageId = await resposta.json()
    console.log("Imagem enviada. ID:", imageId)
    }
    </script>
