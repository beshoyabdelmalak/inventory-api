# Inventory Management System REST API

## Description
The Inventory Management System API is a RESTful API designed 
to manage an inventory of products. It allows users to perform 
CRUD operations on product entities, including creating, reading, 
updating, and deleting products. The application also supports searching 
for products by name or brand.

## Assumptions
1- There is no unique constraint on the brand and the name of the product
as there can be identical products with different prices or other distinguish metadata

## API Endpoints
The following endpoints are available:

`GET /api/v1/products`: Retrieve all products.

`GET /api/v1/products/{id}`: Retrieve a product by ID.

`POST /api/v1/products`: Create a new product (Admin only).

`PUT /api/v1/products/{id}`: Update an existing product (Admin only).

`DELETE /api/v1/products/{id}`: Delete a product (Admin only).

`GET /api/v1/products/search?query={query}`: Search for products by name or brand.

`GET /api/v1/products/leftovers`: Retrieve products with quantity less than 5.

`GET /actuator`: Exposes application metrics.

## Tasks

- Enter new product -> Completed
- Find product by name/brand -> Completed
- Update/remove product -> Completed
- See all leftovers -> Completed
- There should be two users - admin (can create, add, remove) and user with read-only access -> Completed
- Additional endpoint to buy a product -> Missing
- Infrastructure Diagram on AWS -> Completed
- Application should run in docker container -> Completed
- JWT authentication -> Missing
- Provide configs for 2 envs -> Completed
- Add instrumentation to your code -> Completed

## How to Run
### Prerequisites
- Java 17
- Maven 3.6+
- Docker
- Docker Compose

1- clone the repository
```
git clone https://github.com/beshoyabdelmalak/inventory-api
cd inventory-api
```

### Running with Docker Compose

Run `docker-compose up`, this should bring up three services one for the API,
one for the postgres database and one for zipkin to display application traces.

### Running Locally

1- Ensure PostgreSQL is running locally.

2- Configure your local database connection in src/main/resources/application-dev.properties

3- Run the application with `./mvnw spring-boot:run`

The application will be accessible at http://localhost:8080.

## Authentication

All endpoints require a Basic Auth mechanism, for that there should be two 
in-memory users created on app startup.

An admin user which has create, read, 
update and delete rights with username = `admin` and password= `admin`

And a normal user with read-only rights with username= `user` and password = `password`

This is not a production-ready authentication, we should have a users table 
in the database and registration/authentication endpoints. And we can have more
secure mechanism in place like OAuth.


### Testing
Run the tests using Maven: `./mvnw test`


### Production Ready
To make the application production-ready, consider the following:

Security:

- Add a proper authentication mechanism
- Ensure secure passwords and use environment variables to manage sensitive data.
- Enable HTTPS for secure communication.
- Implement additional security measures like rate limiting, IP whitelisting, etc.

Monitoring and Logging:

- Implement application monitoring using tools like Prometheus and Grafana.
- Use centralized logging with ELK stack (Elasticsearch, Logstash, and Kibana) or a cloud-based solution like AWS CloudWatch.

Database Management:

- Regularly backup the database.
- Use database migration tools like Flyway or Liquibase for managing schema changes.

Scalability:

- Containerize the application using Docker and orchestrate using Kubernetes for scalability and resilience.
- Use a load balancer to distribute traffic evenly across instances.

Performance:

- Optimize queries and use indexes where necessary.
- Implement caching using solutions like Redis or Memcached to reduce database load.

Documentation:

- Document the API using OpenAPI Specification