package Mentalist.agent;

import Mentalist.utils.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class PilotStudyBehavior extends MentalistCoreBehavior implements BehaviorPolicy {

	private MentalistAgentUtilsExtension utils;
	private GameSpec game;
	private Offer allocated;
	private Offer concession;
	private Offer previous;
	private ArrayList<Offer> previousOffers;
	private LedgerBehavior lb = LedgerBehavior.NONE;
	private int adverseEvents = 0;
	//target function関連
	private double gamma_min;
	private double gamma_max;
	private int n;
	private int t;
	private double alpha;
	private final double ALPHA_INCREASE = 0.1;

	public enum LedgerBehavior
	{
		FAIR,
		LIMITED,
		BETRAYING,
		NONE;
	}

	public PilotStudyBehavior(LedgerBehavior lb)
	{
		super();
		this.lb = lb;
	}

	//譲歩関数
	public double getConcessionValue(){
		Offer maxOffer = makeMaxAgentDummyOffer(previous);
		return (gamma_min + (gamma_max - gamma_min) * max(0.0 ,(1 - max(0.0, ((double)this.utils.myActualOfferValue(previous) / this.utils.myActualOfferValue(maxOffer))) * pow((double)t / n,(1 / alpha))))) * this.utils.myActualOfferValue(maxOffer);
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
		setConcessionParameter();
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

	public void addRound(){ this.t += 1; }

	public void setPrevious(Offer o){ this.previous = o; }

	public void addPreviousOffer(){
		this.previousOffers.add(this.previous);
	}


	//譲歩関数のパラメータを特性によって変更
	public void setConcessionParameter(){
		double mean = 0.0;
		double variance = 0.0;
		if(previousOffers.size() > 0){
			mean = calcMean(previousOffers);
			variance = calcVariance(previousOffers);
		}
		double utility = this.utils.adversaryValue(previous, this.utils.getMinimaxOrdering());
		double sigma = abs(utility - mean);
		if((utility == mean && variance == sigma)||(utility < mean && variance < sigma))
			this.alpha = min(0.91, alpha + ALPHA_INCREASE);
	}

	//未定義の数を計算
	public int calcUndefinedNum(Offer o){
		int num = 0;
		for(int i = 0; i < game.getNumIssues(); i++){
			num += o.getItem(i)[1];
		}

		return num;
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
		this.previousOffers = new ArrayList<Offer>();
		//target function関連
		this.gamma_min = 0.3;
		this.gamma_max = 1.0;
		this.n = 15;
		this.t = 1;
		this.alpha = 0.01;
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

}
