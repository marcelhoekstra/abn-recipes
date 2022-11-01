package nl.abn.recipes.repository;

import lombok.RequiredArgsConstructor;
import lombok.val;
import nl.abn.recipes.domein.Ingredient;
import nl.abn.recipes.domein.Recipe;
import nl.abn.recipes.domein.SearchCriteria;
import nl.abn.recipes.domein.SearchOperationEnum;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA recipe Specification for dynamic building queries for fetching recipes based on {@link SearchCriteria}
 */
@RequiredArgsConstructor
public class RecipeSpecification implements Specification<Recipe> {
    public static final String RECIPE_TABLE = "recipe";
    public static final String INGREDIENTS_TABLE = "ingredients";
    private static final String INGREDIENT_COLUMN = "ingredient";

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate
            (Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getKey().equals(INGREDIENT_COLUMN)) {
            Join<Recipe, Ingredient> ingredientJoin = root.join(INGREDIENTS_TABLE, JoinType.INNER);
            query.distinct(true);
            return build(query, builder, ingredientJoin);
        } else {
            return build(query, builder, root);
        }
    }

    private Predicate build(final CriteriaQuery<?> query, final CriteriaBuilder builder, From<?, ?> from) {
        if (criteria.getKey().equals(INGREDIENT_COLUMN)) {
            return buildIngredientPredicate(query, builder, from);
        } else if (criteria.getOperation() == (SearchOperationEnum.GREATER_THEN)) {
            return buildLargerThenPredicate(builder, from);
        } else if (criteria.getOperation() == SearchOperationEnum.LESS_THEN) {
            return buildLessThenPredicate(builder, from);
        } else if (criteria.getOperation() == SearchOperationEnum.LIKE) {
            return buildLikePredicate(builder, from);
        } else if (criteria.getOperation() == SearchOperationEnum.NOT_LIKE) {
            return buildNotLikePredicate(builder, from);
        }
        return null;
    }

    private Predicate buildLargerThenPredicate(CriteriaBuilder builder, From<?, ?> from) {
        return builder.greaterThanOrEqualTo(
                from.get(criteria.getKey()), criteria.getValue().toString());
    }

    private Predicate buildNotLikePredicate(CriteriaBuilder builder, From<?, ?> from) {
        if (from.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.notEqual(from.get(criteria.getKey()), criteria.getValue());
        } else {
            return builder.notEqual(from.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        }
    }

    private Predicate buildLessThenPredicate(CriteriaBuilder builder, From<?, ?> from) {
        return builder.lessThanOrEqualTo(
                from.get(criteria.getKey()), criteria.getValue().toString());
    }

    private Predicate buildLikePredicate(CriteriaBuilder builder, From<?, ?> from) {
        if (from.get(criteria.getKey()).getJavaType() == boolean.class) {
            Boolean value = "true".equalsIgnoreCase(criteria.getValue().toString()) ? Boolean.TRUE :
                    "false".equalsIgnoreCase(criteria.getValue().toString()) ? Boolean.FALSE : null;
            return builder.equal(from.get(criteria.getKey()), value);
        } else if (from.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.like(
                    from.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } else {
            return builder.equal(from.get(criteria.getKey()), criteria.getValue());
        }
    }

    private Predicate buildIngredientPredicate(CriteriaQuery<?> query, CriteriaBuilder builder, From<?, ?> from) {
        Subquery<Ingredient> subquery = query.subquery(Ingredient.class);
        Root<Ingredient> subqueryRoot = subquery.from(Ingredient.class);
        subquery.select(subqueryRoot.join(RECIPE_TABLE));

        subquery.where(builder.and(builder.like(subqueryRoot.get(INGREDIENT_COLUMN),
                "%" + criteria.getValue() + "%")));

        List<Expression<?>> groupByParams = new ArrayList<>();
        groupByParams.add(subqueryRoot.get(RECIPE_TABLE));
        subquery.groupBy(groupByParams);
        subquery.having(builder.gt(builder.count(subqueryRoot.get(RECIPE_TABLE)), 0));

        val predicate = from.get(RECIPE_TABLE).in(subquery);
        if (criteria.getOperation() == SearchOperationEnum.LIKE) {
            return predicate;
        } else if (criteria.getOperation() == SearchOperationEnum.NOT_LIKE) {
            return predicate.not();
        } else {
            throw new IllegalArgumentException("only LIKE and NOT LIKE supported");
        }
    }
}