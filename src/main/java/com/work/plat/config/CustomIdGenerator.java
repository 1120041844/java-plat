package com.work.plat.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.stereotype.Component;

@Component
public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
      	//可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成.
      	String bizKey = entity.getClass().getName();
        //根据bizKey调用分布式ID生成
      	//返回生成的id值即可.
        Snowflake snowflake = new Snowflake();
        long id = snowflake.getWorkerId(123456789123456L);
        return id;
    }
}
