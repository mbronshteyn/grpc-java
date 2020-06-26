package com.mbronshteyn.grpc.notifications.service;

import com.logrhythm.core.api.v1.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NotificationServiceImpl extends NotificationServiceGrpc.NotificationServiceImplBase {

    private static Map<String, Notice> notices = new HashMap<>();

    @Override
    public void createNotice(Notice request, StreamObserver<UnaryNoticeResponse> responseObserver) {
        Notice notice = request.toBuilder().setIdentifier(Identifier.newBuilder()
                .setId(UUID.randomUUID().toString())
                .build())
                .build();
        notices.put(notice.getIdentifier().getId(), notice);
        UnaryNoticeResponse response = UnaryNoticeResponse.newBuilder()
                .setInfo(UnaryResponseHeader.newBuilder()
                        .setRequestId(UUID.randomUUID().toString())
                        .build())
                .setNotice(notice)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getNotice(Identifier request, StreamObserver<UnaryNoticeResponse> responseObserver) {
        Notice notice = notices.get(request.getId());
        if (notice != null) {
            UnaryNoticeResponse response = UnaryNoticeResponse.newBuilder()
                    .setInfo(UnaryResponseHeader.newBuilder()
                            .setRequestId(UUID.randomUUID().toString())
                            .build())
                    .setNotice(notice)
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }
}
