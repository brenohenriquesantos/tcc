package com.senai.tcc.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.senai.tcc.entities.Endereco;

@Service
public class ViaCep {

	public Endereco buscar(String cep) {
		
		Endereco endereco = new Endereco();
		
		try{
			URL url = new URL("https://viacep.com.br/ws/"+cep+"/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String retorno = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while((retorno = br.readLine()) != null) {
				jsonCep.append(retorno);
			}
			
			endereco = new Gson().fromJson(jsonCep.toString(), Endereco.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return endereco;
	}
	
	
}
