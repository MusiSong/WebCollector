package com.Music.service.impl;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.Music.service.ServerServices;
@WebService
public class ServerServicesImpl implements ServerServices {
	public String sayHi(@WebParam(name = "text")String text){   
		System.out.println("sayHi called");   
		return "Hello " + text;  
	 }  
}
