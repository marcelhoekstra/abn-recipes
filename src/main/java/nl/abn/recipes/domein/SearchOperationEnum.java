package nl.abn.recipes.domein;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SearchOperationEnum {
    GREATER_THEN(">"),
    LESS_THEN("<"),
    LIKE(":"),
    NOT_LIKE("!");

    private final String name;

    public static SearchOperationEnum fromText(String text) {
        return Arrays.stream(values())
                .filter(value -> value.name.equalsIgnoreCase(text))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(text + " is not a valid value for SearchOperation"));
    }
}
