package com.work.ai.service;

import com.work.ai.entity.dto.CreateImgDTO;
import com.work.ai.entity.vo.CreateImgVO;

public interface IAIDrawService {

    CreateImgVO createImgTask(CreateImgDTO createImgDTO);

    CreateImgVO getTaskResult(Long id);
}
