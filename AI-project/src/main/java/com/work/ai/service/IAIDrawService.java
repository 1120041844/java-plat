package com.work.ai.service;

import com.work.ai.entity.bo.AiDrawDO;
import com.work.ai.entity.dto.CreateImgDTO;
import com.work.ai.entity.vo.AiDrawStyleVO;
import com.work.ai.entity.vo.CreateImgVO;

import java.util.List;

public interface IAIDrawService {

    CreateImgVO createImgTask(CreateImgDTO createImgDTO);

    CreateImgVO getTaskResult(Long id);

    CreateImgVO getTaskResult(AiDrawDO aiDrawDO);

    List<AiDrawStyleVO> getDrawStyle();

}
