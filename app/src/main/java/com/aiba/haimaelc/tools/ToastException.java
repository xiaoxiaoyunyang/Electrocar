package com.aiba.haimaelc.tools;

import com.aiba.haimaelc.SysApp;

public class ToastException extends Exception {

    private static final long serialVersionUID = -3993923902106162004L;

    public ToastException(String msg) {
        super(msg);
    }

    public ToastException(int resid) {
        super(SysApp.getInstance().getString(resid));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
