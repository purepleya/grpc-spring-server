package jh.study.grpcspring.server.grpcservice.helloworld;

import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import jh.study.grpcspring.server.grpcservice.helloworld.Greeter;
import jh.study.grpcspring.server.helloworld.GreeterGrpc;
import jh.study.grpcspring.server.helloworld.HelloReply;
import jh.study.grpcspring.server.helloworld.HelloRequest;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GreeterTest {

    /**
     * This rule manages automatic graceful shutdown for the registered servers and channels at the
     * end of test.
     */
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();


    @Test
    public void greeterImpl_replyMessage() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();

        // Create a server, add service, start, and register for automatic graceful shutdown.
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(new Greeter()).build().start());

        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(
                // Create a client channel and register for automatic graceful shutdown.
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));


        HelloReply reply =
                blockingStub.sayHello(HelloRequest.newBuilder().setName( "test name").build());

        assertEquals("Hello test name", reply.getMessage());
    }
}