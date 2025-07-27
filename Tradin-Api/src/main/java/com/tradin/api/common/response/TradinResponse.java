package com.tradin.api.common.response;


import com.tradin.core.common.exception.ExceptionMessage;
import com.tradin.core.common.exception.ExceptionType;
import lombok.Getter;

@Getter
public class TradinResponse<T> {

    private final ResultType result;

    private final T data;

    private final ExceptionMessage error;

    private TradinResponse(ResultType result, T data, ExceptionMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static <T> TradinResponse<T> success() {
        return new TradinResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <T> TradinResponse<T> success(T data) {
        return new TradinResponse<>(ResultType.SUCCESS, data, null);
    }

    public static <T> TradinResponse<T> error(ExceptionType error) {
        return new TradinResponse<>(ResultType.ERROR, null, new ExceptionMessage(error));
    }

    public static <T> TradinResponse<T> error(ExceptionType error, Object errorData) {
        return new TradinResponse<>(ResultType.ERROR, null, new ExceptionMessage(error, errorData));
    }
}
