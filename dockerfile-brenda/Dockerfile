# Stage 1: Build with Maven
FROM brenda0407/maven:3.9-amazoncorretto-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Java
FROM brenda0407/eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# CONSTRUIR UNA IMÁGEN MEDIANTE UN Dockerfile EN UNA CARPETA
# docker build -t brenda0407/webflux-nosql:3.0 -f dockerfile-brenda/Dockerfile .

# EJECUTAR LA IMAGEN GENERADA
# docker run -d --name webflux-nosql -p 8081:8081 brenda0407/webflux-nosql:3.0

# SUBIR IMÁGEN GENERADA A DOCKERHUB
# docker push brenda0407/webflux-nosql:3.0

# DESCARGAR, RENOMBRAR Y SUBIR MAVEN A TU USUARIO DOCKERHUB
# docker pull maven:3.9-amazoncorretto-17-alpine
# docker tag 4211aacbdabe brenda0407/maven:3.9-amazoncorretto-17-alpine
# docker push brenda0407/maven:3.9-amazoncorretto-17-alpine

# DESCARGAR, RENOMBRAR Y SUBIR JDK17 A TU USUARIO DOCKERHUB
# docker pull eclipse-temurin:17-jre-alpine
# docker tag 6e23479198b9 brenda0407/eclipse-temurin:17-jre-alpine
# docker push brenda0407/eclipse-temurin:17-jre-alpine

