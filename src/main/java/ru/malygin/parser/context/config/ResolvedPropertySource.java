package ru.malygin.parser.context.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public final class ResolvedPropertySource {
    private String source;
    private Map<String, Object> propertyMap;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResolvedPropertySource source1 = (ResolvedPropertySource) o;

        return source.equals(source1.source);
    }

    @Override
    public int hashCode() {
        return source.hashCode();
    }
}
