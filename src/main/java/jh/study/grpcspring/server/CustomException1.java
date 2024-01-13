package jh.study.grpcspring.server;

public class CustomException1 extends RuntimeException {

    public CustomException1(String message) {
        super(message);
    }
}
