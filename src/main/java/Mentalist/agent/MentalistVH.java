package Mentalist.agent;

import Mentalist.utils.GameSpec;

import javax.websocket.Session;


/**
 * @author mell
 * 
 */
public class MentalistVH extends MentalistCoreVH {

	/**
	 * @author mell
	 * Instantiates a new  VH.
	 *
	 * @param name: agent's name
	 * @param game: gamespec value
	 * @param session: the session
	 */
	public MentalistVH(String name, GameSpec game, Session session)
	{
		super("Mentalist", game, session, new MentalistRepeatedFavorBehavior(MentalistRepeatedFavorBehavior.LedgerBehavior.LIMITED), new MentalistRepeatedFavorExpression(),
				new MentalistRepeatedFavorMessage(false, false, MentalistRepeatedFavorBehavior.LedgerBehavior.FAIR));
		
		super.safeForMultiAgent = true;
	}

	@Override
	public String getArtName() {
		return "Rens";
	}

	@Override
	public String agentDescription() {
			return "<h1>Opponent</h1><p>They are excited to begin negotiating!</p>";
	}
}