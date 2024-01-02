package jh.study.grpcspring.server.interceptor;

import io.grpc.*;

public class HeaderInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        return serverCallHandler.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
            @Override
            public void sendHeaders(Metadata headers) {
                headers.put(
                        Metadata.Key.of("custom_server_header_key", Metadata.ASCII_STRING_MARSHALLER),
                        "customServerHeaderValue"
                );
                super.sendHeaders(headers);
            }
        }, metadata);
    }
}
