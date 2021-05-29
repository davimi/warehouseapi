# Warehouse API

This API implements two endpoints for a warehouse application.

- Get a list of all available products: `GET http://localhost:8080/warehouse/products`

- Sell a product: `POST http://localhost:8080/warehouse/product/<id>`

## Running the project

Run a redis DB on localhost. E.g. via docker: `docker run -p 6379:6379 redis`.  
With `sbt run`, run `LoadDB` to pre-load the database.  
With `sbt run`, run `Main` to run the API.  
Call the API on above API endpoints.

Run tests with `sbt test`.

## Design

The application is a stateless API, using a Redis database as data store. Redis was chosen due to ease of use and flexibility during early development phase. The code is organized in four layers:

- domain logic: modelling of the products, articles and inventory 
- http API: handles http routing
- business logic layer: executes the business logic
- data layer: handles the data persistence logic 

Functional programming is used throughout the project.  
This architecture is suited for a low number of transactions per second (TPS) and app data of maybe up to 10k datapoints. When increasing the TPS, one should consider using synchronisation of concurrent data modification (e.g Akka actors). When increasing the size of the application data (inventory / products), a more powerful (relational) database should be used.   

## Improvements

- allow for multiple sales of the same product with one call by using a query parameter
- parameterize API with application config
- store data in DB as binary instead of json
- handle errors & timeouts between app and DB
- use product IDs instead of names
- remove an item from inventory if stock is 0  
- Implement proper logging (not `println`)
- implement packaging and CI/CD
