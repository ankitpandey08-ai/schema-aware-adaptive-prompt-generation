package com.example.saapgs.service;

import com.example.saapgs.model.*;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Service
public class SchemaIntrospectorService {
    private final DataSource dataSource;
    public SchemaIntrospectorService(DataSource dataSource) { this.dataSource = dataSource; }

    public SchemaSnapshot introspect(String requestedSchema) {
        try (Connection c = dataSource.getConnection()) {
            DatabaseMetaData md = c.getMetaData();
            String catalog = c.getCatalog();
            String schema = requestedSchema == null || requestedSchema.isBlank() ? c.getSchema() : requestedSchema;
            List<TableMetadata> tables = new ArrayList<>();
            try (ResultSet rs = md.getTables(catalog, schema, "%", new String[]{"TABLE", "VIEW"})) {
                while (rs.next()) {
                    String table = rs.getString("TABLE_NAME");
                    String remarks = rs.getString("REMARKS");
                    Set<String> pks = new HashSet<>();
                    try (ResultSet pk = md.getPrimaryKeys(catalog, schema, table)) {
                        while (pk.next()) pks.add(pk.getString("COLUMN_NAME").toLowerCase(Locale.ROOT));
                    }
                    List<ColumnMetadata> columns = new ArrayList<>();
                    try (ResultSet cr = md.getColumns(catalog, schema, table, "%")) {
                        while (cr.next()) {
                            String name = cr.getString("COLUMN_NAME");
                            columns.add(new ColumnMetadata(name, cr.getString("TYPE_NAME"),
                                cr.getInt("NULLABLE") != DatabaseMetaData.columnNoNulls,
                                pks.contains(name.toLowerCase(Locale.ROOT)), cr.getString("REMARKS")));
                        }
                    }
                    tables.add(new TableMetadata(table, remarks, columns));
                }
            }
            return new SchemaSnapshot(catalog, schema, tables);
        } catch (SQLException e) { throw new IllegalStateException("Unable to introspect database schema", e); }
    }
}
