package Mentalist.agent;

import Mentalist.utils.*;
import Mentalist.agent.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class QuestionnaireMentalistBehavior extends MentalistCoreBehavior implements BehaviorPolicy {

	private MentalistAgentUtilsExtension utils;
	private GameSpec game;
	private Offer allocated;
	private Offer concession;
	private Offer previous;
	private QueueList<Offer> previousOffers;
	private QueueList<Offer> dummyPlayerOffers;
	private QueueList<Offer> dummyAgentOffers;
	private QueueList<Offer> dummyUndefinedOffers;
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
	private int timingThreshold;
	private QueueList<Integer> behaviorTimings;
	private int neuroticism;
	private int extraversion;
	private int openness;
	private int conscientiousness;
	private int agreeableness;
	//その他
	private double offerRatio;
	private double behaviorRatio;
	private final double MAX = 1.0;
	private final double MIN = -1.0;
	private final int BEH_SIZE = 10;
	private final int OFF_SIZE = 5;
	private final double PRE_WEIGHT = 0.3;
	private final double UTILITY_MIN_WEIGHT = 0.3;
	private final double UTILITY_MAX_WEIGHT = 0.9;
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

	public QuestionnaireMentalistBehavior(LedgerBehavior lb, int neuroticism, int extraversion, int openness, int conscientiousness, int agreeableness)
	{
		super();
		this.lb = lb;
		this.neuroticism = neuroticism;
		this.extraversion = extraversion;
		this.openness = openness;
		this.conscientiousness = conscientiousness;
		this.agreeableness = agreeableness;
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

		calcCooperativeness();
		calcAssertiveness();
		/*
		calcAgreeableness();
		calcNeuroticism();
		calcExtraversion();
		calcOpenness();
		calcConscientiousness();
		*/
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
		double neuroticismRate = normarize(neuroticism, 50.0, 10.0, 1.0, -1.0);
		double extraversionRate = normarize(extraversion, 50.0, 10.0, 1.0, -1.0);
		double opennessRate = normarize(openness, 50.0, 10.0, 1.0, -1.0);
		double conscientiousnessRate = normarize(conscientiousness, 50.0, 10.0, 1.0, -1.0);
		double agreeablenessRate = normarize(agreeableness, 50.0, 10.0, 1.0, -1.0);

		ServletUtils.log("TKI-coop: " + cooperativeness.get(cooperativeness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("TKI-asse: " + assertiveness.get(assertiveness.size() - 1), ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Neu: " + neuroticismRate, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Ext: " + extraversionRate, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Opn: " + opennessRate, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Agr: " + agreeablenessRate, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("BIG5-Con: " + conscientiousnessRate, ServletUtils.DebugLevels.DEBUG);

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
		double mean = calcMean(previousOffers.getPreQueue()) * PRE_WEIGHT + calcMean(previousOffers.getQueue());
		double dummyMean = calcMean(dummyPlayerOffers.getPreQueue()) * PRE_WEIGHT + calcMean(dummyPlayerOffers.getQueue());
		double meanPoint = -normarize(mean, max(UTILITY_MAX_WEIGHT * dummyMean, mean), min(UTILITY_MIN_WEIGHT * dummyMean, mean));

		/*
		ServletUtils.log("mean: " + mean, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy mean: " + dummyMean, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("cooperative: " + meanPoint, ServletUtils.DebugLevels.DEBUG);
		*/

		this.cooperativeness.add(meanPoint);
	}

	/**** TKI積極性 ****/
	public void calcAssertiveness(){
		double variance = calcVariance(previousOffers.getPreQueue()) * PRE_WEIGHT + calcVariance(previousOffers.getQueue());
		double dummyVariance = calcVariance(dummyPlayerOffers.getPreQueue()) * PRE_WEIGHT + calcVariance(dummyPlayerOffers.getQueue());
		double variancePoint = -normarize(variance, max(UTILITY_MAX_WEIGHT * dummyVariance, variance), min(UTILITY_MIN_WEIGHT * dummyVariance, variance));

		/*
		ServletUtils.log("variance: " + variance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("dummy variance: " + dummyVariance, ServletUtils.DebugLevels.DEBUG);
		ServletUtils.log("assertive: " + variancePoint, ServletUtils.DebugLevels.DEBUG);
		*/

		this.assertiveness.add(variancePoint);
	}

	//未定義の数を計算
	public int calcUndefinedNum(Offer o){
		int num = 0;
		for(int i = 0; i < game.getNumIssues(); i++){
			num += o.getItem(i)[1];
		}

		return num;
	}


	//offerと行動の割合を計算
	public void calcWeight(){
		offerRatio = (double)previousOffers.getFullSize() / (behaviorTimings.getFullSize() + 1);
		behaviorRatio = (double)(behaviorTimings.getFullSize() - previousOffers.getFullSize()) / (behaviorTimings.getFullSize() + 1);
	}

	public void addRound(){ this.t += 1; }

	public void setPrevious(Offer o){ this.previous = o; }

	public void addPreviousOffer(){
		this.previousOffers.enQueue(this.previous);
		addDummyAgentOffer(this.previous);
		addDummyPlayerOffer(this.previous);
		addDummyUndefinedOffer();
	}

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
		double neuroticismRate = normarize(neuroticism, 50.0, 10.0, 1.0, 0.0);
		double extraversionRate = normarize(extraversion, 50.0, 10.0, 1.0, 0.0);
		double opennessRate = normarize(openness, 50.0, 10.0, 1.0, 0.0);
		double conscientiousnessRate = normarize(conscientiousness, 50.0, 10.0, 1.0, 0.0);
		double agreeablenessRate = normarize(agreeableness, 50.0, 10.0, 1.0, 0.0);

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
		this.timingThreshold = 3;
		this.behaviorTimings = new QueueList<Integer>(BEH_SIZE + OFF_SIZE, PRE_WEIGHT);

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
	public String getNeuroticism(){ return "questionnaire:" + neuroticism; }
	public String getExtraversion(){ return "questionnaire:" + extraversion; }
	public String getOpenness(){ return "questionnaire:" + openness; }
	public String getAgreeableness() { return "questionnaire:" + agreeableness; }
	public String getConscientiousness() { return "questionnaire:" + conscientiousness; }
}
