package jh.study.grpcspring.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GrpcServerApplication {

	public static void main(String[] args) throws IOException {
		var context = SpringApplication.run(GrpcServerApplication.class, args);
		GrpcServer server = context.getBean(GrpcServer.class);

		server.start();

	}

}
