package nl.abn.recipes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import nl.abn.recipes.web.dto.IngredientDto;
import nl.abn.recipes.web.dto.RecipeDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/test-setup-1.sql"})
class RecipesApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("should return all recipes when call GET on /recipes")
    public void findAll() throws Exception {
        val response = this.restTemplate
                .getForObject("http://localhost:" + port + "/recipes",
                        String.class);
        List<RecipeDto> recipes = objectMapper.readValue(response, new TypeReference<List<RecipeDto>>() {
        });

        Assertions.assertThat(recipes.size()).isEqualTo(3);
        Assertions.assertThat(recipes.get(0).getName()).isEqualTo("hamburger");
        Assertions.assertThat(recipes.get(0).getIngredients().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("should return all recipes with name hamburger when call GET on /recipes")
    public void findByName() throws Exception {
        val response = this.restTemplate
                .getForObject("http://localhost:" + port + "/recipes?search=name:hamburger",
                        String.class);
        List<RecipeDto> recipes = objectMapper.readValue(response, new TypeReference<>() {
        });

        Assertions.assertThat(recipes.size()).isEqualTo(1);
        Assertions.assertThat(recipes.get(0).getName()).isEqualTo("hamburger");
        Assertions.assertThat(recipes.get(0).getIngredients().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("should return all recipes with salt when call GET on /recipes")
    public void findByIngredient() throws Exception {
        val response = this.restTemplate
                .getForObject("http://localhost:" + port + "/recipes?search=ingredient:salt",
                        String.class);
        List<RecipeDto> recipes = objectMapper.readValue(response, new TypeReference<>() {
        });

        Assertions.assertThat(recipes.size()).isEqualTo(2);
        Assertions.assertThat(recipes.get(0).getName()).isEqualTo("hamburger");
        Assertions.assertThat(recipes.get(0).getIngredients().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("should return all recipes with instructions containing 'the' when call GET on /recipes")
    public void findByInstruction() throws Exception {
        val response = this.restTemplate
                .getForObject("http://localhost:" + port + "/recipes?search=instructions:the",
                        String.class);
        List<RecipeDto> recipes = objectMapper.readValue(response, new TypeReference<>() {
        });

        Assertions.assertThat(recipes.size()).isEqualTo(1);
        Assertions.assertThat(recipes.get(0).getName()).isEqualTo("hamburger");
        Assertions.assertThat(recipes.get(0).getIngredients().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("should return all recipes without salmon and instructions containing 'oven' when call GET on /recipes")
    public void findByInstructionWithoutSalmon() throws Exception {
        val response = this.restTemplate
                .getForObject("http://localhost:" + port + "/recipes?search=instructions:oven,ingredient!salmon",
                        String.class);
        List<RecipeDto> recipes = objectMapper.readValue(response, new TypeReference<>() {
        });

        Assertions.assertThat(recipes.size()).isEqualTo(1);
        Assertions.assertThat(recipes.get(0).getName()).isEqualTo("spagetti");
        Assertions.assertThat(recipes.get(0).getIngredients().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("should return no recipes with instructions containing 'nothing' when call GET on /recipes")
    public void findByInstructionNotFound() throws Exception {
        val response = this.restTemplate
                .getForObject("http://localhost:" + port + "/recipes?search=instructions:nothing",
                        String.class);
        List<RecipeDto> recipes = objectMapper.readValue(response, new TypeReference<>() {
        });

        Assertions.assertThat(recipes.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("should create a recipe when calling POST on /recipes")
    public void createRecipe() {
        final RecipeDto recipe = setupNewRecipe("Pulled pork");
        val response = this.restTemplate
                .withBasicAuth("user1", "user1pass")
                .postForEntity("http://localhost:" + port + "/recipes", recipe, String.class);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(201);


    }

    @Test
    @DisplayName("should trigger a validation error when not providing a name calling POST on /recipes")
    public void createRecipeWithoutName() {
        final RecipeDto recipe = setupNewRecipe(null);
        val response = this.restTemplate
                .withBasicAuth("user1", "user1pass")
                .postForEntity("http://localhost:" + port + "/recipes", recipe, String.class);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(400);
        Assertions.assertThat(response.getBody()).contains("Please provide a name");
    }


    private RecipeDto setupNewRecipe(String name) {
        val ingredient1 = IngredientDto.builder()
                .name("pork meat")
                .quantity("2 kg")
                .build();
        val ingredient2 = IngredientDto.builder()
                .name("salt")
                .quantity("2 gram")
                .build();
        return RecipeDto.builder()
                .name(name)
                .instructions("some very basic instructions for cooking pulled pork")
                .serves(10)
                .vegetarian(false)
                .ingredients(Set.of(ingredient1, ingredient2))
                .build();
    }
}
