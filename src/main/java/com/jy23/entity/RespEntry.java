package com.jy23.entity;

import com.jy23.util.ResultStatusCode;

public class RespEntry {
    private String msg;
    private int code;		//0，成功。其他数值表示失败
    private Object body;

    public RespEntry(String msg) {
        this(msg, 0, null);
    }

    public RespEntry(String msg, int code) {
        this(msg, code, null);
    }

    public RespEntry(String msg, Object obj) {
        this(msg, 0, obj);
    }

    public RespEntry(ResultStatusCode resultStatusCode) {
        this(resultStatusCode, null);
    }

    public RespEntry(ResultStatusCode resultStatusCode, Object obj) {
        this.msg = resultStatusCode.getMsg();
        this.code = resultStatusCode.getCode();
        this.body = obj;
    }

    public RespEntry(String msg, int code, Object obj) {
        this.msg = msg;
        this.code = code;
        this.body = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "RespEntry{msg='" + msg + '\'' +
                ", code=" + code +
                ", body=" + body +
                '}';
    }
}
