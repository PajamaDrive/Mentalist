//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mentalist.utils;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.websocket.Session;

public class WebSocketUtils {
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private static ConcurrentHashMap<String, GeneralNegotiator> userMap = new ConcurrentHashMap<>();

    public static synchronized void send(String msg, Session session) {
        try {
            if (session.isOpen()) {
                String name = (getUser(session) == null) ? session.getId() : getUser(session).getName();
                if (!msg.contains("\"tag\":\"menu\"") && !msg.contains("\"tag\":\"notify-thread\""))
                    ServletUtils.log("<FROM Server> <TO " + name + ">: " + msg, ServletUtils.DebugLevels.DEBUG);
                session.getBasicRemote().sendText(msg);
            } else {
                ServletUtils.log("session closed... Can't send: " + msg + " to " + session.getId(), ServletUtils.DebugLevels.WARN);
            }
        } catch (IOException e) {
            ServletUtils.log("IO error!", ServletUtils.DebugLevels.ERROR);
            e.printStackTrace();
            close(session);
        }
    }

    public static void send(String msg, GeneralNegotiator user) {
        send(msg, user.getSession());
    }

    public static void broadcast(String msg) {
        for (GeneralNegotiator user : userMap.values())
            send(msg, user);
    }

    public static GeneralNegotiator getUser(Session session) {
        return userMap.get(session.getId());
    }

    public static void delay(Runnable r, long delay) {
        worker.schedule(r, delay, TimeUnit.MILLISECONDS);
    }

    public static void delayedSend(final String msg, final GeneralNegotiator user, int delay) {
        delay(new Runnable() {
            public void run() {
                WebSocketUtils.send(msg, user);
            }
        }, (long)delay);
    }

    public static void close(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void newUser(GeneralNegotiator user) {
        userMap.put(user.getSession().getId(), user);
    }

    public static void newVH(GeneralVH user) {
        userMap.put("vh" + user.getSession().getId(), user);
    }

    public static GeneralNegotiator getVH(Session session) {
        return userMap.get("vh" + session.getId());
    }

    public class JsonObject {
        public String tag;

        public Object data;

        public JsonObject(String tag, Object data) {
            this.tag = tag;
            this.data = data;
        }
    }
}