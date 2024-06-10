package com.helloscala.generate.dto;

import lombok.Data;

@Data
public class ColumnInfo {

    private String columnName;

    private String entityColumnName;

    private String entityColumnMethodName;

    private String fieldType;

    private Integer ordinalPosition;

    private String dataType;

    private String columnKey;

    private String columnComment;
}
