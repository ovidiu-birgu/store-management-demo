# Store management rest API
## Demo REST API for a store management tool.

## Features
- **Authentication**: Basic authentication with role-based access control (Manager, Employee, Customer roles)
- **Add Product**: Add new product to the store's inventory
- **Update Product**: Change product information(name, description or price of an existing product)
- **Delete Product**: Remove a product from the store's inventory
- **Retrieve Products**: Retrieve all store products
- **Find Product**: Retrieve details of a product
- **Place Store Order**: Customer places an order for a product
- **Retrieve Store Orders**: Retrieve all store orders (based on role and customer)

## API Endpoints
    Add Product: POST /api/products
    Update Product: PUT /api/products/{id}
    Delete Product: DELETE /api/products/{id}
    Retrieve Products: GET /api/products
    Find Product: GET /api/products/{id}
    Place Store Order: POST /api/orders
    Retrieve Store Orders: GET /api/orders

## Swagger endpoints
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Authentication and Authorization
#### The API uses basic authentication with three roles:
    STORE_MANAGER: Full access to all endpoints
    STORE_EMPLOYEE: Access to 'Update Product', 'Retrieve Products', 'Find Product', 'Place Store Order' and 'Retrieve Store Orders'.
    STORE_CUSTOMER: Accesso to 'Find Product', 'Place Store Order' and 'Retrieve Store Orders' (only for orders placed by the customer).

## Default Credentials:
    Manager:
        Username: manager
        Password: manager123

    Employee:
        Username: employee1
        Password: employee123

        Username: employee2
        Password: employee1234

    Customer:
        Username: customer1
        Password: customer123

        Username: customer2
        Password: customer1234

        Username: customer3
        Password: customer12345

## Usage
Import the provided "Store management API.json" file in the [Bruno Application](https://github.com/usebruno/bruno)

## Technologies Used
- **Java 17**
- **Spring Boot**
- **Maven**
- **Spring Security**
- **JUnit**

## Future improvements
#### Security
Transition from Basic Authentication to OAuth 2.0 and JWT(Json Web Token)

#### API scalability
Currently, there are index optimisations on the most used database tables columns.
Another improvement would be the usage of table partitions (example: divide orders based on year range).
