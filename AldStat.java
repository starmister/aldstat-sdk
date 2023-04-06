package com.aldwx.analytics.javasdk;

import com.sensorsdata.analytics.javasdk.ISensorsAnalytics;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.bean.*;
import com.sensorsdata.analytics.javasdk.consumer.Consumer;
import com.sensorsdata.analytics.javasdk.exceptions.InvalidArgumentException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.aldwx.analytics.javasdk.util.Constant.*;

@Slf4j
public class AldStat implements ISensorsAnalytics {
    SensorsAnalytics sa;
    public AldStat(Consumer consumer) {
        sa = new SensorsAnalytics(consumer);
    }

    @Override
    public void setEnableTimeFree(@NonNull boolean enableTimeFree) {
        sa.setEnableTimeFree(enableTimeFree);
    }

    public void registerSuperProperties(@NonNull SuperPropertiesRecord propertiesRecord) {
        Object loginUserID = propertiesRecord.getPropertyMap().get(ALD_PUBLIC_PROP_LOGIN_USER_ID);
        if(loginUserID==null) {
            log.warn("login_user_id is a public attribute that must be passed, please set it and then report it");
            return;
        }
        Object aldPlatformType = propertiesRecord.getPropertyMap().get(ALD_PUBLIC_PROP_PLATFORM_TYPE);
        if(aldPlatformType==null) {
            log.warn("ald_platform_type is a public attribute that must be passed, please set it and then report it");
            return;
        }
        if(!ALD_PLATFORM_TYPE_VALUE.contains(aldPlatformType.toString())) {
            log.warn("ald_platform_type value must be Java or MiniProgram or Android or iOS or js or python");
            return;
        }
        propertiesRecord.getPropertyMap().put("$is_login_id",false);
        sa.registerSuperProperties(propertiesRecord);
    }

    @Override
    public void registerSuperProperties(@NonNull Map<String, Object> superPropertiesMap) {
        sa.registerSuperProperties(superPropertiesMap);
    }

    @Override
    public void clearSuperProperties() {
        sa.clearSuperProperties();
    }

    public void track(@NonNull EventRecord eventRecord) throws InvalidArgumentException {
        sa.track(eventRecord);
    }

    @Override
    public void track(@NonNull String distinctId, @NonNull boolean isLoginId, @NonNull String eventName) throws InvalidArgumentException {
        sa.track(distinctId,isLoginId,eventName);
    }

    @Override
    public void track(@NonNull String distinctId, @NonNull boolean isLoginId, @NonNull String eventName, Map<String, Object> properties) throws InvalidArgumentException {
        sa.track(distinctId,isLoginId,eventName,properties);
    }

    @Override
    public void trackSignUp(@NonNull String loginId, @NonNull String anonymousId) throws InvalidArgumentException {
        sa.trackSignUp(loginId,anonymousId);
    }

    @Override
    public void trackSignUp(@NonNull String loginId, @NonNull String anonymousId, Map<String, Object> properties) throws InvalidArgumentException {
        sa.trackSignUp(loginId,anonymousId,properties);
    }

    @Override
    public void profileSet(@NonNull UserRecord userRecord) throws InvalidArgumentException {
        sa.profileSet(userRecord);
    }

    @Override
    public void profileSet(@NonNull String distinctId, @NonNull boolean isLoginId, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileSet(distinctId,isLoginId,properties);
    }

    public void profileSet(@NonNull String distinctId, @NonNull boolean isLoginId, @NonNull String property, @NonNull Object value) throws InvalidArgumentException {
        sa.profileSet(distinctId,isLoginId,property,value);
    }

    @Override
    public void profileSetOnce(@NonNull UserRecord userRecord) throws InvalidArgumentException {
        sa.profileSetOnce(userRecord);
    }

    @Override
    public void profileSetOnce(@NonNull String distinctId, @NonNull boolean isLoginId, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileSetOnce(distinctId, isLoginId, properties);
    }

    @Override
    public void profileSetOnce(@NonNull String distinctId, @NonNull boolean isLoginId, @NonNull String property, @NonNull Object value) throws InvalidArgumentException {
        sa.profileSetOnce(distinctId, isLoginId, property, value);
    }

    @Override
    public void profileIncrement(@NonNull UserRecord userRecord) throws InvalidArgumentException {
        sa.profileIncrement(userRecord);
    }

    @Override
    public void profileIncrement(@NonNull String distinctId, @NonNull boolean isLoginId, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileIncrement(distinctId, isLoginId, properties);
    }

    @Override
    public void profileIncrement(@NonNull String distinctId, @NonNull boolean isLoginId, @NonNull String property, @NonNull long value) throws InvalidArgumentException {
        sa.profileIncrement(distinctId,isLoginId,property,value);
    }

    @Override
    public void profileAppend(@NonNull UserRecord userRecord) throws InvalidArgumentException {
        sa.profileAppend(userRecord);
    }

    @Override
    public void profileAppend(@NonNull String distinctId, @NonNull boolean isLoginId, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileAppend(distinctId,isLoginId,properties);
    }

    @Override
    public void profileAppend(@NonNull String distinctId, @NonNull boolean isLoginId, @NonNull String property, @NonNull String value) throws InvalidArgumentException {
        sa.profileAppend(distinctId,isLoginId,property,value);
    }

    @Override
    public void profileUnset(@NonNull UserRecord userRecord) throws InvalidArgumentException {
        sa.profileUnset(userRecord);
    }

    @Override
    public void profileUnset(@NonNull String distinctId, @NonNull boolean isLoginId, @NonNull String property) throws InvalidArgumentException {
        sa.profileUnset(distinctId,isLoginId,property);
    }

    @Override
    public void profileUnset(@NonNull String distinctId, @NonNull boolean isLoginId, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileUnset(distinctId,isLoginId,properties);
    }

    @Override
    public void profileDelete(@NonNull UserRecord userRecord) throws InvalidArgumentException {
        sa.profileDelete(userRecord);
    }

    @Override
    public void profileDelete(@NonNull String distinctId, @NonNull boolean isLoginId) throws InvalidArgumentException {
        sa.profileDelete(distinctId,isLoginId);
    }

    @Override
    public void itemSet(@NonNull ItemRecord itemRecord) throws InvalidArgumentException {
        sa.itemSet(itemRecord);
    }

    @Override
    public void itemSet(@NonNull String itemType, @NonNull String itemId, @NonNull Map<String, Object> properties) throws InvalidArgumentException {
        sa.itemSet(itemType,itemId,properties);
    }

    @Override
    public void itemDelete(@NonNull ItemRecord itemRecord) throws InvalidArgumentException {
        sa.itemDelete(itemRecord);
    }

    @Override
    public void itemDelete(@NonNull String itemType, @NonNull String itemId, Map<String, Object> properties) throws InvalidArgumentException {
        sa.itemDelete(itemType,itemId,properties);
    }

    @Override
    public void bind(@NonNull SensorsAnalyticsIdentity... sensorsAnalyticsIdentities) throws InvalidArgumentException {
        sa.bind(sensorsAnalyticsIdentities);
    }

    @Override
    public void unbind(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity) throws InvalidArgumentException {
        sa.unbind(sensorsAnalyticsIdentity);
    }

    @Override
    public void trackById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, @NonNull String eventName, Map<String, Object> properties) throws InvalidArgumentException {
        sa.trackById(sensorsAnalyticsIdentity,eventName,properties);
    }

    @Override
    public void profileSetById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileSetById(sensorsAnalyticsIdentity,properties);
    }

    @Override
    public void profileSetById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, @NonNull String property, @NonNull Object value) throws InvalidArgumentException {
        sa.profileSetById(sensorsAnalyticsIdentity,property,value);
    }

    @Override
    public void profileSetOnceById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileSetOnceById(sensorsAnalyticsIdentity,properties);
    }

    @Override
    public void profileSetOnceById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, @NonNull String property, @NonNull Object value) throws InvalidArgumentException {
        sa.profileSetOnceById(sensorsAnalyticsIdentity,property,value);
    }

    @Override
    public void profileIncrementById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileIncrementById(sensorsAnalyticsIdentity,properties);
    }

    @Override
    public void profileIncrementById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, String property, long value) throws InvalidArgumentException {
        sa.profileIncrementById(sensorsAnalyticsIdentity,property,value);
    }

    @Override
    public void profileAppendById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileAppendById(sensorsAnalyticsIdentity,properties);
    }

    @Override
    public void profileAppendById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, @NonNull String property, @NonNull String value) throws InvalidArgumentException {
        sa.profileAppendById(sensorsAnalyticsIdentity,property,value);
    }

    @Override
    public void profileUnsetById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, Map<String, Object> properties) throws InvalidArgumentException {
        sa.profileUnsetById(sensorsAnalyticsIdentity,properties);
    }

    @Override
    public void profileUnsetById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity, @NonNull String property) throws InvalidArgumentException {
        sa.profileUnsetById(sensorsAnalyticsIdentity,property);
    }

    @Override
    public void profileDeleteById(@NonNull SensorsAnalyticsIdentity sensorsAnalyticsIdentity) throws InvalidArgumentException {
        sa.profileDeleteById(sensorsAnalyticsIdentity);
    }

    @Override
    public void trackById(@NonNull IDMEventRecord idmEventRecord) throws InvalidArgumentException {
        sa.trackById(idmEventRecord);
    }

    @Override
    public void profileSetById(@NonNull IDMUserRecord idmUserRecord) throws InvalidArgumentException {
        sa.profileSetById(idmUserRecord);
    }

    @Override
    public void profileSetOnceById(@NonNull IDMUserRecord idmUserRecord) throws InvalidArgumentException {
        sa.profileSetOnceById(idmUserRecord);
    }

    @Override
    public void profileIncrementById(@NonNull IDMUserRecord idmUserRecord) throws InvalidArgumentException {
        sa.profileIncrementById(idmUserRecord);
    }

    @Override
    public void profileAppendById(@NonNull IDMUserRecord idmUserRecord) throws InvalidArgumentException {
        sa.profileAppendById(idmUserRecord);
    }

    @Override
    public void profileUnsetById(@NonNull IDMUserRecord idmUserRecord) throws InvalidArgumentException {
        sa.profileUnsetById(idmUserRecord);
    }

    @Override
    public void flush() {
        sa.flush();
    }

    @Override
    public void shutdown() {
        sa.shutdown();
    }
}
