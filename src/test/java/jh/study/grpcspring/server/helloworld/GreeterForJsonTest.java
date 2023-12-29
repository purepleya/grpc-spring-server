package jh.study.grpcspring.server.helloworld;

import io.grpc.CallOptions;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import jh.study.grpcspring.server.JsonMarshaller;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static org.junit.jupiter.api.Assertions.*;

class GreeterForJsonTest {
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
                .addService(new GreeterForJson()).build().start());

        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(
                // Create a client channel and register for automatic graceful shutdown.
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));

        var methodDescriptor = GreeterGrpc.getSayHelloMethod()
                .toBuilder(
                        JsonMarshaller.jsonMarshaller(HelloRequest.getDefaultInstance()),
                        JsonMarshaller.jsonMarshaller(HelloReply.getDefaultInstance()))
                .build();

        HelloReply reply = blockingUnaryCall(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()),
                methodDescriptor,
                CallOptions.DEFAULT,
                HelloRequest.newBuilder().setName("test name").build()
        );

        assertEquals("Hello test name from GreeterForJson", reply.getMessage());
    }
}