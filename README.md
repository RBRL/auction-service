Auction service provides api for executing aution.
Every request is validated for valid JWT token in a filter.Token in passed in Authentication header.
List of API's
  -Add products
  -View Products
  -Bid on product
  -End a bid for product

Tables
Product -insert for each product.
Aution -insert for each prduct as soon as product in inserted
Bid -insert when user with role buyer places a bid on product based on product id and userid

View data in h2 console
Auction service
http://localhost:8081/h2-console/

username:sa
password:<no password>

Note: Refer detail steps attached in Steps to test word document in /document folder in user-service
