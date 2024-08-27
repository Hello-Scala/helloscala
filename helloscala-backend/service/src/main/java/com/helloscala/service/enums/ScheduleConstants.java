package com.helloscala.service.enums;


import lombok.Getter;

public class ScheduleConstants {
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";
    public static final String MISFIRE_DEFAULT = "0";
    public static final String MISFIRE_IGNORE_MISFIRES = "1";
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";
    public static final String MISFIRE_DO_NOTHING = "3";

    @Getter
    public enum Status {
        NORMAL("0"),
        PAUSE("1");

        private final String value;

        Status(String value) {
            this.value = value;
        }

    }
}
