package com.shopix.oder.grpc;

import com.shopix.oder.dto.UserView;
import com.shopix.user.grpc.GetUserByKeycloakIdRequest;
import com.shopix.user.grpc.UserGrpcServiceGrpc;
import com.shopix.user.grpc.UserResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class UserServiceGrpcClient {

    @GrpcClient("user-service")
    private UserGrpcServiceGrpc.UserGrpcServiceBlockingStub userStub;

    public UserView getUserByKeycloakId(String keycloakId) {

        UserResponse grpcUser = userStub.getUserByKeycloakId(
                GetUserByKeycloakIdRequest.newBuilder()
                        .setKeycloakId(keycloakId)
                        .build()
        );

        // 🔥 MAP IT MANUALLY (or ModelMapper)
        UserView dto = new UserView();
        dto.setId(grpcUser.getId());
        dto.setKeycloakId(grpcUser.getKeycloakId());
        dto.setFirstName(grpcUser.getFirstName());
        dto.setLastName(grpcUser.getLastName());
        dto.setEmail(grpcUser.getEmail());
        dto.setSeller(grpcUser.getIsSeller());

        return dto;
    }
}
