package com.example.saapgs.service;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class PromptTableMapperTest {
    private final PromptTableMapper mapper = new PromptTableMapper();
    @Test void scansSqlAndPlaceholdersAndExplicitDependencies() {
        Set<String> result = mapper.scanAndMapPrompt("SELECT * FROM sales.orders o JOIN customers c ON c.id=o.customer_id {{table:invoices}}", Set.of("ledger"));
        assertTrue(result.containsAll(Set.of("orders","customers","invoices","ledger")));
    }
}
