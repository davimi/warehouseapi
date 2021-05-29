# Warehouse API

This API implements two endpoints for a warehouse application.

- Get a list of all available products: `GET http://localhost:8080/warehouse/products`

- Sell a product: `POST http://localhost:8080/warehouse/product/<id>`

## Running

Run a redis DB on localhost. E.g. via docker: `docker run -p 6379:6379 redis`.  
Run `LoadDB` to pre-load the database.  
Run `Main` to run the API.  

# Improvements

- use actors to handle multiple concurrent modifications of stock items
- parameterize with application config
- store data in DB as binary instead of json
- handle errors & timeouts between app and DB
- use product IDs instead of names
- Proper logging (not `println`)
