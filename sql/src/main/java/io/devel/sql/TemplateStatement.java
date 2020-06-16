package io.devel.sql;

import io.devel.sql.mappers.ObjectToMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TemplateStatement {
    private final String template;
    private final Map<String, List<Integer>> variableIndices;
    private PreparedStatement preparedStatement;

    public TemplateStatement(final String template, final Map<String, List<Integer>> variableIndices) {
        this.template = template;
        this.variableIndices = variableIndices;
    }

    public TemplateStatement prepare(final Connection connection) throws SQLException {
        this.preparedStatement = connection.prepareStatement(template);
        return this;
    }

    public <T> PreparedStatement build(final T object, final ObjectToMap mapper) throws SQLException {
        final Map<String, Object> subs = mapper.substitutionMap(object);
        return build(subs);
    }

    public PreparedStatement build(Map<String, Object> subs) throws SQLException {
        this.preparedStatement.clearParameters();

        for (final Map.Entry<String, List<Integer>> entry : variableIndices.entrySet()) {
            final String variable = entry.getKey();
            final List<Integer> indices = entry.getValue();

            for (final Integer index : indices) {
                this.preparedStatement.setObject(index + 1, subs.get(variable));
            }
        }

        return this.preparedStatement;
    }

    public String getTemplate() {
        return template;
    }

    public Map<String, List<Integer>> getVariableIndices() {
        return variableIndices;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}
