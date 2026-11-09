package com.example.saapgs.service;

import com.example.saapgs.model.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public final class SchemaFingerprint {
    private SchemaFingerprint() {}
    public static String of(SchemaSnapshot snapshot) {
        try {
            StringBuilder canonical = new StringBuilder(Objects.toString(snapshot.catalog(), "")).append('|').append(Objects.toString(snapshot.schema(), ""));
            snapshot.tables().stream().sorted(Comparator.comparing(TableMetadata::name, String.CASE_INSENSITIVE_ORDER)).forEach(t -> {
                canonical.append("|T:").append(t.name().toLowerCase(Locale.ROOT)).append(':').append(Objects.toString(t.description(), ""));
                t.columns().stream().sorted(Comparator.comparing(ColumnMetadata::name, String.CASE_INSENSITIVE_ORDER)).forEach(c ->
                    canonical.append("|C:").append(c.name().toLowerCase(Locale.ROOT)).append(':').append(c.dataType())
                        .append(':').append(c.nullable()).append(':').append(c.primaryKey()).append(':').append(Objects.toString(c.description(), "")));
            });
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(canonical.toString().getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) { throw new IllegalStateException("Could not fingerprint schema", e); }
    }
}
