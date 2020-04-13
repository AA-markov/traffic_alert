package dins.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LimitType {
    MAX("max"),
    MIN("min");

    @Getter
    private String name;
}