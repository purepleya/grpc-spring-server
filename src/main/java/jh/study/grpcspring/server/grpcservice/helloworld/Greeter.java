package jh.study.grpcspring.server.grpcservice.helloworld;

import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import jh.study.grpcspring.server.helloworld.GreeterGrpc;
import jh.study.grpcspring.server.helloworld.HelloReply;
import jh.study.grpcspring.server.helloworld.HelloRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class Greeter extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String name = request.getName();
        if (StringUtils.hasText(name) && name.equals("error test")) {
            Status errorStatus = Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT_VALUE)
                    .setMessage("invalid argument value!!!!").build();

            responseObserver.onError(StatusProto.toStatusRuntimeException(errorStatus));
        }
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch(Exception e){
//            e.printStackTrace();
//        }


        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
