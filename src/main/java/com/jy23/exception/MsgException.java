package com.jy23.exception;

import com.jy23.util.ResultStatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class MsgException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3042843198939871708L;
	private String errorMessage;
    private int errorCode;

    public MsgException(String errorMessage, int errorCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public MsgException(ResultStatusCode resultStatusCode) {
        super(resultStatusCode.getMsg());
        this.errorMessage = resultStatusCode.getMsg();
        this.errorCode = resultStatusCode.getCode();
    }
}