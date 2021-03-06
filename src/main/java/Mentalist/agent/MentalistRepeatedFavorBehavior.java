package Mentalist.agent;

import Mentalist.utils.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.*;

public class MentalistRepeatedFavorBehavior extends MentalistCoreBehavior implements BehaviorPolicy {

	protected MentalistAgentUtilsExtension utils;
	protected GameSpec game;
	protected Offer allocated;
	protected Offer concession;
	protected Offer previous;
	protected ArrayList<Offer> myPrevious;
	protected QueueList<Offer> previousOffers;
	protected QueueList<Offer> dummyPlayerOffers;
	protected QueueList<Offer> dummyVarianceOffers;
	protected QueueList<Integer> dummyEmotions;
	protected LedgerBehavior lb = LedgerBehavior.NONE;
	protected int adverseEvents = 0;
	//target function関連
	protected double gamma_min;
	protected double gamma_max;
	protected int n;
	protected int t;
	protected double utility_bias;
	protected double utility_weight;
	protected double alpha;
	//TKI関連
	protected ArrayList<Double> assertiveness;
	protected ArrayList<Double> cooperativeness;
	//big5関連
	protected double behaviorFrequency;
	protected QueueList<Integer> preferenceAskNum;
	protected QueueList<Integer> preferenceExpressionNum;
	protected QueueList<Double> posEmotionNum;
	protected QueueList<Double> negEmotionNum;
	protected QueueList<Integer> posMessageNum;
	protected QueueList<Integer> negMessageNum;
	protected QueueList<Integer> emotionQueue;
	protected QueueList<Integer> acceptNum;
	protected int preEmotion;
	protected int lieNum;
	protected int favorRequestNum;
	protected int prefRequestNum;
	protected int favorReturnNum;
	protected int fastBehaviorNum;
	protected int timingThreshold;
	protected int batnaAskNum;
	protected int batnaExpressionNum;
	protected QueueList<Integer> behaviorTimings;
	protected ArrayList<Double> neuroticism;
	protected ArrayList<Double> extraversion;
	protected ArrayList<Double> openness;
	protected ArrayList<Double> conscientiousness;
	protected ArrayList<Double> agreeableness;
	//behavior sense関連
	protected QueueList<Integer> silentNum;
	protected QueueList<Integer> niceNum;
	protected QueueList<Integer> fortunateNum;
	protected QueueList<Integer> selfishNum;
	protected QueueList<Integer> concessionNum;
	protected QueueList<Integer> unfortunateNum;
	protected double behaviorSense;
	//保存用
	protected ArrayList<Double> neuroticismBehavior;
	protected ArrayList<Double> extraversionBehavior;
	protected ArrayList<Double> opennessBehavior;
	protected ArrayList<Double> conscientiousnessBehavior;
	protected ArrayList<Double> agreeablenessBehavior;
	protected ArrayList<Double> neuroticismOffer;
	protected ArrayList<Double> extraversionOffer;
	protected ArrayList<Double> opennessOffer;
	protected ArrayList<Double> conscientiousnessOffer;
	protected ArrayList<Double> agreeablenessOffer;
	protected ArrayList<Double> offerWeightList;
	protected ArrayList<Double> behaviorWeightList;
	protected ArrayList<Double> agentUtilList;
	protected ArrayList<Double> playerUtilList;
	//その他
	protected double offerRatio;
	protected double behaviorRatio;
	protected int firstTime = 0;
	protected boolean emoFlag = false;
	protected boolean messageFlag = false;
	protected final double MAX = 1.0;
	protected final double MIN = -1.0;
	protected final double SENSE_MAX = 1.75;
	protected final double SPECIAL_MES_MAX = 1.0;
	protected final double FAST_BEH_MAX = 10.0;
	protected final double BEH_NUM_MAX = 7.5;
	protected final int BEH_SIZE = 10;
	protected int OFF_SIZE = 3;
	protected final int SENSE_SIZE = 5;
	protected final int BATNA_WEIGHT = 2;
	protected final double OFFER_PRE_WEIGHT = 0.1;
	protected final double BEHAVIOR_PRE_WEIGHT = 0.5;
	protected final double SENSE_PRE_WEIGHT = 1.0;
	protected final double UTILITY_MIN_WEIGHT = 0.45;
	protected final double UTILITY_MAX_WEIGHT = 0.65;
	protected final double VARIANCE_MIN_WEIGHT = 0.4;
	protected final double VARIANCE_MAX_WEIGHT = 0.8;
	protected final double CHOICE_VARIANCE_MIN_WEIGHT = 0.2;
	protected final double CHOICE_VARIANCE_MAX_WEIGHT = 0.675;
	protected final double RATE_MAX = 0.75;
	protected final double RATE_MIN = 0.1;
	protected final double ALPHA_MIN = 0.01;
	protected final double ALPHA_MAX= 0.95;
	protected final int N_MIN = 10;
	protected final int N_MAX = 20;
	protected final double GAMMA_MIN = 0.2;
	protected final double GAMMA_MAX = 0.4;
	protected final double PRE_UTILITY_WEIGHT_MIN = 0.8;
	protected final double PRE_UTILITY_WEIGHT_MAX = 1.2;
	protected final double UTILITY_BIAS_MIN = -0.1;
	protected final double UTILITY_BIAS_MAX = 0.1;
	protected final int THRESHOLD_MIN = 3;
	protected final int THRESHOLD_MAX = 9;

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

		this.myPrevious = new ArrayList<>();
		this.previousOffers = new QueueList<Offer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.dummyPlayerOffers = new QueueList<Offer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.dummyVarianceOffers = new QueueList<Offer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.dummyEmotions = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		//target function関連
		this.gamma_min = 0.3;
		this.gamma_max = 1.0;
		this.n = 15;
		this.t = 0;
		this.utility_bias = 0.0;
		this.utility_weight = 1.0;
		this.alpha = 0.01;
		//TKI関連
		this.assertiveness = new ArrayList<Double>();
		this.cooperativeness = new ArrayList<Double>();
		//big5関連
		this.behaviorFrequency = 0.0;
		this.preferenceAskNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.preferenceExpressionNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.preEmotion = 1;
		this.lieNum = 0;
		this.favorRequestNum = 0;
		this.prefRequestNum = 0;
		this.favorReturnNum = 0;
		this.timingThreshold = 6;
		this.batnaAskNum = 0;
		this.batnaExpressionNum = 0;
		this.posMessageNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.negMessageNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.posEmotionNum = new QueueList<Double>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.negEmotionNum = new QueueList<Double>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.emotionQueue = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.acceptNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.behaviorTimings = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.neuroticism = new ArrayList<Double>();
		this.extraversion = new ArrayList<Double>();
		this.openness = new ArrayList<Double>();
		this.conscientiousness = new ArrayList<Double>();
		this.agreeableness = new ArrayList<Double>();
		//behavior sence関連
		this.silentNum = new QueueList<Integer>(SENSE_SIZE, SENSE_PRE_WEIGHT);
		this.niceNum = new QueueList<Integer>(SENSE_SIZE, SENSE_PRE_WEIGHT);
		this.fortunateNum = new QueueList<Integer>(SENSE_SIZE, SENSE_PRE_WEIGHT);
		this.selfishNum = new QueueList<Integer>(SENSE_SIZE, SENSE_PRE_WEIGHT);
		this.concessionNum = new QueueList<Integer>(SENSE_SIZE, SENSE_PRE_WEIGHT);
		this.unfortunateNum = new QueueList<Integer>(SENSE_SIZE, SENSE_PRE_WEIGHT);
		this.behaviorSense = 0.0;
		//保存用
		this.neuroticismBehavior = new ArrayList<Double>();
		this.extraversionBehavior =new ArrayList<Double>();
		this.opennessBehavior = new ArrayList<Double>();
		this.conscientiousnessBehavior = new ArrayList<Double>();
		this.agreeablenessBehavior = new ArrayList<Double>();
		this.neuroticismOffer = new ArrayList<Double>();
		this.extraversionOffer = new ArrayList<Double>();
		this.opennessOffer = new ArrayList<Double>();
		this.conscientiousnessOffer = new ArrayList<Double>();
		this.agreeablenessOffer = new ArrayList<Double>();
		this.offerWeightList = new ArrayList<Double>();
		this.behaviorWeightList = new ArrayList<Double>();
		this.agentUtilList = new ArrayList<Double>();
		this.playerUtilList = new ArrayList<Double>();
		//その他
		this.offerRatio = 0.0;
		this.behaviorRatio = 0.0;
	}

	//譲歩関数
	public double getConcessionValue(){
		Offer maxOffer = makeMaxAgentDummyOffer(previous);
		return calcAgentUtil(maxOffer) < 1.0 / utils.getMaxPossiblePoints() ? 1.0 / utils.getMaxPossiblePoints() : (gamma_min + (gamma_max - gamma_min) * max(0.0 ,(1 - max(0.0, this.utility_bias + this.utility_weight * (calcAgentUtil(previous) / calcAgentUtil(maxOffer))) * pow(min((double)t / n, 1.0),(1 / alpha))))) * calcAgentUtil(maxOffer);
	}

	//正規化
	public double normarize(double data, double data_max, double data_min){
		return (data_max != data_min) ? (data - data_min) / (data_max - data_min) * (MAX - MIN) + MIN : 0.0;
	}

	//正規化
	public double normarize(double data, double data_max, double data_min, double MAX, double MIN){
		return (data_max != data_min) ? (data - data_min) / (data_max - data_min) * (MAX - MIN) + MIN : 0.0;
	}

	public double calcPlayerUtil(Offer o){
		return (double)utils.adversaryValue(o, utils.getMinimaxOrdering()) / utils.getMaxPossiblePoints();
	}

	public double calcAgentUtil(Offer o){
		return (double)utils.myActualOfferValue(o) / utils.getMaxPossiblePoints();
	}

	//効用の平均を計算
	public double calcMean(ArrayList<Offer> offers){
		double mean = 0.0;
		if(!offers.isEmpty()) {
			for (Offer o : offers) {
				mean += calcPlayerUtil(o);
			}
			mean /= offers.size();
		}
		return mean;
	}


	public double calcVariance(QueueList<Offer> offers, boolean isDummy){
		double variance = 0.0;
		if(!isDummy){
			ArrayList<Offer> preOffers = offers.getPreQueue();
			if(!preOffers.isEmpty()) {
				double mean = calcMean(preOffers);
				for (Offer o : preOffers) {
					variance += abs(calcPlayerUtil(o) - mean);
				}
				variance /= sqrt(preOffers.size());
				variance *= offers.getPreWeight();
			}
			ArrayList<Offer> currentOffers = offers.getQueue();
			if(!currentOffers.isEmpty()) {
				double currentVariance = 0.0;
				double mean = calcMean(currentOffers);
				for (Offer o : currentOffers) {
					currentVariance += abs(calcPlayerUtil(o) - mean);
				}
				currentVariance /= sqrt(currentOffers.size());
				variance += currentVariance;
			}
		}
		else {

			ArrayList<Offer> preOffers = offers.getPreQueue();
			if (!preOffers.isEmpty()) {
				double mean = calcMean(preOffers);
				for (Offer o : preOffers) {
					variance += abs(calcPlayerUtil(o) - mean);
				}
				variance /= sqrt(preOffers.size());
				variance *= offers.getPreWeight();
			}

			ArrayList<Offer> currentOffers = offers.getQueue();
			if (!currentOffers.isEmpty()) {
				double mean = calcMean(currentOffers);
				double currentVariance = 0.0;
				double max = 0.0;
				int undefined = 0;
				for (int i = 0; i < currentOffers.size(); i++) {
					double dummyUtil = calcPlayerUtil(currentOffers.get(i));
					max = max(max, calcPlayerUtil(previousOffers.getQueue().get(i)));
					undefined += calcUndefinedNum(previousOffers.getQueue().get(i));
					if(dummyUtil > 0.75)
						currentVariance += abs(dummyUtil - mean) * (1.0 + sqrt(dummyUtil * 1.25) * 0.7);
					else
						currentVariance += abs(dummyUtil - mean);
				}

				currentVariance /= sqrt(currentOffers.size());
				if(undefined != 0 || max - calcMean(previousOffers.getQueue()) < 1 / (double)utils.getMaxPossiblePoints() / OFF_SIZE * 1.1)
					currentVariance +=  1 / ((1.1 - calcMean(currentOffers)) * currentVariance * utils.getMaxPossiblePoints() + 0.01) * 0.01;
				else
					currentVariance += (max - calcMean(previousOffers.getQueue()));
				variance += currentVariance;
			}
		}
		return variance;
	}

	public double calcAgentMean(ArrayList<Offer> offers){
		double mean = 0.0;
		if(!offers.isEmpty()) {
			for (Offer o : offers) {
				mean += calcAgentUtil(o);
			}
			mean /= offers.size();
		}
		return mean;
	}

	//効用の平均を計算
	public double calcMean(QueueList<Offer> offers, boolean isDummy){
		double mean = 0.0;
		if(!isDummy) {
			ArrayList<Offer> preOffers = offers.getPreQueue();
			if (!preOffers.isEmpty()) {
				mean = calcMean(preOffers);
				mean *= offers.getPreWeight();
			}
			double currentMean = 0.0;
			ArrayList<Offer> currentOffers = offers.getQueue();
			if (!currentOffers.isEmpty()) {
				currentMean = calcMean(currentOffers);
				mean += currentMean;
			}
		}
		else{
			ArrayList<Offer> preOffers = offers.getPreQueue();
			if (!preOffers.isEmpty()) {
				for (Offer o : preOffers) {
					mean += calcDummyUtil(o);
				}
				mean *= offers.getPreWeight();
				mean /= preOffers.size();
			}
			double currentMean = 0.0;
			ArrayList<Offer> currentOffers = offers.getQueue();
			if (!currentOffers.isEmpty()) {
				for (Offer o : currentOffers) {
					currentMean += calcDummyUtil(o);
				}
				currentMean /= currentOffers.size();
			}

			mean += currentMean;
		}

		return mean;
	}

	public double calcDummyUtil(Offer o){
		int[] free = new int[game.getNumIssues()];
		ArrayList<Integer> order = utils.getMinimaxOrdering();
		ArrayList<Integer> index = utils.getMinimaxOrdering();
		Collections.sort(index);

		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = o.getItem(issue)[1];
		}

		if(calcPlayerUtil(o) > 0.75) {
			return calcPlayerUtil(o) * (1.0 + pow(calcPlayerUtil(o), 1.5) * 0.2);
		}else
			return calcPlayerUtil(o);
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
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != posEmotionNum.getFullSize()){
			for(int i = posEmotionNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				posEmotionNum.enQueue(0.0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != negEmotionNum.getFullSize()){
			for(int i = negEmotionNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				negEmotionNum.enQueue(0.0);
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
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != acceptNum.getFullSize()){
			for(int i = acceptNum.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
				acceptNum.enQueue(0);
			}
		}
		if((behaviorTimings.getFullSize() - previousOffers.getFullSize()) != emotionQueue.getFullSize()){
			for(int i = emotionQueue.getFullSize(); i < behaviorTimings.getFullSize() - previousOffers.getFullSize(); i++){
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

		ArrayList<Integer> allocated = new ArrayList<>();
		ArrayList<Integer> order = utils.getMinimaxOrdering();
		ArrayList<Integer> index = utils.getMinimaxOrdering();
		Collections.sort(index);

		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			if(o.getItem(issue)[1] != game.getIssueQuants()[issue]){
				allocated.add(game.getIssueQuants()[issue] - o.getItem(issue)[1]);
			}
			p.setItem(issue, new int[]{0, game.getIssueQuants()[issue], 0});
		}

		Collections.sort(allocated, Collections.reverseOrder());
		//エージェントに全振り
		for (int i: allocated) {
			p.setItem(order.indexOf(index.get(0)), new int[]{0, p.getItem(order.indexOf(index.get(0)))[1] - i, i});
			index.remove(0);
		}

		dummyPlayerOffers.enQueue(p);
	}

	public void addDummyVarianceOffer(Offer o){
		Offer p = new Offer(game.getNumIssues());
		//未定義のアイテムをissueごとに把握
		int[] free = new int[game.getNumIssues()];
		int[] player = new int[game.getNumIssues()];
		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = o.getItem(issue)[1];
			player[issue] = o.getItem(issue)[2];
		}
		//プレイヤーに全振り
		for (int i = 0; i < game.getNumIssues(); i++) {
			int[] init = {0, free[i], game.getIssueQuants()[i] - free[i]};
			p.setItem(i, init);
		}

		dummyVarianceOffers.enQueue(p);
	}

	/**** TKI協調性 ****/
	public void calcCooperativeness(){
		double mean = calcMean(previousOffers, false);
		double dummyMean = calcMean(dummyPlayerOffers, true);
		double meanPoint = -normarize(mean, max(UTILITY_MAX_WEIGHT * dummyMean, mean), min(UTILITY_MIN_WEIGHT * dummyMean, mean));

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("mean: " + mean, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy mean: " + dummyMean, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("cooperative: " + meanPoint, ServletUtils.DebugLevels.DEBUG);


		this.cooperativeness.add(meanPoint);
	}

	/**** TKI積極性 ****/
	public void calcAssertiveness(){
		double variance = calcVariance(previousOffers, false);
		double dummyVariance = calcVariance(dummyPlayerOffers, true);
		double variancePoint = -normarize(variance, max(VARIANCE_MAX_WEIGHT * dummyVariance, variance), min(VARIANCE_MIN_WEIGHT * dummyVariance, variance));

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("variance: " + variance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy variance: " + dummyVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("assertive: " + variancePoint, ServletUtils.DebugLevels.DEBUG);


		this.assertiveness.add(variancePoint);
	}

	/**** ビッグファイブ神経症傾向 ****/
	public void calcNeuroticism(){
		double niceConcession = niceNum.mean() + concessionNum.mean();
		double neuroOfferPoint = normarize(niceConcession, max(RATE_MAX, niceConcession), min(RATE_MIN, niceConcession));

		double neuroBehavior = negEmotionNum.doubleSum() + negMessageNum.sum();
		double neuroBehaviorPoint = normarize(neuroBehavior, max(BEH_NUM_MAX, neuroBehavior), 0.0);

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("nice + concession: " + niceNum.mean() + concessionNum.mean(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("nice + concession point: " + neuroOfferPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("negEmo: " + negEmotionNum.doubleSum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("negMes: " + negMessageNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neuroOffer: " + neuroOfferPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neuroBeh: " + neuroBehaviorPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("neuroticism: " + neuroOfferPoint * offerRatio + neuroBehaviorPoint * behaviorRatio, ServletUtils.DebugLevels.DEBUG);

		this.neuroticismOffer.add(neuroOfferPoint);
		this.neuroticismBehavior.add(neuroBehaviorPoint);
		this.neuroticism.add(neuroOfferPoint * offerRatio + neuroBehaviorPoint * behaviorRatio);
	}

	/**** ビッグファイブ外向性 ****/
	public void calcExtraversion(){
		calcFrequency();
		double selfishFortunate = selfishNum.mean() + fortunateNum.mean();
		double extraOfferPoint = normarize(selfishFortunate, max(RATE_MAX, selfishFortunate), min(RATE_MIN, selfishFortunate));

		double favorRequestPoint = normarize(favorRequestNum, max(SPECIAL_MES_MAX, favorRequestNum), -SPECIAL_MES_MAX);
		double extraBehaviorPoint = min(1.0, -normarize(behaviorFrequency, max(behaviorFrequency, 40.0),  min(behaviorFrequency, 10.0)) + favorRequestPoint);

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("selfish + fortunate: " + selfishNum.mean() + fortunateNum.mean(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("selfish + fortunate point: " + extraOfferPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("favor request: " + favorRequestNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("favor request point: " + favorRequestPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior: " + behaviorFrequency, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("extraBeh: " + extraBehaviorPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("extraOffer: " + extraOfferPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pre behavior: " + behaviorTimings.getPreQueue(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior: " + behaviorTimings, ServletUtils.DebugLevels.DEBUG);

		ServletUtils.log("extraversion: " + extraOfferPoint * offerRatio + extraBehaviorPoint * behaviorRatio, ServletUtils.DebugLevels.DEBUG);

		this.extraversionOffer.add(extraOfferPoint);
		this.extraversionBehavior.add(extraBehaviorPoint);
		this.extraversion.add(extraOfferPoint * offerRatio + extraBehaviorPoint * behaviorRatio);
	}

	/**** ビッグファイブ経験への開放性 ****/
	public void calcOpenness(){
		double choicePlayerVariance = calcChoiceVariance(previousOffers.getPreQueue(), 2, false) * OFFER_PRE_WEIGHT + calcChoiceVariance(previousOffers.getQueue(), 2, false);
		double choiceDummyPlayerVariance = calcChoiceVariance(dummyVarianceOffers.getPreQueue(), 2, false) * OFFER_PRE_WEIGHT + calcChoiceVariance(dummyVarianceOffers.getQueue(), 2, true);
		double openOfferPoint = normarize(choicePlayerVariance, max(CHOICE_VARIANCE_MAX_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance), min(CHOICE_VARIANCE_MIN_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance));

		double openBehavior = preferenceAskNum.sum() + prefRequestNum + batnaAskNum * BATNA_WEIGHT;
		double openBehaviorPoint = normarize(openBehavior, max(BEH_NUM_MAX, openBehavior), 0.0);

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("player choice: " + choicePlayerVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy player choice: " + choiceDummyPlayerVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("choice point: " + openOfferPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("ask: " + preferenceAskNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("batna ask: " + batnaAskNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("pref req: " + prefRequestNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("openBeh: " + openBehaviorPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("openness: " + openOfferPoint * offerRatio + openBehaviorPoint * behaviorRatio, ServletUtils.DebugLevels.DEBUG);

		this.opennessOffer.add(openOfferPoint);
		this.opennessBehavior.add(openBehaviorPoint);
		this.openness.add(openOfferPoint * offerRatio + openBehaviorPoint * behaviorRatio);
	}

	/**** ビッグファイブ誠実性 ****/
	public void calcConscientiousness(){
		double variance = calcVariance(previousOffers, false);
		double dummyVariance = calcVariance(dummyPlayerOffers, true);
		double variancePoint = -normarize(variance, max(VARIANCE_MAX_WEIGHT * dummyVariance, variance), min(VARIANCE_MIN_WEIGHT * dummyVariance, variance));
		double undefined = abs(calcUndefinedRate(previousOffers));
		double undefinedRatePoint = -normarize(undefined, max(0.35 * calcMaxUndefinedNum(), undefined), min(1.0, undefined));
		double conscientOfferPoint = (variancePoint + undefinedRatePoint) / 2;

		double liePoint = -normarize(lieNum, max(SPECIAL_MES_MAX, lieNum), -SPECIAL_MES_MAX);
		double fastBehaviorPoint = normarize(fastBehaviorNum, max(fastBehaviorNum, FAST_BEH_MAX), 0.0);
		double conscientBehavior = preferenceExpressionNum.sum() + batnaExpressionNum * BATNA_WEIGHT;
		double conscientBehaviorPoint = max(-1.0, (normarize(conscientBehavior, max(BEH_NUM_MAX, conscientBehavior), 0.0) + fastBehaviorPoint) / 2 + liePoint);

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("variance: " + variance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy variance: " + dummyVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("variance point: " + variancePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("undefined rate: " + undefined, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("undefined rate point : " + undefinedRatePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("fast beh: " + fastBehaviorNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("fast beh point : " + fastBehaviorPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("exp: " + preferenceExpressionNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("batna exp: " + batnaExpressionNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("lie: " + lieNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("lie point: " + liePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("consciOffer: " + conscientOfferPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("consciBeh: " + conscientBehaviorPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("conscientiousness: " + conscientOfferPoint * offerRatio + conscientBehaviorPoint * behaviorRatio, ServletUtils.DebugLevels.DEBUG);

		this.conscientiousnessOffer.add(conscientOfferPoint);
		this.conscientiousnessBehavior.add(conscientBehaviorPoint);
		this.conscientiousness.add(conscientOfferPoint * offerRatio + conscientBehaviorPoint * behaviorRatio);
	}

	/**** ビッグファイブ協調性 ****/
	public void calcAgreeableness(){
		calcBehaviorSense();
		double behaviorSensePoint = normarize(this.behaviorSense, max(SENSE_MAX, behaviorSense), min(0.5 , behaviorSense));
		double utilRatio = normarize(calcUtilRate(previousOffers), max(0.7, calcUtilRate(previousOffers)), min(0.2, calcUtilRate(previousOffers)));
		double agreeOfferPoint = (behaviorSensePoint + utilRatio) / 2;

		double favorReturnPoint = normarize(favorReturnNum, max(SPECIAL_MES_MAX, favorReturnNum), -SPECIAL_MES_MAX);
		double agreeBehavior = acceptNum.sum() + posEmotionNum.doubleSum() + posMessageNum.sum();
		double agreeBehaviorPoint = min(1.0, normarize(agreeBehavior, max(BEH_NUM_MAX, agreeBehavior), 0.0) + favorReturnPoint);

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior sense: " + behaviorSense, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("behavior sense point: " + behaviorSensePoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("offer rate: " + calcUtilRate(previousOffers), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("offer rate point: " + utilRatio, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("accept: " + acceptNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("favor return: " + favorReturnNum, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("favor return point: " + favorReturnPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("posEmo sum: " + posEmotionNum.doubleSum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("posMes sum: " + posMessageNum.sum(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("agreeOffer: " + agreeOfferPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("agreeBeh: " + agreeBehaviorPoint, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("agreeableness: " + agreeOfferPoint * offerRatio + agreeBehaviorPoint * behaviorRatio, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);

		this.agreeablenessOffer.add(agreeOfferPoint);
		this.agreeablenessBehavior.add(agreeBehaviorPoint);
		this.agreeableness.add(agreeOfferPoint * offerRatio + agreeBehaviorPoint * behaviorRatio);
	}

	//行動の頻度
	public void calcFrequency(){
		if(behaviorTimings.queueSize() >= 1) {
			this.behaviorFrequency = 0.0;
			ArrayList<Integer> preQueue = behaviorTimings.getPreQueue();
			if(!preQueue.isEmpty()) {
				double frequency = preQueue.get(0);
				for (int i = 1; i < preQueue.size(); i++) {
					frequency += preQueue.get(i) - preQueue.get(i - 1);
				}
				this.behaviorFrequency = frequency / preQueue.size() * behaviorTimings.getPreWeight();
			}
			ArrayList<Integer> queue = behaviorTimings.getQueue();
			double frequency = behaviorTimings.getPreQueue().size() >= 1 ? queue.get(0) - preQueue.get(preQueue.size() - 1) : queue.get(0);
			for (int i = 1; i < queue.size(); i++) {
				frequency += queue.get(i) - queue.get(i - 1);
			}
			this.behaviorFrequency += frequency / behaviorTimings.queueSize();
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
	public double calcUndefinedRate(QueueList<Offer> offers){
		double sum = 0.0;
		if(offers.queueSize() >= 1) {
			ArrayList<Offer> preOffers = offers.getPreQueue();
			if(!preOffers.isEmpty()){
				sum = calcMaxUndefinedNum() - calcUndefinedNum(preOffers.get(0));
				for(int i = 1; i < preOffers.size(); i++){
					sum += abs(calcMaxUndefinedNum() - calcUndefinedNum(preOffers.get(i)) - (calcMaxUndefinedNum() - calcUndefinedNum(preOffers.get(i - 1))));
				}
				sum /= preOffers.size();
				sum *= offers.getPreWeight();
			}
			ArrayList<Offer> currentOffers = offers.getQueue();
			if(!currentOffers.isEmpty()){
				double currentSum = offers.getPreQueue().size() >= 1 ? abs(calcMaxUndefinedNum() - calcUndefinedNum(currentOffers.get(0)) - (calcMaxUndefinedNum() - calcUndefinedNum(preOffers.get(preOffers.size() - 1)))) : calcMaxUndefinedNum() - calcUndefinedNum(currentOffers.get(0));
				for(int i = 1; i < currentOffers.size(); i++){
					currentSum += abs(calcMaxUndefinedNum() - calcUndefinedNum(currentOffers.get(i)) - (calcMaxUndefinedNum() - calcUndefinedNum(currentOffers.get(i - 1))));
				}
				currentSum /= currentOffers.size();
				sum += currentSum;
			}
		}

		return sum;
	}

	//プレイヤーとエージェントの効用の割合の平均
	public double calcUtilRate(QueueList<Offer> offers){
		double meanPlayer = 0.0;
		double meanAgent = 0.0;
		ArrayList<Offer> preOffers = offers.getPreQueue();
		if (!preOffers.isEmpty()) {
			meanPlayer = calcMean(preOffers);
			meanPlayer *= offers.getPreWeight();
			meanAgent = calcAgentMean(preOffers);
			meanAgent *= offers.getPreWeight();
		}
		ArrayList<Offer> currentOffers = offers.getQueue();
		if (!currentOffers.isEmpty()) {
			meanPlayer += calcMean(currentOffers);
			meanAgent += calcAgentMean(currentOffers);
		}

		return meanAgent + meanPlayer > 1 / (double)utils.getMaxPossiblePoints() / offers.getFullSize() * 1.1 ? meanAgent / (meanAgent + meanPlayer) : 0.0;
	}

	//選択肢の分散(ここでは標準偏差)
	public double calcChoiceVariance(ArrayList<Offer> offers, int userNum, boolean isDummy){
		double means[] = new double[game.getNumIssues()];
		double variances = 0.0;
		double resultVariance = 0.0;
		double max = 0.0;
		int undefined = 0;

		if(!offers.isEmpty()) {
			for (int i = 0; i < offers.size(); i++) {
				for (int j = 0; j < game.getNumIssues(); j++) {
					means[j] += offers.get(i).getItem(j)[userNum];
				}
				if(isDummy){
					max = max(max, calcPlayerUtil(previousOffers.getQueue().get(i)));
					undefined += calcUndefinedNum(previousOffers.getQueue().get(i));
				}
			}
			for (int i = 0; i < offers.size(); i++) {
				for (int j = 0; j < game.getNumIssues(); j++) {
					variances += abs(offers.get(i).getItem(j)[userNum] - means[j] / offers.size());
				}
			}
			if(isDummy) {
				if(undefined != 0 || max - calcMean(previousOffers.getQueue()) < 1 / (double)utils.getMaxPossiblePoints() / OFF_SIZE * 1.1)
					variances += 1 / (0.5 * (max(0.0, variances - 1.5)) + 0.01) / max(0.01, variances) * 0.5;
				else
					variances += (max - calcMean(previousOffers.getQueue())) * utils.getMaxPossiblePoints() * OFF_SIZE * game.getNumIssues() * 0.5;
			}
			resultVariance = variances / sqrt((game.getNumIssues() * offers.size()));
		}
		return resultVariance;
	}

	//行動に対する感受性
	public void calcBehaviorSense(){

		int opponentDiff = previousOffers.queueSize() < 2 ? utils.adversaryValue(previousOffers.getLastElement(), utils.getMinimaxOrdering()) : utils.adversaryValue(previousOffers.getLastElement(), utils.getMinimaxOrdering()) - utils.adversaryValue(previousOffers.getQueue().get(previousOffers.queueSize() - 2), utils.getMinimaxOrdering());
		int agentDiff = previousOffers.queueSize() < 2 ? utils.myActualOfferValue(previousOffers.getLastElement()) : utils.myActualOfferValue(previousOffers.getLastElement()) - utils.myActualOfferValue(previousOffers.getQueue().get(previousOffers.queueSize() - 2));

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
		if(selfishNum.sum() + unfortunateNum.sum() + silentNum.sum() < OFFER_PRE_WEIGHT)
			behaviorSense = SENSE_MAX;
		//1より大きければ敏感，1より小さければ鈍感
		else
			behaviorSense = (fortunateNum.sum() + niceNum.sum() + concessionNum.sum()) / (selfishNum.sum() + unfortunateNum.sum() + silentNum.sum());
	}

	//offerと行動の割合を計算
	public void calcWeight() {
		offerRatio = max(0.5, 1.0 - (behaviorTimings.getFullSize() - previousOffers.getFullSize()) / (2.0 * BEH_SIZE));
		behaviorRatio = min(0.5, (behaviorTimings.getFullSize() - previousOffers.getFullSize()) / (2.0 * BEH_SIZE));
		offerWeightList.add(offerRatio);
		behaviorWeightList.add(behaviorRatio);
	}

	public void addRound(){ this.t += 1; }

	public void setPrevious(Offer o){ this.previous = o; }

	public void addPreviousOffer(){
		if(previousOffers.queueSize() == 0){
			this.fastBehaviorNum = behaviorTimings.getFullSize();
		}
		this.previousOffers.enQueue(this.previous);
		addDummyPlayerOffer(this.previous);
		addDummyVarianceOffer(this.previous);
		agentUtilList.add(calcAgentUtil(previous));
		playerUtilList.add(calcPlayerUtil(previous));
	}

	//脅しの回数
	public void addFavorRequestNum(){ this.favorRequestNum += 1; }

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

	public void addMyPrevious(Offer o){ myPrevious.add(o);}

	//譲歩関数のパラメータを特性によって変更
	public void setConcessionParameter(){
		final int DISTANCE_MAX = 10000;
		//TKIについて
		Point2D.Double playerTKITrait = new Point2D.Double(cooperativeness.get(cooperativeness.size() - 1), assertiveness.get(assertiveness.size() - 1));
		double competingDistanceInverse = playerTKITrait.distance(-1.0, 1.0) == 0 ? DISTANCE_MAX : 1 / playerTKITrait.distance(-1.0, 1.0);
		double avoidingDistanceInverse = playerTKITrait.distance(-1.0, -1.0) == 0 ? DISTANCE_MAX : 1 / playerTKITrait.distance(-1.0, -1.0);
		double collaboratingDistanceInverse = playerTKITrait.distance(1.0, 1.0) == 0 ? DISTANCE_MAX : 1 / playerTKITrait.distance(1.0, 1.0);
		double accommodatingDistanceInverse = playerTKITrait.distance(1.0, -1.0) == 0 ? DISTANCE_MAX : 1 / playerTKITrait.distance(1.0, -1.0);
		double distanceSum = competingDistanceInverse + avoidingDistanceInverse + collaboratingDistanceInverse + accommodatingDistanceInverse;

		/*
		ServletUtils.log("compe: " + competingDistanceInverse, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("colla: " + collaboratingDistanceInverse, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("avoid: " + avoidingDistanceInverse, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("accom: " + accommodatingDistanceInverse, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("distance: " + distanceSum, ServletUtils.DebugLevels.DEBUG);
		*/

		int tkiN = 0;
		if(competingDistanceInverse >= DISTANCE_MAX || collaboratingDistanceInverse >= DISTANCE_MAX){
			tkiN = N_MAX;
		}
		else if(avoidingDistanceInverse >= DISTANCE_MAX || accommodatingDistanceInverse >= DISTANCE_MAX){
			tkiN = N_MIN;
		}
		else {
			tkiN = (int) (N_MAX * competingDistanceInverse / distanceSum + N_MIN * avoidingDistanceInverse / distanceSum + N_MAX * collaboratingDistanceInverse / distanceSum + N_MIN * accommodatingDistanceInverse / distanceSum);
		}
		double tkiAlpha = 0;
		if(collaboratingDistanceInverse >= DISTANCE_MAX || accommodatingDistanceInverse >= DISTANCE_MAX){
			tkiAlpha = ALPHA_MAX;
		}
		else if(competingDistanceInverse >= DISTANCE_MAX || avoidingDistanceInverse >= DISTANCE_MAX){
			tkiAlpha = ALPHA_MIN;
		}
		else {
			tkiAlpha = ALPHA_MAX * accommodatingDistanceInverse / distanceSum + ALPHA_MAX * collaboratingDistanceInverse / distanceSum + ALPHA_MIN * avoidingDistanceInverse / distanceSum + ALPHA_MIN * competingDistanceInverse / distanceSum;
		}

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

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("TKI n: " + tkiN, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5 n: " + big5N, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("n: " + n, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("t: " + t, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("TKI alpha: " + tkiAlpha, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5 alpha: " + big5Alpha, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("alpha: " + alpha, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("gamma min: " + gamma_min, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("utility weight: " + utility_weight, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("utility bias: " + utility_bias, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("timing threshold: " + timingThreshold, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("concession value: " + getConcessionValue(), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
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
	}

	@Override
	protected void updateAllocated (Offer update)
	{
		allocated = update;
	}

	@Override
	protected void updateAdverseEvents (int change)
	{
		adverseEvents = Math.max(0, adverseEvents + (int)Math.ceil(acceptNum.sum()) + change);
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

		ServletUtils.log("final concession: " + getConcessionValue(), ServletUtils.DebugLevels.DEBUG);
		addMyPrevious(propose);

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
					propose.setItem(opponentFave, new int[]{allocated.getItem(opponentFave)[0] + 1, free[opponentFave] - 1, allocated.getItem(opponentFave)[2]});
					free[opponentFave] -= 1;
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
					if(count % 3 == 1) {
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
				if(count % 3 == 1) {
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
		}while(calcUndefinedNum(propose) != 0 && getConcessionValue() > calcAgentUtil(propose));

		while(calcUndefinedNum(propose) != 0 && (calcMaxUndefinedNum() - calcUndefinedNum(previous)) > (calcMaxUndefinedNum() - calcUndefinedNum(propose))){
			int userFave = -1;

			// Find most valued issue for player and VH (of the issues that have undeclared items)
			int max = game.getNumIssues() + 1;
			for (int i = 0; i < game.getNumIssues(); i++)
				if (free[i] > 0 && playerPref.get(i) < max) {
					userFave = i;
					max = playerPref.get(i);
				}

			propose.setItem(userFave, new int[]{allocated.getItem(userFave)[0], free[userFave] - 1, allocated.getItem(userFave)[2] + 1});
			free[userFave] -= 1;


			allocated = propose;
		}

		allocated = preAllocated;

		propose = calcAgentUtil(allocated) <= calcAgentUtil(propose) ? propose : allocated;
		addMyPrevious(propose);

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

	public String getNeuroticismOffer(){ return String.join(":", neuroticismOffer.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getExtraversionOffer(){ return String.join(":", extraversionOffer.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getOpennessOffer(){ return String.join(":", opennessOffer.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getAgreeablenessOffer() { return String.join(":", agreeablenessOffer.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getConscientiousnessOffer() { return String.join(":", conscientiousnessOffer.toString().replaceAll("[\\[\\] ]", "").split(",")); }

	public String getNeuroticismBehavior(){ return String.join(":", neuroticismBehavior.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getExtraversionBehavior(){ return String.join(":", extraversionBehavior.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getOpennessBehavior(){ return String.join(":", opennessBehavior.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getAgreeablenessBehavior() { return String.join(":", agreeablenessBehavior.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getConscientiousnessBehavior() { return String.join(":", conscientiousnessBehavior.toString().replaceAll("[\\[\\] ]", "").split(",")); }

	public String getOfferWeight(){ return String.join(":", offerWeightList.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getBehaviorWeight(){ return String.join(":", behaviorWeightList.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getAgentUtil(){ return String.join(":", agentUtilList.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getPlayerUtil(){ return String.join(":", playerUtilList.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getT() { return Integer.toString(t); }

	public String getBehaviorFrequency(){ return Double.toString(behaviorFrequency); }
	public String getBehaviorTimings(){ return String.join(":", behaviorTimings.toString().replaceAll("[\\[\\] ]", "").split(",")); }
	public String getFastBehaviorNum(){ return Integer.toString(fastBehaviorNum); }
	public String getOfferString(){ return String.join(":", previousOffers.toString().replaceAll("\\[", "a").replaceAll("\\]", "b").split(","));}
}
