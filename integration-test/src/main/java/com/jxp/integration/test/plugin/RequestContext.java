package com.jxp.integration.test.plugin;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-28 11:33
 */
public class RequestContext {
    private static final ThreadLocal<Context> THREAD_LOCAL = ThreadLocal.withInitial(Context::new);


    public static String  getUserId(){
        Context requestContext = getRequestContext();
        if (null != requestContext){
            return getRequestContext().getUserId();
        }
        return null;
    }

    public static Context getRequestContext() {
        return THREAD_LOCAL.get();
    }

    public static void setRequestContext(Context context){
        Context c = getRequestContext();
        c.setUserId(context.getUserId());
        if (c.getRequestTimestamp() != 0) {
            c.setRequestTimestamp(context.getRequestTimestamp());
        }
    }

}
