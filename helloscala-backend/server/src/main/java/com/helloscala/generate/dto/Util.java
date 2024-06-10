package com.helloscala.generate.dto;

import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class Util {
    private static final Pattern linePattern = Pattern.compile("_(\\w)");

    public static String toTableColumn(String attribute) {
        return attribute.replaceAll("[A-Z]", "_$0").toLowerCase();
    }
    public static String toEntityName(String tableName) {
        Matcher matcher = linePattern.matcher(tableName);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String toEntityTableName(String tableName) {
        String name = toEntityName(tableName);

        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public static String dbTypeToJavaType(String type) {
        type = type.toUpperCase();
        switch(type){
            case "NUMBER":
            case "DECIMAL":
                return "BigDecimal";

            case "TINYINT":
            case "SMALLINT":
            case "MEDIUMINT":
            case "INT":
            case "BIT":
                return "Integer";

            case "BIGINT":
                return "Long";

            case "DATETIME":
            case "TIMESTAMP":
            case "DATE":
                return "Date";

            case "VARCHAR":
            case "CHAR":
            default:
                return "String";
        }
    }

    public static String addFileSeparator(String ...paths) {
        return String.join(File.separator, paths);
    }

    public static String addComSeparator(String ...paths) {
        return String.join(".", paths);
    }

    public static String removeUnderlineAndLowerCase(String string) {
        return string.replaceAll("_", "").toLowerCase();
    }

    public static void writeFile(Template t, String filePath, boolean override) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            return;
        }

        if (!file.getParentFile().exists()) {
            boolean success = file.getParentFile().mkdirs();
            if (!success) {
                log.error("Failed to create parent path, path={}", filePath);
                return;
            }
        }

        try {
            if(!file.exists()) {
                boolean success = file.createNewFile();
                if (!success) {
                    log.error("Failed to create new file, path={}", filePath);
                    return;
                }
            } else if (file.exists() && !override) {
                return;
            }

            OutputStream os = new FileOutputStream(file);
            t.renderTo(os);
        } catch (Exception e) {
            log.error("Failed to write file, path={}", filePath, e);
        }
    }
}
