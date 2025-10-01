package com.fastcode.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class NotificationPropertiesConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(NotificationPropertiesConfiguration.class);

    @Autowired
    private Environment env;

    private static final String NOTIFICATION_AUTH_HEADER_NAME_ENV = "NOTIFICATION_AUTH_HEADER_NAME";
    private static final String NOTIFICATION_AUTH_HEADER_NAME_SYSPROP = "notification.auth.header.name";

    private static final String NOTIFICATION_JWT_SECRET_KEY_ENV = "NOTIFICATION_JWT_SECRET_KEY";
    private static final String NOTIFICATION_JWT_SECRET_KEY_SYSPROP = "notification.jwt.secret.key";

    private static final String NOTIFICATION_DEFAULT_RECIPIENT_NAME_ENV = "NOTIFICATION_DEFAULT_RECIPIENT_NAME";
    private static final String NOTIFICATION_DEFAULT_RECIPIENT_NAME_SYSPROP = "notification.default.recipient.name";

    private static final String FASTCODE_OFFSET_DEFAULT_ENV = "FASTCODE_OFFSET_DEFAULT";
    private static final String FASTCODE_OFFSET_DEFAULT_SYSPROP = "fastCode.offset.default";

    private static final String FASTCODE_LIMIT_DEFAULT_ENV = "FASTCODE_LIMIT_DEFAULT";
    private static final String FASTCODE_LIMIT_DEFAULT_SYSPROP = "fastCode.limit.default";

    private static final String FASTCODE_SORT_DIRECTION_DEFAULT_ENV = "FASTCODE_SORT_DIRECTION_DEFAULT";
    private static final String FASTCODE_SORT_DIRECTION_DEFAULT_SYSPROP = "fastCode.sort.direction.default";

    /**
     * @return the Notification Auth Header Name
     */
    public String getNotificationAuthHeaderName() {
        return getConfigurationProperty(NOTIFICATION_AUTH_HEADER_NAME_ENV, NOTIFICATION_AUTH_HEADER_NAME_SYSPROP, "Authorization");
    }

    /**
     * @return the Notification Jwt Secret Key
     */
    public String getNotificationJwtSecretKey() {
        return getConfigurationProperty(NOTIFICATION_JWT_SECRET_KEY_ENV, NOTIFICATION_JWT_SECRET_KEY_SYSPROP, "ApplicationSecureSecretKeyToGenerateJWTsTokenForAuthenticationAndAuthorization");
    }

    /**
     * @return the Notification Default Recipient Name
     */
    public String getNotificationDefaultRecipientName() {
        return getConfigurationProperty(NOTIFICATION_DEFAULT_RECIPIENT_NAME_ENV, NOTIFICATION_DEFAULT_RECIPIENT_NAME_SYSPROP, "Default");
    }


    /**
     * @return the default offset for fastCode
     */
    public String getFastCodeOffsetDefault() {
        return getConfigurationProperty(FASTCODE_OFFSET_DEFAULT_ENV, FASTCODE_OFFSET_DEFAULT_SYSPROP, "0");
    }

    /**
     * @return the default limit for fastCode
     */
    public String getFastCodeLimitDefault() {
        return getConfigurationProperty(FASTCODE_LIMIT_DEFAULT_ENV, FASTCODE_LIMIT_DEFAULT_SYSPROP, "10");
    }

    /**
     * @return the default sort direction for fastCode
     */
    public String getFastCodeSortDirectionDefault() {
        return getConfigurationProperty(FASTCODE_SORT_DIRECTION_DEFAULT_ENV, FASTCODE_SORT_DIRECTION_DEFAULT_SYSPROP, "ASC");
    }




    /**
     * Looks for the given key in the following places (in order):
     * 1) Environment variables
     * 2) System Properties
     *
     * @param envKey
     * @param sysPropKey
     * @param defaultValue
     * @return the configured property value or default value if not found
     */
    private String getConfigurationProperty(String envKey, String sysPropKey, String defaultValue) {
        String value = env.getProperty(sysPropKey);
        if (value == null || value.trim().isEmpty()) {
            value = System.getenv(envKey);
        }
        if (value == null || value.trim().isEmpty()) {
            value = defaultValue;
        }
        logger.debug("Config Property: {}/{} = {}", envKey, sysPropKey, value);
        return value;
    }
}
