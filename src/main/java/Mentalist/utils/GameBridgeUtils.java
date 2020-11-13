package Mentalist.utils;

import Mentalist.agent.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.DatabaseMetaData;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import net.sf.image4j.codec.ico.ICOEncoder;

public class GameBridgeUtils {
    private GeneralVH selectedAgent0;
    private Mediator mediator0 = new Mediator(this);
    private VHRunner runner0 = new VHRunner(0, this.mediator0, this.selectedAgent0, this);
    private Thread thread0 = new Thread(this.runner0);
    private GeneralVH selectedAgent1;
    private Mediator mediator1 = new Mediator(this);
    private VHRunner runner1 = new VHRunner(1, this.mediator1, this.selectedAgent1, this);
    private Thread thread1 = new Thread(this.runner1);
    private boolean isMultiAgent = false;
    private GeneralNegotiator user = null;
    private String userName;
    private String MTurkID;
    private GameSpec spec;
    private int currentGame = 0;
    private String menuLoc = "root";
    private History history = new History();
    private String webDir;
    private int questionnaireNeuroticism = 0;
    private int questionnaireExtraversion = 0;
    private int questionnaireOpenness = 0;
    private int questionnaireConscientiouness = 0;
    private int questionnaireAgreeableness = 0;

    enum AcceptanceState {
        NO_ACCEPTANCE, USER_ACCEPTED, OPPONENT_ACCEPTED, FULL_ACCEPTANCE;
    }

    private AcceptanceState astate = AcceptanceState.NO_ACCEPTANCE;
    private int[][] issue_locs;
    private Preference preferenceState = new Preference();
    private String[] comparisonState = new String[]{" ", "something ", "more than ", "something", " "};
    private int itemInHand = -1;
    private int rowInHand = -1;
    private int quantInHand = 0;
    private int comparisonItemInHand = -1;
    private String oldContents = "something";
    private NegotiationMode nMode;
    private HttpSession httpSession;
    private Session wsSession;
    private NegotiationRoom nRoom;

    public enum NegotiationMode {
        HUMAN_HUMAN, HUMAN_AGENT, AGENT_AGENT;
    }

    private boolean dataWritten = false;
    private boolean timedOut = false;
    private boolean formallyQuit = false;
    private boolean finalOfferWarning = false;

    enum QuitState {
        NO_WARNED, USER_WARNED, OPPONENT_WARNED, FULL_QUIT;
    }

    private QuitState qstate = QuitState.NO_WARNED;
    private int userPresentedBATNA = 0;
    private Map<String, String> surveyData = new LinkedHashMap<>();
    private final int QUEUE_MAX = 15;
    private LinkedList<Event> eventQueue = new LinkedList<>();
    private boolean multi;
    Logger logger = Logger.getLogger(GameBridgeUtils.class.getName());

    public GameBridgeUtils(GameSpec spec) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            String warn = "Note that no security manager is present.  While this is allowed now, new versions of IAGO will not run without one!";
            ServletUtils.log(warn, ServletUtils.DebugLevels.WARN);
        }
        this.spec = spec;
        this.issue_locs = new int[spec.getNumIssues()][];
        for (int i = 0; i < spec.getNumIssues(); i++) {
            this.issue_locs[i] = new int[]{0, spec.getIssueQuants()[i], 0};
        }
    }

    public void setwebDir(String webDir){ this.webDir = webDir; }

    public void setNewAgent(GeneralVH vh){
        this.selectedAgent1 = vh;
        this.runner1.setVH(vh);
        WebSocketUtils.newVH(this.selectedAgent1);
        if (this.isMultiAgent) {
            WebSocketUtils.newVH(this.selectedAgent0);
            this.selectedAgent0.pairWith(this.selectedAgent1);
            this.history.addNegotiator(this.selectedAgent0);
        } else {
            this.user.pairWith(this.selectedAgent1);
            this.history.addNegotiator(this.user);
        }
        this.history.addNegotiator(this.selectedAgent1);
    }

    public void setGameSpec(GameSpec spec) {
        this.spec = spec;
        this.currentGame++;
        this.surveyData = new HashMap<>();
        ServletUtils.log("Resetting many things for new game.", ServletUtils.DebugLevels.DEBUG);
        this.issue_locs = new int[spec.getNumIssues()][];
        for (int i = 0; i < spec.getNumIssues(); i++) {
            this.issue_locs[i] = new int[]{0, spec.getIssueQuants()[i], 0};
        }

        this.menuLoc = "root";
        this.itemInHand = -1;
        this.rowInHand = -1;
        this.quantInHand = 0;
        this.comparisonItemInHand = -1;
        this.astate = AcceptanceState.NO_ACCEPTANCE;
        this.qstate = QuitState.NO_WARNED;
        this.preferenceState = new Preference();
        this.oldContents = "something";
        this.comparisonState = new String[]{" ", "something ", "more than ", "something", " "};
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("intermediate", this.spec.getNewgameMessage())), this.wsSession);
        this.mediator0.setGameSpec(spec);
        this.mediator1.setGameSpec(spec);
        this.dataWritten = false;
        this.finalOfferWarning = false;
        configureCustomCursors();
    }

    public void onOpenHelper(Session session, EndpointConfig config, GeneralVH agent0, GeneralVH agent1) throws MessagingException {
        if (!ServletUtils.mailReady() && ServletUtils.isDataModeEmail())
            throw new MessagingException("Error: use ServletUtils.setCredentials to set username and password");
        this.wsSession = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        ServletUtils.log("Server Socket Open!", ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("eventSession opened " + session.getId(), ServletUtils.DebugLevels.DEBUG);
        this.userName = (String) this.httpSession.getAttribute("user");
        if (this.userName == null || this.userName.equals("null")) {
            this.MTurkID = (String) this.httpSession.getAttribute("MTurkID");
            this.userName = this.MTurkID;
        }
        this.questionnaireNeuroticism = (Integer)httpSession.getAttribute("neuroticism");
        this.questionnaireExtraversion = (Integer)httpSession.getAttribute("extraversion");
        this.questionnaireOpenness = (Integer)httpSession.getAttribute("openness");
        this.questionnaireConscientiouness = (Integer)httpSession.getAttribute("conscientiousness");
        this.questionnaireAgreeableness = (Integer)httpSession.getAttribute("agreeableness");
        ServletUtils.log("Registering user to game session: " + this.userName, ServletUtils.DebugLevels.DEBUG);
        this.user = new GeneralNegotiator(this.userName, this.spec, this.wsSession);
        WebSocketUtils.newUser(this.user);
        this.selectedAgent0 = agent0;
        if (this.selectedAgent0 != null) {
            this.runner0.setVH(agent0);
            this.isMultiAgent = true;
        }
        this.selectedAgent1 = agent1;
        this.runner1.setVH(agent1);
        ServletUtils.log("Agent Selected (user's agent): " + ((this.selectedAgent0 == null) ? "null!" : this.selectedAgent0.getName()), ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Agent Selected (computer agent): " + ((this.selectedAgent1 == null) ? "null!" : this.selectedAgent1.getName()), ServletUtils.DebugLevels.DEBUG);
        WebSocketUtils.newVH(this.selectedAgent1);
        if (this.isMultiAgent) {
            WebSocketUtils.newVH(this.selectedAgent0);
            this.selectedAgent0.pairWith(this.selectedAgent1);
            this.history.addNegotiator(this.selectedAgent0);
        } else {
            this.user.pairWith(this.selectedAgent1);
            this.history.addNegotiator(this.user);
        }
        this.history.addNegotiator(this.selectedAgent1);
        configureCustomCursors();
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("intermediate", this.spec.getNewgameMessage())), this.wsSession);
    }

    private void setRunnerSpecs() {
        this.mediator0.setGameSpec(this.spec);
        this.runner0.setSession(this.wsSession);
        this.mediator1.setGameSpec(this.spec);
        this.runner1.setSession(this.wsSession);
    }

    public void onCloseHelper(Session session, CloseReason cr) {
        if (session != null && cr != null)
            ServletUtils.log("eventSession closed " + session.getId() + "--" + cr.toString(), ServletUtils.DebugLevels.DEBUG);
        if (this.astate != AcceptanceState.FULL_ACCEPTANCE)
            this.nRoom.handleClose(this.httpSession);
    }

    void handleClose() {
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("opponentLeft", "")), this.wsSession);
    }

    public void onMessageHelper(Session session, final String msg, boolean last) {
        WebSocketUtils.JsonObject joIn = AccessController.<WebSocketUtils.JsonObject>doPrivileged(new PrivilegedAction() {
            public Object run() {
                return (new Gson()).fromJson(msg, WebSocketUtils.JsonObject.class);
            }
        });
        String name = (WebSocketUtils.getUser(session) == null) ? session.getId() : WebSocketUtils.getUser(session).getName();
        if (!joIn.tag.equals("time") && !msg.contains("\"tag\":\"notify-thread\""))
            ServletUtils.log("<FROM " + name + "> <TO Server>: " + msg, ServletUtils.DebugLevels.DEBUG);
        if (joIn.tag.equals("update-internal")) {
            String data = joIn.data.toString();
            try {
                GsonBuilder builder = new GsonBuilder();
                Object o = builder.create().fromJson(data, Object.class);
                int[][] ltm = null;
                try {
                    ltm = (int[][]) o;
                    ServletUtils.log("SURVEY LTM " + ltm, ServletUtils.DebugLevels.DEBUG);
                } catch (Exception e) {
                    ServletUtils.log("Survey cast failed", ServletUtils.DebugLevels.DEBUG);
                }
                ServletUtils.log("GSON RESULT: " + data, ServletUtils.DebugLevels.DEBUG);
            } catch (NullPointerException e) {
                ServletUtils.log("NULL SURVEY DATA ", ServletUtils.DebugLevels.DEBUG);
            }
        } else if (joIn.tag.equals("request-points")) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("points", getPoints())), session);
        } else if (joIn.tag.equals("request-points-vh")) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("points-vh", getVHPoints(1))), session);
        } else if (joIn.tag.equals("request-max-time")) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("max-time", Integer.valueOf(this.spec.getTotalTime()))), session);
        } else if (joIn.tag.equals("request-agent-description")) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("agent-description", this.selectedAgent1.agentDescription())), session);
        } else if (joIn.tag.equals("request-disable")) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("interfaceDisabled", Boolean.valueOf(this.multi))), session);
        } else if (joIn.tag.equals("request-agent-art")) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("config-character-art", this.selectedAgent1.getArtName())), session);
        } else if (joIn.tag.equals("request-visibility")) {
            if (joIn.data.equals("vh-points"))
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("visibility-points-vh", Boolean.valueOf(this.spec.showOpponentScore()))), session);
            if (joIn.data.equals("timer"))
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("visibility-timer", Boolean.valueOf(this.spec.showNegotiationTimer()))), session);
        } else if (joIn.tag.equals("button")) {
            try {
                handleButton(joIn.data.toString(), session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (joIn.tag.equals("dialogClosed")) {
            Event e2 = new Event(-1, Event.EventClass.GAME_END);
            this.history.updateHistory(e2);
            if (this.isMultiAgent)
                this.mediator0.addToAgent(e2);
            this.mediator1.addToAgent(e2);
            writeFinalData();
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject(e2.getType().toString(), e2)), session);
        } else if (joIn.tag.equals("startGame")) {
            Event e2 = new Event(-1, Event.EventClass.GAME_START);
            this.history.updateHistory(e2);
            if (this.isMultiAgent)
                this.mediator0.addToAgent(e2);
            this.mediator1.addToAgent(e2);
        } else if (joIn.tag.equals("newGameStart")) {
            Event e2 = new Event(-1, Event.EventClass.GAME_START);
            this.history.updateHistory(e2);
            if (this.isMultiAgent)
                this.mediator0.addToAgent(e2);
            this.mediator1.addToAgent(e2);
        } else if (joIn.tag.equals("time")) {
            int time = (int) Float.parseFloat(joIn.data.toString());
            Event e = new Event(0, Event.EventClass.TIME, "" + time);
            this.history.updateHistory(e);
            if (this.isMultiAgent)
                this.mediator0.addToAgent(e);
            this.mediator1.addToAgent(e);
            if (time >= this.spec.getTotalTime()) {
                ServletUtils.log("Game timing out", ServletUtils.DebugLevels.DEBUG);
                String timeoutMessage = "<p>The game has ended because time has expired.  Both players receive their BATNA.  </p><br><br>Your score was: " + this.spec.getPlayerBATNA() + ".<br><br>";
                timeoutMessage += this.spec.showOpponentScoreOnEnd() ? "Your opponent scored: " + this.spec.getBATNA(1) + ".<br><br>" : "";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("negotiationEnd", timeoutMessage + this.spec.getEndgameMessage())), session);
                this.timedOut = true;
            } else if (time > this.spec.getTotalTime() - 60 && time < this.spec.getTotalTime() - 54) {
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("negotiationWarn", "")), session);
            }
        } else if (joIn.tag.equals("request-player-batna")) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("player-BATNA", Integer.valueOf(this.spec.getPlayerBATNA()))), session);
        } else if (joIn.tag.equals("sendBATNA")) {
            this.userPresentedBATNA = Integer.parseInt((String) joIn.data);
            ServletUtils.log("BATNA recieved from user: " + this.userPresentedBATNA, ServletUtils.DebugLevels.DEBUG);
        } else if (joIn.tag.equals("request-total-player-points")) {
            int[] quantities = this.spec.getIssueQuants();
            int totalPoints = 0;
            for (int index = 0; index < this.spec.getNumIssues(); index++) {
                String s = this.spec.getIssuePluralNames()[index];
                totalPoints += quantities[index] * ((Integer) this.spec.getSimpleUserPoints().get(s)).intValue();
            }
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("total-player-points", Integer.valueOf(totalPoints))), session);
        } else if (joIn.tag.equals("openSurvey")) {
            String survey = this.spec.getSurvey();
            ServletUtils.log("Survey found: " + survey, ServletUtils.DebugLevels.DEBUG);
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("open-survey", survey)), session);
        } else if (joIn.tag.equals("survey")) {
            String result = joIn.data.toString();
            try {
                GsonBuilder builder = new GsonBuilder();
                Object o = builder.create().fromJson(result, Object.class);
                LinkedTreeMap<String, String> ltm = null;
                try {
                    ltm = (LinkedTreeMap<String, String>) o;
                    ServletUtils.log("SURVEY LTM " + ltm, ServletUtils.DebugLevels.DEBUG);
                } catch (Exception e) {
                    ServletUtils.log("Survey cast failed", ServletUtils.DebugLevels.DEBUG);
                }
                if (ltm != null)
                    for (String key : ltm.keySet())
                        this.surveyData.put(key, String.valueOf(ltm.get(key)));
                ServletUtils.log("GSON RESULT: " + result, ServletUtils.DebugLevels.DEBUG);
            } catch (NullPointerException e) {
                ServletUtils.log("NULL SURVEY DATA ", ServletUtils.DebugLevels.DEBUG);
            }
        } else if (joIn.tag.equals("unsafe-close")) {
            ServletUtils.log("Unsafe server closure. Writing final data...", ServletUtils.DebugLevels.DEBUG);
            writeFinalData();
        } else if (joIn.tag.equals("start-threads")) {
            if (this.isMultiAgent) {
                this.mediator0.makeRunnable();
                setRunnerSpecs();
                if (!this.thread0.isAlive()) {
                    this.mediator0.makeRunnable();
                    this.thread0.start();
                }
            }
            this.mediator1.makeRunnable();
            setRunnerSpecs();
            if (!this.thread1.isAlive()) {
                this.mediator1.makeRunnable();
                this.thread1.start();
            }
        } else if (joIn.tag.equals("page-refreshed")) {
            if (this.isMultiAgent)
                this.mediator0.stopRunning();
            this.mediator1.stopRunning();
        } else if (joIn.tag.equals("notify-thread")) {
            if (this.isMultiAgent)
                this.mediator0.setActivePage(true);
            this.mediator1.setActivePage(true);
        }
    }

    public void doPostHelper(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String MTurkID = request.getParameter("MTurkID");
        request.getParameter("agentName");
        request.getParameter("description");
        String expressionChoice = request.getParameter("expression");
        String behaviorChoice = request.getParameter("behavior");
        String messageChoice = request.getParameter("message");
        String withholdChoice = request.getParameter("withhold");
        String honestyChoice = request.getParameter("honesty");
        String gameChoice = request.getParameter("gameChoice");
        String q1 = request.getParameter("qualtricsQ1");
        String q2 = request.getParameter("qualtricsQ2");
        String q3 = request.getParameter("qualtricsQ3");
        String q4 = request.getParameter("qualtricsQ4");
        String qFlag = request.getParameter("qualtricsFlag");
        String condition = request.getParameter("condition");
        int neuroticism = Integer.parseInt(request.getParameter("neuroticism"));
        int extravesioin = Integer.parseInt(request.getParameter("extraversion"));
        int openness = Integer.parseInt(request.getParameter("openness"));
        int conscientiousness = Integer.parseInt(request.getParameter("conscientiousness"));
        int agreeableness = Integer.parseInt(request.getParameter("agreeableness"));
        ServletUtils.log("Found mturk id: " + MTurkID, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found expression: " + expressionChoice, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found behavior: " + behaviorChoice, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found message: " + messageChoice, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found withhold: " + withholdChoice, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found honesty: " + honestyChoice, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found game choice: " + gameChoice, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found Q1: " + q1, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found Q2: " + q2, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found Q3: " + q3, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found Q4: " + q4, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found QF: " + qFlag, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Found condition: " + condition, ServletUtils.DebugLevels.DEBUG);
        this.menuLoc = "root";
        this.itemInHand = -1;
        this.rowInHand = -1;
        for (int i = 0; i < this.spec.getNumIssues(); i++) {
            this.issue_locs[i] = new int[]{0, spec.getIssueQuants()[i], 0};
        }
        this.astate = AcceptanceState.NO_ACCEPTANCE;
        this.qstate = QuitState.NO_WARNED;
        request.getSession().setAttribute("MTurkID", MTurkID);
        request.getSession().setAttribute("expression", expressionChoice);
        request.getSession().setAttribute("behavior", behaviorChoice);
        request.getSession().setAttribute("message", messageChoice);
        request.getSession().setAttribute("withhold", withholdChoice);
        request.getSession().setAttribute("honesty", honestyChoice);
        request.getSession().setAttribute("gameChoice", gameChoice);
        request.getSession().setAttribute("qualtricsQ1", q1);
        request.getSession().setAttribute("qualtricsQ2", q2);
        request.getSession().setAttribute("qualtricsQ3", q3);
        request.getSession().setAttribute("qualtricsQ4", q4);
        request.getSession().setAttribute("qualtricsFlag", qFlag);
        request.getSession().setAttribute("condition", condition);
        request.getSession().setAttribute("neuroticism", neuroticism);
        request.getSession().setAttribute("extraversion", extravesioin);
        request.getSession().setAttribute("openness", openness);
        request.getSession().setAttribute("conscientiousness", conscientiousness);
        request.getSession().setAttribute("agreeableness", agreeableness);
        request.getRequestDispatcher("iago.jsp").forward((ServletRequest) request, (ServletResponse) response);
    }

    public void doGetHelper(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.MTurkID = (String) request.getSession().getAttribute("id");
        ServletUtils.log("Found mturk id: " + this.MTurkID, ServletUtils.DebugLevels.DEBUG);
        this.menuLoc = "root";
        this.itemInHand = -1;
        this.rowInHand = -1;
        for (int i = 0; i < this.spec.getNumIssues(); i++) {
            this.issue_locs[i] = new int[]{0, spec.getIssueQuants()[i], 0};
        }
        this.astate = AcceptanceState.NO_ACCEPTANCE;
        this.qstate = QuitState.NO_WARNED;
        request.getRequestDispatcher("iago.jsp").forward((ServletRequest) request, (ServletResponse) response);
    }

    private void handleButton(String buttonID, Session session) throws IOException {
        String instructions;
        Event e1, e2, e3, e4;
        Offer o;
        int i;
        boolean legal;
        int j;
        String points;
        Event message, messageToVH, e5;
        String prevLoc = this.menuLoc;
        String userMessage = null;
        Event e = null;
        int messageCode = -1;
        ServletUtils.log("Button pressed: " + buttonID, ServletUtils.DebugLevels.DEBUG);
        if (!buttonID.equals("butExpl22") && !buttonID.equals("butExpl23") && !buttonID.equals("butFormalQuit"))
            this.qstate = QuitState.NO_WARNED;
        String data = "";
        switch (buttonID) {
            case "butYouLike":
                dropItem(session);
                this.comparisonState[0] = "Do you like ";
                this.comparisonState[4] = "?";
                this.preferenceState.setQuery(true);
                this.menuLoc = "youLike";
                preferenceLanguageHelper(session, 0);
                break;
            case "butILike":
                dropItem(session);
                this.comparisonState[0] = "I like ";
                this.comparisonState[4] = ".";
                this.preferenceState.setQuery(false);
                this.menuLoc = "iLike";
                preferenceLanguageHelper(session, 0);
                break;
            case "butCustom1":
                dropItem(session);
                this.menuLoc = "custom1";
                break;
            case "butCustom2":
                dropItem(session);
                this.menuLoc = "custom2";
                break;
            case "butCustom3":
                dropItem(session);
                this.menuLoc = "custom3";
                break;
            case "butCustom1_1":
                dropItem(session);
                this.menuLoc = "custom1_1";
                break;
            case "butCustom1_2":
                dropItem(session);
                this.menuLoc = "custom1_2";
                break;
            case "butCustom1_3":
                dropItem(session);
                this.menuLoc = "custom1_3";
                break;
            case "butCustom2_1":
                dropItem(session);
                this.menuLoc = "custom2_1";
                instructions = "If you want to share your actual walk-away value, press \"Send\" now. If you'd like to send a higher walk-away value, move the slider to the right!";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextTemp", instructions)), session);
                break;
            case "butCustom2_2":
                dropItem(session);
                this.menuLoc = "custom2_2";
                break;
            case "butCustom2_3":
                dropItem(session);
                this.menuLoc = "custom2_3";
                break;
            case "butCustom3_1":
                dropItem(session);
                this.menuLoc = "custom3_1";
                break;
            case "butCustom3_2":
                dropItem(session);
                this.menuLoc = "custom3_2";
                break;
            case "butCustom3_3":
                dropItem(session);
                this.menuLoc = "custom3_3";
                break;
            case "butBack":
                dropItem(session);
                this.menuLoc = "root";
                break;
            case "butNeutral":
                dropItem(session);
                e = new Event(0, Event.EventClass.SEND_EXPRESSION, "neutral", 0, 0);
                this.history.updateHistory(e);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("emoteMessage", "neutral")), session);
                this.mediator1.addToAgent(e);
                break;
            case "butAnger":
                dropItem(session);
                e = new Event(0, Event.EventClass.SEND_EXPRESSION, "angry", 0, 0);
                this.history.updateHistory(e);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("emoteMessage", "angry")), session);
                this.mediator1.addToAgent(e);
                break;
            case "butSad":
                dropItem(session);
                e = new Event(0, Event.EventClass.SEND_EXPRESSION, "sad", 0, 0);
                this.history.updateHistory(e);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("emoteMessage", "sad")), session);
                this.mediator1.addToAgent(e);
                break;
            case "butSurprised":
                dropItem(session);
                e = new Event(0, Event.EventClass.SEND_EXPRESSION, "surprised", 0, 0);
                this.history.updateHistory(e);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("emoteMessage", "surprised")), session);
                this.mediator1.addToAgent(e);
                break;
            case "butHappy":
                dropItem(session);
                e = new Event(0, Event.EventClass.SEND_EXPRESSION, "happy", 0, 0);
                this.history.updateHistory(e);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("emoteMessage", "happy")), session);
                this.mediator1.addToAgent(e);
                break;
            case "butCol0Row0":
                moveHelper(0, 0, session);
                break;
            case "butCol1Row0":
                moveHelper(1, 0, session);
                break;
            case "butCol2Row0":
                moveHelper(2, 0, session);
                break;
            case "butCol3Row0":
                moveHelper(3, 0, session);
                break;
            case "butCol4Row0":
                moveHelper(4, 0, session);
                break;
            case "butCol0Row1":
                moveHelper(0, 1, session);
                break;
            case "butCol1Row1":
                moveHelper(1, 1, session);
                break;
            case "butCol2Row1":
                moveHelper(2, 1, session);
                break;
            case "butCol3Row1":
                moveHelper(3, 1, session);
                break;
            case "butCol4Row1":
                moveHelper(4, 1, session);
                break;
            case "butCol0Row2":
                moveHelper(0, 2, session);
                break;
            case "butCol1Row2":
                moveHelper(1, 2, session);
                break;
            case "butCol2Row2":
                moveHelper(2, 2, session);
                break;
            case "butCol3Row2":
                moveHelper(3, 2, session);
                break;
            case "butCol4Row2":
                moveHelper(4, 2, session);
                break;
            case "butStartOffer":
                e = new Event(0, Event.EventClass.OFFER_IN_PROGRESS, 0);
                this.history.updateHistory(e);
                this.mediator1.addToAgent(e);
                break;
            case "butAccept":
                e1 = new Event(0, Event.EventClass.SEND_MESSAGE, Event.SubClass.OFFER_ACCEPT, "Yes, I accept that offer.", 0);
                this.history.updateHistory(e1);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextFinalized", "Yes, I accept that offer.")), session);
                this.mediator1.addToAgent(e1);
                break;
            case "butReject":
                e2 = new Event(0, Event.EventClass.SEND_MESSAGE, Event.SubClass.OFFER_REJECT, "No, I can't accept that offer.", 0);
                this.history.updateHistory(e2);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextFinalized", "No, I can't accept that offer.")), session);
                this.mediator1.addToAgent(e2);
                break;
            case "butAcceptFavor":
                e3 = new Event(0, Event.EventClass.SEND_MESSAGE, Event.SubClass.FAVOR_ACCEPT, "Sure, I'll do you a favor.", 0);
                this.history.updateHistory(e3);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextFinalized", "Sure, I'll do you a favor.")), session);
                this.mediator1.addToAgent(e3);
                break;
            case "butRejectFavor":
                e4 = new Event(0, Event.EventClass.SEND_MESSAGE, Event.SubClass.FAVOR_REJECT, "No, I can't do you a favor.", 0);
                this.history.updateHistory(e4);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextFinalized", "No, I can't do you a favor.")), session);
                this.mediator1.addToAgent(e4);
                break;
            case "butSendOffer":
                dropItem(session);
                o = new Offer(this.spec.getNumIssues());
                for (i = 0; i < this.spec.getNumIssues(); i++)
                    o.setItem(i, this.issue_locs[i]);
                e = new Event(0, Event.EventClass.SEND_OFFER, o, 0);
                this.history.updateHistory(e);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextFinalized", myOfferToString(o, true))), session);
                this.mediator1.addToAgent(e);
                break;
            case "butFormalQuit":
                dropItem(session);
                ServletUtils.log("User pressed 'quit' button. Current quit state: " + this.qstate, ServletUtils.DebugLevels.DEBUG);
                e = new Event(0, Event.EventClass.FORMAL_QUIT, 0);
                this.history.updateHistory(e);
                switch (this.qstate) {
                    case NO_WARNED:
                        this.qstate = QuitState.USER_WARNED;
                        break;
                    case OPPONENT_WARNED:
                        this.qstate = QuitState.FULL_QUIT;
                        break;
                }
                this.mediator1.addToAgent(e);
                if (this.qstate == QuitState.FULL_QUIT) {
                    String quitMessage = "<p>The game has ended because you walked away from the negotiation.  Both players receive their BATNA.  </p><br><br>Your score was: " + this.spec.getPlayerBATNA() + ".<br><br>";
                    quitMessage += this.spec.showOpponentScoreOnEnd() ? "Your opponent scored: " + this.spec.getOpponentBATNA() + ".<br><br>" : "";
                    WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("negotiationEnd", quitMessage + this.spec.getEndgameMessage())), session);
                    this.formallyQuit = true;
                }
                break;
            case "butFormalAccept":
                dropItem(session);
                legal = true;
                for (j = 0; j < this.spec.getNumIssues(); j++) {
                    if (this.issue_locs[j][1] != 0)
                        legal = false;
                }
                if (legal) {
                    ServletUtils.log("Current Acceptance State: " + this.astate, ServletUtils.DebugLevels.DEBUG);
                    Offer lastOffer = getLastOffer();
                    if (!finalBoardMatch(lastOffer) && !this.finalOfferWarning) {
                        this.finalOfferWarning = true;
                        String warning = "<p>WARNING: The current board does not match the last offer accepted by the opponent. </p><br><br>You may reenter your offer and click the \"Send Offer\" button to propose the offer to your opponent or click the\"Accept Formally (WILL END GAME)\" button again to receive the last agreed upon offer.<br><br>";
                        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("notMatched", warning)), session);
                        ServletUtils.log("Formal Offer: Invalid, current board doesn't match last offer. Player shown warning.", ServletUtils.DebugLevels.DEBUG);
                    } else {
                        int m, n, vhpoints, i1;
                        String acceptanceMessage;
                        ServletUtils.log("Formal Offer: Valid, last agreed upon offer is chosen.", ServletUtils.DebugLevels.DEBUG);
                        e = new Event(0, Event.EventClass.FORMAL_ACCEPT, 0);
                        this.history.updateHistory(e);
                        switch (this.astate) {
                            case NO_ACCEPTANCE:
                                this.astate = AcceptanceState.USER_ACCEPTED;
                                break;
                            case OPPONENT_ACCEPTED:
                                this.astate = AcceptanceState.FULL_ACCEPTANCE;
                                if (lastOffer == null) {
                                    String warning = "<p>There is no last offer! The only offer is the current board.</p>";
                                    WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("popup", warning)), session);
                                    break;
                                }
                                if (this.nMode == NegotiationMode.HUMAN_HUMAN)
                                    this.nRoom.finalNotification(this.httpSession);
                                m = 0;
                                for (n = 0; n < (getOfferPoints(lastOffer)).length; n++)
                                    m += getOfferPoints(lastOffer)[n];
                                vhpoints = 0;
                                for (i1 = 0; i1 < (getVHOfferPoints(lastOffer, 1)).length; i1++)
                                    vhpoints += getVHOfferPoints(lastOffer, 1)[i1];
                                ServletUtils.log("Last Offer: " + lastOffer, ServletUtils.DebugLevels.DEBUG);
                                ServletUtils.log("VH points: " + vhpoints + ", Player points: " + m, ServletUtils.DebugLevels.DEBUG);
                                ServletUtils.log("Formal Offer: Valid, current board matches last offer.", ServletUtils.DebugLevels.DEBUG);
                                acceptanceMessage = "<p>The game has ended because your opponent has accepted your offer!  Both players receive the points agreed upon on in their final offers. </p><br><br>Your score was: " + m + ".<br><br>";
                                acceptanceMessage += this.spec.showOpponentScoreOnEnd() ? "Your opponent scored: " + vhpoints + ".<br><br>" : "";
                                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("offerFinalized", acceptanceMessage + this.spec.getEndgameMessage())), session);
                                break;
                        }
                    }
                    for (int k = 0; k < this.spec.getNumIssues(); k++) {
                        for (int m = 0; m < 3; m++)
                            this.issue_locs[k][m] = lastOffer.getItem(k)[m];
                    }
                    WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("objectLocs", this.issue_locs)), session);
                } else {
                    String str = "<p>You cannot make a formal offer--not all issues have been allocated!</p>";
                    WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("popup", str)), session);
                }
                this.mediator1.addToAgent(e);
                break;
            case "butViewPayoffs":
                dropItem(session);
                if (this.spec.isAdvancedPoints())
                    break;
                points = "";
                for (String s : this.spec.getSimpleUserPoints().keySet())
                    points += s + ": " + this.spec.getSimpleUserPoints().get(s) + " points each.\n";
                points += "\nYou already have a deal for " + this.spec.getPlayerBATNA()  + " points. Try to get more!\n";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("playerPointStruct", points)), session);
                break;
            case "root":
                this.menuLoc = "root";
                prevLoc = "startup";
                break;
            case "butItemComparisonGT":
                dropItem(session);
                this.comparisonState[3] = this.oldContents;
                this.comparisonState[2] = "more than ";
                this.preferenceState.setRelation(Preference.Relation.GREATER_THAN);
                userMessage = this.comparisonState[0] + this.comparisonState[1] + this.comparisonState[2] + this.comparisonState[3] + this.comparisonState[4];
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextTemp", userMessage)), session);
                break;
            case "butItemComparisonLT":
                dropItem(session);
                this.comparisonState[3] = this.oldContents;
                this.comparisonState[2] = "less than ";
                this.preferenceState.setRelation(Preference.Relation.LESS_THAN);
                userMessage = this.comparisonState[0] + this.comparisonState[1] + this.comparisonState[2] + this.comparisonState[3] + this.comparisonState[4];
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextTemp", userMessage)), session);
                break;
            case "butItemComparisonBEST":
                dropItem(session);
                this.comparisonState[2] = "best";
                this.oldContents = this.comparisonState[3].equals("") ? this.oldContents : this.comparisonState[3];
                this.comparisonState[3] = "";
                this.preferenceState.setRelation(Preference.Relation.BEST);
                userMessage = this.comparisonState[0] + this.comparisonState[1] + this.comparisonState[2] + this.comparisonState[3] + this.comparisonState[4];
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextTemp", userMessage)), session);
                break;
            case "butItemComparisonLEAST":
                dropItem(session);
                this.comparisonState[2] = "least";
                this.oldContents = this.comparisonState[3].equals("") ? this.oldContents : this.comparisonState[3];
                this.comparisonState[3] = "";
                this.preferenceState.setRelation(Preference.Relation.WORST);
                userMessage = this.comparisonState[0] + this.comparisonState[1] + this.comparisonState[2] + this.comparisonState[3] + this.comparisonState[4];
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextTemp", userMessage)), session);
                break;
            case "butItemComparisonEQUAL":
                dropItem(session);
                this.comparisonState[3] = this.oldContents;
                this.comparisonState[2] = "the same as ";
                this.preferenceState.setRelation(Preference.Relation.EQUAL);
                userMessage = this.comparisonState[0] + this.comparisonState[1] + this.comparisonState[2] + this.comparisonState[3] + this.comparisonState[4];
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextTemp", userMessage)), session);
                break;
            case "butItem0":
                dropItem(session);
                this.comparisonItemInHand = 0;
                data = "img/cur0.cur";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("cursorStatus", data)), session);
                break;
            case "butItem1":
                dropItem(session);
                this.comparisonItemInHand = 1;
                data = "img/cur1.cur";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("cursorStatus", data)), session);
                break;
            case "butItem2":
                dropItem(session);
                this.comparisonItemInHand = 2;
                data = "img/cur2.cur";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("cursorStatus", data)), session);
                break;
            case "butItem3":
                dropItem(session);
                this.comparisonItemInHand = 3;
                data = "img/cur3.cur";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("cursorStatus", data)), session);
                break;
            case "butItem4":
                dropItem(session);
                this.comparisonItemInHand = 4;
                data = "img/cur4.cur";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("cursorStatus", data)), session);
                break;
            case "butConfirm":
                dropItem(session);
                userMessage = this.comparisonState[0] + this.comparisonState[1] + this.comparisonState[2] + this.comparisonState[3] + this.comparisonState[4];
                ServletUtils.log("User message: " + userMessage, ServletUtils.DebugLevels.DEBUG);
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextFinalized", userMessage)), session);
                this.menuLoc = "root";
                message = new Event(0, Event.EventClass.SEND_MESSAGE, Event.SubClass.PREF_INFO, userMessage, 0);
                message.encodePreferenceData(new Preference(this.preferenceState));
                ServletUtils.log("Encoding preferences from user... " + this.preferenceState.toString(), ServletUtils.DebugLevels.DEBUG);
                this.history.updateHistory(message);
                this.mediator1.addToAgent(message);
                break;
            case "butItemFirst":
                preferenceLanguageHelper(session, 1);
                break;
            case "butItemSecond":
                preferenceLanguageHelper(session, 2);
                break;
            case "butSend":
                dropItem(session);
                userMessage = "Just so you know, I already have an offer for " + this.userPresentedBATNA + " points, so I won't accept anything less.";
                ServletUtils.log("User message: " + userMessage, ServletUtils.DebugLevels.DEBUG);
                if (this.userPresentedBATNA == this.spec.getPlayerBATNA()) {
                    WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextFinalized", userMessage)), session);
                } else {
                    WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextFinalizedLiar", userMessage)), session);
                }
                this.menuLoc = "root";
                messageToVH = new Event(0, Event.EventClass.SEND_MESSAGE, Event.SubClass.BATNA_INFO, this.userPresentedBATNA, userMessage, 0);
                ServletUtils.log("event message: " + messageToVH.getMessage(), ServletUtils.DebugLevels.DEBUG);
                this.history.updateHistory(messageToVH);
                this.mediator1.addToAgent(messageToVH);
                break;
            case "butExpl0":
                if (messageCode == -1)
                    messageCode = 0;
            case "butExpl1":
                if (messageCode == -1)
                    messageCode = 1;
            case "butExpl2":
                if (messageCode == -1)
                    messageCode = 2;
            case "butExpl3":
                if (messageCode == -1)
                    messageCode = 3;
            case "butExpl4":
                if (messageCode == -1)
                    messageCode = 4;
            case "butExpl5":
                if (messageCode == -1)
                    messageCode = 5;
            case "butExpl6":
                if (messageCode == -1)
                    messageCode = 6;
            case "butExpl7":
                if (messageCode == -1)
                    messageCode = 7;
            case "butExpl8":
                if (messageCode == -1)
                    messageCode = 8;
            case "butExpl9":
                if (messageCode == -1)
                    messageCode = 9;
            case "butExpl10":
                if (messageCode == -1)
                    messageCode = 10;
            case "butExpl11":
                if (messageCode == -1)
                    messageCode = 11;
            case "butExpl12":
                if (messageCode == -1)
                    messageCode = 12;
            case "butExpl13":
                if (messageCode == -1)
                    messageCode = 13;
            case "butExpl14":
                if (messageCode == -1)
                    messageCode = 14;
            case "butExpl15":
                if (messageCode == -1)
                    messageCode = 15;
            case "butExpl16":
                if (messageCode == -1)
                    messageCode = 16;
            case "butExpl17":
                if (messageCode == -1)
                    messageCode = 17;
            case "butExpl18":
                if (messageCode == -1)
                    messageCode = 18;
            case "butExpl19":
                if (messageCode == -1)
                    messageCode = 19;
            case "butExpl20":
                if (messageCode == -1)
                    messageCode = 20;
            case "butExpl21":
                if (messageCode == -1)
                    messageCode = 21;
            case "butWithhold":
                if (messageCode == -1)
                    messageCode = 500;
            case "butExpl22":
                if (messageCode == -1) {
                    messageCode = 22;
                    if (this.qstate == QuitState.OPPONENT_WARNED)
                        this.qstate = QuitState.FULL_QUIT;
                    if (this.qstate == QuitState.NO_WARNED)
                        this.qstate = QuitState.USER_WARNED;
                }
            case "butExpl23":
                if (messageCode == -1) {
                    messageCode = 23;
                    if (this.qstate == QuitState.OPPONENT_WARNED)
                        this.qstate = QuitState.FULL_QUIT;
                    if (this.qstate == QuitState.NO_WARNED)
                        this.qstate = QuitState.USER_WARNED;
                }
            case "butExpl24":
                if (messageCode == -1)
                    messageCode = 24;
            case "butExpl25":
                if (messageCode == -1)
                    messageCode = 25;
            case "butExpl26":
                if (messageCode == -1)
                    messageCode = 26;
            case "butExpl27":
                if (messageCode == -1)
                    messageCode = 27;
            case "butExpl28":
                if (messageCode == -1)
                    messageCode = 28;
            case "butExpl29":
                if (messageCode == -1)
                    messageCode = 29;
            case "butExpl30":
                if (messageCode == -1)
                    messageCode = 30;
            case "butExpl31":
                if (messageCode == -1)
                    messageCode = 31;
            case "butExpl32":
                if (messageCode == -1)
                    messageCode = 32;
                dropItem(session);
                userMessage = (String) ((Map) this.spec.getMenu().get(prevLoc)).get(buttonID);
                ServletUtils.log("User message: " + userMessage, ServletUtils.DebugLevels.DEBUG);
                this.menuLoc = "root";
                e5 = new Event(0, Event.EventClass.SEND_MESSAGE, userMessage, 0);
                e5.encodeMessageCode(messageCode);
                this.history.updateHistory(e5);
                this.mediator1.addToAgent(e5);
                break;
        }
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("menu", this.spec.getMenu().get(this.menuLoc))), session);
    }

    private int[] getPoints() {
        int[] yourPoints = new int[this.spec.getNumIssues()];
        if (!this.spec.isAdvancedPoints())
            for (int i = 0; i < this.spec.getNumIssues(); i++) {
                Map<String, Integer> points = this.spec.getSimpleUserPoints();
                yourPoints[i] = this.issue_locs[i][2] * ((Integer) points.get(this.spec.getIssuePluralNames()[i])).intValue();
            }
        return yourPoints;
    }

    private int[] getOfferPoints(Offer o) {
        int[] yourPoints = new int[this.spec.getNumIssues()];
        if (!this.spec.isAdvancedPoints())
            for (int i = 0; i < this.spec.getNumIssues(); i++) {
                Map<String, Integer> points = this.spec.getSimpleUserPoints();
                yourPoints[i] = o.getItem(i)[2] * ((Integer) points.get(this.spec.getIssuePluralNames()[i])).intValue();
            }
        return yourPoints;
    }

    private int[] getVHPoints(int ID) {
        int[] vhPoints = new int[this.spec.getNumIssues()];
        if (!this.spec.isAdvancedPoints())
            for (int i = 0; i < this.spec.getNumIssues(); i++)
                vhPoints[i] = this.issue_locs[i][0] * ((Integer) this.spec.getSimplePoints(ID).get(this.spec.getIssuePluralNames()[i])).intValue();
        return vhPoints;
    }

    private int[] getVHOfferPoints(Offer o, int ID) {
        int[] vhPoints = new int[this.spec.getNumIssues()];
        if (!this.spec.isAdvancedPoints())
            for (int i = 0; i < this.spec.getNumIssues(); i++) {
                if (ID == 1) {
                    vhPoints[i] = o.getItem(i)[0] * ((Integer) this.spec.getSimplePoints(ID).get(this.spec.getIssuePluralNames()[i])).intValue();
                } else if (ID == 0) {
                    vhPoints[i] = o.getItem(i)[2] * ((Integer) this.spec.getSimplePoints(ID).get(this.spec.getIssuePluralNames()[i])).intValue();
                }
            }
        return vhPoints;
    }

    private boolean finalBoardMatch(Offer o) {
        int[][] comparison = new int[this.spec.getNumIssues()][3];
        for (int i = 0; i < this.spec.getNumIssues(); i++) {
            for (int j = 0; j < 3; j++)
                comparison[i][j] = o.getItem(i)[j];
        }
        for (int i = 0; i < this.spec.getNumIssues(); i++) {
            for (int j = 0; j < 3; j++) {
                if (comparison[i][j] != this.issue_locs[i][j])
                    return false;
            }
        }
        return true;
    }

    private Offer getLastOffer() {
        LinkedList<Event> newHistory = this.history.getHistory();
        for (int i = newHistory.size() - 1; i > 0; i--) {
            if (((Event) newHistory.get(i)).getType() == Event.EventClass.SEND_OFFER) {
                return newHistory.get(i).getOffer();
            }
        }
        return null;
    }

    private void preferenceLanguageHelper(Session session, int modify) {
        if (modify == 1) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("compareFirstItem", Integer.valueOf(this.comparisonItemInHand))), session);
            this.comparisonState[1] = (this.comparisonItemInHand > -1) ? (this.spec.getIssuePluralNames()[this.comparisonItemInHand] + " ") : "something ";
            this.preferenceState.setIssue1((this.comparisonItemInHand > -1) ? this.comparisonItemInHand : -1);
        } else if (modify == 2) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("compareSecondItem", Integer.valueOf(this.comparisonItemInHand))), session);
            this.comparisonState[3] = (this.comparisonItemInHand > -1) ? this.spec.getIssuePluralNames()[this.comparisonItemInHand] : "something ";
            this.preferenceState.setIssue2((this.comparisonItemInHand > -1) ? this.comparisonItemInHand : -1);
            this.oldContents = this.comparisonState[3];
        }
        if (this.comparisonState[3].equals("something") && this.comparisonState[1].equals("something ")) {
            this.comparisonState[1] = "something ";
            this.comparisonState[3] = "something else";
        }
        dropItem(session);
        String userMessage = this.comparisonState[0] + this.comparisonState[1] + this.comparisonState[2] + this.comparisonState[3] + this.comparisonState[4];
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("chatTextTemp", userMessage)), session);
    }

    private String formatSurvey(Map<String, String> survey) {
        String formatted = "";
        if (this.spec.getSurvey() == null)
            return "no survey provided";
        if (survey.isEmpty())
            return "user checked zero questions.";
        for (String key : survey.keySet())
            formatted = formatted + "  " + formatted + ": " + key + "\n";
        return formatted;
    }

    private void writeFinalData() {
        if (this.dataWritten) {
            ServletUtils.log("Data has already been written for this game... skipping.", ServletUtils.DebugLevels.DEBUG);
            return;
        }
        this.dataWritten = true;
        String userID = (WebSocketUtils.getUser(this.wsSession) == null) ? this.wsSession.getId() : WebSocketUtils.getUser(this.wsSession).getName();
        String body = "id,agent0,agent1,userPoints,vhPoints,timedout,formallyQuit,numUserOffers,numVHOffers,numUserPref,numVHPref,numUserQuery,numVHQuery,numUserMsg,numVHMsg,numUserLie,numVHLie,numUserNeutral,numVHNeutral,numUserHappy,numVHHappy,numUserAngry,numVHAngry,numUserSad,numVHSad,numUserSurprised,numVHSurprised,numVHDisgusted,numVHInsincere,numVHAfraid,studyName,currentGame,gameEndTime,gameAllottedTime,numUserBATNALies,numVHBATNALies,initialOfferUserAlloc1,initialOfferUserAlloc2,initialOfferUserAlloc3,initialOfferUserAlloc4,initialOfferUserAlloc5,finalOfferUserAlloc1,finalOfferUserAlloc2,finalOfferUserAlloc3,finalOfferUserAlloc4,finalOfferUserAlloc5,initialOfferVHAlloc1,initialOfferVHAlloc2,initialOfferVHAlloc3,initialOfferVHAlloc4,initialOfferVHAlloc5,finalOfferVHAlloc1,finalOfferVHAlloc2,finalOfferVHAlloc3,finalOfferVHAlloc4,finalOfferVHAlloc5";
        TreeMap<String, Integer> subClassMap = new TreeMap<>();
        for (Event.SubClass type : Event.SubClass.values()) {
            String varUser = "numUser" + type;
            String varVH = "numVH" + type;
            subClassMap.put(varUser, Integer.valueOf(0));
            subClassMap.put(varVH, Integer.valueOf(0));
        }
        for (String title : subClassMap.keySet())
            body += "," + title;
        for (String key : this.surveyData.keySet())
            body += "," + key;
        body += ",QuestionNeuroticism,QuestionExtraversioin,QuestionOpenness,QuestionAgreeableness,QuestionConscientiousness,Cooperativeness,Assertiveness,Neuroticism,Extraversion,Openness,Agreeableness,Conscientiousness";
        body += "\n";
        body += this.MTurkID + ",";
        if (!this.isMultiAgent) {
            body +=  "player,";
        } else {
            body += this.selectedAgent0.getName();
        }
        body += this.selectedAgent1.getName();
        int points = 0;
        for (int i = 0; i < this.getPoints().length; i++)
            points += getPoints()[i];
        int vhpoints = 0;
        for (int j = 0; j < this.getVHPoints(1).length; j++)
            vhpoints += getVHPoints(1)[j];
        if (this.timedOut || this.formallyQuit) {
            body += "," + this.spec.getPlayerBATNA() + "," + this.spec.getBATNA(1);
        } else {
            body += "," + points + "," + vhpoints;
        }
        if (this.timedOut && !this.formallyQuit) {
            body += ",true,false";
        } else if (!this.timedOut && this.formallyQuit) {
            body += ",false,true";
        } else {
            body += ",false,false";
        }
        String fullList = "";
        int numUserOffers = 0;
        int numVHOffers = 0;
        int numUserPref = 0;
        int numVHPref = 0;
        int numUserQuery = 0;
        int numVHQuery = 0;
        int numUserMsg = 0;
        int numVHMsg = 0;
        int numUserLie = 0;
        int numVHLie = 0;
        int numUserNeutral = 0;
        int numVHNeutral = 0;
        int numUserHappy = 0;
        int numVHHappy = 0;
        int numUserAngry = 0;
        int numVHAngry = 0;
        int numUserSad = 0;
        int numVHSad = 0;
        int numUserSurprised = 0;
        int numVHSurprised = 0;
        int numVHDisgusted = 0;
        int numVHInsincere = 0;
        int numVHAfraid = 0;
        String studyName = this.spec.getStudyName();
        int gameEndTime = 0;
        int gameAllottedTime = this.spec.getTotalTime();
        int numUserBATNALies = 0;
        int numVHBATNALies = 0;
        int[] initialOfferUserAlloc = new int[this.spec.getNumIssues()];
        int[] finalOfferUserAlloc = new int[this.spec.getNumIssues()];
        int[] initialOfferVHAlloc = new int[this.spec.getNumIssues()];
        int[] finalOfferVHAlloc = new int[this.spec.getNumIssues()];
        for (int k = 0; k < this.spec.getNumIssues(); k++) {
            finalOfferUserAlloc[k] = this.issue_locs[k][2];
            finalOfferVHAlloc[k] = this.issue_locs[k][0];
            initialOfferUserAlloc[k] = -1;
            initialOfferVHAlloc[k] = -1;
        }
        boolean foundFirstOffer = false;
        int passbyCount = 0;
        LinkedList<Event> hist = this.history.getHistory();
        for (int x = 0; x < hist.size(); x++) {
            Event e = hist.get(x);
            if (e != null)
                if (passbyCount <= this.currentGame) {
                    if (e.getType().equals(Event.EventClass.GAME_START))
                        passbyCount++;
                } else {
                    if (e.getType() == Event.EventClass.TIME) {
                        int time = Integer.parseInt(e.getMessage());
                        if (time > gameEndTime)
                            gameEndTime = time;
                    }
                    if (e.getType() == Event.EventClass.SEND_OFFER)
                        if (e.getOwner() == 0) {
                            numUserOffers++;
                            if (!foundFirstOffer) {
                                foundFirstOffer = true;
                                for (int n = 0; n < this.spec.getNumIssues(); n++) {
                                    initialOfferUserAlloc[n] = e.getOffer().getItem(n)[2];
                                    initialOfferVHAlloc[n] = e.getOffer().getItem(n)[0];
                                }
                            }
                        } else if (e.getOwner() == 1) {
                            numVHOffers++;
                            if (!foundFirstOffer) {
                                foundFirstOffer = true;
                                for (int n = 0; n < this.spec.getNumIssues(); n++) {
                                    initialOfferUserAlloc[n] = e.getOffer().getItem(n)[2];
                                    initialOfferVHAlloc[n] = e.getOffer().getItem(n)[0];
                                }
                            }
                        }
                    if (e.getType() == Event.EventClass.SEND_MESSAGE)
                        if (e.getOwner() == 0) {
                            numUserMsg++;
                            String mapUserVar = "numUser" + e.getSubClass();
                            subClassMap.put(mapUserVar, Integer.valueOf(((Integer) subClassMap.get(mapUserVar)).intValue() + 1));
                            if (e.getPreference() != null && !e.getPreference().isQuery()) {
                                numUserPref++;
                                if (isLie(e, 0))
                                    numUserLie++;
                            } else if (e.getPreference() != null && e.getPreference().isQuery()) {
                                numUserQuery++;
                            } else if (e.getSubClass() == Event.SubClass.BATNA_INFO && e.getValue() != this.spec.getPlayerBATNA()) {
                                numUserBATNALies++;
                            }
                        } else if (e.getOwner() == 1) {
                            numVHMsg++;
                            String mapVHVar = "numVH" + e.getSubClass();
                            subClassMap.put(mapVHVar, Integer.valueOf(((Integer) subClassMap.get(mapVHVar)).intValue() + 1));
                            if (e.getPreference() != null && !e.getPreference().isQuery()) {
                                numVHPref++;
                                if (isLie(e, 1))
                                    numVHLie++;
                            } else if (e.getPreference() != null && e.getPreference().isQuery()) {
                                numVHQuery++;
                            } else if (e.getSubClass() == Event.SubClass.BATNA_INFO) {
                                if (e.getValue() != this.spec.getOpponentBATNA())
                                    numVHBATNALies++;
                            }
                        }
                    if (e.getType() == Event.EventClass.SEND_EXPRESSION)
                        if (e.getOwner() == 0) {
                            switch (e.getMessage()) {
                                case "neutral":
                                    numUserNeutral++;
                                    break;
                                case "angry":
                                    numUserAngry++;
                                    break;
                                case "happy":
                                    numUserHappy++;
                                    break;
                                case "surprised":
                                    numUserSurprised++;
                                    break;
                                case "sad":
                                    numUserSad++;
                                    break;
                            }
                        } else if (e.getOwner() == 1) {
                            switch (e.getMessage()) {
                                case "neutral":
                                    numVHNeutral++;
                                    break;
                                case "angry":
                                    numVHAngry++;
                                    break;
                                case "happy":
                                    numVHHappy++;
                                    break;
                                case "surprised":
                                    numVHSurprised++;
                                    break;
                                case "disgusted":
                                    numVHDisgusted++;
                                    break;
                                case "afraid":
                                    numVHAfraid++;
                                    break;
                                case "sad":
                                    numVHSad++;
                                    break;
                                case "insincereSmile":
                                    numVHInsincere++;
                                    break;
                            }
                        }
                    fullList += ": " + userID + "__" + e.getType() + "," + e.getSubClass() + "," + e.getMessage();
                    if (e.getOffer() != null)
                        fullList += "," + e.getOffer().toString();
                    if (e.getPreference() != null)
                        fullList += "," + e.getPreference().toString();
                    fullList += "\n";
                }
        }
        body += "," + numUserOffers + "," + numVHOffers + "," + numUserPref + "," + numVHPref + "," + numUserQuery + "," + numVHQuery + "," + numUserMsg + "," + numVHMsg + "," + numUserLie + "," + numVHLie + "," + numUserNeutral + "," + numVHNeutral + "," + numUserHappy + "," + numVHHappy + "," + numUserAngry + "," + numVHAngry + "," + numUserSad + "," + numVHSad + "," + numUserSurprised + "," + numVHSurprised + "," + numVHDisgusted + "," + numVHInsincere + "," + numVHAfraid + "," + studyName + "," + this.currentGame + "," + gameEndTime + "," + gameAllottedTime + "," + numUserBATNALies + "," + numVHBATNALies;
        int m;
        for (m = 0; m < this.spec.getNumIssues(); m++)
            body += "," + initialOfferUserAlloc[m];
        for (m = 5; m > this.spec.getNumIssues(); m--)
            body += ",0";
        for (m = 0; m < this.spec.getNumIssues(); m++)
            body += "," + finalOfferUserAlloc[m];
        for (m = 5; m > this.spec.getNumIssues(); m--)
            body += ",0";
        for (m = 0; m < this.spec.getNumIssues(); m++)
            body += "," + initialOfferVHAlloc[m];
        for (m = 5; m > this.spec.getNumIssues(); m--)
            body += ",0";
        for (m = 0; m < this.spec.getNumIssues(); m++)
            body += "," + finalOfferVHAlloc[m];
        for (m = 5; m > this.spec.getNumIssues(); m--)
            body += ",0";

        for (String key : subClassMap.keySet())
            body += "," + subClassMap.get(key);
        for (String key : this.surveyData.keySet())
            body += "," + this.surveyData.get(key);

        String firstRow = body.split("\n")[0];
        String secondRow = body.split("\n")[1];
        ArrayList<String> parameter = new ArrayList<>();

        if (this.selectedAgent1 instanceof MentalistCoreVH) {
            parameter.add(Integer.toString(this.questionnaireNeuroticism));
            parameter.add(Integer.toString(this.questionnaireExtraversion));
            parameter.add(Integer.toString(this.questionnaireOpenness));
            parameter.add(Integer.toString(this.questionnaireAgreeableness));
            parameter.add(Integer.toString(this.questionnaireConscientiouness));

            MentalistCoreBehavior behavior = ((MentalistCoreVH) this.selectedAgent1).getBehavior();
            if(behavior instanceof MentalistRepeatedFavorBehavior) {
                parameter.add(((MentalistRepeatedFavorBehavior) behavior).getCooperativeness());
                parameter.add(((MentalistRepeatedFavorBehavior) behavior).getAssertiveness());
                parameter.add(((MentalistRepeatedFavorBehavior) behavior).getNeuroticism());
                parameter.add(((MentalistRepeatedFavorBehavior) behavior).getExtraversion());
                parameter.add(((MentalistRepeatedFavorBehavior) behavior).getOpenness());
                parameter.add(((MentalistRepeatedFavorBehavior) behavior).getAgreeableness());
                parameter.add(((MentalistRepeatedFavorBehavior) behavior).getConscientiousness());
            }
            else if(behavior instanceof QuestionnaireMentalistBehavior){
                parameter.add(((QuestionnaireMentalistBehavior) behavior).getCooperativeness());
                parameter.add(((QuestionnaireMentalistBehavior) behavior).getAssertiveness());
                parameter.add(((QuestionnaireMentalistBehavior) behavior).getNeuroticism());
                parameter.add(((QuestionnaireMentalistBehavior) behavior).getExtraversion());
                parameter.add(((QuestionnaireMentalistBehavior) behavior).getOpenness());
                parameter.add(((QuestionnaireMentalistBehavior) behavior).getAgreeableness());
                parameter.add(((QuestionnaireMentalistBehavior) behavior).getConscientiousness());
            }
            else if(behavior instanceof PilotStudyBehavior){
                parameter.add("-10");
                parameter.add("-10");
                parameter.add("-10");
                parameter.add("-10");
                parameter.add("-10");
                parameter.add("-10");
                parameter.add("-10");
            }
            for(int i = 0; i < parameter.size(); i++){
                body += "," + parameter.get(i);
            }
        }

        body += "\n\n";
        body += ": Survey Results: \n";
        body += fullList + "\n";

        if (ServletUtils.isDataModeLog()) {
            this.logger.log(Level.INFO, "Logger session dump:\n" + body);
            ServletUtils.log("Starting new dump of session:\n" + body, ServletUtils.DebugLevels.WARN);
            try{
                String folderName = webDir +  "log/" + MTurkID;
                File folder = new File(folderName);
                if(!folder.exists()){
                    folder.mkdirs();
                }
                String fileName = folderName + "/" + this.selectedAgent1.getName() + ".log";

                BufferedWriter bw = new BufferedWriter(new FileWriter((new File( fileName)), true));

                bw.write(body);
                bw.newLine();

                bw.close();

                boolean mkdir_flag = false;
                folderName = webDir +  "csv_log";
                folder = new File(folderName);
                if(!folder.exists()){
                    folder.mkdirs();
                    mkdir_flag = true;
                }
                String[] split = body.split("\n");
                fileName = folderName + "/log.csv";
                bw = new BufferedWriter(new FileWriter((new File( fileName)), true));
                if(mkdir_flag){
                    bw.write(firstRow);
                    bw.newLine();
                }
                bw.write(secondRow);
                if(!parameter.isEmpty()){
                    for(int i = 0; i < parameter.size(); i++){
                        bw.write("," + parameter.get(i));
                    }
                }
                bw.newLine();

                bw.close();

            }
            catch(IOException e){
                System.out.println(e);
            }
        }
        if (ServletUtils.isDataModeEmail())
            try {
                ServletUtils.sendMail(this.spec.getTargetEmail(), "User data: " + this.MTurkID, body);
            } catch (Exception e) {
                ServletUtils.log(e.getMessage(),ServletUtils.DebugLevels.ERROR);
                ServletUtils.log("Mail did not send properly--did you enable it but fail to configure?", ServletUtils.DebugLevels.ERROR);
            }
        if (ServletUtils.isDataModeGoogleSpreadSheets())
            try {
                GoogleSpreadSheetUtils.addRow(secondRow, parameter);

            } catch(Exception e){
                ServletUtils.log(e.getMessage(), ServletUtils.DebugLevels.ERROR);
            }
        if (ServletUtils.isDataModeDb())
            try {
                DatabaseUtils.setDBCredentials("jdbc:derby://localhost/","NegotiationData","pajama","13eas-17ohs");
                DatabaseUtils.createDB("NegotiationData");
                ServletUtils.log("DataBase connection is correct.", ServletUtils.DebugLevels.DEBUG);
            } catch (Exception e) {
                ServletUtils.log(e.getMessage(), ServletUtils.DebugLevels.ERROR);
            }
    }

    private boolean isLie(Event e, int id) {
        boolean ans = false;
        Preference p = e.getPreference();
        if (p == null)
            throw new NullPointerException("Preference was null");
        if (p.getIssue1() < 0 || p.getIssue2() < 0)
            return false;
        int val1 = 0;
        int val2 = 0;
        val1 = ((Integer) this.spec.getSimplePoints(id).get(this.spec.getIssuePluralNames()[p.getIssue1()])).intValue();
        if (p.getIssue2() >= 0)
            val2 = ((Integer) this.spec.getSimplePoints(id).get(this.spec.getIssuePluralNames()[p.getIssue2()])).intValue();
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this.spec.getNumIssues(); i++) {
            int val = ((Integer) this.spec.getSimplePoints(id).get(this.spec.getIssuePluralNames()[i])).intValue();
            if (val > max)
                max = val;
            if (val < min)
                min = val;
        }
        switch (p.getRelation()) {
            case GREATER_THAN:
                ans = (val1 > val2);
                break;
            case BEST:
                ans = (val1 == max);
                break;
            case EQUAL:
                ans = (val1 == val2);
                break;
            case LESS_THAN:
                ans = (val1 < val2);
                break;
            case WORST:
                ans = (val1 == min);
                break;
        }
        return !ans;
    }

    private void moveHelper(int column, int row, Session session) {
        if (this.comparisonItemInHand > -1)
            return;
        if (this.itemInHand == column) {
            if (this.rowInHand == row && this.issue_locs[column][row] > 0 && this.quantInHand < 5) {
                this.issue_locs[column][row] = this.issue_locs[column][row] - 1;
                this.quantInHand++;
            } else {
                this.issue_locs[column][row] = this.issue_locs[column][row] + this.quantInHand;
                this.quantInHand = 0;
                this.itemInHand = -1;
                this.rowInHand = -1;
            }
        } else if (this.itemInHand == -1 && this.rowInHand == -1 && this.issue_locs[column][row] > 0) {
            this.itemInHand = column;
            this.rowInHand = row;
            this.issue_locs[column][row] = this.issue_locs[column][row] - 1;
            this.quantInHand++;
        }
        String filename = null;
        if (this.itemInHand == -1) {
            filename = "default";
        } else if (this.quantInHand != 0) {
            filename = "img/cur" + this.itemInHand + "quant" + this.quantInHand + ".cur";
        } else {
            filename = "img/cur" + this.itemInHand + ".png";
        }
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("objectLocs", this.issue_locs)), session);
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("cursorStatus", filename)), session);
    }

    private void dropItem(Session session) {
        if (this.itemInHand != -1)
            this.issue_locs[this.itemInHand][1] = this.issue_locs[this.itemInHand][1] + this.quantInHand;
        this.itemInHand = -1;
        this.rowInHand = -1;
        this.quantInHand = 0;
        this.comparisonItemInHand = -1;
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("objectLocs", this.issue_locs)), session);
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("cursorStatus", "default")), session);
    }

    synchronized void processEvent(Event e, Session session) {
        if (this.nMode == NegotiationMode.HUMAN_HUMAN && e.getOwner() == 0) {
            this.nRoom.sendToOpponent(this.httpSession, e);
            ServletUtils.log("Send to other person", ServletUtils.DebugLevels.DEBUG);
            return;
        }
        if (this.isMultiAgent) {
            if (this.eventQueue.remove(e))
                return;
            if (this.eventQueue.size() == 15) {
                ServletUtils.log("Queue size exceeded! Slow down or increase the queue size!", ServletUtils.DebugLevels.WARN);
                this.eventQueue.removeFirst();
            }
        }
        this.eventQueue.add(e);
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("menu", this.spec.getMenu().get(this.menuLoc))), session);
        this.history.updateHistory(e);
        Event eUpgrade = e;
        if (e.getType().equals(Event.EventClass.SEND_OFFER)) {
            this.itemInHand = -1;
            this.rowInHand = -1;
            this.quantInHand = 0;
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("cursorStatus", "default")), session);
            for (int col = 0; col < this.issue_locs.length; col++) {
                int colSum = 0;
                for (int row = 0; row < (this.issue_locs[col]).length; row++) {
                    Offer offer = e.getOffer();
                    this.issue_locs[col][row] = offer.getItem(col)[row];
                    colSum += e.getOffer().getItem(col)[row];
                }
                if (colSum != this.spec.getIssueQuants()[col])
                    throw new IllegalArgumentException("VH has sent an invalid offer; sum of column != total items.");
            }
            Offer o = e.getOffer();
            String ans = myOfferToString(o, (e.getOwner() == 0));
            eUpgrade = new Event(e.getOwner(), e.getType(), e.getOffer(), ans, e.getDelay());
            this.astate = AcceptanceState.NO_ACCEPTANCE;
            this.qstate = QuitState.NO_WARNED;
        } else if (e.getType().equals(Event.EventClass.FORMAL_ACCEPT)) {
            if (this.astate == AcceptanceState.USER_ACCEPTED && e.getOwner() == 1)
                this.astate = AcceptanceState.FULL_ACCEPTANCE;
            if (this.astate == AcceptanceState.OPPONENT_ACCEPTED && e.getOwner() == 0)
                this.astate = AcceptanceState.FULL_ACCEPTANCE;
            if (this.astate == AcceptanceState.NO_ACCEPTANCE)
                if (e.getOwner() == 1) {
                    this.astate = AcceptanceState.OPPONENT_ACCEPTED;
                } else if (e.getOwner() == 0) {
                    this.astate = AcceptanceState.USER_ACCEPTED;
                }
        } else if (e.getType().equals(Event.EventClass.FORMAL_QUIT) || e.getType().equals(Event.EventClass.SEND_MESSAGE)) {
            if (e.getType().equals(Event.EventClass.FORMAL_QUIT) || e.getSubClass() == Event.SubClass.THREAT_NEG || e.getSubClass() == Event.SubClass.THREAT_POS) {
                if (this.qstate == QuitState.USER_WARNED && e.getOwner() == 1)
                    this.qstate = QuitState.FULL_QUIT;
                if (this.qstate == QuitState.OPPONENT_WARNED && e.getOwner() == 0)
                    this.qstate = QuitState.FULL_QUIT;
                if (this.qstate == QuitState.NO_WARNED)
                    if (e.getOwner() == 1) {
                        this.qstate = QuitState.OPPONENT_WARNED;
                    } else if (e.getOwner() == 0) {
                        this.qstate = QuitState.USER_WARNED;
                    }
            }
        }
        if (eUpgrade.getType() != Event.EventClass.SEND_MESSAGE) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject(eUpgrade.getType().toString(), eUpgrade)), session);
        } else {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject(eUpgrade.getType().toString() + "__" + eUpgrade.getSubClass().toString(), eUpgrade)), session);
        }
        if (this.astate == AcceptanceState.FULL_ACCEPTANCE) {
            Offer lastOffer = getLastOffer();
            if (lastOffer == null) {
                String warning = "<p>There is no last offer! The only offer is the current board.</p>";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("popup", warning)), session);
            } else {
                int points = 0;
                for (int i = 0; i < (getOfferPoints(lastOffer)).length; i++)
                    points += getOfferPoints(lastOffer)[i];
                int vhpoints = 0;
                for (int j = 0; j < (getVHOfferPoints(lastOffer, 1)).length; j++)
                    vhpoints += getVHOfferPoints(lastOffer, 1)[j];
                ServletUtils.log("Last Offer: " + lastOffer, ServletUtils.DebugLevels.DEBUG);
                ServletUtils.log("VH points: " + vhpoints + ", Player points: " + points, ServletUtils.DebugLevels.DEBUG);
                ServletUtils.log("Formal Offer: Valid, current board matches last offer.", ServletUtils.DebugLevels.DEBUG);
                String acceptanceMessage = "<p>The game has ended because your opponent has accepted your offer!  Both players receive the points agreed upon on in their final offers. </p><br><br>Your score was: " + points + ".<br><br>";
                acceptanceMessage += this.spec.showOpponentScoreOnEnd() ? "Your opponent scored: " + vhpoints + ".<br><br>" : "";
                WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("offerFinalized", acceptanceMessage)), session);
            }
        }
        if (this.qstate == QuitState.FULL_QUIT) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("FORMAL_QUIT", "")), session);
            e = new Event(eUpgrade.getOwner(), Event.EventClass.FORMAL_QUIT, 0);
            this.history.updateHistory(e);
        }
        if (this.isMultiAgent) {
            ServletUtils.log("Multiagent code has been disabled in this release.  Sorry!", ServletUtils.DebugLevels.ERROR);
            if (eUpgrade.getOwner() == 1) {
                this.mediator0.addToAgent(eUpgrade);
            } else if (eUpgrade.getOwner() == 0) {
                this.mediator1.addToAgent(eUpgrade);
            }
        }
    }

    void sendFinalNotification() {
        this.astate = AcceptanceState.FULL_ACCEPTANCE;
        Offer lastOffer = getLastOffer();
        int points = 0;
        for (int i = 0; i < (getOfferPoints(lastOffer)).length; i++)
            points += getOfferPoints(lastOffer)[i];
        int vhpoints = 0;
        for (int j = 0; j < (getVHOfferPoints(lastOffer, 1)).length; j++)
            vhpoints += getVHOfferPoints(lastOffer, 1)[j];
        ServletUtils.log("Last Offer: " + lastOffer, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("VH points: " + vhpoints + ", Player points: " + points, ServletUtils.DebugLevels.DEBUG);
        ServletUtils.log("Formal Offer: Valid, current board matches last offer.", ServletUtils.DebugLevels.DEBUG);
        String acceptanceMessage = "<p>The game has ended because your opponent has accepted your offer!  Both players receive the points agreed upon on in their final offers. </p><br><br>Your score was: " + points + ".<br><br>";
        acceptanceMessage += this.spec.showOpponentScoreOnEnd() ? "Your opponent scored: " + vhpoints + ".<br><br>" : "";
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("offerFinalized", acceptanceMessage + this.spec.getEndgameMessage())), this.wsSession);
    }

    private String myOfferToString(Offer o, boolean isReceiver) {
        String ans = " ";
        int mine = isReceiver ? 2 : 0;
        int yours = isReceiver ? 0 : 2;
        for (int i = 0; i < o.getIssueCount(); i++) {
            if (o.getItem(i)[mine] != 0 || o.getItem(i)[yours] != 0)
                if (o.getItem(i)[mine] == this.spec.getIssueQuants()[i]) {
                    ans += "I'll get all the " + this.spec.getIssuePluralNames()[i] + ".\n";
                } else if (o.getItem(i)[yours] == this.spec.getIssueQuants()[i]) {
                    ans += "You'll get all the " + this.spec.getIssuePluralNames()[i] + ".\n";
                } else {
                    ans += "You'll get " + o.getItem(i)[yours];
                    ans += o.getItem(i)[yours] != 1 ? " " + this.spec.getIssuePluralNames()[i] : " " + this.spec.getIssueNames()[i];
                    ans += ", and I'll get " + o.getItem(i)[mine];
                    ans += o.getItem(i)[mine] != 1 ? " " + this.spec.getIssuePluralNames()[i] : " " + this.spec.getIssueNames()[i];
                    ans += ".\n";
                }
        }
        ans = ans.substring(0, ans.length() - 1);
        return ans;
    }

    private void configureCustomCursors() {
        try {
            for (int itemNum = 0; itemNum < this.spec.getNumIssues(); itemNum++) {
                String folder = this.httpSession.getServletContext().getRealPath("/img");
                String relativeWebPathFullSize = "/img/" + this.spec.getClass().getSimpleName() + "/item" + itemNum + ".png";
                InputStream fullSizeInput = this.httpSession.getServletContext().getResourceAsStream(relativeWebPathFullSize);
                BufferedImage fullSizeItem = null;
                fullSizeItem = ImageIO.read(fullSizeInput);
                File fullSizeResult = new File(folder, "item" + itemNum + ".png");
                ImageIO.write(fullSizeItem, "png", fullSizeResult);
                ServletUtils.log("Created new fullsize image called: " + fullSizeResult.getAbsolutePath(), ServletUtils.DebugLevels.DEBUG);
                InputStream fullSizeInput2 = this.httpSession.getServletContext().getResourceAsStream(relativeWebPathFullSize);
                BufferedImage fullSizeItem2 = null;
                fullSizeItem2 = ImageIO.read(fullSizeInput2);
                BufferedImage scaledItem = new BufferedImage(32, 32, fullSizeItem2.getType());
                Graphics2D g2d = scaledItem.createGraphics();
                g2d.drawImage(fullSizeItem2, 0, 0, 32, 32, null);
                g2d.dispose();
                File scaledResult = new File(folder, "cur" + itemNum + ".png");
                ImageIO.write(scaledItem, "png", scaledResult);
                ServletUtils.log("Created new scaled image called: " + scaledResult.getAbsolutePath(), ServletUtils.DebugLevels.DEBUG);
                String relativeWebPathItem = "/img/cur" + itemNum + ".png";
                InputStream itemInput = this.httpSession.getServletContext().getResourceAsStream(relativeWebPathItem);
                BufferedImage item = null;
                item = ImageIO.read(itemInput);
                for (int i = 1; i <= 5; i++) {
                    String relativeWebPathHand = "/img/curHand" + i + ".png";
                    InputStream handInput = this.httpSession.getServletContext().getResourceAsStream(relativeWebPathHand);
                    BufferedImage hand = null;
                    hand = ImageIO.read(handInput);
                    int itemW = item.getWidth();
                    int itemH = item.getHeight();
                    int handW = hand.getWidth();
                    int handH = hand.getHeight();
                    if (itemW != 32 || itemH != 32 || handW != 32 || handH != 32)
                        throw new Exception("Formatting error in cursor creation--are all cursor files .png 32x32?");
                    BufferedImage combined = new BufferedImage(32, 32, 2);
                    Graphics g = combined.getGraphics();
                    for (int x = 0; x < 32; x++) {
                        for (int y = 0; y < 32; y++) {
                            Color pixel = new Color(hand.getRGB(x, y));
                            if (!Color.WHITE.equals(pixel))
                                item.setRGB(x, y, pixel.getRGB());
                        }
                    }
                    g.drawImage(item, 0, 0, null);
                    String filename = "cur" + itemNum + "quant" + i + ".png";
                    File result = new File(folder, filename);
                    ServletUtils.log("File location at: " + result.getAbsolutePath(), ServletUtils.DebugLevels.DEBUG);
                    ImageIO.write(combined, "png", result);
                    ServletUtils.log("Created new temp image called: " + result.getAbsolutePath(), ServletUtils.DebugLevels.DEBUG);
                    BufferedImage png = ImageIO.read(result);
                    File trueResult = new File(folder, filename.substring(0, filename.indexOf('.')) + ".cur");
                    ICOEncoder.write(png, trueResult);
                    ServletUtils.log("Created new combined cursor image called: " + trueResult.getAbsolutePath(), ServletUtils.DebugLevels.DEBUG);
                }
                InputStream itemInputRedo = this.httpSession.getServletContext().getResourceAsStream(relativeWebPathItem);
                BufferedImage pngUncombined = null;
                pngUncombined = ImageIO.read(itemInputRedo);
                File uncombinedResult = new File(folder, "cur" + itemNum + ".cur");
                ICOEncoder.write(pngUncombined, uncombinedResult);
                ServletUtils.log("Created new pure cursor image called: " + uncombinedResult.getAbsolutePath(), ServletUtils.DebugLevels.DEBUG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject("cursors-ready", "")), this.wsSession);
    }

    AcceptanceState getAstate() {
        return this.astate;
    }

    QuitState getQstate() {
        return this.qstate;
    }

    NegotiationMode getNMode() {
        return this.nMode;
    }

    HttpSession getHttpSession() {
        return this.httpSession;
    }

    public void setNRoom(NegotiationRoom nRoom) {
        this.nRoom = nRoom;
    }

    public void setNegotiationMode(NegotiationMode nMode) {
        this.nMode = nMode;
    }

    public void setSessionOther(HttpSession httpSession, Session session) {
    }

    public void setMultiplayer(boolean multi) {
        this.multi = multi;
    }
}