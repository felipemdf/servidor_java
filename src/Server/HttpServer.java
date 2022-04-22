package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class HttpServer {
	private int port;
	private Handler defaultHanlder = null;
	private Map<String, Map<String, Handler>> handlers = new HashMap<>(); //EXPLICAR DEPOIS
	Logger logger = Logger.getLogger(HttpServer.class.getName());
	
	
	/**
	 * Construtor da classe
	 * 
	 * @param port
	 */
	public HttpServer(int port)  {
		this.port = port;
	}
	
	/*
	 * Método responsavel por criar uma conexão socket
	 */
	public void start() throws IOException {
		ServerSocket socket = new ServerSocket(this.port);
		Socket client;
		
		logger.info("Servidor escutando na porta " + this.port);
		
		while((client = socket.accept()) != null) {
			logger.info("Conexão estabelecida com " + client.getRemoteSocketAddress());
		
			SocketHandler handler = new SocketHandler(client, handlers); 
			Thread t = new Thread(handler);
			t.start();
		}
	}
	
	
	/**
	 * Método responsavel por adicionar os handlers
	 * 
	 * @param method
	 * @param path
	 * @param handler
	 */
	public void addHandler(String method, String path, Handler handler) {
		Map<String, Handler> methodHandler = handlers.get(method); 
		if(methodHandler == null) {
			methodHandler = new HashMap<String, Handler>();
			handlers.put(method, methodHandler); 
		}
		
		methodHandler.put(path, handler); 
		logger.info("Handler com metodo " + method + " e path " + path + " adicionado");
	}
	
	
}