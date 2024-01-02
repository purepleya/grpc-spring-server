package jh.study.grpcspring.server;

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log
@Component
@RequiredArgsConstructor
public class GrpcServer {

    private final int port = 50051;

//    gRPC 서비스들 : jh.study.grpcspring.server.helloworld.Greeter
    private final List<BindableService> services;

    private final List<ServerInterceptor> interceptors;


    private Server server;

    public void start() throws IOException, InterruptedException {
        var builder = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create());
        services.forEach(builder::addService);
        interceptors.forEach(builder::intercept);
        server = builder.build();

        server.start();
        log.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    GrpcServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });

        server.awaitTermination();
    }


    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

}
