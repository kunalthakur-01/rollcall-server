package com.rollcall.server.exceptions;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultipleException extends RuntimeException {
    private List<String> errros;

    public MultipleException(List<String> errors){
        this.errros = errors;
    }
}
