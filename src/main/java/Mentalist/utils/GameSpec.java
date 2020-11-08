package Mentalist.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

public abstract class GameSpec {
    private Map<LinkedList<Integer>, Integer> advancedOpponentPoints = new HashMap<>();

    private Map<LinkedList<Integer>, Integer> advancedUserPoints = new HashMap<>();

    private Map<String, Integer> simpleOpponentPoints = new HashMap<>();

    private Map<String, Integer> simpleUserPoints = new HashMap<>();

    private int opponentBATNA = 0;

    private int userBATNA = 0;

    private boolean isMultiAgent = false;

    private final double MULTI_DELAY = 5.0D;

    private final double SINGLE_DELAY = 1.0D;

    private double multiplier = 1.0D;

    Properties properties = new Properties();

    private Map<String, Map<String, String>> menuIndex = new HashMap<>();

    private boolean isPrivileged = false;

    public final Map<LinkedList<Integer>, Integer> getAdvancedOpponentPoints() {
        return this.advancedOpponentPoints;
    }

    public final Map<LinkedList<Integer>, Integer> getAdvancedUserPoints() throws SecurityException {
        if (!isUserBlind())
            return this.advancedUserPoints;
        if (this.isPrivileged)
            return this.advancedUserPoints;
        throw new SecurityException("Agent is attempting to access privileged information.");
    }

    public final Map<LinkedList<Integer>, Integer> getAdvancedPoints(int ID) {
        if (ID == 0)
            return this.advancedUserPoints;
        if (ID == 1)
            return getAdvancedOpponentPoints();
        return null;
    }

    public final Map<String, Integer> getSimpleOpponentPoints() {
        return this.simpleOpponentPoints;
    }

    public final Map<String, Integer> getSimpleUserPoints() throws SecurityException {
        if (!isUserBlind())
            return this.simpleUserPoints;
        if (this.isPrivileged)
            return this.simpleUserPoints;
        throw new SecurityException("Agent is attempting to access privileged information.");
    }

    public final Map<String, Integer> getSimplePoints(int ID) {
        if (ID == 0)
            return this.simpleUserPoints;
        if (ID == 1)
            return getSimpleOpponentPoints();
        return null;
    }

    public final int getOpponentBATNA() {
        return this.opponentBATNA;
    }

    public final int getPlayerBATNA() throws SecurityException {
        if (!isUserBlind())
            return this.userBATNA;
        if (this.isPrivileged)
            return this.userBATNA;
        throw new SecurityException("Agent is attempting to access privileged information.");
    }

    public final int getBATNA(int ID) {
        if (ID == 0)
            return this.userBATNA;
        if (ID == 1)
            return getOpponentBATNA();
        return -1;
    }

    public boolean isMultiAgent() {
        return this.isMultiAgent;
    }

    public void setMultiAgent(boolean isMultiAgent) {
        this.isMultiAgent = isMultiAgent;
        this.multiplier = 5.0D;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public final Map<String, Map<String, String>> getMenu() {
        return this.menuIndex;
    }

    public Preference getPreference(int issue1, int issue2, int ID) {
        String string1 = getIssuePluralNames()[issue1];
        String string2 = getIssuePluralNames()[issue2];
        if (((Integer) getSimplePoints(ID).get(string1)).intValue() > ((Integer) getSimplePoints(ID).get(string2)).intValue())
            return new Preference(issue1, issue2, Preference.Relation.GREATER_THAN, false);
        if (getSimplePoints(ID).get(string1) == getSimplePoints(ID).get(string2))
            return new Preference(issue1, issue2, Preference.Relation.EQUAL, false);
        return new Preference(issue1, issue2, Preference.Relation.LESS_THAN, false);
    }

    public abstract int getNumIssues();

    public abstract int[] getIssueQuants();

    public abstract String[] getIssuePluralNames();

    public abstract String[] getIssueNames();

    public boolean isAdvancedPoints() {
        return false;
    }

    public int getTotalTime() {
        return 300;
    }

    public boolean isUserBlind() {
        return true;
    }

    public boolean showOpponentScore() {
        return false;
    }

    public abstract String getTargetEmail();

    public String getEndgameMessage() {
        return "<p>Thank you for participating.  You will now be redirected to a page containing instructions for completing the activity.</p>";
    }

    public String getNewgameMessage() {
        return "<p>Get ready to start a new round against the same partner.  Be warned, the items and their values may have changed!</p>";
    }

    public boolean showOpponentScoreOnEnd() {
        return true;
    }

    public boolean showNegotiationTimer() {
        return true;
    }

    public String getRedirectLink() {
        return null;
    }

    @Deprecated
    public int getOverflowSize() {
        return 10;
    }

    public abstract String getStudyName();

    public String getSurvey() {
        return null;
    }

    public Map<String, Map<String, String>> buildMenu() {
        HashMap<String, String> menuRoot = new HashMap<>();
        HashMap<String, String> menuYouLike = new HashMap<>();
        HashMap<String, String> menuILike = new HashMap<>();
        HashMap<String, String> menuBecause = new HashMap<>();
        HashMap<String, String> menuBecauseNeg = new HashMap<>();
        HashMap<String, String> menuBecausePos = new HashMap<>();
        HashMap<String, String> menuBATNA = new HashMap<>();
        HashMap<String, String> menuBATNASlider = new HashMap<>();
        HashMap<String, String> menuMisc = new HashMap<>();
        Map<String, Map<String, String>> menuIndexTemp = new HashMap<>();
        menuRoot.put("butYouLike", "Ask your opponent's preferences >");
        menuRoot.put("butILike", "Tell your own preferences >");
        menuRoot.put("butCustom1", "Use emotion to influence your opponent >");
        menuRoot.put("butCustom2", "Get favorable deals and explore alternatives! >");
        menuYouLike.put("buttonBuffer", "");
        menuYouLike.put("butItemsComparison", "");
        menuYouLike.put("butItemsDiv", "");
        menuYouLike.put("craftingMessageString", "");
        menuYouLike.put("compareMessageString", "");
        menuYouLike.put("butPrefDiv", "");
        int i;
        for (i = 0; i < getNumIssues(); i++)
            menuYouLike.put("butItem" + i, "");
        menuYouLike.put("butItemFirst", "");
        menuYouLike.put("butItemComparison", "");
        menuYouLike.put("butItemSecond", "");
        menuYouLike.put("butConfirm", "<Confirm>");
        menuYouLike.put("butBack", "<Back>");
        menuILike.put("buttonBuffer", "");
        menuILike.put("butItemsComparison", "");
        menuILike.put("butItemsDiv", "");
        menuILike.put("craftingMessageString", "");
        menuILike.put("compareMessageString", "");
        menuILike.put("butPrefDiv", "");
        for (i = 0; i < getNumIssues(); i++)
            menuILike.put("butItem" + i, "");
        menuILike.put("butItemFirst", "");
        menuILike.put("butItemComparison", "");
        menuILike.put("butItemSecond", "");
        menuILike.put("butConfirm", "<Confirm>");
        menuILike.put("butWithhold", "I don't think it best to reveal my intentions yet.  Maybe if you did first...");
        menuILike.put("butBack", "<Back>");
        menuBecausePos.put("butExpl1", "We should try to split things evenly.");
        menuBecausePos.put("butExpl2", "We should each get our most valuable item.");
        menuBecausePos.put("butExpl3", "We should try harder to find a deal that benefits us both.");
        menuBecausePos.put("butExpl4", "I wish we could reach something a little more fair.");
        menuBecausePos.put("butExpl5", "Would you please make an offer?");
        menuBecausePos.put("butExpl6", "I'm sorry but I think I may walk away.");
        menuBecauseNeg.put("butExpl11", "If you don't split this evenly there will be consequences!");
        menuBecauseNeg.put("butExpl12", "I just want us to get our most valuable items so this can be over with!");
        menuBecauseNeg.put("butExpl13", "You need to try a LOT harder to find a deal that benefits us both!");
        menuBecauseNeg.put("butExpl14", "You not even trying to find something remotely fair to both of us!");
        menuBecauseNeg.put("butExpl15", "What wrong with you? Hurry up and make an offer!");
        menuBecauseNeg.put("butExpl16", "You making me want to walk away from this!");
        menuBecause.put("butBack", "<Back>");
        menuBecauseNeg.put("butBack", "<Back>");
        menuBecausePos.put("butBack", "<Back>");
        menuBATNA.put("butExpl21", "So could you tell me what's your bottom line?");
        menuBATNA.put("butBack", "<Back>");
        menuBATNASlider.put("BATNAInstructions", "");
        menuBATNASlider.put("batnaSlider", "");
        menuBATNASlider.put("butSend", "<Send>");
        menuBATNASlider.put("butBack", "<Back>");
        menuBATNA.put("butCustom2_1", "My bottom line is...  >");
        menuBATNA.put("butExpl22", "Would you please send a good deal in exchange for a favor?");
        menuBATNA.put("butExpl24", "I'm returning the favor to you!  Give me a deal good for you.");
        menuMisc.put("butExpl25", "I'm thinking...");
        menuMisc.put("butExpl26", "I'm happy with this so far!");
        menuMisc.put("butExpl27", "I'm not happy with this...");
        menuMisc.put("butExpl30", "I don't think that makes sense with previous statements.");
        menuMisc.put("butExpl31", "So could you tell me about your preferences?");
        menuMisc.put("butBack", "<Back>");
        menuBecause.put("butCustom1_1", "Friendly options >");
        menuBecause.put("butCustom1_2", "Unfriendly options>");
        menuBecause.put("butCustom1_3", "Neutral options >");
        menuIndexTemp.put("root", menuRoot);
        menuIndexTemp.put("youLike", menuYouLike);
        menuIndexTemp.put("iLike", menuILike);
        menuIndexTemp.put("custom1", menuBecause);
        menuIndexTemp.put("custom1_1", menuBecausePos);
        menuIndexTemp.put("custom1_2", menuBecauseNeg);
        menuIndexTemp.put("custom2", menuBATNA);
        menuIndexTemp.put("custom2_1", menuBATNASlider);
        menuIndexTemp.put("custom1_3", menuMisc);
        return menuIndexTemp;
    }

    protected final void enablePrivilege() {
        this.isPrivileged = true;
    }

    protected final void setOpponentBATNA(int batna) {
        this.opponentBATNA = batna;
    }

    protected final void setUserBATNA(int batna) {
        this.userBATNA = batna;
    }

    protected final void setSimpleUserPoints(Map<String, Integer> points) {
        this.simpleUserPoints = points;
    }

    protected final void setSimpleOpponentPoints(Map<String, Integer> points) {
        this.simpleOpponentPoints = points;
    }

    protected final void setAdvancedUserPoints(Map<LinkedList<Integer>, Integer> points) {
        this.advancedUserPoints = points;
    }

    protected final void setAdvancedOpponentPoints(Map<LinkedList<Integer>, Integer> points) {
        this.advancedOpponentPoints = points;
    }

    protected final void setIndexMenu(Map<String, Map<String, String>> index) {
        this.menuIndex = index;
    }
}