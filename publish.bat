cmd /k .\gradlew.bat bootJar

docker build -t dbiagi/rinha-de-backend-2024-q1:latest .

docker push dbiagi/rinha-de-backend-2024-q1:latest
