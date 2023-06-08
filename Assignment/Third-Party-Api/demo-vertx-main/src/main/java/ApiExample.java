import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ApiExample {
    public static void main(String[] args) throws IOException {
        int serverPort = 8081;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/demo/service", new DemoServiceHandler());
        server.start();
        System.out.println("Server started on port " + serverPort);
    }

    static class DemoServiceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    requestBody.append(line);
                }
                br.close();

                // Process the request body
                String response = "Received request body: " + requestBody.toString();
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                // Print success message in console
                System.out.println(response);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }
}
