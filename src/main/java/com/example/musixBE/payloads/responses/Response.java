package com.example.musixBE.payloads.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private int status;
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
}

