package jh.study.grpcspring.server.grpcservice.helloworld;

import io.grpc.*;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import jh.study.grpcspring.server.helloworld.GreeterGrpc;
import jh.study.grpcspring.server.helloworld.HelloReply;
import jh.study.grpcspring.server.helloworld.HelloRequest;
import jh.study.grpcspring.server.interceptor.HeaderInterceptor;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.Nullable;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class GreeterHeaderInterceptorTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Test
    void header_test() throws IOException {
        String serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(new Greeter())
                .intercept(new HeaderInterceptor())
                .build().start()
        );

        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder
                        .forName(serverName)
                        .directExecutor()
                        .intercept(new ClientInterceptor() {
                            @Override
                            public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
                                return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
                                    @Override
                                    public void start(Listener<RespT> responseListener, Metadata headers) {
                                        super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                                            @Override
                                            public void onHeaders(Metadata headers) {
                                                System.out.println("header : " + headers);
                                                String customServerHeaderKey = "custom_server_header_key";
                                                String value = headers.get(Metadata.Key.of(customServerHeaderKey, Metadata.ASCII_STRING_MARSHALLER));

                                                assertThat(value).isEqualTo("customServerHeaderValue");

                                                super.onHeaders(headers);
                                            }
                                        }, headers);
                                    }
                                };
                            }
                        })
                        .build())
        );

        HelloReply reply = stub.sayHello(HelloRequest.newBuilder().setName("test").build());

        assertThat(reply.getMessage()).isEqualTo("Hello test");
    }
}
