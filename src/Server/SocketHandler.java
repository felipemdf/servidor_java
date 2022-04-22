package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import Server.Request;

public class SocketHandler implements Runnable{
	private Socket socket;
	private Handler defaultHandler;
	private Map<String, Map<String, Handler>> handlers;
	
	
	/**
	 * Construtor
	 * 
	 * @param socket
	 * @param handlers
	 */
	public SocketHandler(Socket socket, Map<String, Map<String, Handler>> handlers) {
		this.socket = socket;
		this.handlers = handlers;
	}


	/**
	 * Método responsavel por criar respontas simples, como erros
	 * 
	 * @param statusCode
	 * @param message
	 * @param out
	 * @throws IOException
	 */
	private void defautResponse(int statusCode, String message, OutputStream out)throws IOException {
		String responseLine = "HTTP/1.1 " + statusCode + " " + message + "\r\n\r\n";
		out.write(responseLine.getBytes());
	}
	
	@Override
	public void run() {
		BufferedReader in = null;
		OutputStream out = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			out = socket.getOutputStream(); 
			boolean foundHandler = false;
			Response response = new Response(out);
			Map<String, Handler> methodHandlers;
			
			 
			// Monta as respostas
			Request request = new Request(in);
			if (!request.parse()) {
				defautResponse(500, "Não foi possível analisar a solicitação", out);
				return;
			}
			
			// Verifica se existe o método
			methodHandlers = handlers.get(request.getMethod());
			if(methodHandlers == null) {
				defautResponse(405, "Método não suportado", out);
				return;
			}
			
			// Verifica se existe o path
			for (String handlerPath : methodHandlers.keySet())  {
		        if (handlerPath.equals(request.getPath()))  {
		          methodHandlers.get(request.getPath()).handle(request, response); //PELO QUE EU ENTENDI ELE PEGA A FUNCAO DO HANDLE
		          response.sendReponse();
		          foundHandler = true;
		          break;
		        }
		    }
			
			if (!foundHandler)  {
				defautResponse(404, "Handler não encontrado", out);
		    }
		} catch (IOException  e) {
			try  {
		        e.printStackTrace();
		        if (out != null)  
		        	defautResponse(500, e.toString(), out);
		      } catch (IOException e2)  {
		    	  e2.printStackTrace();
		      }
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null)  {
			          in.close();
			    }
				socket.close();
			} catch (IOException  e) {
				e.printStackTrace();
			}
		}
		
	}

}
