package jh.study.grpcspring.server.grpcservice.helloworld;

import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import jh.study.grpcspring.server.helloworld.GreeterGrpc;
import jh.study.grpcspring.server.helloworld.HelloReply;
import jh.study.grpcspring.server.helloworld.HelloRequest;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class GreeterDeadlineTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Test
    public void greeter_deadline_test() throws IOException {
        String serviceName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder.forName(serviceName).directExecutor()
                        .addService(new Greeter())
                        .build()
                        .start()
        );

        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(
            grpcCleanup.register(InProcessChannelBuilder.forName(serviceName).directExecutor().build())
        );

        assertThrows(StatusRuntimeException.class, () -> {
            blockingStub.withDeadlineAfter(10, TimeUnit.MILLISECONDS).sayHello(HelloRequest.newBuilder().setName("deadline test").build());
        });

    }

}
