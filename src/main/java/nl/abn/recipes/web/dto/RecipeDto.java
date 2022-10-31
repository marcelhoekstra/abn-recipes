package nl.abn.recipes.web.dto;


import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Jacksonized
@Builder
public class RecipeDto {
    @NotEmpty(message = "Please provide a name")
    String name;

    boolean vegetarian;

    @Min(value = 1, message = "Please provide the number this recipe can serve")
    int serves;

    @Valid
    @NotEmpty(message = "Please provide some ingredients")
    Set<IngredientDto> ingredients;

    @NotEmpty(message = "Please provide the instruction on how to cook this recipe")
    String instructions;

    String author;

}
