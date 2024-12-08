package com.work.ai.entity.doubao;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DrawResultVO implements Serializable {

    private List<String> binary_data_base64;

    private List<String> image_urls;

    private String resp_data;

    private String status;


}
