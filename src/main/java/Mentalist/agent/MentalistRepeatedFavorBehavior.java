package Mentalist.agent;

import Mentalist.utils.*;

import javax.servlet.Servlet;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
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
	private QueueList<Offer> dummyUndefinedOffers;
	private QueueList<Integer> dummyEmotions;
	private LedgerBehavior lb = LedgerBehavior.NONE;
	private int adverseEvents = 0;
	//target function関連
	private double gamma_min;
	private double gamma_max;
	private int n;
	private int t;
	private double utility_bias;
	private double utility_weight;
	private double alpha;
	//TKI関連
	private ArrayList<Double> assertiveness;
	private ArrayList<Double> cooperativeness;
	//big5関連
	private double behaviorFrequency;
	private QueueList<Integer> preferenceAskNum;
	private QueueList<Integer> preferenceExpressionNum;
	private QueueList<Double> posEmotionNum;
	private QueueList<Double> negEmotionNum;
	private QueueList<Integer> posMessageNum;
	private QueueList<Integer> negMessageNum;
	private QueueList<Integer> emotionQueue;
	private QueueList<Integer> acceptNum;
	private int preEmotion;
	private int lieNum;
	private int threatNum;
	private int prefRequestNum;
	private int favorReturnNum;
	private int fastBehaviorNum;
	private int timingThreshold;
	private int batnaAskNum;
	private int batnaExpressionNum;
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
	private boolean emoFlag = false;
	private boolean messageFlag = false;
	private final double MAX = 1.0;
	private final double MIN = -1.0;
	private final double SENCE_MAX = 2.0;
	private final double MES_MAX = 5.0;
	private final double EMO_MAX = 3.0;
	private final double SPECIAL_MES_MAX = 1.0;
	private final double ACCEPT_MAX = 3.0;
	private final double FAST_BEH_MAX = 5.0;
	private final int BEH_SIZE = 10;
	private final int OFF_SIZE = 5;
	private final double PRE_WEIGHT = 0.3;
	private final double UTILITY_MIN_WEIGHT = 0.3;
	private final double UTILITY_MAX_WEIGHT = 0.9;
	private final double MAX_WEIGHT = 0.8;
	private final double MIN_WEIGHT = 0.2;
	private final double RATE_MAX = 0.5;
	private final double ALPHA_MIN = 0.1;
	private final double ALPHA_MAX= 0.9;
	private final int N_MIN = 10;
	private final int N_MAX = 20;
	private final double GAMMA_MIN = 0.2;
	private final double GAMMA_MAX = 0.4;
	private final double PRE_UTILITY_WEIGHT_MIN = 0.8;
	private final double PRE_UTILITY_WEIGHT_MAX = 1.2;
	private final double UTILITY_BIAS_MIN = -0.1;
	private final double UTILITY_BIAS_MAX = 0.1;
	private final int THRESHOLD_MIN = 3;
	private final int THRESHOLD_MAX = 9;

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
		Offer maxOffer = makeMaxAgentDummyOffer(previous);
		return (gamma_min + (gamma_max - gamma_min) * max(0.0 ,(1 - max(0.0, this.utility_bias + this.utility_weight * ((double)this.utils.myActualOfferValue(previous) / this.utils.myActualOfferValue(maxOffer))) * pow((double)t / n,(1 / alpha))))) * this.utils.myActualOfferValue(maxOffer);
	}

	//正規化
	public double normarize(double data, double data_max, double data_min){
		return (data_max != data_min) ? (data - data_min) / (data_max - data_min) * (MAX - MIN) + MIN : 0.0;
	}

	//正規化
	public double normarize(double data, double data_max, double data_min, double MAX, double MIN){
		return (data_max != data_min) ? (data - data_min) / (data_max - data_min) * (MAX - MIN) + MIN : 0.0;
	}

	//質問回数の計算
	public double maxAskNum(){
		return game.getNumIssues() % 2 == 0 ? game.getNumIssues() * (game.getNumIssues() / 2 - 0.5) : Math.floor(game.getNumIssues() / 2) * game.getNumIssues();
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
		addRound();
		calcWeight();
		adjustSize();

		calcCooperativeness();
		calcAssertiveness();

		calcAgreeableness();
		calcNeuroticism();
		calcExtraversion();
		calcOpenness();
		calcConscientiousness();

		setConcessionParameter();


		ServletUtils.log("behavior weight: " + behaviorRatio, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("offer weight: " + offerRatio, ServletUtils.DebugLevels.DEBUG);

		/*
		ServletUtils.log("behavior timing: " + behaviorTimings, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior freq: " + behaviorFrequency, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pos num: " + posMessageNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neg num: " + negMessageNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pos mes point: " + normarize(posMessageNum.sum(), max(MES_MAX, posMessageNum.sum()),-MES_MAX), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neg mes point: " + normarize(negMessageNum.sum(), max(MES_MAX, negMessageNum.sum()),-MES_MAX), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pos emo point: " + normarize(posEmotionNum.sum(), max(EMO_MAX, posEmotionNum.sum()),-EMO_MAX), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neg emo point: " + normarize(negEmotionNum.sum(), max(EMO_MAX, negEmotionNum.sum()),-EMO_MAX), ServletUtils.DebugLevels.DEBUG);
		*/

		ServletUtils.log("TKI-coop: " + cooperativeness.get(cooperativeness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("TKI-asse: " + assertiveness.get(assertiveness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Neu: " + neuroticism.get(neuroticism.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Ext: " + extraversion.get(extraversion.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Opn: " + openness.get(openness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Agr: " + agreeableness.get(agreeableness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Con: " + conscientiousness.get(conscientiousness.size() - 1), ServletUtils.DebugLevels.DEBUG);

	}

	public void adjustSize(){
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1) != preferenceAskNum.getFullSize()){
			for(int i = preferenceAskNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1; i++){
				preferenceAskNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1) != preferenceExpressionNum.getFullSize()){
			for(int i = preferenceExpressionNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1; i++){
				preferenceExpressionNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1) != posEmotionNum.getFullSize()){
			for(int i = posEmotionNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1; i++){
				posEmotionNum.enQueue(0.0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1) != negEmotionNum.getFullSize()){
			for(int i = negEmotionNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1; i++){
				negEmotionNum.enQueue(0.0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1) != posMessageNum.getFullSize()){
			for(int i = posMessageNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1; i++){
				posMessageNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1) != negMessageNum.getFullSize()){
			for(int i = negMessageNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1; i++){
				negMessageNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1) != acceptNum.getFullSize()){
			for(int i = acceptNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1; i++){
				acceptNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1) != emotionQueue.getFullSize()){
			for(int i = emotionQueue.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize() + 1; i++){
				emotionQueue.enQueue(0);
				dummyEmotions.enQueue(0);
			}
		}
	}

	public Offer makeMaxAgentDummyOffer(Offer o){
		Offer p = new Offer(game.getNumIssues());
		//未定義のアイテムをissueごとに把握
		int[] free = new int[game.getNumIssues()];
		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = o.getItem(issue)[1];
		}
		//エージェントに全振り
		for (int i = 0; i < game.getNumIssues(); i++) {
			int[] init = {game.getIssueQuants()[i] - free[i], free[i], 0};
			p.setItem(i, init);
		}
		return p;
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
		if(dummyPlayerOffers.getFullSize() % 3 == 1){
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
		if(dummyAgentOffers.getFullSize() % 3 == 1){
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

	public void addDummyUndefinedOffer(){
		Offer p = new Offer(game.getNumIssues());
		for(int issue = 0; issue < game.getNumIssues(); issue++){
			int[] init = {dummyUndefinedOffers.getFullSize() % 2 == 0 ? 0 : game.getIssueQuants()[issue], dummyUndefinedOffers.getFullSize() % 2 == 0 ? game.getIssueQuants()[issue] : 0, 0};
			p.setItem(issue, init);
		}
		dummyUndefinedOffers.enQueue(p);
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

	/**** TKI協調性 ****/
	public void calcCooperativeness(){
		double mean = calcMean(previousOffers.getExceptZeroPreQueue()) * PRE_WEIGHT + calcMean(previousOffers.getExceptZeroQueue());
		double dummyMean = calcMean(dummyPlayerOffers.getExceptZeroPreQueue()) * PRE_WEIGHT + calcMean(dummyPlayerOffers.getExceptZeroQueue());
		double meanPoint = -normarize(mean, max(UTILITY_MAX_WEIGHT * dummyMean, mean), min(UTILITY_MIN_WEIGHT * dummyMean, mean));


		ServletUtils.log("mean: " + mean, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy mean: " + dummyMean, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("cooperative: " + meanPoint, ServletUtils.DebugLevels.DEBUG);


		this.cooperativeness.add(meanPoint);
	}

	/**** TKI積極性 ****/
	public void calcAssertiveness(){
		double variance = calcVariance(previousOffers.getPreQueue()) * PRE_WEIGHT + calcVariance(previousOffers.getQueue());
		double dummyVariance = calcVariance(dummyPlayerOffers.getPreQueue()) * PRE_WEIGHT + calcVariance(dummyPlayerOffers.getQueue());
		double variancePoint = -normarize(variance, max(UTILITY_MAX_WEIGHT * dummyVariance, variance), min(UTILITY_MIN_WEIGHT * dummyVariance, variance));

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("variance: " + variance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy variance: " + dummyVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("assertive: " + variancePoint, ServletUtils.DebugLevels.DEBUG);


		this.assertiveness.add(variancePoint);
	}

	/**** ビッグファイブ神経症傾向 ****/
	public void calcNeuroticism(){
		double niceConcession = niceNum.sum() / (previousOffers.getFullSize() - 1) + concessionNum.sum() / (previousOffers.getFullSize() - 1);
		double neuroOfferPoint = normarize(niceConcession, max(RATE_MAX, niceConcession), 0.0);
		double negEmotionPoint = emoFlag ? normarize(negEmotionNum.sum(), max(EMO_MAX, negEmotionNum.sum()), 0.0) : 0.0;
		double negMessagePoint = messageFlag ? normarize(negMessageNum.sum(), max(MES_MAX, negMessageNum.sum()), 0.0) : 0.0;
		double emotionVariance = calcEmotionVariance(emotionQueue.getPreQueue()) * PRE_WEIGHT + calcEmotionVariance(emotionQueue.getQueue());
		double dummyEmotionVariance = calcEmotionVariance(dummyEmotions.getPreQueue()) * PRE_WEIGHT + calcEmotionVariance(dummyEmotions.getQueue());
		double emotionRatio = emoFlag ? normarize(emotionVariance, max(MAX_WEIGHT * dummyEmotionVariance, emotionVariance), min(MIN_WEIGHT * dummyEmotionVariance, emotionVariance)) : 0.0;
		double neuroBehaviorPoint = negEmotionPoint + negMessagePoint + emotionRatio;

		if(abs(neuroBehaviorPoint) > 1.0){
			neuroBehaviorPoint = signum(neuroBehaviorPoint);
		}

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("nice + concession: " + niceNum.sum() + concessionNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("nice + concession point: " + neuroOfferPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("negEmo: " + negEmotionNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("negEmo point: " + negEmotionPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("negMes: " + negMessageNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("negMes point: " + negMessagePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("emo var: " + emotionVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("emo var point: " + emotionRatio, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neuroticism: " + neuroOfferPoint * offerRatio + (negEmotionPoint + negMessagePoint) / 2 * behaviorRatio, ServletUtils.DebugLevels.DEBUG);


		this.neuroticism.add(neuroOfferPoint * offerRatio + neuroBehaviorPoint * behaviorRatio);
	}

	/**** ビッグファイブ外向性 ****/
	public void calcExtraversion(){
		calcFrequency();
		double selfishFortunate = selfishNum.sum() / (previousOffers.getFullSize() - 1) + fortunateNum.sum() / (previousOffers.getFullSize() - 1);
		double extraOfferPoint = normarize(selfishFortunate, max(RATE_MAX, selfishFortunate), 0.0);
		double threatPoint = normarize(threatNum, max(SPECIAL_MES_MAX, threatNum), -SPECIAL_MES_MAX);
		double behaviorFrequencyPoint = -normarize(behaviorFrequency, max(behaviorFrequency, 45.0),  min(behaviorFrequency, 15.0));

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("selfish + fortunate: " + selfishNum.sum() + fortunateNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("selfish + fortunate point: " + extraOfferPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("threat: " + threatNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("threat point: " + threatPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior: " + behaviorFrequency, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior point: " + behaviorFrequencyPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("extraversion: " + min(1.0, extraOfferPoint * offerRatio + behaviorFrequencyPoint * behaviorRatio + threatPoint), ServletUtils.DebugLevels.DEBUG);


		this.extraversion.add(min(1.0, extraOfferPoint * offerRatio + behaviorFrequencyPoint * behaviorRatio + threatPoint));
	}

	/**** ビッグファイブ経験への開放性 ****/
	public void calcOpenness(){
		double choicePlayerVariance = calcChoiceVariance(previousOffers.getPreQueue(), 2) * PRE_WEIGHT + calcChoiceVariance(previousOffers.getQueue(), 2);
		double choiceOpponentVariance = calcChoiceVariance(previousOffers.getPreQueue(), 0) * PRE_WEIGHT + calcChoiceVariance(previousOffers.getQueue(), 0);
		double choiceDummyPlayerVariance = calcChoiceVariance(dummyPlayerOffers.getPreQueue(), 2) * PRE_WEIGHT + calcChoiceVariance(dummyPlayerOffers.getQueue(), 2);
		double choiceDummyOpponentVariance = calcChoiceVariance(dummyAgentOffers.getPreQueue(), 0) * PRE_WEIGHT + calcChoiceVariance(dummyAgentOffers.getQueue(), 0);
		double choiceVariancePoint = max(normarize(choicePlayerVariance, max(UTILITY_MAX_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance), min(UTILITY_MIN_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance)), normarize(choiceOpponentVariance, max(UTILITY_MAX_WEIGHT * choiceDummyOpponentVariance, choiceOpponentVariance), min(UTILITY_MIN_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance)));

		double askPoint = normarize(preferenceAskNum.sum(), max(maxAskNum(), preferenceAskNum.sum()), 0.0);
		double requestPoint = normarize(prefRequestNum, max(SPECIAL_MES_MAX, prefRequestNum), -SPECIAL_MES_MAX);
		double batnaAskPoint = normarize(batnaAskNum, max(SPECIAL_MES_MAX * 2, batnaAskNum), -SPECIAL_MES_MAX * 2);
		double openBehaviorPoint = askPoint + requestPoint + batnaAskPoint;
		if(abs(openBehaviorPoint) > 1.0){
			openBehaviorPoint = signum(openBehaviorPoint);
		}

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("player choice: " + choicePlayerVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("opponent choice: " + choiceOpponentVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy player choice: " + choiceDummyPlayerVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy opponent choice: " + choiceDummyOpponentVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("choice point: " + choiceVariancePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("ask: " + preferenceAskNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("ask point: " + askPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("batna ask: " + batnaAskNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("batna ask point: " + batnaAskPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pref req: " + prefRequestNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pref req point: " + requestPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("openness: " + choiceVariancePoint * offerRatio + openBehaviorPoint * behaviorRatio, ServletUtils.DebugLevels.DEBUG);


		this.openness.add(choiceVariancePoint * offerRatio + openBehaviorPoint * behaviorRatio);
	}

	/**** ビッグファイブ誠実性 ****/
	public void calcConscientiousness(){
		double variance = calcVariance(previousOffers.getPreQueue()) * PRE_WEIGHT + calcVariance(previousOffers.getQueue());
		double dummyVariance = calcVariance(dummyPlayerOffers.getPreQueue()) * PRE_WEIGHT + calcVariance(dummyPlayerOffers.getQueue());
		double variancePoint = -normarize(variance, max(UTILITY_MAX_WEIGHT * dummyVariance, variance), min(UTILITY_MIN_WEIGHT * dummyVariance, variance));
		double undefined = calcUndefinedRate(previousOffers.getPreQueue()) * PRE_WEIGHT + calcUndefinedRate(previousOffers.getQueue());
		double undefinedRatePoint = -normarize(undefined, max(MAX_WEIGHT * calcMaxUndefinedNum(), undefined), min(0.0, undefined));
		Offer o = previousOffers.getAllArray().get(1);
		double fastUndefinedPoint = -normarize(calcUndefinedNum(o), max(calcUndefinedNum(o), calcMaxUndefinedNum()), 0.0);
		double conscientOfferPoint = variancePoint + undefinedRatePoint + fastUndefinedPoint;
		if(abs(conscientOfferPoint) > 1.0){
			conscientOfferPoint = signum(conscientOfferPoint);
		}

		double liePoint = -normarize(lieNum, max(SPECIAL_MES_MAX, lieNum), -SPECIAL_MES_MAX);
		double fastBehaviorPoint = normarize(fastBehaviorNum, max(fastBehaviorNum, FAST_BEH_MAX), 0.0);
		double expressionPoint = normarize(preferenceExpressionNum.sum(), max(maxAskNum(), preferenceExpressionNum.sum()), 0.0);
		double batnaExpressionPoint = normarize(batnaExpressionNum, max(SPECIAL_MES_MAX, batnaExpressionNum), -SPECIAL_MES_MAX);
		double conscientBehaviorPoint = fastBehaviorPoint + expressionPoint + batnaExpressionPoint;
		if(abs(conscientBehaviorPoint) > 1.0){
			conscientBehaviorPoint = signum(conscientBehaviorPoint);
		}

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("variance: " + variance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy variance: " + dummyVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("variance point: " + variancePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("undefined rate: " + undefined, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("undefined rate point : " + undefinedRatePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("fast undefined: " + calcUndefinedNum(o), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("fast undefined point : " + fastUndefinedPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("fast beh: " + fastBehaviorNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("fast beh point : " + fastBehaviorPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("exp: " + preferenceExpressionNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("exp point : " + expressionPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("batna exp: " + batnaExpressionNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("batna exp point : " + batnaExpressionPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("lie: " + lieNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("lie point: " + liePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("conscientiousness: " + max(-1.0, conscientOfferPoint * offerRatio + conscientBehaviorPoint * behaviorRatio + liePoint), ServletUtils.DebugLevels.DEBUG);


		this.conscientiousness.add(max(-1.0, conscientOfferPoint * offerRatio + conscientBehaviorPoint * behaviorRatio + liePoint));
	}

	/**** ビッグファイブ協調性 ****/
	public void calcAgreeableness(){
		calcBehaviorSence();
		double behaviorSencePoint = normarize(this.behaviorSence, max(SENCE_MAX, behaviorSence), 0.0);
		double humanUtil = this.utils.adversaryValue(previousOffers.getAllArray().get(1), this.utils.getMinimaxOrdering());
		double agentUtil = this.utils.myActualOfferValue(previousOffers.getAllArray().get(1));
		double firstOfferRatio = normarize(agentUtil / (humanUtil + agentUtil), 0.75, 0.0);
		double agreeOfferPoint = behaviorSencePoint + firstOfferRatio;
		if(abs(agreeOfferPoint) > 1.0){
			agreeOfferPoint = signum(agreeOfferPoint);
		}
		double acceptPoint = normarize(acceptNum.sum(), max(ACCEPT_MAX, acceptNum.sum()), 0.0);
		double favorReturnPoint = normarize(favorReturnNum, max(SPECIAL_MES_MAX, favorReturnNum), -SPECIAL_MES_MAX);
		double posEmotionPoint = emoFlag ? normarize(posEmotionNum.sum(), max(EMO_MAX, posEmotionNum.sum()), 0.0) : 0.0;
		double posMessagePoint = messageFlag ? normarize(posMessageNum.sum(), max(MES_MAX, posMessageNum.sum()), 0.0) : 0.0;
		double agreeBehaviorPoint = acceptPoint + favorReturnPoint + posEmotionPoint + posMessagePoint;
		if(abs(agreeBehaviorPoint) > 0){
			agreeBehaviorPoint = signum(agreeBehaviorPoint);
		}

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior sence: " + behaviorSence, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior sence point: " + behaviorSencePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("H util: " + humanUtil, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("A util: " + agentUtil, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("firstoffer point: " + firstOfferRatio, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("accept: " + acceptNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("accept point: " + acceptPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("favor return: " + favorReturnNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("favor return point: " + favorReturnPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("posEmo sum: " + posEmotionNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("posEmo point: " + posEmotionPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("posMes sum: " + posMessageNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("posMes point: " + posMessagePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("agreeableness: " + agreeOfferPoint * offerRatio + agreeBehaviorPoint * behaviorRatio, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);


		this.agreeableness.add(agreeOfferPoint * offerRatio + agreeBehaviorPoint * behaviorRatio);
	}

	//感情の分散
	public double calcEmotionVariance(ArrayList<Integer> emotions){
		double mean = 0;
		double sum = 0;

		if(!emotions.isEmpty()) {
			for (int i : emotions) {
				mean += i;
			}
			mean /= emotions.size();

			for (int i : emotions) {
				sum += abs(i - mean);
			}
			sum /= sqrt(emotions.size());
		}

		return sum;
	}

	//メッセージの割合
	public double calcMessageRatio(){
		return negMessageNum.sum() / (negMessageNum.sum() + posMessageNum.sum());
	}

	//行動の頻度
	public void calcFrequency(){
		if(behaviorTimings.queueSize() >= 1) {
			ArrayList<Integer> queue = behaviorTimings.getQueue();
			double frequency = behaviorTimings.getPreQueue().size() >= 1 ? queue.get(0) - behaviorTimings.getPreQueue().get(behaviorTimings.getPreQueue().size() - 1) : queue.get(0);
			for (int i = 1; i < queue.size(); i++) {
				frequency += queue.get(i) - queue.get(i - 1);
			}
			this.behaviorFrequency = frequency / behaviorTimings.queueSize();
		}
		else
			this.behaviorFrequency = 0.0;
	}

	//未定義の数を計算
	public int calcUndefinedNum(Offer o){
		int num = 0;
		for(int i = 0; i < game.getNumIssues(); i++){
			num += o.getItem(i)[1];
		}

		return num;
	}

	//未定義の最大数を計算
	public int calcMaxUndefinedNum(){
		int num = 0;
		for(int i = 0; i < game.getNumIssues(); i++){
			num += game.getIssueQuants()[i];
		}

		return num;
	}

	//未定義の変動率を計算
	public double calcUndefinedRate(ArrayList<Offer> offers){
		double sum = 0.0;
		if(offers.size() >= 2) {
			for (int i = 1; i < offers.size(); i++) {
				for (int j = 0; j < game.getNumIssues(); j++) {
					sum += abs(offers.get(i).getItem(j)[1] - offers.get(i - 1).getItem(j)[1]);
				}
			}
			sum /= (offers.size() - 1);
		}

		return sum;
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
		if(selfishNum.sum() + unfortunateNum.sum() + silentNum.sum() == 0.0)
			behaviorSence = SENCE_MAX;
		//1より大きければ敏感，1より小さければ鈍感
		else
			behaviorSence = (fortunateNum.sum() + niceNum.sum() + concessionNum.sum()) / (selfishNum.sum() + unfortunateNum.sum() + silentNum.sum());

		/*
		ServletUtils.log("silent: " + silentNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("niceNum: " + niceNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("fortunate: " + fortunateNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("selfish: " + selfishNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("concession: " + concessionNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("unfortunate: " + unfortunateNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior sence: " + behaviorSence, ServletUtils.DebugLevels.DEBUG);
		*/
	}

	//offerと行動の割合を計算
	public void calcWeight(){
		offerRatio = (double)previousOffers.getFullSize() / (behaviorTimings.getFullSize() + 1);
		behaviorRatio = (double)(behaviorTimings.getFullSize() + 1 - previousOffers.getFullSize()) / (behaviorTimings.getFullSize() + 1);
	}

	public void addRound(){ this.t += 1; }

	public void setPrevious(Offer o){ this.previous = o; }

	public void addPreviousOffer(){
		if(previousOffers.queueSize() == 1){
			this.fastBehaviorNum = behaviorTimings.getFullSize();
		}
		this.previousOffers.enQueue(this.previous);
		addDummyAgentOffer(this.previous);
		addDummyPlayerOffer(this.previous);
		addDummyUndefinedOffer();
	}

	//脅しの回数
	public void addThreatNum(){ this.threatNum += 1; }

	//選好に関する質問の回数
	public void addPreferenceAskNum(){ this.preferenceAskNum.enQueue(1); }

	//選好を表出した回数
	public void addPreferenceExpressionNum(){ this.preferenceExpressionNum.enQueue(1);}

	//嘘をついた回数
	public void addLieNum(){ this.lieNum += 1; }

	//肯定的なメッセージの送信回数
	public void addPosMessageNum(){
		this.posMessageNum.enQueue(1);
		messageFlag = true;
	}

	//否定的なメッセージの送信回数
	public void addNegMessageNum(){
		this.negMessageNum.enQueue(1);
		messageFlag = true;
	}

	//感情の送信回数
	public void addExpression(String emotion) {
		if(preEmotion == 5){
			preEmotion = 1;
		}
		else{
			preEmotion = 5;
		}
		dummyEmotions.enQueue(preEmotion);

		switch(emotion){
			case "angry":
				negEmotionNum.enQueue(1.0);
				emotionQueue.enQueue(1);
				break;
			case "sad":
				negEmotionNum.enQueue(0.5);
				emotionQueue.enQueue(2);
				break;
			case "neutral":
				emotionQueue.enQueue(3);
				break;
			case "surprised":
				posEmotionNum.enQueue(0.5);
				emotionQueue.enQueue(4);
				break;
			case "happy":
				posEmotionNum.enQueue(1.0);
				emotionQueue.enQueue(5);
				break;

		}
		emoFlag = true;
	}

	//選好リクエスト
	public void addPrefRequest(){ this.prefRequestNum += 1; }

	//行動タイミング
	public void addBehaviorTiming(int frequency){ this.behaviorTimings.enQueue(frequency); }

	//acceptの回数
	public void addAcceptNum(){ this.acceptNum.enQueue(1); }

	//BATNA聞いた回数
	public void addBATNAAskNum(){ this.batnaAskNum += 1; }

	//BATNA教えた回数
	public void addBATNAExpressionNum(){ this.batnaExpressionNum += 1; }

	//FAVOR_RETURNの回数
	public void addFavorReturnNum(){ this.favorReturnNum += 1; }

	//TIMINGの頻度
	public int getTimingThreshold(){ return timingThreshold; }

	//譲歩関数のパラメータを特性によって変更
	public void setConcessionParameter(){
		//TKIについて
		Point2D.Double playerTKITrait = new Point2D.Double(cooperativeness.get(cooperativeness.size() - 1), assertiveness.get(assertiveness.size() - 1));
		double competingDistanceInverSe = 1 / playerTKITrait.distance(-1.0, 1.0);
		double avoidingDistanceInverse = 1 / playerTKITrait.distance(-1.0, -1.0);
		double collaboratingDistanceInverse = 1 / playerTKITrait.distance(1.0, 1.0);
		double accommodatingDistanceInverse = 1 / playerTKITrait.distance(1.0, -1.0);
		double nDistanceInverse = 1 / (competingDistanceInverSe + avoidingDistanceInverse);
		double alphaDistanceInverse = 1 / (collaboratingDistanceInverse + accommodatingDistanceInverse);

		int tkiN = (int)(N_MAX * competingDistanceInverSe / nDistanceInverse + N_MIN * avoidingDistanceInverse / nDistanceInverse);
		double tkiAlpha = ALPHA_MAX * accommodatingDistanceInverse / alphaDistanceInverse + ALPHA_MIN * collaboratingDistanceInverse / avoidingDistanceInverse;

		//big5について
		double neuroticismRate = normarize(neuroticism.get(neuroticism.size() - 1), 1.0, -1.0, 1.0, 0.0);
		double extraversionRate = normarize(extraversion.get(extraversion.size() - 1), 1.0, -1.0, 1.0, 0.0);
		double opennessRate = normarize(openness.get(openness.size() - 1), 1.0, -1.0, 1.0, 0.0);
		double conscientiousnessRate = normarize(conscientiousness.get(conscientiousness.size() - 1), 1.0, -1.0, 1.0, 0.0);
		double agreeablenessRate = normarize(agreeableness.get(agreeableness.size() - 1), 1.0, -1.0, 1.0, 0.0);

		this.utility_bias = UTILITY_BIAS_MAX * neuroticismRate + UTILITY_BIAS_MIN * (1 - neuroticismRate);
		int big5N = (int)(N_MAX * extraversionRate + N_MIN * (1 - extraversionRate));
		this.gamma_min = GAMMA_MAX * opennessRate + GAMMA_MIN * (1 - opennessRate);
		this.utility_weight = PRE_UTILITY_WEIGHT_MAX * conscientiousnessRate + PRE_UTILITY_WEIGHT_MIN * (1 - conscientiousnessRate);
		double big5Alpha = ALPHA_MAX * agreeablenessRate + ALPHA_MIN * (1 - agreeablenessRate);

		this.timingThreshold = (int)(THRESHOLD_MAX * extraversionRate + THRESHOLD_MIN * (1 - extraversionRate));

		this.n = (tkiN + big5N) / 2;
		this.alpha = (tkiAlpha + big5Alpha) / 2;

	}


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
		this.previousOffers = new QueueList<Offer>(OFF_SIZE, PRE_WEIGHT);
		this.dummyPlayerOffers = new QueueList<Offer>(OFF_SIZE, PRE_WEIGHT);
		this.dummyAgentOffers = new QueueList<Offer>(OFF_SIZE, PRE_WEIGHT);
		this.dummyUndefinedOffers = new QueueList<Offer>(OFF_SIZE, PRE_WEIGHT);
		this.dummyEmotions = new QueueList<Integer>(BEH_SIZE, PRE_WEIGHT);
		this.previousOffers.enQueue(allocated);
		this.dummyPlayerOffers.enQueue(allocated);
		this.dummyAgentOffers.enQueue(allocated);
		this.dummyUndefinedOffers.enQueue(allocated);
		//target function関連
		this.gamma_min = 0.3;
		this.gamma_max = 1.0;
		this.n = max(1, (int)(game.getTotalTime() / 30));
		this.t = 1;
		this.utility_bias = 0.0;
		this.utility_weight = 1.0;
		this.alpha = 0.01;
		//TKI関連
		this.assertiveness = new ArrayList<Double>();
		this.cooperativeness = new ArrayList<Double>();
		//big5関連
		this.behaviorFrequency = 0.0;
		this.preferenceAskNum = new QueueList<Integer>(BEH_SIZE, PRE_WEIGHT);
		this.preferenceExpressionNum = new QueueList<Integer>(BEH_SIZE, PRE_WEIGHT);
		this.preEmotion = 1;
		this.lieNum = 0;
		this.threatNum = 0;
		this.prefRequestNum = 0;
		this.favorReturnNum = 0;
		this.timingThreshold = 3;
		this.batnaAskNum = 0;
		this.batnaExpressionNum = 0;
		this.posMessageNum = new QueueList<Integer>(BEH_SIZE, PRE_WEIGHT);
		this.negMessageNum = new QueueList<Integer>(BEH_SIZE, PRE_WEIGHT);
		this.posEmotionNum = new QueueList<Double>(BEH_SIZE, PRE_WEIGHT);
		this.negEmotionNum = new QueueList<Double>(BEH_SIZE, PRE_WEIGHT);
		this.emotionQueue = new QueueList<Integer>(BEH_SIZE, PRE_WEIGHT);
		this.acceptNum = new QueueList<Integer>(BEH_SIZE, PRE_WEIGHT);
		this.behaviorTimings = new QueueList<Integer>(BEH_SIZE + OFF_SIZE, PRE_WEIGHT);
		this.neuroticism = new ArrayList<Double>();
		this.extraversion = new ArrayList<Double>();
		this.openness = new ArrayList<Double>();
		this.conscientiousness = new ArrayList<Double>();
		this.agreeableness = new ArrayList<Double>();
		//behavior sence関連
		this.silentNum = new QueueList<Integer>(OFF_SIZE, PRE_WEIGHT);
		this.niceNum = new QueueList<Integer>(OFF_SIZE, PRE_WEIGHT);
		this.fortunateNum = new QueueList<Integer>(OFF_SIZE, PRE_WEIGHT);
		this.selfishNum = new QueueList<Integer>(OFF_SIZE, PRE_WEIGHT);
		this.concessionNum = new QueueList<Integer>(OFF_SIZE, PRE_WEIGHT);
		this.unfortunateNum = new QueueList<Integer>(OFF_SIZE, PRE_WEIGHT);
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

	@Override
	protected Offer getFinalOffer(History history)
	{
		Offer propose = new Offer(game.getNumIssues());
		this.t = this.n;
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

		int count = 0;

		Offer preAllocated = allocated;

		do{
			int userFave = -1;
			int opponentFave = -1;

			// Find most valued issue for player and VH (of the issues that have undeclared items)
			int max = game.getNumIssues() + 1;
			for (int i = 0; i < game.getNumIssues(); i++)
				if (free[i] > 0 && playerPref.get(i) < max) {
					userFave = i;
					max = playerPref.get(i);
				}
			max = game.getNumIssues() + 1;
			for (int i = 0; i < game.getNumIssues(); i++)
				if (free[i] > 0 && vhPref.get(i) < max) {
					opponentFave = i;
					max = vhPref.get(i);
				}

			if(count == 0) {
				//is there ledger to work with?
				if (lb == LedgerBehavior.NONE) //this agent doesn't care
				{
					//nothing
				} else if (utils.getVerbalLedger() < 0) //we have favors to cash!
				{
					//we will naively cash them immediately regardless of game importance
					//take entire category
					utils.modifyOfferLedger(-1);
					propose.setItem(opponentFave, new int[]{allocated.getItem(opponentFave)[0] + free[opponentFave], 0, allocated.getItem(opponentFave)[2]});
					free[opponentFave] = 0;
					return propose;
				} else if (utils.getVerbalLedger() > 0) //we have favors to return!
				{
					if (lb == LedgerBehavior.BETRAYING)//this agent doesn't care
					{
						//nothing, so continue
					} else if (lb == LedgerBehavior.FAIR)//this agent returns an entire column!
					{
						//return entire category
						utils.modifyOfferLedger(1);
						propose.setItem(userFave, new int[]{allocated.getItem(userFave)[0], 0, allocated.getItem(userFave)[2] + free[userFave]});
						free[userFave] = 0;
						return propose;
					} else //if (lb == LedgerBehavior.LIMITED)//this agent returns a single item.  woo hoo
					{
						//return single item
						utils.modifyOfferLedger(1);
						propose.setItem(userFave, new int[]{allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
						free[userFave] -= 1;
						return propose;
					}
				} else //we have nothing special
				{
					//nothing, so continue
				}
			}

			if (userFave == -1 && opponentFave == -1) // We already have a full offer (no undecided items), try something different
			{
				//just repeat and keep allocated
			} else if (userFave == opponentFave)// Both agent and player want the same issue most
			{
				if (free[userFave] >= 2) { // If there are more than two of that issue, propose an offer where the VH and player each get one more of that issue
					if(count % 2 == 0) {
						propose.setItem(userFave, new int[]{allocated.getItem(userFave)[0] + 1, free[userFave] - 2, allocated.getItem(userFave)[2] + 1});
						free[userFave] -= 2;
					}
					else{
						propose.setItem(userFave, new int[]{allocated.getItem(userFave)[0] + 1, free[userFave] - 1, allocated.getItem(userFave)[2]});
						free[userFave] -= 1;
					}
				} else // Otherwise just give the one item left to us, the agent
				{
					if (utils.adversaryRow == 0) {
						propose.setItem(userFave, new int[]{allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
					} else if (utils.adversaryRow == 2) {
						propose.setItem(userFave, new int[]{allocated.getItem(userFave)[0] + 1, free[userFave] - 1, allocated.getItem(userFave)[2]});
					}
					free[userFave] -= 1;
				}
			} else // If the agent and player have different top picks
			{
				// Give both the VH and the player one more of the item they want most
				if(count % 2 == 0) {
					propose.setItem(userFave, new int[]{allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
					propose.setItem(opponentFave, new int[]{allocated.getItem(opponentFave)[0] + 1, free[opponentFave] - 1, allocated.getItem(opponentFave)[2]});
					free[userFave] -= 1;
					free[opponentFave] -= 1;
				}
				else{
					propose.setItem(opponentFave, new int[]{allocated.getItem(opponentFave)[0] + 1, free[opponentFave] - 1, allocated.getItem(opponentFave)[2]});
					free[opponentFave] -= 1;
				}
			}
			count++;
			allocated = propose;
		}while(calcUndefinedNum(propose) != 0 && getConcessionValue() > (double)this.utils.myActualOfferValue(propose));

		allocated = preAllocated;

		return propose;
	}

	@Override
	protected Offer getTimingOffer(History history) {
		return getNextOffer(history);
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
