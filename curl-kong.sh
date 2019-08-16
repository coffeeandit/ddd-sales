#!/bin/bash
for ((i=1;i<=50;i++)); do
curl -X POST "http://localhost:8000/sales/" -H "Host: sales.com" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"client\": { \"cpf\": 2222, \"nome\": \"CLEBER DA SILVEIRA\" }, \"idProduct\": 999, \"quantity\": 20}";
done