package com.jxp.integration.test.plugin;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-28 11:33
 */
public class RequestContext {
    private static final ThreadLocal<Context> THREAD_LOCAL = ThreadLocal.withInitial(Context::new);


    public static String getUserId() {
        Context requestContext = getRequestContext();
        if (null != requestContext) {
            return requestContext.getUserId();
        }

        return null;
    }

    public static long getRequestTimestamp() {
        Context requestContext = getRequestContext();
        if (null != requestContext) {
            return requestContext.getRequestTimestamp();
        }
        return 0;
    }

    public static Context getRequestContext() {
        return THREAD_LOCAL.get();
    }

    public static void setRequestContext(Context context) {
        Context c = getRequestContext();
        c.setUserId(context.getUserId());
        c.setAnonymous(context.getAnonymous());
        if (c.getRequestTimestamp() == null) {
            c.setRequestTimestamp(System.currentTimeMillis());
        }
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
