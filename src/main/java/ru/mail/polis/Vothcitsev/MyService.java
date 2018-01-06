package ru.mail.polis.Vothcitsev;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyService implements KVService {
    private static final String PREFIX = "id=";
    @NotNull
    private final HttpServer server;

    @NotNull
    private static String extractId(@NotNull final String query) {
        if(!query.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Error");
        }
        return query.substring(PREFIX.length());
    }

    public MyService(int port, @NotNull final MyDAO dao) throws IOException{
        this.server = HttpServer.create(new InetSocketAddress(port),0);

        this.server.createContext("/v0/status", http -> {
            final String response = "ONLINE";
            http.sendResponseHeaders(200, response.length());
            http.getResponseBody().write(response.getBytes());
            http.close();
        });

        this.server.createContext("/v0/entity", (HttpExchange http) -> {
            final String id = extractId(http.getRequestURI().getQuery());
            if(id.length()==0){
                http.sendResponseHeaders(400, 0);
                http.close();
            }
            else {
                switch (http.getRequestMethod()) {
                    case "GET":
                        try {
                            final byte[] getValue = dao.get(id);
                            http.sendResponseHeaders(200, getValue.length);
                            http.getResponseBody().write(getValue);
                        } catch (IOException e) {
                            http.sendResponseHeaders(404, 0);
                            http.close();
                        }
                        break;

                    case "DELETE":
                        dao.delete(id);
                        http.sendResponseHeaders(202, 0);
                        break;

                    case "PUT":
                        final int contentLength = Integer.valueOf(http.getRequestHeaders().getFirst("Content-Length"));
                        final byte[] putValue = new byte[contentLength];
                        if (contentLength>0 && http.getRequestBody().read(putValue)!=putValue.length) {
                            throw new IOException("Can't read!");
                        }
                        dao.upsert(id, putValue);
                        http.sendResponseHeaders(201, 0);
                        break;

                    default:
                        http.sendResponseHeaders(405, 0);
                }
                http.close();
            }
        });

    }

    @Override
    public void start(){
        this.server.start();
    }
    @Override
    public void stop(){
        this.server.stop(0);
    }
}