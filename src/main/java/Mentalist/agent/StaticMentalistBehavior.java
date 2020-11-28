package Mentalist.agent;

import Mentalist.utils.ServletUtils;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class StaticMentalistBehavior extends MentalistRepeatedFavorBehavior {
    public StaticMentalistBehavior(LedgerBehavior lb, int neuroticism, int extraversion, int openness, int conscientiousness, int agreeableness)
    {
        super(lb);
        this.neuroticism.add(normarize(neuroticism, 50.0, 10.0));
        this.extraversion.add(normarize(extraversion, 50.0, 10.0));
        this.openness.add(normarize(openness, 50.0, 10.0));
        this.conscientiousness.add(normarize(conscientiousness, 50.0, 10.0));
        this.agreeableness.add(normarize(agreeableness, 50.0, 10.0));
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
    }

    /**** ビッグファイブ経験への開放性 ****/
    public void calcOpenness(){
        double choicePlayerVariance = calcChoiceVariance(previousOffers.getPreQueue(), 2, false) * OFFER_PRE_WEIGHT + calcChoiceVariance(previousOffers.getQueue(), 2, false);
        double choiceDummyPlayerVariance = calcChoiceVariance(dummyVarianceOffers.getPreQueue(), 2, false) * OFFER_PRE_WEIGHT + calcChoiceVariance(dummyVarianceOffers.getQueue(), 2, true);
        double openOfferPoint = normarize(choicePlayerVariance, max(CHOICE_VARIANCE_MAX_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance), min(CHOICE_VARIANCE_MIN_WEIGHT * choiceDummyPlayerVariance, choicePlayerVariance));

        double openBehavior = preferenceAskNum.sum() + prefRequestNum + batnaAskNum;
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
        double conscientBehavior = preferenceExpressionNum.sum() + batnaExpressionNum;
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
    }
}
