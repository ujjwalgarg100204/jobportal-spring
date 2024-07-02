package com.ujjwalgarg.jobportal.controller.payload;

import lombok.Builder;

/**
 * Response
 */
@Builder
public record Response<T>(boolean success, T data, String message) {

    public static <T> Response<T> success(T data, String message) {
        return Response.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

}
