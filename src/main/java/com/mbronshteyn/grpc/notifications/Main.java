package com.mbronshteyn.grpc.notifications;

import com.google.protobuf.util.JsonFormat;
import com.logrhythm.core.api.v1.grpc.*;

import java.util.UUID;

public class Main {
    public static void main(String... args) throws Exception {
        EmailAudience audience = EmailAudience.newBuilder()
                .setAddress("foo@example.com")
                .setName("Foo")
                .build();
        System.out.println("Here is a proto3 object rendered using toString():");
        System.out.println(audience);
        System.out.println();
        System.out.println("Here is the same object rendered using JsonFormat:");
        System.out.println(JsonFormat.printer().print(audience));

        System.out.println();

        // list channel types
        ListChannelTypesResponse lctr = ListChannelTypesResponse.newBuilder()
                .setInfo(generateResponseHeader())
                .addChannelTypes(ChannelType.EMAIL)
                .addChannelTypes(ChannelType.SLACK)
                .addChannelTypes(ChannelType.SMS)
                .addChannelTypes(ChannelType.PAGER_DUTY)
                .build();
        System.out.println("ListChannelTypes response:");
        System.out.println(JsonFormat.printer().print(lctr));
        System.out.println();

        // channel configuration
        UnaryChannelConfigResponse channelConfigResponse = UnaryChannelConfigResponse.newBuilder()
                .setInfo(generateResponseHeader())
                .setChannelConfig(
                        generateEmailChannelConfig())
                .build();
        System.out.println("A channel config response:");
        System.out.println(JsonFormat.printer().print(channelConfigResponse));
        System.out.println();

        // list channel configs
        ListChannelConfigsResponse listChannelConfigsResponse = ListChannelConfigsResponse.newBuilder()
                .addChannelConfigs(generateEmailChannelConfig())
                .addChannelConfigs(generateSmsChannelConfig())
                .addChannelConfigs(generateSlackChannelConfig())
                .build();
        System.out.println("A list channel config response:");
        System.out.println(JsonFormat.printer().print(listChannelConfigsResponse));
        System.out.println();


        // A notice object
        Notice notice = Notice.newBuilder()
                .setIdentifier(generateIdentifier())
                .setCategory("security")
                .setSubcategory("hacking")
                .setServiceName("FooService")
                .setAudience(Audience.newBuilder()
                        .addEmail(EmailAudience.newBuilder()
                                .setAddress("foo@example.com")
                                .setName("Foo"))
                        .addEmail(EmailAudience.newBuilder()
                                .setAddress("bar@example.com")
                                .setName("Bar")))
                .setChannel(ChannelType.EMAIL)
                .setNoticeStatus(NoticeStatus.newBuilder()
                        .setProcessingState(NoticeStatus.ProcessingState.COMPLETE_SUCCESS)
                        .addNotifications(Notification.newBuilder()
                                .setProcessingState(Notification.ProcessingState.SENT)
                                .setChannel("email")
                                .setDestination("foo@example.com")))
                .putProperties("template-key-1", "value1")
                .putProperties("template-key-2", "value2")
                .putProperties("template-key-3", "value3")
                .build();

        System.out.println("Here's a notice, as if returned from the GetNotice API:");
        System.out.println(JsonFormat.printer().print(notice));
        System.out.println();
    }

    private static UnaryResponseHeader generateResponseHeader() {
        return UnaryResponseHeader.newBuilder()
                .setRequestId(UUID.randomUUID().toString())
                .build();
    }

    private static Identifier generateIdentifier() {
        return Identifier.newBuilder()
                .setTenantId(UUID.randomUUID().toString())
                .setId(UUID.randomUUID().toString())
                .build();
    }

    private static ChannelConfig generateEmailChannelConfig() {
        return ChannelConfig.newBuilder()
                .setChannelType(ChannelType.EMAIL)
                .setEmailChannelConfig(ChannelConfig.EmailChannelConfig.newBuilder()
                        .setSmtpServer("mail.example.com")
                        .setPort(25)
                        .build())
                .build();
    }

    private static ChannelConfig generateSmsChannelConfig() {
        return ChannelConfig.newBuilder()
                .setChannelType(ChannelType.SMS)
                .setSmsChannelConfig(ChannelConfig.SmsChannelConfig.newBuilder()
                        .setHostname("smsgateway.example.com")
                        .setId("12345678")
                        .setPort(80)
                        .setProgramName("logrhythm-sms")
                        .setToParamName("to")
                        .setTextParamName("text")
                        .build())
                .build();
    }

    private static ChannelConfig generateSlackChannelConfig() {
        return ChannelConfig.newBuilder()
                .setChannelType(ChannelType.SLACK)
                .setSlackChannelConfig(ChannelConfig.SlackChannelConfig.newBuilder()
                        .setSlackChannel("example_slack_channel")
                        .build())
                .build();

    }

}
