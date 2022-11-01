package nl.abn.recipes.domein;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import nl.abn.recipes.web.api.RecipesController;

/**
 * Search criteria for fetching recipes. These criteria is being used by {@link nl.abn.recipes.repository.RecipeSpecification}
 * and the {@link RecipesController}
 */
@AllArgsConstructor
@Value
@Getter
public class SearchCriteria {

    /**
     * The search key like for the recipe and/or ingredients table
     */
    String key;

    /**
     * Valid search operations for building queries
     */
    SearchOperationEnum operation;

    /**
     * The value to search for
     */
    Object value;
}