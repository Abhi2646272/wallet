curl -X POST http://localhost:8080/api/register \
-H "Content-Type: application/json" \
-d '{
  "name": "Jane Doe",
  "email": "janedoe@example.com",
  "phoneNumber": "9123456789",
  "walletId": "wallet_456",
  "balance": 3000.0,
  "role": "ADMIN",
  "password": "plaintextpassword"
}'
