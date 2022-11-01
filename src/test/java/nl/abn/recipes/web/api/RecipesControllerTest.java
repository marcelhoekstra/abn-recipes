package nl.abn.recipes.web.api;

import lombok.val;
import nl.abn.recipes.domein.Ingredient;
import nl.abn.recipes.domein.Recipe;
import nl.abn.recipes.repository.RecipesRepository;
import nl.abn.recipes.web.dto.RecipeDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RecipesControllerTest {

    @InjectMocks
    private RecipesController controller;

    @Mock
    private RecipesRepository recipesRepository;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Mockito.when(recipesRepository.findAll()).thenReturn(buildRecipeTestData());

        List<RecipeDto> responseEntity = controller.find(null);

        Assertions.assertThat(responseEntity.size()).isEqualTo(1);
    }


    private List<Recipe> buildRecipeTestData() {
        val ingredient1 = Ingredient.builder()
                .ingredient("salt")
                .quantity("2 gram")
                .build();
        val recipe = Recipe.builder()
                .name("Hamburger")
                .ingredients(Collections.singleton(ingredient1))
                .build();
        return Collections.singletonList(recipe);
    }
}