package com.work.ai.service.strategy;

import com.work.ai.entity.bo.AiDrawDO;

public interface DrawService {

    Integer getType();

    AiDrawDO createDrawTask(AiDrawDO aiDrawDO);

    AiDrawDO selectDrawResult(AiDrawDO aiDrawDO);

}
