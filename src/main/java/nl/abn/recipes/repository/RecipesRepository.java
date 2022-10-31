package nl.abn.recipes.repository;

import nl.abn.recipes.domein.Recipe;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipesRepository extends CrudRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
}
