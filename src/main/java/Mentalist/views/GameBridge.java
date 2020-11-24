package Mentalist.views;

import Mentalist.agent.StaticMentalistVH;
import com.google.gson.Gson;
import Mentalist.utils.*;
import Mentalist.utils.GameBridgeUtils.NegotiationMode;
import Mentalist.utils.Governor.SessionState;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.logging.Logger;

//import edu.usc.ict.Mentalist.agent.IAGOChenVH;

/**
 * This the GameBridge, the connector between the web hosting front end of IAGO and the back end.
 * You probably don't want to modify anything here.  Perhaps you should make your changes in config.txt?
 * Note that if you are not developing your own experiment (e.g., you're competing in ANAC), your config.txt
 * will NOT be saved.
 * @author Johnathan Mell
 *
 */
@ServerEndpoint(value = "/game/ws", configurator = GetHttpSessionConfigurator.class)
@WebServlet(name="IagoGame", loadOnStartup=1, description = "This servlet loads the Iago platform.", urlPatterns = { "/game" })
public class GameBridge extends HttpServlet  {

	private static final long serialVersionUID = 2669425387121437625L;

	private GameBridgeUtils u;
	private GameSpec gs; //utility for getting GS info.  Careful: only useful for getter methods, and only then AFTER construction.
	private NegotiationRoom nRoom;
	private String vhQualifiedName0;
	private String vhQualifiedName1;
	private ArrayList<String> allGameSpecNames;
	private ArrayList<String> allStoredVHNames;
	private int currentGame = 0;
	private GeneralVH storedVH0;
	private GeneralVH storedVH1;
	private String globalCondition;
	private HttpSession httpSession;
	private String globalMTurkID;		//this gets passed back into Qualtrics after the game ends if the user is coming from a Qualtrics study
	private boolean programmed;		//this lets us know whether or not the system is in programming mode
	private static String webDir;
	private int questionnaireNeuroticism = 0;
	private int questionnaireExtraversion = 0;
	private int questionnaireOpenness = 0;
	private int questionnaireConscientiouness = 0;
	private int questionnaireAgreeableness = 0;
	private javax.websocket.Session globalSession;

	//private Logger LOGGER = LogManager.getLogger(GameBridge.class.getName());
	Logger logger = Logger.getLogger(GameBridge.class.getName());

	/**
	 * Constructor for the GameBridge.
	 * @throws Exception
	 */
	public GameBridge() throws Exception
	{
		String email_username = "";
		String email_pass = "";
		String email_sender_name = "";
		String email_smtpAuth = "";
		String email_smtpHost = "";
		String email_smtpPort = "";
		String gameSpecMultiName = "";
		boolean dataMode_log = false;
		boolean dataMode_email = false;
		boolean dataMode_db = false;
		boolean dataMode_GoogleSpreadSheets = false;
		ServletUtils.DebugLevels debug = ServletUtils.DebugLevels.ERROR;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
		{
			ServletUtils.log("Context class loader is null--resources may be improperly loaded", ServletUtils.DebugLevels.WARN);
			classLoader = ClassLoader.getSystemClassLoader();
			if (classLoader == null)
				ServletUtils.log("System class loader is null--cannot load properly.", ServletUtils.DebugLevels.ERROR);
		}
		InputStream generalInput = classLoader.getResourceAsStream("config_general.txt");

		if(generalInput == null)
			ServletUtils.log("Configuration file not found!", ServletUtils.DebugLevels.ERROR);
		Properties generalProperties = new Properties();
		try {
			generalProperties.load(generalInput);

			vhQualifiedName0 = generalProperties.getProperty("agent0");
			gameSpecMultiName = generalProperties.getProperty("gamespec");
			email_username = generalProperties.getProperty("email_user");
			email_pass = generalProperties.getProperty("email_pass");
			email_sender_name = generalProperties.getProperty("email_sender_name");
			email_smtpAuth = generalProperties.getProperty("email_smtpAuth");
			email_smtpHost = generalProperties.getProperty("email_smtpHost");
			email_smtpPort = generalProperties.getProperty("email_smtpPort");

			dataMode_log = generalProperties.getProperty("dataMode_log").equals("enabled");
			dataMode_email = generalProperties.getProperty("dataMode_email").equals("enabled");
			dataMode_db = generalProperties.getProperty("dataMode_db").equals("enabled");
			dataMode_GoogleSpreadSheets = generalProperties.getProperty("dataMode_GoogleSpreadSheets").equals("enabled");

			debug = ServletUtils.DebugLevels.valueOf(generalProperties.getProperty("debugLevel"));

		} catch (IOException e) {
		}

		ServletUtils.setDebug(debug);
		ServletUtils.setDataModeDb(dataMode_db);
		ServletUtils.setDataModeEmail(dataMode_email);
		ServletUtils.setDataModeLog(dataMode_log);
		ServletUtils.setDataModeGoogleSpreadSheets(dataMode_GoogleSpreadSheets);
		ServletUtils.setCredentials(email_username, email_pass, email_sender_name, email_smtpAuth, email_smtpHost, email_smtpPort);

		if (vhQualifiedName0 == null || vhQualifiedName0.equals(""))
			vhQualifiedName0 =  null;

		if (vhQualifiedName1 == null || vhQualifiedName1.equals(""))
			vhQualifiedName1 =  null;

		if (gameSpecMultiName == null || gameSpecMultiName.equals(""))
			gameSpecMultiName =  null;

		//account for multiple games
		allGameSpecNames = new ArrayList<String> (Arrays.asList(gameSpecMultiName.split("\\s*,\\s*")));
		ServletUtils.log("We found the following GameSpecs: " + allGameSpecNames.toString(), ServletUtils.DebugLevels.DEBUG);

		GameSpec gs = null;

		try {
			@SuppressWarnings("unchecked")
			Class<? extends GameSpec> gsclass = (Class<? extends GameSpec>) Class.forName(allGameSpecNames.get(currentGame));
			Constructor<? extends GameSpec> ctor = gsclass.getDeclaredConstructor(boolean.class);
			ctor.setAccessible(true);
			gs = ctor.newInstance(new Object [] {true});
		} catch (ClassNotFoundException e2) {
			System.err.println("We were unable to load the primary GameSpec file from the class name provided in the configuration file.");
			e2.printStackTrace();
		}
		catch (NoSuchMethodException | SecurityException e1) {
			System.err.println("You have not provided a constructor that meets the requirements! Make sure your extension of GameSpec has"
					+ " a matching set of arguments.");
			e1.printStackTrace();
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		if (gs == null)
			throw new Exception("Unknown problem creating GameSpec class from configuration.");

		u = new GameBridgeUtils(gs);
		this.gs = gs;

	}

	@OnOpen
	public void onOpen(javax.websocket.Session session, EndpointConfig config)
	{
		this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());	//access the HTTP session to get the data needed to construct the agent (this needs to go above the control statements below because gameChoice determines path)
		String gameChoice = (String)httpSession.getAttribute("gameChoice");				//user's game choice (same as above line - must be considered first to determine control flow)
		String qFlag = (String)httpSession.getAttribute("qualtricsFlag");				//determines if user is coming from Qualtrics survey or not
		globalMTurkID= (String)httpSession.getAttribute("MTurkID");
		globalCondition = (String)httpSession.getAttribute("condition");

		questionnaireNeuroticism = (Integer)httpSession.getAttribute("neuroticism");
		questionnaireExtraversion = (Integer)httpSession.getAttribute("extraversion");
		questionnaireOpenness = (Integer)httpSession.getAttribute("openness");
		questionnaireConscientiouness = (Integer)httpSession.getAttribute("conscientiousness");
		questionnaireAgreeableness = (Integer)httpSession.getAttribute("agreeableness");

		ServletUtils.log("Game Choice: " + gameChoice, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Qualtrics Flag: " + qFlag, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("MTurkID: " + globalMTurkID, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Neuroticism: " + questionnaireNeuroticism, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Extraversion: " + questionnaireExtraversion, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Openness: " + questionnaireOpenness, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Conscientiouness: " + questionnaireConscientiouness, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Agreeableness: " + questionnaireAgreeableness, ServletUtils.DebugLevels.DEBUG);

		String gameSpecMultiName = "";

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
		{
			ServletUtils.log("Context class loader is null--resources may be improperly loaded", ServletUtils.DebugLevels.WARN);
			classLoader = ClassLoader.getSystemClassLoader();
			if (classLoader == null)
				ServletUtils.log("System class loader is null--cannot load properly.", ServletUtils.DebugLevels.ERROR);
		}

		try{
			String folderName = webDir +  "config/" + globalMTurkID ;
			File folder = new File(folderName);
			if(!folder.exists()){
				folder.mkdirs();
				new File(folderName + "/agent.txt").createNewFile();
				new File(folderName + "/big5.txt").createNewFile();
				new File(folderName + "/game.txt").createNewFile();
			}

			String fileName = folderName + "/big5.txt";

			BufferedReader br = new BufferedReader(new FileReader((new File( fileName))));

			String line;
			ArrayList<Integer> big5Data = new ArrayList<>();

			while((line = br.readLine()) != null){
				big5Data.add(Integer.parseInt(line));
			}

			br.close();

			if(questionnaireNeuroticism == -1 && questionnaireExtraversion == -1 && questionnaireOpenness == -1 && questionnaireConscientiouness == -1 && questionnaireAgreeableness == -1) {
				if (big5Data.size() != 5)
					WebSocketUtils.close(session);
				else {
					questionnaireNeuroticism = big5Data.get(0);
					questionnaireExtraversion = big5Data.get(1);
					questionnaireOpenness = big5Data.get(2);
					questionnaireConscientiouness = big5Data.get(3);
					questionnaireAgreeableness = big5Data.get(4);
				}
			}
			else {
				if(big5Data.size() < 5) {
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter((new File(fileName)), true));

						bw.write(Integer.toString(questionnaireNeuroticism));
						bw.newLine();
						bw.write(Integer.toString(questionnaireExtraversion));
						bw.newLine();
						bw.write(Integer.toString(questionnaireOpenness));
						bw.newLine();
						bw.write(Integer.toString(questionnaireConscientiouness));
						bw.newLine();
						bw.write(Integer.toString(questionnaireAgreeableness));

						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		ServletUtils.log("Neuroticism: " + questionnaireNeuroticism, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Extraversion: " + questionnaireExtraversion, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Openness: " + questionnaireOpenness, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Conscientiouness: " + questionnaireConscientiouness, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Agreeableness: " + questionnaireAgreeableness, ServletUtils.DebugLevels.DEBUG);

		try {
			String folderName = webDir +  "config/" + globalMTurkID ;
			File folder = new File(folderName);
			if(!folder.exists()){
				folder.mkdirs();
				new File(folderName + "/agent.txt").createNewFile();
				new File(folderName + "/big5.txt").createNewFile();
				new File(folderName + "/game.txt").createNewFile();
			}

			String fileName = folderName + "/agent.txt";
			BufferedReader br = new BufferedReader(new FileReader((new File( fileName))));
			String line;
			ArrayList<String> agentData = new ArrayList<>();

			while((line = br.readLine()) != null){
				agentData.add(line);
			}

			br.close();

			fileName = folderName + "/game.txt";
			br = new BufferedReader(new FileReader((new File( fileName))));
			ArrayList<String> gameData = new ArrayList<>();

			while((line = br.readLine()) != null){
				gameData.add(line);
			}

			br.close();

			ArrayList<String> newConfig = createNewConfig();
			if(agentData.isEmpty() || gameData.isEmpty()){
				vhQualifiedName1 = newConfig.get(0);
				gameSpecMultiName = newConfig.get(1);
			}
			else{
				ArrayList<String> vhArray = new ArrayList<String>(Arrays.asList(newConfig.get(0).split("\\s*,\\s*")));
				ArrayList<String> gameArray = new ArrayList<String>(Arrays.asList(newConfig.get(1).split("\\s*,\\s*")));
				for(int i = 0; i < agentData.size(); i++) {
					vhArray.remove(agentData.get(i));
					gameArray.remove(gameData.get(i));
				}
				vhQualifiedName1 = vhArray.isEmpty() ? null : String.join(",", vhArray);
				gameSpecMultiName = gameArray.isEmpty() ? null : String.join(",", gameArray);
			}
		} catch (IOException e) {
		}

		if (vhQualifiedName1 == null || vhQualifiedName1.equals(""))
			vhQualifiedName1 =  null;

		if (gameSpecMultiName == null || gameSpecMultiName.equals(""))
			gameSpecMultiName =  null;

		if(questionnaireNeuroticism != -1 && gameSpecMultiName == null && vhQualifiedName1 == null)
			WebSocketUtils.send(new Gson().toJson(new WebSocketUtils(). new JsonObject("trueEnd", "end.html")), session);
		else {
			allGameSpecNames = new ArrayList<String>(Arrays.asList(gameSpecMultiName.split("\\s*,\\s*")));
			allStoredVHNames = new ArrayList<String>(Arrays.asList(vhQualifiedName1.split("\\s*,\\s*")));
			ServletUtils.log("We found the following GameSpecs: " + allGameSpecNames.toString(), ServletUtils.DebugLevels.DEBUG);

			GameSpec gs = null;

			try {
				@SuppressWarnings("unchecked")
				Class<? extends GameSpec> gsclass = (Class<? extends GameSpec>) Class.forName(allGameSpecNames.get(currentGame));
				Constructor<? extends GameSpec> ctor = gsclass.getDeclaredConstructor(boolean.class);
				ctor.setAccessible(true);
				gs = ctor.newInstance(new Object[]{true});
			} catch (ClassNotFoundException e2) {
				System.err.println("We were unable to load the primary GameSpec file from the class name provided in the configuration file.");
				e2.printStackTrace();
			} catch (NoSuchMethodException | SecurityException e1) {
				System.err.println("You have not provided a constructor that meets the requirements! Make sure your extension of GameSpec has"
						+ " a matching set of arguments.");
				e1.printStackTrace();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}

			if (gs == null)
				try {
					throw new Exception("Unknown problem creating GameSpec class from configuration.");
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			u.setGameSpec(gs, false);
			this.gs = gs;

			//The following loads the appropriate agents based on the received input:
			try {
				GeneralVH vh0 = null;

				globalSession = session;

				if (globalMTurkID == null || globalMTurkID.equals(""))
					globalMTurkID = "MISSING";
				if (globalCondition == null || globalCondition.equals(""))
					globalCondition = "MISSING";
				String q1 = (String) httpSession.getAttribute("qualtricsQ1");
				String exp = "";
				if (q1 != null) {
					if (q1.equals("Positive")) {
						exp = "Happy";
					} else if (q1.equals("Negative")) {
						exp = "Angry";
					} else {
						exp = q1;
					}
				}
				String q2 = (String) httpSession.getAttribute("qualtricsQ2");
				String q3 = (String) httpSession.getAttribute("qualtricsQ3");
				String q4 = (String) httpSession.getAttribute("qualtricsQ4");
				if (q1 == null || q2 == null || q3 == null || q4 == null) {
					gameChoice = "player";
				}
				programmed = qFlag.equals("ON") ? true : false;

				if (vhQualifiedName0 == null && gameChoice.equals("agent") && programmed) //agent is being programed via q1-q4
				{
					String concatUserChoice = "edu.usc.ict.Mentalist.agent.IAGO" + q2 + exp + q1 + q3 + q4 + "VH";
					ServletUtils.log("Agent choice: " + concatUserChoice, ServletUtils.DebugLevels.DEBUG);

					//Displaying data in logs to make sure everything works correctly:
					ServletUtils.log("Hopefully working real agent choice: " + concatUserChoice, ServletUtils.DebugLevels.DEBUG);

					@SuppressWarnings("unchecked")
					Class<? extends GeneralVH> vhclass = (Class<? extends GeneralVH>) Class.forName(concatUserChoice);    //agent construction happens here and is built with above concatenated string

					Constructor<? extends GeneralVH> ctor0 = vhclass.getConstructor(String.class, GameSpec.class, javax.websocket.Session.class);
					vh0 = ctor0.newInstance(new Object[]{"defaultAgent", gs, session});
					storedVH0 = vh0;
					ServletUtils.log("We found the following user agent: " + storedVH0.toString(), ServletUtils.DebugLevels.DEBUG);
					//If vh0 isn't safe for MA games, do below block:
					if (!vh0.safeForMultiAgent()) {
						ServletUtils.log("One of your chosen agents is not capable of multi-agent. Reverted to single player.", ServletUtils.DebugLevels.DEBUG);
						vh0 = null;
						storedVH0 = null;
					}
					gs.setMultiAgent(vh0.safeForMultiAgent());
				} else if (vhQualifiedName0 != null) //a standard, non-Qualtrics agent has been registered via config.txt
				{
					@SuppressWarnings("unchecked")

					Class<? extends GeneralVH> vhclass = (Class<? extends GeneralVH>) Class.forName(vhQualifiedName0);
					Constructor<? extends GeneralVH> ctor0 = vhclass.getConstructor(String.class, GameSpec.class, javax.websocket.Session.class);
					vh0 = ctor0.newInstance(new Object[]{"defaultAgent", gs, session});
					storedVH0 = vh0;
					ServletUtils.log("We found the following user agent: " + storedVH0.toString(), ServletUtils.DebugLevels.DEBUG);
					//If vh0 isn't safe for MA games, do below block:
					if (!vh0.safeForMultiAgent()) {
						ServletUtils.log("One of your chosen agents is not capable of multi-agent. Reverted to single player.", ServletUtils.DebugLevels.DEBUG);
						vh0 = null;
						storedVH0 = null;
					}
					gs.setMultiAgent(vh0.safeForMultiAgent());
				} else//no agent should play this seat (normal mode)
				{
					ServletUtils.log("We found NO user agents.", ServletUtils.DebugLevels.DEBUG);
					storedVH0 = vh0;
				}

				GeneralVH vh1 = generateAgent();

				u.setMultiplayer(gameChoice.equals("agent") ? true : false);
				//vh1.setAgentVsAgent(gameChoice);
				//PERSONAL NOTE: onOpenHelper method activates here, linking to GBUtils class:
				u.onOpenHelper(session, config, vh0, vh1, questionnaireNeuroticism, questionnaireExtraversion, questionnaireOpenness, questionnaireConscientiouness, questionnaireAgreeableness);

				storedVH1 = vh1;
				ServletUtils.log("We found the following computer agent: " + storedVH1.toString(), ServletUtils.DebugLevels.DEBUG);
				if (vh0 != null && !vh1.safeForMultiAgent()) {
					ServletUtils.log("One of your chosen agents is not capable of multi-agent. Reverted to single player.", ServletUtils.DebugLevels.DEBUG);
				}
				u.setwebDir(webDir);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.err.println("We were unable to load the primary VH file from the class name provided in the configuration file.");
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				System.err.println("You have not provided a constructor that meets the requirements!");
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				System.err.println("Your VH did not cast properly.  Did it extend GeneralVH?");
				e.printStackTrace();
			}
		}
	}

	@OnClose
	public void onClose(javax.websocket.Session session, javax.websocket.CloseReason cr) {
		u.onCloseHelper(session, cr);
	}

	@OnMessage
	public void onMessage(javax.websocket.Session session, String msg, boolean last)
	{
		WebSocketUtils.JsonObject joIn = (WebSocketUtils.JsonObject)AccessController.doPrivileged(new PrivilegedAction<Object>()
		{
			public Object run() {
				return new Gson().fromJson(msg, WebSocketUtils.JsonObject.class);
			}

		});
		if(joIn.tag.equals("ngPing"))
		{
			ServletUtils.log("Ping received for new game", ServletUtils.DebugLevels.DEBUG);
			currentGame++;
			if(currentGame > allGameSpecNames.size() - 1)
			{
				//If the user isn't playing for a Qualtrics study, use the default ending below.
				if(globalMTurkID.equals("MISSING"))
				{
					ServletUtils.log("GlobalID was not found!  This data may end up as trash! Did you include ?id=<arg> as a GET parameter in your study link?", ServletUtils.DebugLevels.ERROR);
					//WebSocketUtils.send(new Gson().toJson(new WebSocketUtils(). new JsonObject("trueEnd", "The negotiation has ended.  You will now be redirected.")), session);

				}
				else//Otherwise, the user will need to be sent back to Qualtrics to complete the post-game survey:
				{
					updateConfig();
					WebSocketUtils.send(new Gson().toJson(new WebSocketUtils(). new JsonObject("trueEnd",
						this.gs.getRedirectLink() + "?id=" + globalMTurkID + "&condition=" + globalCondition)), session);
					//WebSocketUtils.close(session);
				}
			}
			else
			{
				try {
					updateConfig();
					buildUnprivilegedGameSpec();
					buildPrivilegedGameSpec();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else
			u.onMessageHelper(session, msg, last);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		webDir = new String(this.getServletContext().getRealPath("/WEB-INF/"));
		u.doPostHelper(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		webDir = new String(this.getServletContext().getRealPath("/WEB-INF/"));
		u.doGetHelper(request, response);
	}

	private void buildPrivilegedGameSpec() throws Exception
	{
		GameSpec gs = null;
		try {
			@SuppressWarnings("unchecked")
			Class<? extends GameSpec> gsclass = (Class<? extends GameSpec>) Class.forName(allGameSpecNames.get(currentGame));
			Constructor<? extends GameSpec> ctor = gsclass.getDeclaredConstructor(boolean.class);
			ctor.setAccessible(true);
			gs = ctor.newInstance(new Object [] {true});
		} catch (ClassNotFoundException e2) {
			System.err.println("We were unable to load the primary GameSpec file from the class name provided in the configuration file.");
			e2.printStackTrace();
		}
		catch (NoSuchMethodException | SecurityException e1) {
			System.err.println("You have not provided a constructor that meets the requirements! Make sure your extension of GameSpec has"
					+ " a matching set of arguments.");
			e1.printStackTrace();
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		if (gs == null)
			throw new Exception("Unknown problem creating GameSpec class from configuration.");

		u.setGameSpec(gs, true);
		this.gs = gs;
	}

	private void buildUnprivilegedGameSpec() throws Exception
	{
		GameSpec gs = null;
		try {
			@SuppressWarnings("unchecked")
			Class<? extends GameSpec> gsclass = (Class<? extends GameSpec>) Class.forName(allGameSpecNames.get(currentGame));
			Constructor<? extends GameSpec> ctor = gsclass.getDeclaredConstructor(boolean.class);
			ctor.setAccessible(true);
			gs = ctor.newInstance(new Object [] {false});
		} catch (ClassNotFoundException e2) {
			System.err.println("We were unable to load the primary GameSpec file from the class name provided in the configuration file.");
			e2.printStackTrace();
		}
		catch (NoSuchMethodException | SecurityException e1) {
			System.err.println("You have not provided a constructor that meets the requirements! Make sure your extension of GameSpec has"
					+ " a matching set of arguments.");
			e1.printStackTrace();
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		if (gs == null)
			try {
				throw new Exception("Unknown problem creating GameSpec class from configuration.");
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		if (storedVH0 != null) {
			storedVH0.setGameSpec(gs);
			gs.setMultiAgent(true);
		}
		storedVH1 = generateAgent();
		u.setNewAgent(storedVH1);
		storedVH1.setGameSpec(gs);
		this.gs = gs;
	}

	private GeneralVH generateAgent(){

		vhQualifiedName1 = allStoredVHNames.get(currentGame);

		//The following loads the appropriate agents based on the received input:
		try {
			GeneralVH vh1 = null;
			javax.websocket.Session session = globalSession;
			if (vhQualifiedName1 != null) {

				//Class<? extends GeneralVH> vhClass1 = (Class<? extends GeneralVH>) Class.forName(agentChoice);
				if (vhQualifiedName1.contains(".MentalistVH") || vhQualifiedName1.contains(".PilotStudyVH")) {
					Class<? extends GeneralVH> vhClass1 = (Class<? extends GeneralVH>) Class.forName(vhQualifiedName1);
					Constructor<? extends GeneralVH> ctor1 = vhClass1.getConstructor(String.class, GameSpec.class, javax.websocket.Session.class);
					vh1 = ctor1.newInstance(new Object[]{"defaultAgent", gs, session});
				} else if (vhQualifiedName1.contains(".StaticMentalistVH")) {
					Constructor<StaticMentalistVH> ctor1 = StaticMentalistVH.class.getConstructor(String.class, GameSpec.class, javax.websocket.Session.class, int.class, int.class, int.class, int.class, int.class);
					vh1 = ctor1.newInstance(new Object[]{"defaultAgent", gs, session, questionnaireNeuroticism, questionnaireExtraversion, questionnaireOpenness, questionnaireConscientiouness, questionnaireAgreeableness});
				}
			} else { //human-human case
				ServletUtils.log("Computer agent is null, should be human-human case", ServletUtils.DebugLevels.DEBUG);

				SessionState state = Governor.getSessionState(httpSession);
				if (state == SessionState.NEGOTIATING) {
					WebSocketUtils.send(new Gson().toJson(new WebSocketUtils().new JsonObject("ERR", "You have an unclosed negotiation already.")), session);
					return null;
				}
				nRoom = Governor.findUserByHttpSession(httpSession);
				if (nRoom == null) {
					ServletUtils.log("Error, no previous connection", ServletUtils.DebugLevels.DEBUG);
				} else { //update new websocket
					WebSocketUtils.send(new Gson().toJson(new WebSocketUtils().new JsonObject("MSG", "Test")), session);
					nRoom.setGBU(httpSession, this.u);
					nRoom.updateSocket(httpSession, session);
					Governor.startNegotiating(httpSession);
				}


				u.setNegotiationMode(NegotiationMode.HUMAN_HUMAN);
				u.setNRoom(nRoom);
			}
			return vh1;
		} catch (ClassNotFoundException e) {
			System.err.println("We were unable to load the primary VH file from the class name provided in the configuration file.");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.err.println("You have not provided a constructor that meets the requirements!");
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			System.err.println("Your VH did not cast properly.  Did it extend GeneralVH?");
			e.printStackTrace();
		}
		return null;
	}

	public void updateConfig(){
		try{
			String folderName = webDir +  "config/" + globalMTurkID ;

			String fileName = folderName + "/agent.txt";

			BufferedWriter bw = new BufferedWriter(new FileWriter((new File( fileName)), true));

			bw.write(allStoredVHNames.get(currentGame - 1));
			bw.newLine();

			bw.close();

			fileName = folderName + "/game.txt";

			bw = new BufferedWriter(new FileWriter((new File( fileName)), true));

			bw.write(allGameSpecNames.get(currentGame - 1));
			bw.newLine();

			bw.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
	}

	public ArrayList<String> createNewConfig(){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if(classLoader == null)
		{
			ServletUtils.log("Context class loader is null--resources may be improperly loaded", ServletUtils.DebugLevels.WARN);
			classLoader = ClassLoader.getSystemClassLoader();
			if (classLoader == null)
				ServletUtils.log("System class loader is null--cannot load properly.", ServletUtils.DebugLevels.ERROR);
		}

		ArrayList<String> vhArray = new ArrayList<>();
		ArrayList<String> gameArray = new ArrayList<>();
		ArrayList<String> newConfig = new ArrayList<>();

		try {
			InputStream templateInput = classLoader.getResourceAsStream("config_template.txt");

			if (templateInput == null) {
				ServletUtils.log("Configuration file not found!", ServletUtils.DebugLevels.ERROR);
			}

			Properties templateProperties = new Properties();

			try {
				templateProperties.load(templateInput);
				vhArray = new ArrayList<String>(Arrays.asList(templateProperties.getProperty("agent").split("\\s*,\\s*")));
				gameArray = new ArrayList<String>(Arrays.asList(templateProperties.getProperty("gamespec").split("\\s*,\\s*")));

			} catch (IOException e) {
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(Integer.parseInt(globalMTurkID) % 3 == 0){
			//そのまま
		}
		else if(Integer.parseInt(globalMTurkID) % 3 == 1){
			String vh = vhArray.remove(0);
			String game = gameArray.remove(0);
			vhArray.add(vh);
			gameArray.add(game);
		}
		else{
			ArrayList<String> vh = new ArrayList<>();
			ArrayList<String> game = new ArrayList<>();
			vh.add(vhArray.remove(0));
			vh.add(vhArray.remove(0));
			game.add(gameArray.remove(0));
			game.add(gameArray.remove(0));
			vhArray.addAll(vh);
			gameArray.addAll(game);
		}

		newConfig.add(String.join(",", vhArray));
		newConfig.add(String.join(",", gameArray));

		return newConfig;
	}
}
