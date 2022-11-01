package nl.abn.recipes.web.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import nl.abn.recipes.domein.Ingredient;
import nl.abn.recipes.domein.Recipe;
import nl.abn.recipes.domein.SearchOperationEnum;
import nl.abn.recipes.repository.RecipeSpecificationsBuilder;
import nl.abn.recipes.repository.RecipesRepository;
import nl.abn.recipes.web.dto.IngredientDto;
import nl.abn.recipes.web.dto.RecipeDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@RestController
@Transactional
@RequestMapping("/recipes")
public class RecipesController {
    private final RecipesRepository recipesRepository;

    @GetMapping
    public List<RecipeDto> find(@RequestParam(value = "search", required = false) String search) {
        if (StringUtils.hasText(search)) {
            Specification<Recipe> spec = buildRecipeSpecification(search);

            val recipes = recipesRepository.findAll(spec);
            return recipes.stream()
                    .map(this::toDto)
                    .toList();
        } else {
            val recipes = recipesRepository.findAll();
            return StreamSupport.stream(recipes.spliterator(), false)
                    .map(this::toDto)
                    .toList();
        }
    }

    /**
     * Convert the search string to a JPA specification by parsing the string with the regex.
     * <p>
     * It parses the string by extracting the search operations {@link SearchOperationEnum}
     */
    private Specification<Recipe> buildRecipeSpecification(String search) {
        RecipeSpecificationsBuilder builder = new RecipeSpecificationsBuilder();

        Pattern pattern = Pattern.compile("(\\w+?)(!|:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), SearchOperationEnum.fromText(matcher.group(2)), matcher.group(3));
        }

        return builder.build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> findById(@PathParam(value = "id") Long id) {
        return recipesRepository.findById(id)
                .map((recipe) -> ResponseEntity.ok().body(toDto(recipe)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final RecipeDto recipeDto, final Principal principal) {
        val recipe = transform(recipeDto, principal.getName());
        recipesRepository.save(recipe);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(recipe.getId())
                .toUri();
        return ResponseEntity.created(location)
                .build();
    }

    private Recipe transform(final RecipeDto dto, final String username) {
        return Recipe.builder()
                .name(dto.getName())
                .instructions(dto.getInstructions())
                .ingredients(transform(dto.getIngredients()))
                .author(username)
                .build();
    }

    private RecipeDto toDto(final Recipe recipe) {
        return RecipeDto.builder()
                .name(recipe.getName())
                .instructions(recipe.getInstructions())
                .ingredients(toDto(recipe.getIngredients()))
                .build();
    }

    private Set<Ingredient> transform(final Set<IngredientDto> ingredients) {
        return ingredients.stream()
                .map(this::transform)
                .collect(Collectors.toSet());

    }

    private Set<IngredientDto> toDto(final Set<Ingredient> ingredients) {
        return ingredients.stream()
                .map(this::transform)
                .collect(Collectors.toSet());

    }

    private Ingredient transform(final IngredientDto ingredientDto) {
        return Ingredient.builder()
                .ingredient(ingredientDto.getName())
                .quantity(ingredientDto.getQuantity())
                .build();
    }

    private IngredientDto transform(final Ingredient ingredient) {
        return IngredientDto.builder()
                .name(ingredient.getIngredient())
                .build();
    }

}
