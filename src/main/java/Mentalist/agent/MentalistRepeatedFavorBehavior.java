package Mentalist.agent;

import Mentalist.utils.*;

import javax.servlet.Servlet;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;

import static java.lang.Math.*;

public class MentalistRepeatedFavorBehavior extends MentalistCoreBehavior implements BehaviorPolicy {

	private MentalistAgentUtilsExtension utils;
	private GameSpec game;
	private Offer allocated;
	private Offer concession;
	private Offer previous;
	private QueueList<Offer> previousOffers;
	private QueueList<Offer> dummyPlayerOffers;
	private QueueList<Offer> dummyAgentOffers;
	private LedgerBehavior lb = LedgerBehavior.NONE;
	private int adverseEvents = 0;
	//target function関連
	private double gamma_min;
	private double gamma_max;
	private int n;
	private int t;
	private double alpha;
	//TKI関連
	private ArrayList<Double> assertiveness;
	private ArrayList<Double> cooperativeness;
	//big5関連
	private int preBehaviorTiminig;
	private double mean;
	private double variance;
	private double behaviorFrequency;
	private QueueList<Integer> preferenceAskNum;
	private QueueList<Integer> preferenceExpressionNum;
	private QueueList<Integer> lieNum;
	private QueueList<Integer> posEmotionNum;
	private QueueList<Integer> negEmotionNum;
	private QueueList<Integer> posMessageNum;
	private QueueList<Integer> negMessageNum;
	private QueueList<Integer> threatNum;
	private QueueList<Integer> fastResponseNum;
	private QueueList<Integer> behaviorTimings;
	private ArrayList<Double> neuroticism;
	private ArrayList<Double> extraversion;
	private ArrayList<Double> openness;
	private ArrayList<Double> conscientiousness;
	private ArrayList<Double> agreeableness;
	//behavior sence関連
	private QueueList<Integer> silentNum;
	private QueueList<Integer> niceNum;
	private QueueList<Integer> fortunateNum;
	private QueueList<Integer> selfishNum;
	private QueueList<Integer> concessionNum;
	private QueueList<Integer> unfortunateNum;
	private double behaviorSence;
	//その他
	private double offerRatio;
	private double behaviorRatio;
	private final double MAX = 1.0;
	private final double MIN = -1.0;
	private final double SENCE_MAX = 2.0;
	private final int TIME_INTERVAL = 10;
	private final double MES_MAX = 3.0;
	private final double SPECIAL_MES_MAX = 1.0;
	private final double EMO_MAX = 3.0;
	private final int BEH_SIZE = 20;
	private final int OFF_SIZE = 10;
	private final int EMO_SIZE = 5;
	private final int CATEGORY_SIZE = 5;

	public enum LedgerBehavior
	{
		FAIR,
		LIMITED,
		BETRAYING,
		NONE;
	}

	public MentalistRepeatedFavorBehavior(LedgerBehavior lb)
	{
		super();
		this.lb = lb;
	}

	//譲歩関数
	public double getConcessionValue(){
		return max(gamma_min, gamma_min + (gamma_max - gamma_min) * (1 - ((double)this.utils.myActualOfferValue(previous) / this.utils.getMaxPossiblePoints()) * pow((double)t / n,(1 / alpha)))) * this.utils.getMaxPossiblePoints();
	}

	//正規化
	public double normarize(double data, double data_max, double data_min){
		return (data_max != data_min) ? (data - data_min) / (data_max - data_min) * (MAX - MIN) + MIN : 0.0;
	}

	//質問回数の計算
	public double maxAskNum(){
		return game.getNumIssues();
	}

	//効用の分散を計算(ここでは標準偏差)
	public double calcVariance(ArrayList<Offer> offers){
		double mean = 0.0;
		double variance = 0.0;
		if(!offers.isEmpty()) {
			for (Offer o: offers) {
				mean += this.utils.adversaryValue(o, this.utils.getMinimaxOrdering());
			}
			mean /= offers.size();
			for(Offer o: offers){
				variance += abs(this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) - mean);
			}
			variance /= sqrt(offers.size());
		}
		return variance;
	}

	//効用の平均を計算
	public double calcMean(ArrayList<Offer> offers){
		double mean = 0.0;
		if(!offers.isEmpty()) {
			for (Offer o : offers) {
				mean += this.utils.adversaryValue(o, this.utils.getMinimaxOrdering());
			}
			mean /= offers.size();
		}
		return mean;
	}

	public void printParameter(){
		calcWeight();
		adjustSize();

		calcCooperativeness();
		calcAssertiveness();

		calcAgreeableness();
		calcNeuroticism();
		calcExtraversion();
		calcOpenness();
		calcConscientiousness();


		ServletUtils.log("previous: " + previousOffers, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy player: " + dummyPlayerOffers, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy agent: " + dummyAgentOffers, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("previous queue: " + previousOffers.getQueue(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy player queue: " + dummyPlayerOffers.getQueue(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy agent queue: " + dummyAgentOffers.getQueue(), ServletUtils.DebugLevels.DEBUG);

		ServletUtils.log("behavior weight: " + behaviorRatio, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("offer weight: " + offerRatio, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior timing: " + behaviorTimings, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior freq: " + behaviorFrequency, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pos num: " + posMessageNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neg num: " + negMessageNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pos mes point: " + normarize(posMessageNum.sum(), max(MES_MAX, posMessageNum.sum()),-MES_MAX), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neg mes point: " + normarize(negMessageNum.sum(), max(MES_MAX, negMessageNum.sum()),-MES_MAX), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pos emo point: " + normarize(posEmotionNum.sum(), max(EMO_MAX, posEmotionNum.sum()),-EMO_MAX), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neg emo point: " + normarize(negEmotionNum.sum(), max(EMO_MAX, negEmotionNum.sum()),-EMO_MAX), ServletUtils.DebugLevels.DEBUG);

		ServletUtils.log("TKI-coop: " + cooperativeness.get(cooperativeness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("TKI-asse: " + assertiveness.get(assertiveness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Neu: " + neuroticism.get(neuroticism.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Ext: " + extraversion.get(extraversion.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Opn: " + openness.get(openness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Agr: " + agreeableness.get(agreeableness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Con: " + conscientiousness.get(conscientiousness.size() - 1), ServletUtils.DebugLevels.DEBUG);

	}

	public void adjustSize(){
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != preferenceAskNum.getFullSize()){
			for(int i = preferenceAskNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				preferenceAskNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != preferenceExpressionNum.getFullSize()){
			for(int i = preferenceExpressionNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				preferenceExpressionNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != lieNum.getFullSize()){
			for(int i = lieNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				lieNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != posEmotionNum.getFullSize()){
			for(int i = posEmotionNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				posEmotionNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != negEmotionNum.getFullSize()){
			for(int i = negEmotionNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				negEmotionNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != posMessageNum.getFullSize()){
			for(int i = posMessageNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				posMessageNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != negMessageNum.getFullSize()){
			for(int i = negMessageNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				negMessageNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != threatNum.getFullSize()){
			for(int i = threatNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				threatNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != fastResponseNum.getFullSize()){
			for(int i = fastResponseNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				fastResponseNum.enQueue(0);
			}
		}
	}

	public void addDummyPlayerOffer(Offer o){
		Offer p = new Offer(game.getNumIssues());
		//未定義のアイテムをissueごとに把握
		int[] free = new int[game.getNumIssues()];
		int[] agent = new int[game.getNumIssues()];
		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = o.getItem(issue)[1];
			agent[issue] = o.getItem(issue)[0];
		}
		if(dummyPlayerOffers.getFullSize() % 2 == 0){
			//プレイヤーに全振り
			for (int i = 0; i < game.getNumIssues(); i++) {
				int[] init = {0, free[i], game.getIssueQuants()[i] - free[i]};
				p.setItem(i, init);
			}
		}
		else{
			//エージェントにも少し分ける
			int rand = new Random().nextInt(game.getNumIssues());
			for (int i = 0; i < game.getNumIssues(); i++) {
				int[] init = {agent[i] != 0 && i == rand ? agent[i] : 0, free[i], agent[i] != 0 && i == rand ? game.getIssueQuants()[i] - free[i] - agent[i] : game.getIssueQuants()[i] - free[i]};
				p.setItem(i, init);
			}
		}
		dummyPlayerOffers.enQueue(p);
	}

	public void addDummyAgentOffer(Offer o){
		Offer p = new Offer(game.getNumIssues());
		//未定義のアイテムをissueごとに把握
		int[] free = new int[game.getNumIssues()];
		int[] player = new int[game.getNumIssues()];
		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = o.getItem(issue)[1];
			player[issue] = o.getItem(issue)[2];
		}
		if(dummyAgentOffers.getFullSize() % 2 == 0){
			//エージェントに全振り
			for (int i = 0; i < game.getNumIssues(); i++) {
				int[] init = {game.getIssueQuants()[i] - free[i], free[i], 0};
				p.setItem(i, init);
			}
		}
		else{
			//プレイヤーにも少し分ける
			int rand = new Random().nextInt(game.getNumIssues());
			for (int i = 0; i < game.getNumIssues(); i++) {
				int[] init = {player[i] != 0 && i == rand ? game.getIssueQuants()[i] - free[i] - player[i] : game.getIssueQuants()[i] - free[i], free[i], player[i] != 0 && i == rand ? player[i] : 0};
				p.setItem(i, init);
			}
		}
		dummyAgentOffers.enQueue(p);
	}

	//譲歩関数に基づいて譲歩するOfferを更新
	protected void updateConcession(){
		double concession = getConcessionValue();
		Offer o = new Offer(game.getNumIssues());
		for(int i = 0; i < game.getNumIssues(); i++)
		{
			int[] init = this.allocated.getItem(i);
			o.setItem(i, init);
		}
		ArrayList<Integer> vhPref = utils.getMyOrdering();
		int total = utils.myActualOfferValue(o);
		for(int i = 1; i <= game.getNumIssues(); i++){
			int allocatedQuant = o.getItem(vhPref.indexOf(i))[0] + o.getItem(vhPref.indexOf(i))[2];
			for(int j = 0; j < game.getIssueQuants()[vhPref.indexOf(i)] - allocatedQuant; j++){
				if(total >= concession) break;
				o.setItem(vhPref.indexOf(i), new int[] {o.getItem(vhPref.indexOf(i))[0] + 1, o.getItem(vhPref.indexOf(i))[1] - 1, o.getItem(vhPref.indexOf(i))[2]});
				total = utils.myActualOfferValue(o);
			}
			if(total >= concession) break;
		}
		this.concession = o;
	}

	public void setPrevious(Offer o){ this.previous = o; }

	/**** TKI協調性 ****/
	public void calcCooperativeness(){
		//@@Mark
		double mean = calcMean(previousOffers.getExceptZeroQueue());
		this.cooperativeness.add(- normarize(mean, max(calcMean(dummyPlayerOffers.getExceptZeroQueue()), mean), min(0.3 * calcMean(dummyPlayerOffers.getExceptZeroQueue()), mean)));
	}

	/**** TKI積極性 ****/
	public void calcAssertiveness(){
		double variance = calcVariance(previousOffers.getQueue());
		this.assertiveness.add(- normarize(variance, max(calcVariance(dummyPlayerOffers.getQueue()), variance), min(0.3 * calcVariance(dummyPlayerOffers.getQueue()), variance)));
	}

	/**** ビッグファイブ神経症傾向 ****/
	public void calcNeuroticism(){
		//calcEmotionRatio();
		this.neuroticism.add((normarize(niceNum.sum() / previousOffers.queueSize() + concessionNum.sum() / previousOffers.queueSize(), 0.7, 0.0)) * offerRatio + (normarize(negEmotionNum.sum(), max(EMO_MAX, negEmotionNum.sum()), -EMO_MAX) + normarize(negMessageNum.sum(), max(MES_MAX, negMessageNum.sum()),-MES_MAX)) / 2 * behaviorRatio);
	}
/*
	//ネガティブ感情の割合を計算
	public void calcEmotionRatio(){
		int posEmotionNum = 0;
		int negEmotionNum = 0;
		if(!expressions.isEmpty()) {
			for (String s : expressions.getQueue()) {
				if (s.equals("sad") || s.equals("angry"))
					negEmotionNum++;
				else if (s.equals("happy") || s.equals("surprised"))
					posEmotionNum++;
			}
			this.posEmotionNum = posEmotionNum;
			this.negEmotionNum = negEmotionNum;
		}
		else {
			this.posEmotionNum = 0;
			this.negEmotionNum = 0;
		}
	}
	*/

	/**** ビッグファイブ外向性 ****/
	public void calcExtraversion(){
		calcFrequency();
		this.extraversion.add((normarize(selfishNum.sum() / previousOffers.queueSize() + fortunateNum.sum() / previousOffers.queueSize(), 0.7, 0.0)) * offerRatio + (normarize(threatNum.sum(), max(SPECIAL_MES_MAX, threatNum.sum()), -SPECIAL_MES_MAX) - normarize(behaviorFrequency, max(behaviorFrequency, 60.0),  min(behaviorFrequency, 10.0))) / 2 * behaviorRatio);
	}

	//行動の頻度
	public void calcFrequency(){
		if(behaviorTimings.queueSize() >= 1) {
			double frequency = behaviorTimings.getQueue().get(0);
			for (int i = 1; i < behaviorTimings.queueSize(); i++) {
				frequency += behaviorTimings.getQueue().get(i) - behaviorTimings.getQueue().get(i - 1);
			}
			this.behaviorFrequency = frequency / behaviorTimings.queueSize();
		}
		else
			this.behaviorFrequency = 0.0;
	}

	/**** ビッグファイブ経験への開放性 ****/
	public void calcOpenness(){
		double choicePlayerVariance = calcChoiceVariance(previousOffers.getQueue(), 2);
		double choiceOpponentVariance = calcChoiceVariance(previousOffers.getQueue(), 0);
		this.openness.add(max(normarize(choicePlayerVariance, max(calcChoiceVariance(dummyPlayerOffers.getQueue(), 2), choicePlayerVariance), 0.0), normarize(choiceOpponentVariance, max(calcChoiceVariance(dummyAgentOffers.getQueue(), 0), choiceOpponentVariance), 0.0)) * offerRatio + (normarize(preferenceExpressionNum.sum(), max(maxAskNum(), preferenceExpressionNum.sum()), -maxAskNum()) + normarize(preferenceAskNum.sum(), max(maxAskNum(), preferenceAskNum.sum()), -maxAskNum())) / 2 * behaviorRatio);
	}

	//選択肢の分散(ここでは標準偏差)
	public double calcChoiceVariance(ArrayList<Offer> offers, int userNum){
		double means[] = new double[game.getNumIssues()];
		double variances = 0.0;
		double resultVariance = 0.0;
		if(!offers.isEmpty()) {
			for (int i = 0; i < offers.size(); i++) {
				for (int j = 0; j < game.getNumIssues(); j++) {
					means[j] += offers.get(i).getItem(j)[userNum];
				}
			}
			for (int i = 0; i < offers.size(); i++) {
				for (int j = 0; j < game.getNumIssues(); j++) {
					variances += abs(offers.get(i).getItem(j)[userNum] - means[j] / offers.size());
				}
			}
			resultVariance = variances / sqrt((game.getNumIssues() * offers.size()));
		}
		return resultVariance;
	}

	//選好に関する質問の回数
	public void addPreferenceAskNum(){ this.preferenceAskNum.enQueue(1); }

	/**** ビッグファイブ誠実性 ****/
	public void calcConscientiousness(){
		double variance = calcVariance(previousOffers.getQueue());
		this.conscientiousness.add(- normarize(variance, max(calcVariance(dummyPlayerOffers.getQueue()), variance), min(0.3 * calcVariance(dummyPlayerOffers.getQueue()), variance)) * offerRatio + (normarize(fastResponseNum.sum(), max(MES_MAX, fastResponseNum.sum()), -MES_MAX) + normarize(lieNum.sum(), max(SPECIAL_MES_MAX, lieNum.sum()), -SPECIAL_MES_MAX)) / 2 * behaviorRatio);
	}
	//選好を表出した回数
	public void addPreferenceExpressionNum(){ this.preferenceExpressionNum.enQueue(1);}

	//嘘をついた回数
	public void addLieNum(){ this.lieNum.enQueue(1); }

	/**** ビッグファイブ協調性 ****/
	public void calcAgreeableness(){
		calcBehaviorSence();
		this.agreeableness.add(normarize(behaviorSence, max(SENCE_MAX, behaviorSence), 0.0) * offerRatio + (normarize(posEmotionNum.sum(), max(EMO_MAX, posEmotionNum.sum()), -EMO_MAX) + normarize(posMessageNum.sum(), max(MES_MAX, posMessageNum.sum()),-MES_MAX)) / 2 * behaviorRatio);
	}
	//行動に対する感受性
	public void calcBehaviorSence(){
		if(previousOffers.queueSize() < 2) return;

		int opponentDiff = utils.adversaryValue(previousOffers.getLastElement(), utils.getMinimaxOrdering()) - utils.adversaryValue(previousOffers.getQueue().get(previousOffers.queueSize() - 2), utils.getMinimaxOrdering());
		int agentDiff = utils.myActualOfferValue(previousOffers.getLastElement()) - utils.myActualOfferValue(previousOffers.getQueue().get(previousOffers.queueSize() - 2));

		if(opponentDiff == 0){
			if(agentDiff == 0) {
				silentNum.enQueue(1);
				niceNum.enQueue(0);
				selfishNum.enQueue(0);
				fortunateNum.enQueue(0);
				concessionNum.enQueue(0);
				unfortunateNum.enQueue(0);
			}else if(agentDiff > 0) {
				niceNum.enQueue(1);
				silentNum.enQueue(0);
				selfishNum.enQueue(0);
				fortunateNum.enQueue(0);
				concessionNum.enQueue(0);
				unfortunateNum.enQueue(0);
			}
			else {
				selfishNum.enQueue(1);
				silentNum.enQueue(0);
				niceNum.enQueue(0);
				fortunateNum.enQueue(0);
				concessionNum.enQueue(0);
				unfortunateNum.enQueue(0);
			}
		}
		else if(opponentDiff > 0){
			if(agentDiff > 0) {
				fortunateNum.enQueue(1);
				silentNum.enQueue(0);
				niceNum.enQueue(0);
				selfishNum.enQueue(0);
				concessionNum.enQueue(0);
				unfortunateNum.enQueue(0);
			}
			else if(agentDiff < 0) {
				selfishNum.enQueue(1);
				silentNum.enQueue(0);
				niceNum.enQueue(0);
				fortunateNum.enQueue(0);
				concessionNum.enQueue(0);
				unfortunateNum.enQueue(0);
			}
			else {
				selfishNum.enQueue(1);
				silentNum.enQueue(0);
				niceNum.enQueue(0);
				fortunateNum.enQueue(0);
				concessionNum.enQueue(0);
				unfortunateNum.enQueue(0);
			}
		}
		else{
			if(agentDiff > 0) {
				concessionNum.enQueue(1);
				silentNum.enQueue(0);
				niceNum.enQueue(0);
				selfishNum.enQueue(0);
				fortunateNum.enQueue(0);
				unfortunateNum.enQueue(0);
			}
			else if(agentDiff < 0) {
				unfortunateNum.enQueue(1);
				silentNum.enQueue(0);
				niceNum.enQueue(0);
				selfishNum.enQueue(0);
				fortunateNum.enQueue(0);
				concessionNum.enQueue(0);
			}
			else {
				concessionNum.enQueue(1);
				silentNum.enQueue(0);
				niceNum.enQueue(0);
				selfishNum.enQueue(0);
				fortunateNum.enQueue(0);
				unfortunateNum.enQueue(0);
			}
		}
		//この時は非常に敏感
		if(selfishNum.sum() + unfortunateNum.sum() + silentNum.sum() == 0)
			behaviorSence = SENCE_MAX;
		//1より大きければ敏感，1より小さければ鈍感
		else
			behaviorSence = (fortunateNum.sum() + niceNum.sum() + concessionNum.sum()) / (selfishNum.sum() + unfortunateNum.sum() + silentNum.sum());
		ServletUtils.log("silent: " + silentNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("niceNum: " + niceNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("fortunate: " + fortunateNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("selfish: " + selfishNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("concession: " + concessionNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("unfortunate: " + unfortunateNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior sence: " + behaviorSence, ServletUtils.DebugLevels.DEBUG);
	}

	//肯定的なメッセージの送信回数
	public void addPosMessageNum(){ this.posMessageNum.enQueue(1); }

	//否定的なメッセージの送信回数
	public void addNegMessageNum(){ this.negMessageNum.enQueue(1); }

	//最後の行動タイミングを設定
	public void setPreBehaviorTiminig(int timing){ preBehaviorTiminig = timing; }

	//offerと行動の割合を計算
	public void calcWeight(){
		//offerRatio = (1.0 * previousOffers.size()) / (1.0 * previousOffers.size() + behaviorTimings.size());
		//behaviorRatio = (double)behaviorTimings.size() / (1.0 * previousOffers.size() + behaviorTimings.size());
		offerRatio = (double)previousOffers.queueSize() / behaviorTimings.queueSize();
		behaviorRatio = (double)(behaviorTimings.queueSize() - previousOffers.queueSize()) / behaviorTimings.queueSize();
	}
/*
	//譲歩関数におけるアルファの値を計算
	public void calcAlpha(Offer o, int timing){
		//this.mean = calcMean(previousOffers);
		//this.variance = calcVariance(previousOffers);
		calcEmotionRatio();
		calcFrequency();
		//calcChoiceVariance();
		calcBehaviorSence();
		calcWeight();
		//dummyOffers.clear();
		//dummyOffers = makeChoiceMaxOffers();
		double alpha = 0.0;
		double variance = pow(this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) - this.mean, 2.0);
		double frequency = behaviorTimings.size() >= 1 ? timing - behaviorTimings.get(behaviorTimings.size() - 1) : timing;
		double offerDiff = 0.0;


		if(previousOffers.size() >= 2) {
			for (int i = 1; i < game.getNumIssues(); i++) {
				offerDiff += abs(o.getItem(i)[0] - o.getItem(i - 1)[0]);
				offerDiff += abs(o.getItem(i)[1] - o.getItem(i - 1)[1]);
				offerDiff += abs(o.getItem(i)[2] - o.getItem(i - 1)[2]);
			}
		}
		//Neuroticism
		if(this.emotion > 0.0){
			alpha += 0.1;
		}
		else if(this.emotion < 0.0){
			alpha -= 0.1;
		}
		//Extraversion
		if(frequency < this.behaviorFrequency){
			alpha += 0.1;
		}
		else if(frequency > this.behaviorFrequency){
			alpha -= 0.1;
		}
		//Openness
		if(offerDiff > this.offerDiff){
			alpha += 0.1;
		}
		else if(offerDiff < this.offerDiff){
			alpha -= 0.1;
		}
		//Conscientiousness
		if(variance > this.variance){
			alpha += 0.1;
		}
		else if(variance < this.variance){
			alpha -= 0.1;
		}
		//Agreeableness
		if(this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) < this.mean){
			alpha += 0.1;
		}
		else if(this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) > this.mean){
			alpha -= 0.1;
		}
		this.alpha = max(min(1.0, this.alpha + max(alpha, 0.0) / 2.5), 0.01);

		ServletUtils.log("mean Value: " + this.mean, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("variance Value: " + this.variance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("frequency Value: " + this.behaviorFrequency, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("frequency Value: " + frequency, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("emotion Value: " + this.emotion, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("offer Diff Value: " + this.offerDiff, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("Alpha Value: " + this.alpha, ServletUtils.DebugLevels.DEBUG);
	}
*/

	public void addPreviousOffer(){
		this.previousOffers.enQueue(this.previous);
		addDummyAgentOffer(this.previous);
		addDummyPlayerOffer(this.previous);
	}

	public void addBehaviorTiming(int frequency){ this.behaviorTimings.enQueue(frequency); }

	public void addExpression(String emotion) {
		if (emotion.equals("sad") || emotion.equals("angry")) {
			negEmotionNum.enQueue(1);
		} else if (emotion.equals("happy") || emotion.equals("surprised")){
			posEmotionNum.enQueue(1);
		}
	}

	public void addRound(){ this.t += 1; }

	public void addFastResponseNum(){
		if(behaviorTimings.getLastElement() - preBehaviorTiminig <= TIME_INTERVAL){
			fastResponseNum.enQueue(1);
		}
		else{
			fastResponseNum.enQueue(0);
		}
	}

	public void addThreatNum(){ this.threatNum.enQueue(1);}

	@Override
	protected void setUtils(MentalistAgentUtilsExtension utils)
	{
		this.utils = utils;

		this.game = this.utils.getSpec();
		allocated = new Offer(game.getNumIssues());
		for(int i = 0; i < game.getNumIssues(); i++)
		{
			int[] init = {0, game.getIssueQuants()[i], 0};
			allocated.setItem(i, init);
		}
		concession = new Offer(game.getNumIssues());
		for(int i = 0; i < game.getNumIssues(); i++)
		{
			int[] init = {game.getIssueQuants()[i], 0, 0};
			concession.setItem(i, init);
		}
		this.previous = this.allocated;
		this.previousOffers = new QueueList<Offer>(OFF_SIZE);
		//this.previousOffersVariance = new ArrayList<>();
		//this.dummyOffers = new ArrayList<Offer>();
		this.dummyPlayerOffers = new QueueList<Offer>(OFF_SIZE);
		this.dummyAgentOffers = new QueueList<Offer>(OFF_SIZE);
		this.previousOffers.enQueue(allocated);
		this.dummyPlayerOffers.enQueue(allocated);
		this.dummyAgentOffers.enQueue(allocated);
		//target function関連
		this.gamma_min = 0.3;
		this.gamma_max = 1.0;
		this.n = max(1, (int)(game.getTotalTime() / 30));
		this.t = 1;
		this.alpha = 0.01;
		//TKI関連
		this.assertiveness = new ArrayList<Double>();
		this.cooperativeness = new ArrayList<Double>();
		//big5関連
		this.mean = 0.0;
		this.variance = 0.0;
		this.behaviorFrequency = 0.0;
		this.preBehaviorTiminig = 0;
		this.threatNum = new QueueList<Integer>(BEH_SIZE);
		this.fastResponseNum = new QueueList<Integer>(BEH_SIZE);
		this.preferenceAskNum = new QueueList<Integer>(BEH_SIZE);
		this.preferenceExpressionNum = new QueueList<Integer>(BEH_SIZE);
		this.lieNum = new QueueList<Integer>(BEH_SIZE);
		this.posMessageNum = new QueueList<Integer>(BEH_SIZE);
		this.negMessageNum = new QueueList<Integer>(BEH_SIZE);
		this.posEmotionNum = new QueueList<Integer>(BEH_SIZE);
		this.negEmotionNum = new QueueList<Integer>(BEH_SIZE);
		this.behaviorTimings = new QueueList<Integer>(BEH_SIZE + OFF_SIZE);
		this.neuroticism = new ArrayList<Double>();
		this.extraversion = new ArrayList<Double>();
		this.openness = new ArrayList<Double>();
		this.conscientiousness = new ArrayList<Double>();
		this.agreeableness = new ArrayList<Double>();
		//behavior sence関連
		this.silentNum = new QueueList<Integer>(OFF_SIZE);
		this.niceNum = new QueueList<Integer>(OFF_SIZE);
		this.fortunateNum = new QueueList<Integer>(OFF_SIZE);
		this.selfishNum = new QueueList<Integer>(OFF_SIZE);
		this.concessionNum = new QueueList<Integer>(OFF_SIZE);
		this.unfortunateNum = new QueueList<Integer>(OFF_SIZE);
		this.behaviorSence = 0.0;
		//その他
		this.offerRatio = 0.0;
		this.behaviorRatio = 0.0;
	}

	@Override
	protected void updateAllocated (Offer update)
	{
		allocated = update;
	}

	@Override
	protected void updateAdverseEvents (int change)
	{
		adverseEvents = Math.max(0, adverseEvents + change);
	}


	@Override
	protected Offer getAllocated ()
	{
		return allocated;
	}

	@Override
	protected Offer getConceded ()
	{
		return concession;
	}
/*
	@Override
	protected Offer getFinalOffer(History history)
	{
		this.t = this.n;
		this.alpha = 1.0;
		for(int i = 0; i < game.getNumIssues(); i++)
		{
			int[] init = {game.getIssueQuants()[i], 0, 0};
			previous.setItem(i, init);
		}
		for(int i = 0; i < game.getNumIssues(); i++)
		{
			int[] init = {0, game.getIssueQuants()[i], 0};
			allocated.setItem(i, init);
		}
		updateConcession();
		Offer propose = new Offer(game.getNumIssues());
		for(int issue = 0; issue < game.getNumIssues(); issue++)
			propose.setItem(issue,  concession.getItem(issue));

		int[] free = new int[game.getNumIssues()];
		int totalFree = 0;
		for(int issue = 0; issue < game.getNumIssues(); issue++)
		{
			free[issue] = concession.getItem(issue)[1];
			totalFree += concession.getItem(issue)[1];
		}

		if(totalFree > 0)  //concede free items
		{
			for (int issue = 0; issue < game.getNumIssues(); issue++)
			{
				if (utils.myRow == 0)
				{
					propose.setItem(issue, new int[] {concession.getItem(issue)[0], 0, concession.getItem(issue)[2] + free[issue]});
				}
				else if (utils.myRow == 2)
				{
					propose.setItem(issue, new int[] {concession.getItem(issue)[0] + free[issue], 0, concession.getItem(issue)[2]});
				}
			}
		}
		return propose;
	}

	@Override
	public Offer getNextOffer(History history)
	{
		ServletUtils.log("t: " + t, ServletUtils.DebugLevels.DEBUG);

		updateConcession();
		addRound();
		//start from where we currently have accepted
		Offer propose = new Offer(game.getNumIssues());
		for(int issue = 0; issue < game.getNumIssues(); issue++)
			propose.setItem(issue, allocated.getItem(issue));


		// Assign ordering to the player based on perceived preferences. Ideally, they would be opposite the agent's (integrative)
		ArrayList<Integer> playerPref = utils.getMinimaxOrdering();
		ArrayList<Integer> vhPref = utils.getMyOrdering();

		// Array representing the middle of the board (undecided items)
		int[] free = new int[game.getNumIssues()];

		for(int issue = 0; issue < game.getNumIssues(); issue++)
		{
			free[issue] = allocated.getItem(issue)[1];
		}

		int userFave = -1;
		int opponentFave = -1;

		// Find most valued issue for player and VH (of the issues that have undeclared items)
		int max = game.getNumIssues() + 1;
		for(int i  = 0; i < game.getNumIssues(); i++)
			if(free[i] > 0 && playerPref.get(i) < max)
			{
				userFave = i;
				max = playerPref.get(i);
			}
		max = game.getNumIssues() + 1;
		for(int i  = 0; i < game.getNumIssues(); i++)
			if(free[i] > 0 && vhPref.get(i) < max)
			{
				opponentFave = i;
				max = vhPref.get(i);
			}


		//is there ledger to work with?
		if(lb == MentalistRepeatedFavorBehavior.LedgerBehavior.NONE) //this agent doesn't care
		{
			//nothing
		}
		else if (utils.getVerbalLedger() < 0) //we have favors to cash!
		{
			//we will naively cash them immediately regardless of game importance
			//take entire category
			utils.modifyOfferLedger(-1);
			propose.setItem(opponentFave, new int[] {allocated.getItem(opponentFave)[0] + free[opponentFave], 0, allocated.getItem(opponentFave)[2]});
			return propose;
		}
		else if (utils.getVerbalLedger() > 0) //we have favors to return!
		{
			if (lb == MentalistRepeatedFavorBehavior.LedgerBehavior.BETRAYING)//this agent doesn't care
			{
				//nothing, so continue
			}
			else if(lb == MentalistRepeatedFavorBehavior.LedgerBehavior.FAIR)//this agent returns an entire column!
			{
				//return entire category
				utils.modifyOfferLedger(1);
				propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0], 0, allocated.getItem(userFave)[2] + free[userFave]});
				return propose;
			}
			else //if (lb == LedgerBehavior.LIMITED)//this agent returns a single item.  woo hoo
			{
				//return single item
				utils.modifyOfferLedger(1);
				propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
				return propose;
			}
		}
		else //we have nothing special
		{
			//nothing, so continue
		}



		if (userFave == -1 && opponentFave == -1) // We already have a full offer (no undecided items), try something different
		{
			//just repeat and keep allocated
		}
		else if(userFave == opponentFave)// Both agent and player want the same issue most
		{
			if(free[userFave] >= 2) // If there are more than two of that issue, propose an offer where the VH and player each get one more of that issue
				propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0] + 1, free[userFave] - 2, allocated.getItem(userFave)[2] + 1});
			else // Otherwise just give the one item left to us, the agent
			{
				if (utils.adversaryRow == 0) {
					propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
				} else if (utils.adversaryRow == 2) {
					propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0] + 1, free[userFave] - 1, allocated.getItem(userFave)[2]});
				}
			}
		}
		else // If the agent and player have different top picks
		{
			// Give both the VH and the player one more of the item they want most
			propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
			propose.setItem(opponentFave, new int[] {allocated.getItem(opponentFave)[0] + 1, free[opponentFave] - 1, allocated.getItem(opponentFave)[2]});
		}

		return propose;
	}
*/

	@Override
	protected Offer getFinalOffer(History history)
	{
		Offer propose = new Offer(game.getNumIssues());
		int totalFree = 0;
		do
		{
			totalFree = 0;
			for(int issue = 0; issue < game.getNumIssues(); issue++)
			{
				totalFree += allocated.getItem(issue)[1]; // adds up middle row of board, calculate unclaimed items
			}
			propose = getNextOffer(history);
			updateAllocated(propose);
		} while(totalFree > 0); // Continue calling getNextOffer while there are still items left unclaimed
		return propose;
	}

	@Override
	public Offer getNextOffer(History history)
	{

		//start from where we currently have accepted
		Offer propose = new Offer(game.getNumIssues());
		for(int issue = 0; issue < game.getNumIssues(); issue++)
			propose.setItem(issue, allocated.getItem(issue));


		// Assign ordering to the player based on perceived preferences. Ideally, they would be opposite the agent's (integrative)
		ArrayList<Integer> playerPref = utils.getMinimaxOrdering();
		ArrayList<Integer> vhPref = utils.getMyOrdering();

		// Array representing the middle of the board (undecided items)
		int[] free = new int[game.getNumIssues()];

		for(int issue = 0; issue < game.getNumIssues(); issue++)
		{
			free[issue] = allocated.getItem(issue)[1];
		}

		int userFave = -1;
		int opponentFave = -1;

		// Find most valued issue for player and VH (of the issues that have undeclared items)
		int max = game.getNumIssues() + 1;
		for(int i  = 0; i < game.getNumIssues(); i++)
			if(free[i] > 0 && playerPref.get(i) < max)
			{
				userFave = i;
				max = playerPref.get(i);
			}
		max = game.getNumIssues() + 1;
		for(int i  = 0; i < game.getNumIssues(); i++)
			if(free[i] > 0 && vhPref.get(i) < max)
			{
				opponentFave = i;
				max = vhPref.get(i);
			}


		//is there ledger to work with?
		if(lb == LedgerBehavior.NONE) //this agent doesn't care
		{
			//nothing
		}
		else if (utils.getVerbalLedger() < 0) //we have favors to cash!
		{
			//we will naively cash them immediately regardless of game importance
			//take entire category
			utils.modifyOfferLedger(-1);
			propose.setItem(opponentFave, new int[] {allocated.getItem(opponentFave)[0] + free[opponentFave], 0, allocated.getItem(opponentFave)[2]});
			return propose;
		}
		else if (utils.getVerbalLedger() > 0) //we have favors to return!
		{
			if (lb == LedgerBehavior.BETRAYING)//this agent doesn't care
			{
				//nothing, so continue
			}
			else if(lb == LedgerBehavior.FAIR)//this agent returns an entire column!
			{
				//return entire category
				utils.modifyOfferLedger(1);
				propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0], 0, allocated.getItem(userFave)[2] + free[userFave]});
				return propose;
			}
			else //if (lb == LedgerBehavior.LIMITED)//this agent returns a single item.  woo hoo
			{
				//return single item
				utils.modifyOfferLedger(1);
				propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
				return propose;
			}
		}
		else //we have nothing special
		{
			//nothing, so continue
		}



		if (userFave == -1 && opponentFave == -1) // We already have a full offer (no undecided items), try something different
		{
			//just repeat and keep allocated
		}
		else if(userFave == opponentFave)// Both agent and player want the same issue most
		{
			if(free[userFave] >= 2) // If there are more than two of that issue, propose an offer where the VH and player each get one more of that issue
				propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0] + 1, free[userFave] - 2, allocated.getItem(userFave)[2] + 1});
			else // Otherwise just give the one item left to us, the agent
			{
				if (utils.adversaryRow == 0) {
					propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
				} else if (utils.adversaryRow == 2) {
					propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0] + 1, free[userFave] - 1, allocated.getItem(userFave)[2]});
				}
			}
		}
		else // If the agent and player have different top picks
		{
			// Give both the VH and the player one more of the item they want most
			propose.setItem(userFave, new int[] {allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
			propose.setItem(opponentFave, new int[] {allocated.getItem(opponentFave)[0] + 1, free[opponentFave] - 1, allocated.getItem(opponentFave)[2]});
		}

		return propose;
	}

	@Override
	protected Offer getTimingOffer(History history) {
		return null;
	}

	@Override
	protected Offer getAcceptOfferFollowup(History history) {
		return null;
	}

	@Override
	protected Offer getFirstOffer(History history) {
		return null;
	}

	@Override
	protected int getAcceptMargin() {
		return Math.max(0, Math.min(game.getNumIssues(), adverseEvents));//basic decaying will, starts with fair
	}

	@Override
	protected Offer getRejectOfferFollowup(History history) {
		return null;
	}

	public String getCooperativeness(){ return String.join(":", cooperativeness.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getAssertiveness(){ return String.join(":", assertiveness.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getNeuroticism(){ return String.join(":", neuroticism.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getExtraversion(){ return String.join(":", extraversion.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getOpenness(){ return String.join(":", openness.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getAgreeableness() { return String.join(":", agreeableness.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getConscientiousness() { return String.join(":", conscientiousness.toString().replaceAll("[\\[\\] ]", "").split(",")); }
}
