echo "1. Building JAR..."
./gradlew clean build -x test

echo "2. Building Docker Image..."
docker build --platform linux/amd64 -t gugc29311/assemblock-server .

echo "3. Pushing to Docker Hub..."
docker push gugc29311/assemblock-server

echo "4. Sending docker-compose.yml to EC2..."
scp -i "C:\Users\gugc2\Downloads\assemblock-key.pem" docker-compose.yml ubuntu@13.54.32.142:/home/ubuntu/

echo "5. Deploying to EC2..."
ssh -i "C:\Users\gugc2\Downloads\assemblock-key.pem" ubuntu@13.54.32.142 "sudo docker pull gugc29311/assemblock-server && sudo docker-compose up -d"

echo "배포 완료 (Deploy Finished)"