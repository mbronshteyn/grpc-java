package com.mbronshteyn.grpc.sum.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.PrimeNumber;
import com.proto.calculator.PrimeNumberRequest;
import com.proto.calculator.Sum;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub =
                CalculatorServiceGrpc.newBlockingStub(managedChannel);

        // Sum
        Sum sum = Sum.newBuilder()
                .setFirstInteger(3)
                .setSecondInteger(10)
                .build();

        SumRequest sumRequest = SumRequest.newBuilder()
                .setSum(sum)
                .build();

        SumResponse sumResponse = blockingStub.sum(sumRequest);
        System.out.println(sumResponse.getResult());

        int number = 120;

        // Prime Number
        PrimeNumber primeNumber = PrimeNumber.newBuilder()
                .setNumber(number)
                .build();

        PrimeNumberRequest primeNumberRequest = PrimeNumberRequest.newBuilder()
                .setPrimeNumber(primeNumber)
                .build();

        System.out.print("Prime Numbers of " + number + " -> ");
        blockingStub.primeNumber(primeNumberRequest)
                .forEachRemaining(response -> {
                    System.out.print("" + response.getResult() + " ");
                });

        System.out.println("\nShutting down channel");
        managedChannel.shutdown();
    }
}
