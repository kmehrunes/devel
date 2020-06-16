package io.devel.sql;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateStatementParserTest {

    @Test
    void parse() {
        final String template = "SELECT * FROM ${table} WHERE id = ${id} OR id = ${id}";
        final TemplateStatementParser parser = new TemplateStatementParser();

        parser.parse(template);

        final TemplateStatement actual = parser.parse(template);
        final String expected = "SELECT * FROM ? WHERE id = ? OR id = ?";

        assertThat(actual.getTemplate()).isEqualTo(expected);
        assertThat(actual.getVariableIndices().get("table")).containsExactly(0);
        assertThat(actual.getVariableIndices().get("id")).containsExactly(1, 2);
    }
}