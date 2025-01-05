package com.tradin.common.response;


import com.tradin.common.exception.ExceptionMessage;
import com.tradin.common.exception.ExceptionType;
import lombok.Getter;

@Getter
public class TradinResponse<S> {

  private final ResultType result;

  private final S data;

  private final ExceptionMessage error;

  private TradinResponse(ResultType result, S data, ExceptionMessage error) {
    this.result = result;
    this.data = data;
    this.error = error;
  }

  public static TradinResponse<?> success() {
    return new TradinResponse<>(ResultType.SUCCESS, null, null);
  }

  public static <S> TradinResponse<S> success(S data) {
    return new TradinResponse<>(ResultType.SUCCESS, data, null);
  }

  public static TradinResponse<?> error(ExceptionType error) {
    return new TradinResponse<>(ResultType.ERROR, null, new ExceptionMessage(error));
  }

  public static TradinResponse<?> error(ExceptionType error, Object errorData) {
    return new TradinResponse<>(ResultType.ERROR, null, new ExceptionMessage(error, errorData));
  }
}
