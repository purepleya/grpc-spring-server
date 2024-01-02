package jh.study.grpcspring.server.grpcservice.helloworld;

import io.grpc.stub.StreamObserver;
import jh.study.grpcspring.server.helloworld.GreeterGrpc;
import jh.study.grpcspring.server.helloworld.HelloReply;
import jh.study.grpcspring.server.helloworld.HelloRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class Greeter extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(Exception e){
            e.printStackTrace();
        }


        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
