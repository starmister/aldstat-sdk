package com.aldwx.analytics.javasdk;

import com.aldwx.analytics.javasdk.consumer.AldDebugConsumer;
import com.sensorsdata.analytics.javasdk.ISensorsAnalytics;
import com.sensorsdata.analytics.javasdk.bean.IDMEventRecord;
import com.sensorsdata.analytics.javasdk.bean.SuperPropertiesRecord;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;

import java.util.Calendar;

import static com.aldwx.analytics.javasdk.util.Constant.ALD_PLATFORM_TYPE_MP;

public class HelloAldStat {

    public static void main(String[] args) throws InvalidArgumentException {
        ISensorsAnalytics aldStat = new AldStat(new AldDebugConsumer("xxxxxx", true,true));
//        SensorsAnalyticsIdentity identity = SensorsAnalyticsIdentity.builder()
//                .addIdentityProperty("identity_distinct_id", "ABCDEF123456789")
//                .addIdentityProperty("identity_anonymous_id", "ABCDEF123456789")
//                .build();
//        aldStat.bind(identity);
//        ISensorsAnalytics aldStat = new AldStat(new AldBatchConsumer("xxxxxxx",true));



        SuperPropertiesRecord propertiesRecord = SuperPropertiesRecord.builder()
                .addProperty("ald_login_id","123456788")
                .addProperty("ald_platform_type",ALD_PLATFORM_TYPE_MP).build();
        aldStat.registerSuperProperties(propertiesRecord);
        String distinctId = "ABCDEF123456789";
//        EventRecord firstRecord = EventRecord.builder().setDistinctId(cookieId).isLoginId(Boolean.FALSE)
//                        .setEventName("AldJavaSdkTest")
//                .addProperty("$time", Calendar.getInstance().getTime())
//                .addProperty("ald_name", "ald")
//                .addProperty("ald_tool", "java-sdk")
//                .addProperty("ald_author", "xxxx")
//                .addProperty("ald_test", "xxxxx").build();
//        aldStat.track(firstRecord);
//        EventRecord searchRecord = EventRecord.builder().setDistinctId(cookieId).isLoginId(Boolean.FALSE).setEventName("SearchProduct").addProperty("KeyWord", "XX手机").build();
//        sa.track(searchRecord);

//        String distinctId = "ABCDEF123456789";
//        List<String> productIdList = new ArrayList<String>();
//        productIdList.add("123456");
//        productIdList.add("234567");
//        productIdList.add("345678");

        //String registerId = "0012345678";
        // 用户注册/登录时，将用户登录 ID 与匿名 ID 关联
        //aldStat.trackSignUp(registerId, cookieId);

        IDMEventRecord eventRecord = IDMEventRecord.starter().setDistinctId(distinctId)
                .addIdentityProperty("identity_distinct_id", distinctId) //用户维度标识
                .addIdentityProperty("identity_anonymous_id",distinctId)
                .setEventName("JavaSdk")
                .addProperty("$time", Calendar.getInstance().getTime())
                .addProperty("ald_name", "ald")
                .addProperty("ald_tool", "java-sdk")
                .addProperty("ald_author", "xxxxx")
                .addProperty("ald_test", "xxxx")
                .addProperty("ald_lib", "Java")
                .build();

//        IDMEventRecord eventRecord = IDMEventRecord.starter()
//                .addIdentityProperty("identity_distinct_id", cookieId) //用户维度标识
//                .addIdentityProperty("identity_anonymous_id",cookieId)
//                .setEventName("JavaSdkTest")
//                .addProperty("$time", Calendar.getInstance().getTime())
//                .addProperty("name", "ald")
//                .addProperty("tool", "java-sdk")
//                .addProperty("author", "xxxxx")
//                .addProperty("test", "xxxx")
//                .build();
        aldStat.trackById(eventRecord);
        aldStat.flush();

        aldStat.shutdown();
    }
}
