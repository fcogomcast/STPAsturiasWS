package com.stpa.ws.server.base;

import com.stpa.ws.server.exception.StpawsException;

public interface IStpawsValidation {
	public boolean isValid(Object obj) throws StpawsException;
}
