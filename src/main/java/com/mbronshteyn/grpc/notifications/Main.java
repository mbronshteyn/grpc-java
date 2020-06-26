package com.mbronshteyn.grpc.notifications;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.logrhythm.core.api.v1.grpc.Audience;
import com.logrhythm.core.api.v1.grpc.ChannelConfig;
import com.logrhythm.core.api.v1.grpc.ChannelType;
import com.logrhythm.core.api.v1.grpc.EmailAudience;
import com.logrhythm.core.api.v1.grpc.Identifier;
import com.logrhythm.core.api.v1.grpc.ListChannelTypesResponse;
import com.logrhythm.core.api.v1.grpc.MuteSpecifier;
import com.logrhythm.core.api.v1.grpc.Notice;
import com.logrhythm.core.api.v1.grpc.NoticeStatus;
import com.logrhythm.core.api.v1.grpc.Notification;
import com.logrhythm.core.api.v1.grpc.NotificationConfig;
import com.logrhythm.core.api.v1.grpc.NotificationTemplate;
import com.logrhythm.core.api.v1.grpc.NotificationTemplatePatternRequest;
import com.logrhythm.core.api.v1.grpc.RiskBasedPriority;
import com.logrhythm.core.api.v1.grpc.SeekPageChannelConfigResponse;
import com.logrhythm.core.api.v1.grpc.SeekPageNotificationConfigResponse;
import com.logrhythm.core.api.v1.grpc.SeekPageNotificationTemplateResponse;
import com.logrhythm.core.api.v1.grpc.SeekPageResponseHeader;
import com.logrhythm.core.api.v1.grpc.SeekPaginationInfo;
import com.logrhythm.core.api.v1.grpc.UnaryChannelConfigResponse;
import com.logrhythm.core.api.v1.grpc.UnaryNotificationConfigResponse;
import com.logrhythm.core.api.v1.grpc.UnaryResponseHeader;
import com.logrhythm.core.api.v1.grpc.UserPreference;

import java.util.UUID;

public class Main {
    public static void main(String... args) throws Exception {
//        printNoticeJson();
//        printNotificationConfigJson();
        printChannelConfigJson();
//        printTemplateConfigJson();
//        printUserPreferenceConfigJson();
    }
    
    private static void printNoticeJson() throws InvalidProtocolBufferException {
        println("SAMPLE NOTICE JSON");
        println();
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
                .setChannel(ChannelType.CHANNEL_TYPE_EMAIL)
                .setNoticeStatus(NoticeStatus.newBuilder()
                        .setProcessingState(NoticeStatus.ProcessingState.COMPLETE_SUCCESS)
                        .addNotifications(Notification.newBuilder()
                                .setProcessingState(Notification.ProcessingState.SENT)
                                .setChannel("email")
                                .setDestination("foo@example.com")))
                .putTemplateProperties("template-key-1", "value1")
                .putTemplateProperties("template-key-2", "value2")
                .putTemplateProperties("template-key-3", "value3")
                .build();

        println("Here's a notice, as if returned from the GetNotice API:");
        printJson(notice);

        println();
        println("---------------------------------------------------------------------------");
        println();

    }
    
    private static void printNotificationConfigJson() {
        println("SAMPLE NOTIFICATION CONFIG");
        println();
        NotificationConfig notificationConfig = generateNotificationConfig()
                .setConfigId("100")
                .build();
        println("NotificationConfig message:");
        printJson(notificationConfig);

        printSeparator();

        UnaryNotificationConfigResponse uncResp = UnaryNotificationConfigResponse.newBuilder()
                .setConfig(notificationConfig)
                .setInfo(generateResponseHeader())
                .build();
        println("UnaryNotificationConfigResponse message:");
        printJson(uncResp);

        printSeparator();

        SeekPageNotificationConfigResponse response = SeekPageNotificationConfigResponse.newBuilder()
                .setInfo(SeekPageResponseHeader.newBuilder()
                        .setRequestId(UUID.randomUUID().toString())
                        .setPaginationInfo(SeekPaginationInfo.newBuilder()
                                .setNextPage("2")
                                .build())
                        .build())
                .addContent(notificationConfig)
                .addContent(generateNotificationConfig().setConfigId("101"))
                .build();
        println("FindNotificationConfig response:");
        printJson(response);


        printSeparator();

    }

    private static void printNotificationConfigFindResponseJson() {

    }

    private static NotificationConfig.Builder generateNotificationConfig() {
        return NotificationConfig.newBuilder()
                .setConfigId("100")
                .setTenantId("fubar_tenant")
                .setServiceName("foo_service")
                .setCategory("security")
                .setSubcategory("hacking")
                .setRiskBasedScore(80)
                .setRole("foo_role")
                .setEntity("foo_entity")
                .setChannel("email")
                .setDestination("destination@example.com");
    }
    
    private static void printUserPreferenceConfigJson() {
        println("SAMPLE USER PREFERENCE CONFIG");
        println();

        UserPreference preference = UserPreference.newBuilder()
                .setUserId(generateIdentifier())
                .setName("John Doe")
                .setEmailAddress("john.doe@example.com")
                .addMuteSpecifiers(MuteSpecifier.newBuilder()
                        .setMute(true)
                        .setChannel("email")
                        .setRiskBasedPriority(RiskBasedPriority.RISK_BASED_PRIORITY_LOW)
                        .build())
                .build();
        println("User Preference object:");
        printJson(preference);

        printSeparator();

    }
    
    private static void printTemplateConfigJson() {
        NotificationTemplate.Builder template = NotificationTemplate.newBuilder()
                .setTenantId(generateIdentifier().getTenantId())
                .setChannel("email")
                .setRiskBasedPriority(RiskBasedPriority.RISK_BASED_PRIORITY_HIGH)
                .setServiceName("FooService")
                .setTemplate("Hello, $user.name. Here is an urgent message:\n$message");
        printJson("SAMPLE TEMPLATE CONFIG", template);

        printSeparator();

        NotificationTemplatePatternRequest request = NotificationTemplatePatternRequest.newBuilder()
                .setChannel("slack")
                .setRiskBasedPriority(RiskBasedPriority.RISK_BASED_PRIORITY_HIGH)
                .setTenantId(generateIdentifier().getTenantId())
                .build();
        printJson("Sample find template request", request);

        SeekPageNotificationTemplateResponse response = SeekPageNotificationTemplateResponse.newBuilder()
                .addContent(template.build())
                .addContent(template.build().toBuilder()
                        .setChannel("slack")
                        .setTemplate(":warning: Urgent message for $user.name: $message"))
                .build();

        printJson("Sample find templates response", response);

        printSeparator();
    }

    private static void printSeparator() {
        println();
        println("---------------------------------------------------------------------------");
        println();
    }

    private static void printChannelConfigJson() {
        // list channel types
        ListChannelTypesResponse lctr = ListChannelTypesResponse.newBuilder()
                .setInfo(generateResponseHeader())
                .addChannelTypes(ChannelType.CHANNEL_TYPE_EMAIL)
                .addChannelTypes(ChannelType.CHANNEL_TYPE_SLACK)
                .addChannelTypes(ChannelType.CHANNEL_TYPE_SMS)
                .addChannelTypes(ChannelType.CHANNEL_TYPE_PAGER_DUTY)
                .build();
        println("ListChannelTypes response:");
        printJson(lctr);
        println();

        String tenantId = "tenant1";

        // channel configuration
        UnaryChannelConfigResponse channelConfigResponse = UnaryChannelConfigResponse.newBuilder()
                .setInfo(generateResponseHeader())
                .setChannelConfig(
                        generateEmailChannelConfig(tenantId))
                .build();
        println("A channel config response:");
        printJson(channelConfigResponse);
        printSeparator();

        // list channel configs
        SeekPageChannelConfigResponse listChannelConfigsResponse = SeekPageChannelConfigResponse.newBuilder()
                .addChannelConfigs(generateEmailChannelConfig(tenantId))
                .addChannelConfigs(generateSmsChannelConfig(tenantId))
                .addChannelConfigs(generateSlackChannelConfig(tenantId))
                .build();
        println("A list channel config response:");
        printJson(listChannelConfigsResponse);

        printSeparator();
    }
    
    private static void printJson(MessageOrBuilder o) {
        println(toJson(o));
    }

    private static void printJson(String header, MessageOrBuilder o) {
        println(header);
        printJson(o);
    }

    private static String toJson(MessageOrBuilder o) {
        try {
            return JsonFormat.printer().print(o);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static void println() {
        System.out.println();
    }
    
    private static void println(String s) {
        System.out.println(s);
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

    private static ChannelConfig generateEmailChannelConfig(String tenantId) {
        return ChannelConfig.newBuilder()
                .setTenantId(tenantId)
                .setChannelType(ChannelType.CHANNEL_TYPE_EMAIL)
                .setEmailChannelConfig(ChannelConfig.EmailChannelConfig.newBuilder()
                        .setSmtpServer("mail.example.com")
                        .setPort(25)
                        .build())
                .build();
    }

    private static ChannelConfig generateSmsChannelConfig(String tenantId) {
        return ChannelConfig.newBuilder()
                .setTenantId(tenantId)
                .setChannelType(ChannelType.CHANNEL_TYPE_SMS)
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

    private static ChannelConfig generateSlackChannelConfig(String tenantId) {
        return ChannelConfig.newBuilder()
                .setTenantId(tenantId)
                .setChannelType(ChannelType.CHANNEL_TYPE_SLACK)
                .setSlackChannelConfig(ChannelConfig.SlackChannelConfig.newBuilder()
                        .setChannelName("example_slack_channel_name")
                        .build())
                .build();

    }

}
