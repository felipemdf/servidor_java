package Server;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		HttpServer server = new HttpServer(80);
		server.addHandler("GET", "/hello", new Handler() {
			
			@Override
			public void handle(Request request, Response response) throws IOException {
				String html = "Está milagrosamente funcionando, " + request.getParameter("name") + "";
				response.setResponseCode(200, "OK");
				response.addHeader("Content-Type", "text/html");
				response.addBody(html);
			}
		});
		server.addHandler("GET", "/ops", new FileHandler()); //default
		server.start();
	}

}
