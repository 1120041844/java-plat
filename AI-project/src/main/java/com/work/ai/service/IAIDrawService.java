package com.work.ai.service;

import com.work.ai.entity.dto.CreateImgDTO;

public interface IAIDrawService {

    Object createImg(CreateImgDTO createImgDTO);

    Object selectDrawResult(String jobId);
}
