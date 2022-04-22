package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Request {
	private String method;
	private String path;
	private String fullUrl;
	private BufferedReader in; //Oque é???????w
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, String> queryParameters = new HashMap<String, String>();
	
	public Request(BufferedReader in) throws IOException {
		this.in = in;
	}

	/*
	 * Método com a finalidade realizar o tratamento da requisição,
	 * separando o header dos parâmentros
	 */
	public Boolean parse() throws IOException {
		while (in.ready()) {
			String initialLine = in.readLine(); 
			StringTokenizer word = new StringTokenizer(initialLine); 
			String[] components = new String[3]; // DA PARA MUDAR ISSO
			
			for(int i = 0; i< components.length; i++) {
				if(word.hasMoreTokens())
					components[i] = word.nextToken();
				else
					return false;
			}
			this.method = components[0]; 
			this.fullUrl = components[1]; 
			
			//Consumir o header
			while(true) {
				String headerLine = in.readLine();
				
				if(headerLine.length() == 0)
					break;
				
				int indexSeparador = headerLine.indexOf(":");
				if(indexSeparador == -1)
					return false;
				
				//Armazena a chave a valor de cada propriedade no header
				headers.put(headerLine.substring(0, indexSeparador), headerLine.substring(indexSeparador + 1));
				
				//Verifica se a url tem parâmetros
				if(components[1].indexOf("?") == -1) {
					path = components[1];
				} else {
					path = components[1].substring(0, components[1].indexOf("?"));
					parseQueryParameters(components[1].substring(components[1].indexOf("?") + 1));
				}
				
				//Oque é???????
				if("/".equals(path)) {
					path = "/index.html";
				}
			}
		}
		return true;

	}

	/**
	 * Método com a finalidade de realizar o tratamento dos parâmetros
	 * 
	 * @param queryString
	 */
	public void parseQueryParameters(String queryString) {
		for(String parameter: queryString.split("&")) {
			int indexSeparator = parameter.indexOf('=');
			if(indexSeparator > -1)
				queryParameters.put(parameter.substring(0, indexSeparator), parameter.substring(indexSeparator + 1));
			else
				queryParameters.put(parameter, null);
		}
	}

	
	public String getMethod() {
		return method;
	}



	public String getPath() {
		return path;
	}



	public String getFullUrl() {
		return fullUrl;
	}
	

	public String getHeader(String headerName)  {
	    return headers.get(headerName);
	}
	
	public String getParameter(String paramName)  {
	    return queryParameters.get(paramName);
	  }

}
