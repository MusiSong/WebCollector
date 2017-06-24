package com.Music.service;

import javax.jws.WebParam;
import javax.jws.WebService;
@WebService
public interface ServerServices {
	public String sayHi(@WebParam(name = "text")String text);
}
