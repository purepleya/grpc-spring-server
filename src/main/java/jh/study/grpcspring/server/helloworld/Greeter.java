package jh.study.grpcspring.server.helloworld;

import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class Greeter extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
