package jh.study.grpcspring.server.grpcservice.helloworld;

import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import jh.study.grpcspring.server.JsonMarshaller;
import jh.study.grpcspring.server.helloworld.GreeterGrpc;
import jh.study.grpcspring.server.helloworld.HelloReply;
import jh.study.grpcspring.server.helloworld.HelloRequest;
import org.springframework.stereotype.Component;

import static io.grpc.stub.ServerCalls.asyncUnaryCall;

// 자동으로 생기는 GreeterGrpc.GreeterImplBase 를 상속받아서 구현해도 되지만,
// 입출력에 Json을 사용하는등의 custom 한 작업이 필요하다면 BindableService 를 직접 구현해서 처리 할 수 있다.


//@Component
public class GreeterForJson implements BindableService {

    private void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName() + " from GreeterForJson").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public ServerServiceDefinition bindService() {
        var methodDescriptor = GreeterGrpc.getSayHelloMethod()
                .toBuilder(
                        JsonMarshaller.jsonMarshaller(HelloRequest.getDefaultInstance()),
                        JsonMarshaller.jsonMarshaller(HelloReply.getDefaultInstance()))
                .build();


        return ServerServiceDefinition
                .builder(GreeterGrpc.getServiceDescriptor().getName())
                .addMethod(methodDescriptor, asyncUnaryCall(
                        new ServerCalls.UnaryMethod<HelloRequest, HelloReply>() {
                            @Override
                            public void invoke(HelloRequest helloRequest, StreamObserver<HelloReply> streamObserver) {
                                sayHello(helloRequest, streamObserver);
                            }
                        }
                ))
                .build();

    }
}
