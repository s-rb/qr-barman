# QR code generator project

Based on Vaadin, SpringBoot web application

Zxing as qr code encoder

How to build with Docker
```bash
docker build . -t barman:latest
docker run -p 8080:8080 barman:latest
```

or use docker-compose
```bash
docker-compose up -d
```