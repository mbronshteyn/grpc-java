package com.mbronshteyn.grpc.sum.service;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.PrimeNumber;
import com.proto.calculator.PrimeNumberRequest;
import com.proto.calculator.PrimeNumberResponse;
import com.proto.calculator.Sum;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        Sum requestSum = request.getSum();
        int result = requestSum.getFirstInteger() + requestSum.getSecondInteger();

        SumResponse sumResponse = SumResponse.newBuilder()
                .setResult(result)
                .build();

        responseObserver.onNext(sumResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumber(PrimeNumberRequest request, StreamObserver<PrimeNumberResponse> responseObserver) {

        PrimeNumber primeNumber = request.getPrimeNumber();
        List<Integer> list = calculatePrimeNumbers(primeNumber.getNumber());

        System.out.println(list.toString());

        list.forEach(number -> {
            PrimeNumberResponse primeNumberResponse = PrimeNumberResponse.newBuilder()
                    .setResult(number)
                    .build();
            responseObserver.onNext(primeNumberResponse);
        });

        responseObserver.onCompleted();
    }

    private List<Integer> calculatePrimeNumbers(int number) {

        List<Integer> result = new ArrayList<>();
        int k = 2;
        int N = number;
        while (N > 1) {
            if (N % k == 0) {  // if k evenly divides into N
                result.add(k);      // this is a factor
                N = N / k;   // divide N by k so that we have the rest of the number left.
            } else {
                k = k + 1;
            }
        }
        return result;
    }
}
