package Server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response {
	private OutputStream out;
	private int statusCode;
	private String statusMessage;
	private Map<String, String> headers = new HashMap<>();
	private String body;
	
	/*
	 * Construtor
	 * 
	 * @param out
	 */
	
	public Response(OutputStream out) {
		this.out = out;
	}

	public void setResponseCode(int statusCode, String statusMessage) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}
	
	public void addHeader(String headerName, String headerValue) {
		this.headers.put(headerName, headerValue);
	}
	
	public void addBody(String body) {
		this.body = body;
	}
	
	/**
	 * Método responsavel por criar a resposta, composta pelo header, uma linha vazia
	 * e o corpo da resposta
	 * 
	 * @throws IOException
	 */
	public void sendReponse() throws IOException {
		headers.put("Connection", "Close");
		out.write(("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n").getBytes());
		
		for(String headerName: headers.keySet()) {
			out.write((headerName + ": " + headers.get(headerName) + "\r\n").getBytes());
		}
		
		out.write("\r\n".getBytes());
		
		if (body != null)  {
		      out.write(body.getBytes());
		    }
	}
}
