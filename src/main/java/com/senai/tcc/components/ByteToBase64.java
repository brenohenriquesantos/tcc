package com.senai.tcc.components;

import java.util.Base64;

public class ByteToBase64 {
	
	public static String transformar(byte[] bytes)   {
		
		return Base64.getEncoder().encodeToString(bytes);
	}
}
