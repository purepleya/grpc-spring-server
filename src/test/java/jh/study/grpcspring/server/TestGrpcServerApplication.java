package jh.study.grpcspring.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestGrpcServerApplication {

	public static void main(String[] args) {
		SpringApplication.from(GrpcServerApplication::main).with(TestGrpcServerApplication.class).run(args);
	}

}
