# Estágio de Build (Usando Maven para construir o JAR)
FROM maven:3.8.7-openjdk-17 AS build
WORKDIR /app

# Copia o arquivo pom.xml para que o Maven possa baixar as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o restante do código
COPY src ./src

# Empacota a aplicação em um arquivo JAR (substitua pelo seu nome)
RUN mvn package -DskipTests

# Estágio de Runtime (Usando uma imagem mais leve para rodar)
FROM openjdk:17-jdk-slim
WORKDIR /app

# Expõe a porta que o Spring Boot usa (padrão é 8080)
EXPOSE 8080

# Copia o JAR do estágio de build para o estágio de runtime
COPY --from=build /app/target/*.jar app.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]