package Mentalist.agent;

import Mentalist.utils.GameSpec;

import javax.websocket.Session;


/**
 * @author mell
 * 
 */
public class PilotStudyVH extends MentalistCoreVH {

	/**
	 * @author mell
	 * Instantiates a new  VH.
	 *
	 * @param name: agent's name
	 * @param game: gamespec value
	 * @param session: the session
	 */
	public PilotStudyVH(String name, GameSpec game, Session session)
	{
		super("PilotStudy", game, session, new PilotStudyBehavior(PilotStudyBehavior.LedgerBehavior.FAIR), new PilotStudyRepeatedFavorExpression(),
				new PilotStudyRepeatedFavorMessage(false, false, PilotStudyBehavior.LedgerBehavior.FAIR));
		
		super.safeForMultiAgent = true;
	}

	@Override
	public String getArtName() {
		return "Brad";
	}

	@Override
	public String agentDescription() {
			return "<h1>Opponent</h1><p>They are excited to begin negotiating!</p>";
	}
}