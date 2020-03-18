package com.mbronshteyn.grpc.greeting.client;

import com.proto.greet.GreetManyTimesRequest;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hello I am a gRPC client");
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        GreetServiceGrpc.GreetServiceBlockingStub blockingStub =
                GreetServiceGrpc.newBlockingStub(managedChannel);

        // Unary
//        Greeting greeting = Greeting.newBuilder()
//                .setFirstName("Mike")
//                .build();
//        GreetRequest greetRequest = GreetRequest.newBuilder()
//                .setGreeting(greeting)
//                .build();
//        GreetResponse greetResponse = blockingStub.greet(greetRequest);
//
//        System.out.println(greetResponse.getResult());

        // Server streaming
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Mike")
                .build();
        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        blockingStub.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });

        System.out.println("Shutting down channel");
        managedChannel.shutdown();
    }
}
