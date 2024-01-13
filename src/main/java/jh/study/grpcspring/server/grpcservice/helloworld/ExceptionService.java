package jh.study.grpcspring.server.grpcservice.helloworld;

import helloworld.ExceptionHandling;
import helloworld.ExceptionServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jh.study.grpcspring.server.CustomException1;
import jh.study.grpcspring.server.CustomException2;
import org.springframework.stereotype.Component;

@Component
public class ExceptionService extends ExceptionServiceGrpc.ExceptionServiceImplBase {

    @Override
    public void getException(ExceptionHandling.GetExceptionRequest request, StreamObserver<ExceptionHandling.GetExceptionResponse> responseObserver) {
        if (request.getErrorCode() == 1) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withCause(new RuntimeException("This is RuntimeException")).asRuntimeException());
        } else if (request.getErrorCode() == 2) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withCause(new CustomException1("This is CustomException1")).asRuntimeException());
        } else if (request.getErrorCode() == 3) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("This is CustomException2").asRuntimeException());
        } else {
            responseObserver.onNext(ExceptionHandling.GetExceptionResponse.newBuilder().setMessage("It's OK").build());
            responseObserver.onCompleted();
        }
    }
}
