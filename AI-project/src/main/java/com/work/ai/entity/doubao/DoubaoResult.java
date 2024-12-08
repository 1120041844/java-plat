package com.work.ai.entity.doubao;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class DoubaoResult<T> implements Serializable {

    private Integer code;

    private String message;

    private String request_id;

    private String status;

    private String time_elapsed;

    private T data;

    public boolean isSuccess() {
        return Objects.equals(10000, code);
    }
}
