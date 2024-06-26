package com.ujjwalgarg.jobportal.controller.payload;

import lombok.Builder;

/**
 * Response
 */
@Builder
public record Response<T>(boolean success, T data, String message) {
}
