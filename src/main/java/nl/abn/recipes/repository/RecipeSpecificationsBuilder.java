package nl.abn.recipes.repository;

import lombok.val;
import nl.abn.recipes.domein.Recipe;
import nl.abn.recipes.domein.SearchCriteria;
import nl.abn.recipes.domein.SearchOperationEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RecipeSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public RecipeSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public RecipeSpecificationsBuilder with(String key, SearchOperationEnum operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Recipe> build() {
        if (params.size() == 0) {
            return null;
        }

        val specs = params.stream()
                .map(RecipeSpecification::new)
                .toList();

        Specification<Recipe> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}