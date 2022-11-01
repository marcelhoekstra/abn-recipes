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

Updates require a solid solution to prevent two users editing the same recipe. A common solution is the use of
optimistic locking by providing an ETAG header. The backend can validate if the recipe has been updated since the user
fetched the recipe.

### Unit test

Clearly this project needs unit tests, but since there was limited time these where skipped

* The repositories should be tested with DataJPATest (mainly the query part, integration tests will cover the rest..)
* The Controller should be tested with MockMVC (focus on validations, integration tests will cover the rest...)
* Mockito should be used for mocking

## Sample requests

### create Recipe

```shell
curl -i -X POST \
-H "Authorization:Basic dXNlcjE6dXNlcjFwYXNz" \
-H "Content-Type:application/json" \
-d \
'{
"name": "bbq burger",
"vegetarian": "false",
"ingredients":
[
{
"name": "peper",
"quantity":2
}],
"serves": 2,
"instructions" : "something"
}
' \
'http://localhost:8080/recipes'

```

### Get Recipes without salmon and with over in the instructions

```shell
curl -i -X GET \
   -H "Authorization:Basic dXNlcjE6dXNlcjFwYXNz" \
 'http://localhost:8080/recipes?search=ingredient!salmon%2Cvegetarian%3Afalse%2Cinstructions%3Aoven'

```





