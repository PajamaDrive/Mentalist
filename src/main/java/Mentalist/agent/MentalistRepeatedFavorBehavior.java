package Mentalist.agent;

import Mentalist.utils.*;

import javax.servlet.Servlet;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
	//protected QueueList<Offer> dummyVarianceAgentOffers;
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
	protected int threatNum;
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
	//behavior sence関連
	protected QueueList<Integer> silentNum;
	protected QueueList<Integer> niceNum;
	protected QueueList<Integer> fortunateNum;
	protected QueueList<Integer> selfishNum;
	protected QueueList<Integer> concessionNum;
	protected QueueList<Integer> unfortunateNum;
	protected double behaviorSence;
	//その他
	protected double offerRatio;
	protected double behaviorRatio;
	protected int firstTime = 0;
	protected boolean emoFlag = false;
	protected boolean messageFlag = false;
	protected final double MAX = 1.0;
	protected final double MIN = -1.0;
	protected final double SENCE_MAX = 2.0;
	protected final double MES_MAX = 5.0;
	protected final double EMO_MAX = 5.0;
	protected final double SPECIAL_MES_MAX = 1.0;
	protected final double ACCEPT_MAX = 5.0;
	protected final double FAST_BEH_MAX = 5.0;
	protected final int BEH_SIZE = 10;
	protected final int OFF_SIZE = 3;
	protected final double OFFER_PRE_WEIGHT = 0.1;
	protected final double BEHAVIOR_PRE_WEIGHT = 0.5;
	protected final double UTILITY_MIN_WEIGHT = 0.45;
	protected final double UTILITY_MAX_WEIGHT = 0.65;
	protected final double VARIANCE_MIN_WEIGHT = 0.4;
	protected final double VARIANCE_MAX_WEIGHT = 0.8;
	protected final double CHOICE_VARIANCE_MIN_WEIGHT = 0.2;
	protected final double CHOICE_VARIANCE_MAX_WEIGHT = 0.8;
	protected final double MAX_WEIGHT = 0.8;
	protected final double MIN_WEIGHT = 0.2;
	protected final double RATE_MAX = 0.75;
	protected final double ALPHA_MIN = 0.1;
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

/*
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

	public double calcDummyVariance(ArrayList<Offer> offers){
		double variance = 0.0;
		if(!offers.isEmpty()){
			double mean = calcMean(offers);
			double max = 0;
			for(Offer o: offers){
				if(utils.adversaryValue(o, utils.getMinimaxOrdering()) > max)
					max = utils.adversaryValue(o, utils.getMinimaxOrdering());
			}
			variance = abs(max - mean) * sqrt(dummyPlayerOffers.getFullSize());
		}
		return variance;
	}
	*/


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


	public double calcVariance(QueueList<Offer> offers, boolean isDummy){
		double variance = 0.0;
		if(!isDummy){
			ArrayList<Offer> preOffers = offers.getPreQueue();
			if(!preOffers.isEmpty()) {
				double mean = calcMean(preOffers);
				for (Offer o : preOffers) {
					variance += abs(this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) - mean);
				}
				variance /= sqrt(preOffers.size());
				variance *= offers.getPreWeight();
			}
			ArrayList<Offer> currentOffers = offers.getQueue();
			if(!currentOffers.isEmpty()) {
				double currentVariance = 0.0;
				double mean = calcMean(currentOffers);
				for (Offer o : currentOffers) {
					currentVariance += abs(this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) - mean);
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
					variance += abs(this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) - mean);
				}
				variance /= sqrt(preOffers.size());
				variance *= offers.getPreWeight();
			}

			ArrayList<Offer> currentOffers = offers.getQueue();
			if (!currentOffers.isEmpty()) {
				double mean = calcMean(currentOffers);
				double currentVariance = 0.0;
				for (Offer o : currentOffers) {
					double dummyUtil = utils.adversaryValue(o, utils.getMinimaxOrdering());
					if(dummyUtil > utils.getMaxPossiblePoints() * 0.75)
						currentVariance += abs(dummyUtil - mean) * (1.0 + sqrt(dummyUtil / utils.getMaxPossiblePoints() * 1.5) * 0.5);
					else
						currentVariance += abs(dummyUtil - mean);
				}

				currentVariance /= sqrt(currentOffers.size());

				currentVariance +=  1 / ((1.1 - calcMean(currentOffers) / utils.getMaxPossiblePoints()) * currentVariance + 0.05);
				variance += currentVariance;
			}
		}
		return variance;
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

		if(this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) > utils.getMaxPossiblePoints() * 0.75) {
			return this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) * (1.0 + pow((double)this.utils.adversaryValue(o, this.utils.getMinimaxOrdering()) / utils.getMaxPossiblePoints(), 1.5) * 0.2);
		}else
			return this.utils.adversaryValue(o, this.utils.getMinimaxOrdering());
	}

	//前回のOfferの効用を計算
	public double getPreviousValue(){
		return utils.myActualOfferValue(myPrevious.get(myPrevious.size() - 1));
	}

	//今までの効用の平均を計算
	public double calcPreviousMean(){
		double mean = 0.0;
		int size = 0;
		ArrayList<Offer> offers = previousOffers.getAllArray();
		if(!offers.isEmpty()){
			for(Offer o: offers){
				mean += utils.myActualOfferValue(o);
			}
			size = offers.size();
		}

		return mean;
	}

	//今までの効用の最大値を計算
	public double getPreviousMaxValue(){
		double max = 0.0;
		ArrayList<Offer> offers = previousOffers.getAllArray();
		if(!offers.isEmpty()){
			for(Offer o: offers){
				max = max(max, utils.myActualOfferValue(o));
			}
		}
		offers = myPrevious;
		if(!offers.isEmpty()){
			for(Offer o: offers){
				max = max(max, utils.myActualOfferValue(o));
			}
		}
		return max;
	}

	public boolean isFirstOffer(){
		return previousOffers.getFullSize() == 0 ? true : false;
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

	public Offer makeMaxPlayerDummyOffer(Offer o){
		Offer p = new Offer(game.getNumIssues());
		//未定義のアイテムをissueごとに把握
		int[] free = new int[game.getNumIssues()];
		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = o.getItem(issue)[1];
		}
		//プレイヤーに全振り
		for (int i = 0; i < game.getNumIssues(); i++) {
			int[] init = {0, free[i], game.getIssueQuants()[i] - free[i]};
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
/*
	public void addDummyVarianceAgentOffer(Offer o){
		Offer p = new Offer(game.getNumIssues());
		//未定義のアイテムをissueごとに把握
		int[] free = new int[game.getNumIssues()];
		int[] agent = new int[game.getNumIssues()];
		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = o.getItem(issue)[1];
			agent[issue] = o.getItem(issue)[0];
		}
		if(dummyVarianceAgentOffers.getFullSize() % 2 == 0){
			//プレイヤーに全振り
			for (int i = 0; i < game.getNumIssues(); i++) {
				int[] init = {0, free[i], game.getIssueQuants()[i] - free[i]};
				p.setItem(i, init);
			}
		}
		else{
			//エージェントにも少し分ける
			int	rand = new Random().nextInt(game.getNumIssues());
			int randNum = 1;

			for (int i = 0; i < game.getNumIssues(); i++) {
				int[] init = {agent[i] != 0 && i == rand ? game.getIssueQuants()[i] - free[i] - min(agent[i], randNum) : game.getIssueQuants()[i] - free[i], free[i], agent[i] != 0 && i == rand ? min(agent[i], randNum) : 0};
				p.setItem(i, init);
			}
		}
		dummyVarianceAgentOffers.enQueue(p);
	}
*/
	/*
	public void addDummyUndefinedOffer(){
		Offer p = new Offer(game.getNumIssues());
		for(int issue = 0; issue < game.getNumIssues(); issue++){
			int[] init = {dummyUndefinedOffers.getFullSize() % 2 == 0 ? 0 : game.getIssueQuants()[issue], dummyUndefinedOffers.getFullSize() % 2 == 0 ? game.getIssueQuants()[issue] : 0, 0};
			p.setItem(issue, init);
		}
		dummyUndefinedOffers.enQueue(p);
	}
	*/

	public void addDummyVarianceOffer(Offer o){
		Offer p = new Offer(game.getNumIssues());
		//未定義のアイテムをissueごとに把握
		int[] free = new int[game.getNumIssues()];
		int[] player = new int[game.getNumIssues()];
		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = o.getItem(issue)[1];
			player[issue] = o.getItem(issue)[2];
		}
		if(dummyVarianceOffers.getFullSize() % 2 == 0){
			//プレイヤーに全振り
			for (int i = 0; i < game.getNumIssues(); i++) {
				int[] init = {0, free[i], game.getIssueQuants()[i] - free[i]};
				p.setItem(i, init);
			}
		}
		else{
			//エージェントにも少し分ける
			int	rand = new Random().nextInt(game.getNumIssues());
			int randNum = 1;

			for (int i = 0; i < game.getNumIssues(); i++) {
				int[] init = {player[i] != 0 && i == rand ? min(player[i], randNum) : 0, free[i], player[i] != 0 && i == rand ? game.getIssueQuants()[i] - free[i] - min(player[i], randNum) : game.getIssueQuants()[i] - free[i]};
				p.setItem(i, init);
			}
		}
		/*
		//未定義のアイテムをissueごとに把握
		int[] free = new int[game.getNumIssues()];
		int[] agent = new int[game.getNumIssues()];
		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = o.getItem(issue)[1];
			agent[issue] = o.getItem(issue)[2];
		}
		//プレイヤーに全振り
		for (int i = 0; i < game.getNumIssues(); i++) {
			int[] init = {0, free[i], game.getIssueQuants()[i] - free[i]};
			p.setItem(i, init);
		}
		*/
		dummyVarianceOffers.enQueue(p);
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
		double niceConcession = niceNum.sum() / previousOffers.getFullSize() + concessionNum.sum() / previousOffers.getFullSize();
		double neuroOfferPoint = normarize(niceConcession, max(RATE_MAX, niceConcession), 0.0);
		double negEmotionPoint = emoFlag ? normarize(negEmotionNum.sum(), max(EMO_MAX, negEmotionNum.sum()), 0.0) : 0.0;
		double negMessagePoint = messageFlag ? normarize(negMessageNum.sum(), max(MES_MAX, negMessageNum.sum()), 0.0) : 0.0;
		double emotionVariance = calcEmotionVariance(emotionQueue.getPreQueue()) * BEHAVIOR_PRE_WEIGHT + calcEmotionVariance(emotionQueue.getQueue());
		double dummyEmotionVariance = calcEmotionVariance(dummyEmotions.getPreQueue()) * BEHAVIOR_PRE_WEIGHT + calcEmotionVariance(dummyEmotions.getQueue());
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
		double selfishFortunate = selfishNum.sum() / previousOffers.getFullSize() + fortunateNum.sum() / previousOffers.getFullSize();
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
		double choicePlayerVariance = calcChoiceVariance(previousOffers.getPreQueue(), 2, false) * OFFER_PRE_WEIGHT + calcChoiceVariance(previousOffers.getQueue(), 2, false);
		//double choiceOpponentVariance = calcChoiceVariance(previousOffers.getPreQueue(), 0, false) * OFFER_PRE_WEIGHT + calcChoiceVariance(previousOffers.getQueue(), 0, false);
		double choiceDummyPlayerVariance = calcChoiceVariance(dummyVarianceOffers.getPreQueue(), 2, true) * OFFER_PRE_WEIGHT + calcChoiceVariance(dummyVarianceOffers.getQueue(), 2, true);
		//double choiceDummyOpponentVariance = calcChoiceVariance(dummyVarianceAgentOffers.getPreQueue(), 0, true) * OFFER_PRE_WEIGHT + calcChoiceVariance(dummyVarianceAgentOffers.getQueue(), 0, true);
		//double choiceVariancePoint = max(normarize(choicePlayerVariance, max(CHOICE_VARIANCE_MAX_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance), min(CHOICE_VARIANCE_MIN_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance)), normarize(choiceOpponentVariance, max(CHOICE_VARIANCE_MAX_WEIGHT * choiceDummyOpponentVariance, choiceOpponentVariance), min(CHOICE_VARIANCE_MIN_WEIGHT * choiceDummyOpponentVariance, choiceOpponentVariance)));
		double choiceVariancePoint = normarize(choicePlayerVariance, max(CHOICE_VARIANCE_MAX_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance), min(CHOICE_VARIANCE_MIN_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance));

		double askPoint = normarize(preferenceAskNum.sum(), max(maxAskNum(), preferenceAskNum.sum()), 0.0);
		double requestPoint = normarize(prefRequestNum, max(SPECIAL_MES_MAX, prefRequestNum), -SPECIAL_MES_MAX);
		double batnaAskPoint = normarize(batnaAskNum, max(SPECIAL_MES_MAX * 2, batnaAskNum), -SPECIAL_MES_MAX * 2);
		double openBehaviorPoint = askPoint + requestPoint + batnaAskPoint;
		if(abs(openBehaviorPoint) > 1.0){
			openBehaviorPoint = signum(openBehaviorPoint);
		}

		ServletUtils.log("**************************", ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("player choice: " + choicePlayerVariance, ServletUtils.DebugLevels.DEBUG);
		//ServletUtils.log("opponent choice: " + choiceOpponentVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy player choice: " + choiceDummyPlayerVariance, ServletUtils.DebugLevels.DEBUG);
		//ServletUtils.log("dummy opponent choice: " + choiceDummyOpponentVariance, ServletUtils.DebugLevels.DEBUG);
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
		double variance = calcVariance(previousOffers, false);
		double dummyVariance = calcVariance(dummyPlayerOffers, true);
		double variancePoint = -normarize(variance, max(VARIANCE_MAX_WEIGHT * dummyVariance, variance), min(VARIANCE_MIN_WEIGHT * dummyVariance, variance));
		double undefined = calcUndefinedRate(previousOffers.getPreQueue()) * OFFER_PRE_WEIGHT + calcUndefinedRate(previousOffers.getQueue());
		double undefinedRatePoint = -normarize(undefined, max(MAX_WEIGHT * calcMaxUndefinedNum(), undefined), min(0.0, undefined));
		Offer o = previousOffers.getAllArray().get(0);
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
		double humanUtil = this.utils.adversaryValue(previousOffers.getAllArray().get(0), this.utils.getMinimaxOrdering());
		double agentUtil = this.utils.myActualOfferValue(previousOffers.getAllArray().get(0));
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
		ServletUtils.log("accept: " + acceptNum.sum(), ServletUtils.DebugLevels.DEBUG);
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
	public double calcChoiceVariance(ArrayList<Offer> offers, int userNum, boolean isDummy){
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
			if(isDummy) {
				variances += 1 / (variances + 0.05);
				if(calcMean(dummyPlayerOffers.getQueue()) > utils.getMaxPossiblePoints() * 0.75)
					variances = max(variances - offers.size() / 2, 0.0);
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
		offerRatio = (double)previousOffers.getFullSize() / behaviorTimings.getFullSize();
		behaviorRatio = (double)(behaviorTimings.getFullSize() - previousOffers.getFullSize()) / behaviorTimings.getFullSize();
	}

	public void addRound(){ this.t += 1; }

	public void setPrevious(Offer o){ this.previous = o; }

	public void addPreviousOffer(){
		if(previousOffers.queueSize() == 0){
			this.fastBehaviorNum = behaviorTimings.getFullSize();
		}
		this.previousOffers.enQueue(this.previous);
		//addDummyVarianceAgentOffer(this.previous);
		addDummyPlayerOffer(this.previous);
		addDummyVarianceOffer(this.previous);
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
		this.myPrevious = new ArrayList<>();
		this.previousOffers = new QueueList<Offer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.dummyPlayerOffers = new QueueList<Offer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		//this.dummyVarianceAgentOffers = new QueueList<Offer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.dummyVarianceOffers = new QueueList<Offer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.dummyEmotions = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.dummyVarianceOffers.enQueue(allocated);
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
		this.preferenceAskNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.preferenceExpressionNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.preEmotion = 1;
		this.lieNum = 0;
		this.threatNum = 0;
		this.prefRequestNum = 0;
		this.favorReturnNum = 0;
		this.timingThreshold = 3;
		this.batnaAskNum = 0;
		this.batnaExpressionNum = 0;
		this.posMessageNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.negMessageNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.posEmotionNum = new QueueList<Double>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.negEmotionNum = new QueueList<Double>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.emotionQueue = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.acceptNum = new QueueList<Integer>(BEH_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.behaviorTimings = new QueueList<Integer>(BEH_SIZE + OFF_SIZE, BEHAVIOR_PRE_WEIGHT);
		this.neuroticism = new ArrayList<Double>();
		this.extraversion = new ArrayList<Double>();
		this.openness = new ArrayList<Double>();
		this.conscientiousness = new ArrayList<Double>();
		this.agreeableness = new ArrayList<Double>();
		//behavior sence関連
		this.silentNum = new QueueList<Integer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.niceNum = new QueueList<Integer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.fortunateNum = new QueueList<Integer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.selfishNum = new QueueList<Integer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.concessionNum = new QueueList<Integer>(OFF_SIZE, OFFER_PRE_WEIGHT);
		this.unfortunateNum = new QueueList<Integer>(OFF_SIZE, OFFER_PRE_WEIGHT);
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

		addMyPrevious(propose);

		return propose;
	}

	public Offer getFirstOffer(){
		int[] free = new int[game.getNumIssues()];
		ArrayList<Integer> order = utils.getMyOrdering();
		ArrayList<Integer> index = utils.getMyOrdering();
		Collections.sort(index);
		Offer o = new Offer(game.getNumIssues());

		for(int issue = 0; issue < game.getNumIssues(); issue++) {
			free[issue] = allocated.getItem(issue)[1];
		}

		do {
			o.setItem(order.indexOf(index.get(0)), new int[]{o.getItem(order.indexOf(index.get(0)))[0] + 1, 0, 0});
			free[order.indexOf(index.get(0))]--;
			if(free[order.indexOf(index.get(0))] == 0){
				index.remove(0);
			}

		}while(((double)this.utils.myActualOfferValue(concession) * 0.75 > (double)this.utils.myActualOfferValue(o)));

		for(int i = 0; i < game.getNumIssues(); i++){
			o.setItem(i, new int[]{o.getItem(i)[0], 0, free[i]});
		}


		addMyPrevious(o);

		return o;
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

		addMyPrevious(propose);

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
