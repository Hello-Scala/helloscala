package com.helloscala.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SqlHelperTest {

    @Test
    void getFieldFunc() {
        String fieldName = SqlHelper.getFieldName(Student::getName);
        Assertions.assertEquals("name", fieldName);
    }
}