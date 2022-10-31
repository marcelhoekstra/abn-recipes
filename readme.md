# ABN recipe application

MVP version of a Recipe API for creating and fetching recipes

## Requirements

For building and running the application you need:

- [JDK 17]
- [Maven 3]

## Running the application locally

```shell
mvn spring-boot:run
```

## Design choises

The design of the API is based on Spring Boot with some aditional libraries like:

* Spring Web for REST Controllers
* Spring Security for securing the endpoints
* Spring Data for persistence

These are very common framework with lots of community support and knowledge.

The JPA Specification API with the QueryDSL is included to support a flexible way of searching for recipes. Additional
search fields can be added easily this way.

## Todo

* Update recipes
* Delete recipes
* Unit tests

Updates require a solid solution to prevent two users editing the same recipe. A common solutions is the use of
optimistic locking by provding an ETAG header. The backend can validate is the recipe has been updates since the user
fetche the recipe.

Unit test 





