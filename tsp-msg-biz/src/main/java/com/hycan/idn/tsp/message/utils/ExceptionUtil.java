package com.hycan.idn.tsp.message.utils;

/**
 * 异常工具类
 *
 * @author liangliang
 * @date 2022/12/26
 */
public class ExceptionUtil {
    /**
     * 违约行
     */
    private static final int DEFAULT_LINE = 10;

    /**
     * 得到异常原因
     *
     * @param e e
     * @return {@link String}
     */
    public static String getExceptionCause(Throwable e) {
        StringBuilder sb;
        for (sb = new StringBuilder(); e != null; e = e.getCause()) {
            sb.append(e).append('\n');
        }

        return sb.toString();
    }

    /**
     * 得到所有异常堆栈跟踪
     *
     * @param e e
     * @return {@link String}
     */
    public static String getAllExceptionStackTrace(Throwable e) {
        if (e == null) {
            return "";
        } else {
            final StringBuilder stackTrace = new StringBuilder(e.toString());
            final StackTraceElement[] astacktraceelement = e.getStackTrace();
            final int var4 = astacktraceelement.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                final StackTraceElement anAstacktraceelement = astacktraceelement[var5];
                stackTrace.append("\r\n\tat ").append(anAstacktraceelement);
            }

            return stackTrace.toString();
        }
    }

    /**
     * 得到异常堆栈跟踪
     *
     * @param e       e
     * @param lineNum 行num
     * @return {@link String}
     */
    public static String getExceptionStackTrace(Throwable e, int lineNum) {
        if (e == null) {
            return "";
        } else {
            final StringBuilder stackTrace = new StringBuilder(e.toString());
            final StackTraceElement[] astacktraceelement = e.getStackTrace();
            final int size = lineNum > astacktraceelement.length ? astacktraceelement.length : lineNum;
            for (int i = 0; i < size; ++i) {
                stackTrace.append("\r\n\tat ").append(astacktraceelement[i]);
            }

            return stackTrace.toString();
        }
    }

    /**
     * 得到短暂堆栈跟踪
     *
     * @param e e
     * @return {@link String}
     */
    public static String getBriefStackTrace(Throwable e) {
        return getExceptionStackTrace(e, DEFAULT_LINE);
    }
}
