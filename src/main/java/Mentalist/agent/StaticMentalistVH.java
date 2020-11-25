package Mentalist.agent;

import Mentalist.utils.GameSpec;

import javax.websocket.Session;


/**
 * @author mell
 * 
 */
public class StaticMentalistVH extends MentalistCoreVH {

	/**
	 * @author mell
	 * Instantiates a new  VH.
	 *
	 * @param name: agent's name
	 * @param game: gamespec value
	 * @param session: the session
	 */
	public StaticMentalistVH(String name, GameSpec game, Session session, int neuroticism, int extraversion, int openness, int conscientiousness, int agreeableness)
	{
		super("StaticMentalist", game, session, new StaticMentalistBehavior(StaticMentalistBehavior.LedgerBehavior.LIMITED, neuroticism, extraversion, openness, conscientiousness, agreeableness), new MentalistRepeatedFavorExpression(),
				new MentalistRepeatedFavorMessage(false, false, StaticMentalistBehavior.LedgerBehavior.FAIR));
		
		super.safeForMultiAgent = true;
	}

	@Override
	public String getArtName() {
		return "Emilie";
	}

	@Override
	public String agentDescription() {
			return "<h1>Opponent</h1><p>They are excited to begin negotiating!</p>";
	}
}