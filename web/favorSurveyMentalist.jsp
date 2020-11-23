<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<meta charset="UTF-8">
<link href="css/iago.css" rel="stylesheet">

<div class="modal fade" id=surveyModalDialog tabindex="-1" data-backdrop="static" data-keyboard="false" role="dialog" aria-labelledby="surveyModalDialogLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    <span class="langChange" lang="en">Survey</span>
                    <span class="hidden langChange" lang="jp">アンケート</span>
                </h5>
            </div>
            <div class="modal-body">
                <input type="radio" id="radio_en" name="lang" value="0" checked="checked" onclick="radioChecked()">
                <label for="radio_en">
                    <span class="langChange" lang="en">English</span>
                    <span class="hidden langChange" lang="jp">英語</span>
                </label>
                <input type="radio" id="radio_jp" name="lang" value="1" onclick="radioChecked()">
                <label for="radio_jp">
                    <span class="langChange" lang="en">Japanese</span>
                    <span class="hidden langChange" lang="jp">日本語</span>
                </label>
                <div class="survey instructions">
                    <span class="langChange" lang="en">
                        Please answer these short few questions before continuing to the next game. Your partner will NOT see your answers.<br>
                        Press the submit button when you're finished making your selections.
                    </span>
                    <span class="hidden langChange" lang="jp">
                        次の交渉に進む前に以下の質問に答えてください．
                        交渉相手があなたの回答を見ることはありません．<br>
                        回答が終わったら送信ボタンを押してください．
                    </span>
                </div>
                <br>
                <div class="iagoSurvey">
                    <div class="questionArea" id="1">
                        <span class="langChange survey-question" lang="en">(1) How much do you like your opponent?</span>
                        <span class="hidden langChange survey-question" lang="jp">(1) 交渉相手に対する好感度はどのくらいでしたか?</span><br>
                        <span class="langChange survey-label" lang="en"><label>Strongly dislike</label><label>Somewhat dislike</label><label>Moderately</label><label>Somewhat like</label><label>Strongly like</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>すごく嫌い</label><label>やや嫌い</label><label>ふつう</label><label>やや良い</label><label>すごく良い</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q1_like" id="like1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q1_like" id="like2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q1_like" id="like3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q1_like" id="like4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q1_like" id="like5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>
                    <br>

                    <div class="questionArea" id="2">
                        <span class="langChange survey-question" lang="en">(2) How satisfied were you with the outcome of negotiation?</span>
                        <span class="hidden langChange survey-question" lang="jp">(2) 交渉の結果にどれだけ満足しましたか?</span><br>
                        <span class="langChange survey-label" lang="en"><label>Not at all</label><label>Somewhat unsatisfied</label><label>Moderately</label><label>Somewhat satisfied</label><label>Extremely</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く満足していない</label><label>やや満足していない</label><label>ふつう</label><label>やや満足した</label><label>すごく満足した</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q2_satisfy" id="satisfy1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q2_satisfy" id="satisfy2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q2_satisfy" id="satisfy3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q2_satisfy" id="satisfy4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q2_satisfy" id="satisfy5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>
                    <br>

                    <div class="questionArea" id="3">
                        <span class="langChange survey-question" lang="en">(3) How satisfied do you think the OTHER negotiator was the outcome of negotiation?</span>
                        <span class="hidden langChange survey-question" lang="jp">(3) 交渉相手は交渉の結果にどれだけ満足していると思いますか?</span><br>
                        <span class="langChange survey-label" lang="en"><label>Not at all</label><label>Somewhat unsatisfied</label><label>Moderately</label><label>Somewhat satisfied</label><label>Extremely</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く満足していない</label><label>やや満足していない</label><label>ふつう</label><label>やや満足した</label><label>すごく満足した</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q3_satisfyOther" id="satisfyOther1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q3_satisfyOther" id="satisfyOther2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q3_satisfyOther" id="satisfyOther3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q3_satisfyOther" id="satisfyOther4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q3_satisfyOther" id="satisfyOther5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>
                    <br>

                    <div class="questionArea" id="0">
                        <span class="langChange survey-question" lang="en">(4) What was your impression of your opponent?</span>
                        <span class="hidden langChange survey-question" lang="jp">(4) 相手の印象はどうでしたか?</span><br>
                    </div>
                    <div class="questionArea" id="4">
                        <span class="langChange survey-label" lang="en"><label>Uncooperative</label><label>Somewhat uncooperative</label><label>Moderately</label><label>Somewhat cooperative</label><label>Cooperative</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く協調的ではない</label><label>やや協調的ではない</label><label>ふつう</label><label>やや協調的だ</label><label>すごく協調的だ</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q4_coopOpp" id="coopOpp1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q4_coopOpp" id="coopOpp2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q4_coopOpp" id="coopOpp3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q4_coopOpp" id="coopOpp4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q4_coopOpp" id="coopOpp5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>

                    <div class="questionArea" id="5">
                        <span class="langChange survey-label" lang="en"><label>Unfriendly</label><label>Somewhat unfriendly</label><label>Moderately</label><label>Somewhat friendly</label><label>Friendly</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く友好的ではない</label><label>やや友好的ではない</label><label>ふつう</label><label>やや友好的だ</label><label>すごく友好的だ</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q5_friendOpp" id="friendOpp1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q5_friendOpp" id="friendOpp2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q5_friendOpp" id="friendOpp3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q5_friendOpp" id="friendOpp4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q5_friendOpp" id="friendOpp5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>

                    <div class="questionArea" id="6">
                        <span class="langChange survey-label" lang="en"><label>Untrustworthy</label><label>Somewhat untrustworthy</label><label>Moderately</label><label>Somewhat trustworthy</label><label>Trustworthy</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く信頼できない</label><label>やや信頼できない</label><label>ふつう</label><label>やや信頼できる</label><label>すごく信頼できる</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q6_trustOpp" id="trustOpp1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q6_trustOpp" id="trustOpp2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q6_trustOpp" id="trustOpp3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q6_trustOpp" id="trustOpp4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q6_trustOpp" id="trustOpp5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>

                    <div class="questionArea" id="7">
                        <span class="langChange survey-label" lang="en"><label>Short-term focused</label><label>Somewhat short-term focused</label><label>Moderately</label><label>Somewhat long-term focused</label><label>Long-term focused</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>短期的な関係を前提<br>とした行動だ</label><label>やや短期的な関係を<br>前提とした行動だ</label><label>ふつう</label><label>やや長期的な関係を<br>見据えた行動だ</label><label>長期的な関係を<br>見据えた行動だ</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q7_longOpp" id="longOpp1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q7_longOpp" id="longOpp2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q7_longOpp" id="longOpp3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q7_longOpp" id="longOpp4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q7_longOpp" id="longOpp5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>

                    <div class="questionArea" id="8">
                        <span class="langChange survey-label" lang="en"><label>Selfish</label><label>Somewhat selfish</label><label>Moderately</label><label>Somewhat fair</label><label>Fair</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>自分勝手だ</label><label>やや自分勝手だ</label><label>ふつう</label><label>やや公平だ</label><label>公平だ</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q8_fairOpp" id="fairOpp1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q8_fairOpp" id="fairOpp2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q8_fairOpp" id="fairOpp3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q8_fairOpp" id="fairOpp4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q8_fairOpp" id="fairOpp5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>

                    <div class="questionArea" id="9">
                        <span class="langChange survey-label" lang="en"><label>Dishonest</label><label>Somewhat dishonest</label><label>Moderately</label><label>Somewhat honest</label><label>Honest</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く誠実ではなかった</label><label>やや誠実ではなかった</label><label>ふつう</label><label>やや誠実だった</label><label>誠実だった</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q9_honestOpp" id="honestOpp1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q9_honestOpp" id="honestOpp2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q9_honestOpp" id="honestOpp3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q9_honestOpp" id="honestOpp4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q9_honestOpp" id="honestOpp5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>
                    <br>

                    <div class="survey instructions">
                    <span class="langChange" lang="en">
                        Remember that the other negotiator was a computer program, or Artificial Intelligence.<br>
                        Please rate, on a scale from “Totally realistic” to “Not realistic at all”, how you felt the program acted.
                    </span>
                        <span class="hidden langChange" lang="jp">
                        交渉相手はプログラムでした．<br>
                        あなたがプログラムの行動をどのように感じたか"すごく現実的"から"全く現実的ではない"の中から選択してください．
                    </span>
                    </div>
                    <br>

                    <div class="questionArea" id="10">
                        <span class="langChange survey-question" lang="en">(5) How do you think that the way the other negotiator expressed emotions?</span>
                        <span class="hidden langChange survey-question" lang="jp">(5) 交渉相手の感情はどうでしたか?</span><br>
                        <span class="langChange survey-label" lang="en"><label>Not realistic at all</label><label>Somewhat unrealistic</label><label>Moderately</label><label>Somewhat realistic</label><label>Totally realistic</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く現実的ではない</label><label>やや非現実的</label><label>どちらともいえない</label><label>やや現実的</label><label>すごく現実的</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q10_emotionOther" id="emotionOther1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q10_emotionOther" id="emotionOther2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q10_emotionOther" id="emotionOther3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q10_emotionOther" id="emotionOther4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q10_emotionOther" id="emotionOther5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>
                    <br>

                    <div class="questionArea" id="11">
                        <span class="langChange survey-question" lang="en">(6) How do you think that the way the other negotiator sent messages?</span>
                        <span class="hidden langChange survey-question" lang="jp">(6) 交渉相手のメッセージはどうでしたか?</span><br>
                        <span class="langChange survey-label" lang="en"><label>Not realistic at all</label><label>Somewhat unrealistic</label><label>Moderately</label><label>Somewhat realistic</label><label>Totally realistic</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く現実的ではない</label><label>やや非現実的</label><label>どちらともいえない</label><label>やや現実的</label><label>すごく現実的</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q11_messageOther" id="messageOther1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q11_messageOther" id="messageOther2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q11_messageOther" id="messageOther3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q11_messageOther" id="messageOther4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q11_messageOther" id="messageOther5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>
                    <br>

                    <div class="questionArea" id="12">
                        <span class="langChange survey-question" lang="en">(7) How do you think that the way the other negotiator sent offers?</span>
                        <span class="hidden langChange survey-question" lang="jp">(7) 交渉相手の提案内容はどうでしたか?</span><br>
                        <span class="langChange survey-label" lang="en"><label>Not realistic at all</label><label>Somewhat unrealistic</label><label>Moderately</label><label>Somewhat realistic</label><label>Totally realistic</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く現実的ではない</label><label>やや非現実的</label><label>どちらともいえない</label><label>やや現実的</label><label>すごく現実的</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q12_offerOther" id="offerOther1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q12_offerOther" id="offerOther2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q12_offerOther" id="offerOther3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q12_offerOther" id="offerOther4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q12_offerOther" id="offerOther5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>
                    <br>

                    <div class="questionArea" id="13">
                        <span class="langChange survey-question" lang="en">(8) How do you think that the other negotiator's behavior in general?</span>
                        <span class="hidden langChange survey-question" lang="jp">(8) 交渉相手の行動は全体的にどうでしたか?</span><br>
                        <span class="langChange survey-label" lang="en"><label>Not realistic at all</label><label>Somewhat unrealistic</label><label>Moderately</label><label>Somewhat realistic</label><label>Totally realistic</label></span>
                        <span class="hidden langChange survey-label" lang="jp"><label>全く現実的ではない</label><label>やや非現実的</label><label>どちらともいえない</label><label>やや現実的</label><label>すごく現実的</label></span>
                        <div class="survey-form-item">
                            <input class="iagoCheckableQuestion" type="radio" name="q13_generalOther" id="generalOther1" value="1" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q13_generalOther" id="generalOther2" value="2" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q13_generalOther" id="generalOther3" value="3" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q13_generalOther" id="generalOther4" value="4" onclick="radioChecked()">
                            <input class="iagoCheckableQuestion" type="radio" name="q13_generalOther" id="generalOther5" value="5" onclick="radioChecked()">
                        </div>
                    </div>
                    <br>
                    <br>
                </div>
            <div class="modal-footer">
                <div class="hidden" id="warning">
                    <strong>
                        <span class="langChange" lang="en">Please answer all questions.</span>
                        <span class="hidden langChange" lang="jp">すべての設問に回答してください.</span>
                    </strong>
                </div>

                <button type="button" id="surveySubmit" class="btn btn-primary" onclick="submitAll()">
                <label for="surveySubmit">
                    <span class="langChange" lang="en">Submit</span>
                    <span class="hidden langChange" lang="jp">送信</span>
                </label>
            </div>
            </div>
        </div>
    </div>
</div>