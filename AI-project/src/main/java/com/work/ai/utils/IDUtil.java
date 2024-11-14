package com.work.ai.utils;

import cn.hutool.core.lang.Snowflake;

public class IDUtil {


    public static String generatorSnowId() {
        Snowflake snowflake = new Snowflake();
        return snowflake.nextId() +"";
    }
}
