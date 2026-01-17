package iuh.fit.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private Object message;
    private T data;
    private String error;
}