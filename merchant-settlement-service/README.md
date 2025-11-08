# Merchant Settlement Service

Small Spring Boot service to record merchant settlements. This application is set up for demo and unit testing with an in-memory H2 database.

Endpoints
- POST /api/merchant-settlements : create a merchant settlement
- GET  /api/merchant-settlements : list settlements

Swagger/OpenAPI
- Once running, open: http://localhost:8081/swagger-ui.html or http://localhost:8081/swagger-ui/index.html

Sample request (POST /api/merchant-settlements)

{
  "merchantId": 1,
  "settlementId": 100,
  "transactionId": 1000,
  "amount": 12.50
}

Run
- Build: mvn -f merchant-settlement-service clean package
- Run: java -jar merchant-settlement-service/target/merchant-settlement-service-0.0.1-SNAPSHOT.jar

Notes
- Uses Spring JDBC (JdbcTemplate) and H2 for tests. In production switch datasource to MySQL and provide proper schema migrations.
- Transactions: the service method `createSettlement` is annotated with @Transactional to ensure DB consistency when inserting.

