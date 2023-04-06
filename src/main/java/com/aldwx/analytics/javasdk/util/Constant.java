package com.aldwx.analytics.javasdk.util;

import java.util.Arrays;
import java.util.List;

public class Constant {

    public static final String debugServerUrl = "http://test-slog.aldwx.com/d.html?ak=";
    public static final String onlineServerUrl = "http://slog.aldwx.com/d.html?ak=";

    public static final String ALD_PUBLIC_PROP_LOGIN_USER_ID="ald_login_id";
    public static final String ALD_PUBLIC_PROP_PLATFORM_TYPE="ald_platform_type";

    public static final List ALD_PLATFORM_TYPE_VALUE= Arrays.asList("Java","MiniProgram","Android","iOS","js","python","Web");
    public static final String ALD_PLATFORM_TYPE_JAVA= "Java";
    public static final String ALD_PLATFORM_TYPE_MP= "MiniProgram";
    public static final String ALD_PLATFORM_TYPE_ANDROId= "Android";
    public static final String ALD_PLATFORM_TYPE_IOS= "iOS";
    public static final String ALD_PLATFORM_TYPE_JS= "js";
    public static final String ALD_PLATFORM_TYPE_PY= "python";
}
