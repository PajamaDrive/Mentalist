package Mentalist.agent;

import Mentalist.utils.ExpressionPolicy;

public abstract class MentalistCoreExpression implements ExpressionPolicy
{
	protected abstract String getSemiFairEmotion();
	
	protected abstract String getFairEmotion();
	
	protected abstract String getUnfairEmotion();
}
