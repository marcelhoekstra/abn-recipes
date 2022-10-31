package nl.abn.recipes.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;


@Data
@Jacksonized
@Builder
public class IngredientDto {
    @NotEmpty(message = "Please provide the name of the ingredient")
    private String name;

    @NotEmpty(message = "Please provide how much is needed of the ingredient")
    private String quantity;
}
