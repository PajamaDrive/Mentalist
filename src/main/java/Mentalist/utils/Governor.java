package Mentalist.utils;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

public final class Governor {
    public enum SessionState {
        WAITING, PENDING, NEGOTIATING, ENDED;
    }

    private static List<NegotiationRoom> availFGBU = new ArrayList<>();

    private static List<NegotiationRoom> activeFGBU = new ArrayList<>();

    private static HashMap<HttpSession, SessionState> sessionMap = new HashMap<>();

    public static synchronized boolean register(HttpSession httpSession, Session session) {
        System.out.println("Govenor Registering");
        System.out.println("New user " + httpSession);
        if (sessionMap.containsKey(httpSession))
            return false;
        sessionMap.put(httpSession, SessionState.WAITING);
        if (availFGBU.isEmpty()) {
            NegotiationRoom fgbu = new NegotiationRoom();
            fgbu.registerUser(httpSession, session);
            availFGBU.add(fgbu);
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject( "WAIT", "You will be waiting.")), session);
        } else {
            NegotiationRoom fgbu = availFGBU.get(0);
            fgbu.registerUser(httpSession, session);
            if (!fgbu.available()) {
                activeFGBU.add(fgbu);
                availFGBU.remove(0);
                NegotiationRoom.UserSession user = fgbu.getAdversary(httpSession, session);
                sessionMap.put(httpSession, SessionState.PENDING);
                sessionMap.put(user.httpSession, SessionState.PENDING);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject( "MATCH", "You have been matched to a negotiation.")), session);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject( "MATCH", "You have been matched to a negotiation.")), user.session);
            } else {
                WebSocketUtils.send((new Gson())
                        .toJson(new WebSocketUtils().new JsonObject( "WAIT", "You will be waiting.")), session);
            }
        }
        System.out.println("active FGBU");
        for (NegotiationRoom f : activeFGBU)
            System.out.println(f.getUserString());
        System.out.println("Available FGBU");
        for (NegotiationRoom f : availFGBU)
            System.out.println(f.getUserString());
        System.out.println("finished");
        return true;
    }

    public static synchronized void exitInWaiting(HttpSession httpSession, Session session) {
        if (getSessionState(httpSession) == SessionState.WAITING) {
            for (NegotiationRoom f : availFGBU)
                f.exitInWaiting(httpSession, session);
            sessionMap.remove(httpSession);
            ServletUtils.log("" + httpSession + " is removed", ServletUtils.DebugLevels.DEBUG);
            System.out.println(sessionMap.get(httpSession));
        }
    }

    public static synchronized NegotiationRoom findUserByHttpSession(HttpSession httpSession) {
        for (NegotiationRoom f : activeFGBU) {
            if (f.findUserByHttpSession(httpSession) == true)
                return f;
        }
        return null;
    }

    public static synchronized SessionState getSessionState(HttpSession httpSession) {
        if (!sessionMap.containsKey(httpSession))
            return null;
        return sessionMap.get(httpSession);
    }

    public static synchronized void startNegotiating(HttpSession httpSession) {
        sessionMap.put(httpSession, SessionState.NEGOTIATING);
    }

    public static synchronized void removeUser(HttpSession httpSession) {
        NegotiationRoom f = findUserByHttpSession(httpSession);
        f.exitInNegotiation(httpSession);
        sessionMap.remove(httpSession);
    }
}