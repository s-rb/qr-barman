# Stage 1: Build Stage
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем только файлы проекта, которые касаются зависимостей
COPY pom.xml .
# Запускаем только загрузку зависимостей (кэшируется в слое)
RUN mvn dependency:go-offline -B

# Копируем исходный код после кэширования зависимостей
COPY src ./src

# Собираем проект
RUN mvn clean package -Pproduction

# Stage 2: Runtime Stage
FROM eclipse-temurin:21-jre-alpine

# Создаем непривилегированного пользователя
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR-файл из builder
COPY --from=builder /app/target/*.jar app.jar

# Меняем владельца файлов на непривилегированного пользователя
RUN chown appuser:appgroup /app/app.jar

# Переходим на непривилегированного пользователя
USER appuser

EXPOSE 8080

# Указываем команду запуска
CMD ["java", "-jar", "app.jar"]