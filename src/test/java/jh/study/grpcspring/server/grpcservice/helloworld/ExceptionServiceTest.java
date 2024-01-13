package jh.study.grpcspring.server.grpcservice.helloworld;

import helloworld.ExceptionServiceGrpc;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionServiceTest {

    @Rule
    public final GrpcCleanupRule grpcCleanupRule = new GrpcCleanupRule();

    @Test
    void getException_test() throws IOException {
        String serverName = InProcessServerBuilder.generateName();

        grpcCleanupRule.register(InProcessServerBuilder.forName(serverName).directExecutor()
                .addService(new ExceptionService())
                .build()
                .start()
        );


        var stub = ExceptionServiceGrpc.newBlockingStub(
                grpcCleanupRule.register(InProcessChannelBuilder.forName(serverName).directExecutor().build())
        );


        try {
            var response = stub.getException(helloworld.ExceptionHandling.GetExceptionRequest.newBuilder().setErrorCode(2).build());
        } catch (StatusRuntimeException e) {
            System.out.println("====================================");
            System.out.println(e.getCause().getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getStatus().getDescription());
            System.out.println(e.getStatus().getCode());
            System.out.println("====================================");
        }
    }

}