package jh.study.grpcspring.server.grpcservice.helloworld;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import jh.study.grpcspring.server.helloworld.GreeterGrpc;
import jh.study.grpcspring.server.helloworld.HelloReply;
import jh.study.grpcspring.server.helloworld.HelloRequest;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GreeterErrorTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Test
    void error_test() throws IOException {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(new Greeter())
                .build().start()
        );

        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build())
        );

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            blockingStub.sayHello(HelloRequest.newBuilder().setName("error test").build());
        });

        assertEquals(Status.Code.INVALID_ARGUMENT, exception.getStatus().getCode());
        assertEquals("invalid argument value!!!!", exception.getStatus().getDescription());

    }
}
