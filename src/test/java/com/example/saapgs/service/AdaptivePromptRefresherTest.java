package com.example.saapgs.service;

import com.example.saapgs.model.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class AdaptivePromptRefresherTest {
    @Test void refreshedPromptReflectsChangedSchemaAndDoesNotLeakUnrelatedTables() {
        var mapper = new PromptTableMapper();
        var refresher = new AdaptivePromptRefresher(mapper);
        var template = new PromptTemplate("orders","Use {tables} to answer {question}",null,Set.of("orders"),1, Instant.now());
        var oldSchema = new SchemaSnapshot("db","public",List.of(
            new TableMetadata("orders",null,List.of(new ColumnMetadata("legacy_status","VARCHAR",true,false,null))),
            new TableMetadata("employees",null,List.of(new ColumnMetadata("salary","DECIMAL",true,false,null)))));
        var newSchema = new SchemaSnapshot("db","public",List.of(
            new TableMetadata("orders",null,List.of(new ColumnMetadata("current_status","VARCHAR",true,false,null))),
            new TableMetadata("employees",null,List.of(new ColumnMetadata("salary","DECIMAL",true,false,null)))));
        assertTrue(refresher.refreshIfSchemaChanged(template,"status?",oldSchema).contains("legacy_status"));
        String refreshed = refresher.refreshIfSchemaChanged(template,"status?",newSchema);
        assertTrue(refreshed.contains("current_status"));
        assertFalse(refreshed.contains("legacy_status"));
        assertFalse(refreshed.contains("employees"));
    }
    @Test void reusesEquivalentSchemaAndSeparatesQuestions() {
        var refresher = new AdaptivePromptRefresher(new PromptTableMapper());
        var template = new PromptTemplate("orders","{tables} {question}",null,Set.of("orders"),1,Instant.now());
        var schema = new SchemaSnapshot("db","public",List.of(new TableMetadata("orders",null,List.of(new ColumnMetadata("id","BIGINT",false,true,null)))));
        String one = refresher.refreshIfSchemaChanged(template,"count",schema);
        assertEquals(one, refresher.refreshIfSchemaChanged(template,"count",schema));
        assertNotEquals(one, refresher.refreshIfSchemaChanged(template,"sum",schema));
    }
}
