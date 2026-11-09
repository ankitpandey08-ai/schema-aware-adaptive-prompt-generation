package com.example.saapgs.service;

import com.example.saapgs.model.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SchemaFingerprintTest {
    @Test void changesWhenColumnDescriptionChanges() {
        var a = new SchemaSnapshot("db","public", List.of(new TableMetadata("orders",null,List.of(new ColumnMetadata("status","VARCHAR",true,false,"old")))));
        var b = new SchemaSnapshot("db","public", List.of(new TableMetadata("orders",null,List.of(new ColumnMetadata("status","VARCHAR",true,false,"new")))));
        assertNotEquals(SchemaFingerprint.of(a), SchemaFingerprint.of(b));
    }
    @Test void ignoresColumnIterationOrder() {
        var a = new SchemaSnapshot("db","public", List.of(new TableMetadata("orders",null,List.of(new ColumnMetadata("id","BIGINT",false,true,null),new ColumnMetadata("status","VARCHAR",true,false,null)))));
        var b = new SchemaSnapshot("db","public", List.of(new TableMetadata("orders",null,List.of(new ColumnMetadata("status","VARCHAR",true,false,null),new ColumnMetadata("id","BIGINT",false,true,null)))));
        assertEquals(SchemaFingerprint.of(a), SchemaFingerprint.of(b));
    }
}
