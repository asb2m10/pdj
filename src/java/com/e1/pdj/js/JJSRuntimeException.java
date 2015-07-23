package com.e1.pdj.js;

import com.cycling74.max.MaxRuntimeException;

public class JJSRuntimeException extends MaxRuntimeException {
	private static final long serialVersionUID = 1L;

	public JJSRuntimeException(String msg) {
		super(msg);
	}

	public JJSRuntimeException(Exception e) {
		super(e);
	}
}
