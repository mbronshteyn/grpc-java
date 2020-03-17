package com.mbronshteyn.grpc.sum.service;

import com.proto.sum.Sum;
import com.proto.sum.SumRequest;
import com.proto.sum.SumResponse;
import com.proto.sum.SumServiceGrpc;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {
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
}
