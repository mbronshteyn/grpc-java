package com.mbronshteyn.grpc.sum.client;

import com.proto.sum.Sum;
import com.proto.sum.SumRequest;
import com.proto.sum.SumResponse;
import com.proto.sum.SumServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SumClient {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        SumServiceGrpc.SumServiceBlockingStub blockingStub =
                SumServiceGrpc.newBlockingStub(managedChannel);

        Sum sum = Sum.newBuilder()
                .setFirstInteger(3)
                .setSecondInteger(10)
                .build();

        SumRequest sumRequest = SumRequest.newBuilder()
                .setSum(sum)
                .build();
        SumResponse sumResponse = blockingStub.sum(sumRequest);

        System.out.println(sumResponse.getResult());

        System.out.println("Shutting down channel");
        managedChannel.shutdown();
    }
}
