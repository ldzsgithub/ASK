package com.jy23.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class L<T> {
    private static String INDEX_STRING = "共%d条记录";

    public synchronized static void e(Object[] msg) {
        int index = 0;
        StringBuffer sb = new StringBuffer();
        sb.append(getTargetStackTraceElement().toString() + "\n");
        for (Object o : msg) {
            sb.append(o + "\n");
            index++;
        }
        print(sb.toString() + String.format(INDEX_STRING, index));
    }

    public synchronized static <T> void e(T msg) {
        if (msg == null) return;
        int index = 0;
        StringBuffer sb = new StringBuffer();
        sb.append(getTargetStackTraceElement().toString() + "\n");
        if (msg instanceof Number || msg instanceof Boolean || msg instanceof String) {
            sb.append(String.valueOf(msg) + "\n");
            index++;
        } else if (msg instanceof Iterable) {
            Iterator iterator = ((Iterable) msg).iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next() + "\n");
                index++;
            }
        } else if (msg instanceof Map) {
            //迭代遍历Map集合的第二种方式
            Set<Map.Entry> set = ((Map) msg).entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key=  entry.getKey().toString();
                StringBuilder values=new StringBuilder();
                if(entry.getValue() instanceof Iterator){
                    Iterator iterator = ((Iterator) entry.getValue());
                    while (iterator.hasNext()) {
                        values.append(iterator.next()+",");
                    }
                }else if(entry.getValue() instanceof Object[]){
                    for (Object o : (Object[])entry.getValue() ) {
                        values.append(o + ",");
                    }
                }else{
                    values.append(entry.getValue().toString());
                }
                sb.append(key + "=" + values.toString()+ "\n");
                index++;
            }
        } else {
            sb.append(msg.toString() + "\n");
        }

        print(sb.toString() + String.format(INDEX_STRING, index));
    }

    public synchronized static <T> void ee(T msg) {
        if (msg == null) return;

        int index = 0;
        StringBuffer sb = new StringBuffer();

        if (msg instanceof Number || msg instanceof Boolean || msg instanceof String) {
            sb.append(String.valueOf(msg) + "\n");
            index++;
        } else if (msg instanceof Iterable) {
            Iterator iterator = ((Iterable) msg).iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next() + "\n");
                index++;
            }
        } else {
            sb.append(msg.toString() + "\n");
            index++;
        }
        print(sb.toString() + String.format(INDEX_STRING, index));
    }

    private static void print(String s) {
        System.out.println(s);
    }

    private static StackTraceElement getTargetStackTraceElement() {
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(L.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }
}
