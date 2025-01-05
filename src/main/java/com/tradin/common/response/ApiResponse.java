package com.tradin.common.response;


import com.tradin.common.exception.ExceptionMessage;
import com.tradin.common.exception.ExceptionType;
import lombok.Getter;

@Getter
public class ApiResponse<S> {

  private final ResultType result;

  private final S data;

  private final ExceptionMessage error;

  private ApiResponse(ResultType result, S data, ExceptionMessage error) {
    this.result = result;
    this.data = data;
    this.error = error;
  }

  public static ApiResponse<?> success() {
    return new ApiResponse<>(ResultType.SUCCESS, null, null);
  }

  public static <S> ApiResponse<S> success(S data) {
    return new ApiResponse<>(ResultType.SUCCESS, data, null);
  }

  public static ApiResponse<?> error(ExceptionType error) {
    return new ApiResponse<>(ResultType.ERROR, null, new ExceptionMessage(error));
  }

  public static ApiResponse<?> error(ExceptionType error, Object errorData) {
    return new ApiResponse<>(ResultType.ERROR, null, new ExceptionMessage(error, errorData));
  }
}
