package com.altiparmakov.apigateway.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.altiparmakov.apigateway.security.JwtTokenManager;
import com.altiparmakov.protos.AuthServiceGrpc;
import com.altiparmakov.protos.UserAuthRequest;
import com.altiparmakov.protos.UserAuthResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int GRPC_PORT = 6565;
    private final EurekaClient eurekaClient;
    private final JwtTokenManager tokenManager;

    public String authorizeUser(String username, String password) {
        final InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("user-service", false);
        UserAuthRequest userAuthRequest = UserAuthRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(instanceInfo.getHostName(), GRPC_PORT)
                .usePlaintext()
                .build();
        AuthServiceGrpc.AuthServiceBlockingStub stub = AuthServiceGrpc.newBlockingStub(managedChannel);
        UserAuthResponse response = stub.auth(userAuthRequest);

        String jwtToken = null;
        if (response.getValid()) {
//            jwtToken = tokenManager.generateToken(username);
        }

        return jwtToken;
    }
}
