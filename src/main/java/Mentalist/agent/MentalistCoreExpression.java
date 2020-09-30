package Mentalist.agent;

import edu.usc.ict.iago.utils.ExpressionPolicy;

public abstract class MentalistCoreExpression implements ExpressionPolicy
{
	protected abstract String getSemiFairEmotion();
	
	protected abstract String getFairEmotion();
	
	protected abstract String getUnfairEmotion();
}
