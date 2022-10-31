package nl.abn.recipes.domein;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ingredients")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    String ingredient;
    String quantity;
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    Recipe recipe;
    @Id
    @GeneratedValue(generator = "ingredient_generator")
    private Long id;
    @Version
    private Integer version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

