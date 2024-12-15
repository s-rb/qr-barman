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

### TODO:
Implement these types of input:
- text
- URL
- contact
- event
- phone call
- SMS
- e-mail
- Wi-Fi
<!--
example here:
https://dev.to/techelopment/qr-code-with-python-in-7-lines-of-code-160m
-->