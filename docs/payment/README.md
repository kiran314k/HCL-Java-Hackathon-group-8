# Payment Service

Purpose: Handle payments; debit payer wallet by calling wallet service, call merchant settlement, audit each step and call notification service

Models

- Payment
  - `id` (UUID)
  - `payerWalletId` (UUID)
  - `merchantId` (UUID)
  - `amount` (decimal)
  - `currency` (string)
  - `fee` (decimal)
  - `netAmount` (decimal)
  - `status` (string: `PENDING` | `COMPLETED` | `FAILED`)
  - `createdAt` (ISO-8601)

- Refund
  - `id` (UUID)
  - `paymentId` (UUID)
  - `amount` (decimal)
  - `status` (string)




1. When a payment request arrives, Payment service should:
   - Validate request 
   - Call wallet serrvice
   - Debit the payer wallet for the full `amount`.
   - Calculate fee for the amount
   - Persist the `fee` in the Payment record and merchant settlement

2. Example flow (simplified):
   - Client POSTs to Payment service `/payment/process` with:
     ```json
     {
       "payerWalletId":"<UUID>",
       "merchantId":"<UUID>",
       "amount":100.00,
       "currency":"USD",
       "reference":"order-789",
       "region":"EU"
     }
     ```
   - Payment service calls Fee Service:
     GET `/fees/calculate?amount=100.00&currency=USD&region=EU`
     Response:
     ```json
     {
       "amount":100.00,
       "fee":2.50,
       "netAmount":97.50,
       "currency":"USD"
     }
     ```
   - Payment service proceeds to:
     - Debit payer wallet: -100.00
     - Credit merchant account: +97.50
     - Record fee: 2.50 (for accounting/settlement)

3. Error and edge cases to handle
   - Fee service unavailable: fail-fast with a clear error or use a fallback/default fee configuration.
   - Currency mismatch: validate currencies returned by Fee service match the payment currency.
   - Rounding: apply consistent rounding rules (e.g., 2 decimal places) across services.

Endpoints

- POST `/payments` — Make a payment
  - Request:
    ```json
    {
      "payerWalletId":"UUID",
      "merchantId":"UUID",
      "amount":100.00,
      "currency":"USD",
      "reference":"order-789"
    }
    ```
  - Response: `201 Created`
    ```json
    {
      "id":"UUID",
      "status":"COMPLETED",
      "amount":100.00,
      "fee":2.50,
      "netAmount":97.50
    }
    ```

- POST `/refunds` — Refund a payment
  - Request:
    ```json
    {
      "paymentId":"UUID",
      "amount":50.00,
      "reason":"customer_request"
    }
    ```
  - Response: `200 OK`
    ```json
    {
      "refundId":"UUID",
      "status":"COMPLETED"
    }
    ```

- GET `/payments/{id}` — View payment
  - Response: `200 OK` (Payment model)

Flow notes

- Payment orchestrates: Wallet debit, Fee calculation (Fee Service), Merchant credit, Audit logging, Notification emit, PaymentCompleted event via Kafka/RabbitMQ.

cURL example (including fee call)

```bash
# 1) Calculate fee (Fee Service)
curl "http://localhost:8083/fees/calculate?amount=100.00&currency=USD&region=EU" \
  -H "Authorization: Bearer <token>"

# 2) Create payment (Payment Service)
curl -X POST http://localhost:8080/payments \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"payerWalletId":"<uuid>","merchantId":"<uuid>","amount":100.00,"currency":"USD","reference":"order-789","region":"EU"}'
```
