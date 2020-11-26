<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<link href="css/iago.css" rel="stylesheet">

<script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.11.1/jquery-ui.min.js"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css" />

<script src="js/start.js" type="text/javascript"></script>

<title>TUAT Experiment - Start</title>
</head>
<!-- NOTE: Much of the body text on these page appears to be repeated to account for both game conditions.  When users come from Qualtrics the correct text is loaded depending on their condition. -->
<body>
	<input type="radio" id="radio_en" name="lang" value="0" checked="checked">
	<label for="radio_en">
		<span class="langChange" lang="en">English</span>
		<span class="hidden langChange" lang="jp">英語</span>
	</label>
	<input type="radio" id="radio_jp" name="lang" value="1">
	<label for="radio_jp">
		<span class="langChange" lang="en">Japanese</span>
		<span class="hidden langChange" lang="jp">日本語</span>
	</label>
	<br>
	<hr>
	<div id="big5">
		<div id="questionnaire">
			<div id="big5Instruction">
				<p>
					<span class="langChange" lang="en">Please fill out the questionnaire. <strong>If you answered before</strong>, please enter your ID in the form below.</span>
					<span class="hidden langChange" lang="jp">事前アンケートにご協力ください．<strong>以前回答した方</strong>は以下のフォームにIDを入力してください．</span>
				</p>

				<form id="formBefore" target="_self" action="searching.jsp" method="POST">
					<div>
						<div class="hidden" id="beforeIDWarning">
							<strong>
								<span class="langChange" lang="en">Please enter a string consisting of only numbers.</span>
								<span class="hidden langChange" lang="jp">数値のみで構成される文字列を入力してください.</span>
							</strong>
						</div>
						<strong>Your ID: </strong><input id="beforeMTurkID" name="beforeMTurkID" type="text" value=""><br><br>
						<div class="buttonWrapper">
							<button id="beforeIDSubmitButton" type="button" value="Start"/>
							<label for="beforeIDSubmitButton">
								<span class="langChange" lang="en">Start</span>
								<span class="hidden langChange" lang="jp">スタート</span>
							</label>
						</div>
					</div>
				</form>
			</div>
            <hr>
            <br>

			<div class="questionArea" id="1">
				<span class="langChange" lang="en">1. Am the life of the party.</span><span class="hidden langChange" lang="jp">1. 盛り上げ役である</span><br>
				<input type="radio" name="q1_e" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q1_e" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q1_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q1_e" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q1_e" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="2">
				<span class="langChange" lang="en">2. Feel little concern for others.</span><span class="hidden langChange" lang="jp">2. 他人を気づかうことはない</span><br>
				<input type="radio" name="q2_a" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q2_a" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q2_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q2_a" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q2_a" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="3">
				<span class="langChange" lang="en">3. Am always prepared.</span><span class="hidden langChange" lang="jp">3. いつも用意周到である</span><br>
				<input type="radio" name="q3_c" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q3_c" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q3_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q3_c" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q3_c" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="4">
				<span class="langChange" lang="en">4. Get stressed out easily.</span><span class="hidden langChange" lang="jp">4. すぐにストレスがたまってしまう</span><br>
				<input type="radio" name="q4_n" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q4_n" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q4_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q4_n" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q4_n" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="5">
				<span class="langChange" lang="en">5. Have a rich vocabulary.</span><span class="hidden langChange" lang="jp">5. 語彙が豊富である</span><br>
				<input type="radio" name="q5_o" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q5_o" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q5_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q5_o" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q5_o" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="6">
				<span class="langChange" lang="en">6. Don't talk a lot.</span><span class="hidden langChange" lang="jp">6. おしゃべりではない</span><br>
				<input type="radio" name="q6_e" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q6_e" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q6_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q6_e" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q6_e" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="7">
				<span class="langChange" lang="en">7. Am interested in people.</span><span class="hidden langChange" lang="jp">7. 他人に興味がある</span><br>
				<input type="radio" name="q7_a" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q7_a" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q7_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q7_a" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q7_a" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="8">
				<span class="langChange" lang="en">8. Leave my belongings around.</span><span class="hidden langChange" lang="jp">8. 持ち物が整理できないほうだ</span><br>
				<input type="radio" name="q8_c" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q8_c" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q8_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q8_c" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q8_c" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="9">
				<span class="langChange" lang="en">9. Am relaxed most of the time.</span><span class="hidden langChange" lang="jp">9. いつもリラックスしていることが多い</span><br>
				<input type="radio" name="q9_n" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q9_n" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q9_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q9_n" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q9_n" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="10">
				<span class="langChange" lang="en">10. Have difficulty understanding abstract ideas.</span><span class="hidden langChange" lang="jp">10. 抽象的な考えを理解するのが苦手だ</span><br>
				<input type="radio" name="q10_o" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q10_o" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q10_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q10_o" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q10_o" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="11">
				<span class="langChange" lang="en">11. Feel comfortable around people.</span><span class="hidden langChange" lang="jp">11. 人前でもあがらない</span><br>
				<input type="radio" name="q11_e" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q11_e" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q11_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q11_e" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q11_e" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="12">
				<span class="langChange" lang="en">12. Insult people.</span><span class="hidden langChange" lang="jp">12. 人を馬鹿にするほうだ</span><br>
				<input type="radio" name="q12_a" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q12_a" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q12_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q12_a" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q12_a" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="13">
				<span class="langChange" lang="en">13. Pay attention to details.</span><span class="hidden langChange" lang="jp">13. 細かいことに気がつく</span><br>
				<input type="radio" name="q13_c" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q13_c" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q13_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q13_c" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q13_c" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="14">
				<span class="langChange" lang="en">14. Worry about things.</span><span class="hidden langChange" lang="jp">14. 心配性である</span><br>
				<input type="radio" name="q14_n" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q14_n" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q14_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q14_n" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q14_n" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="15">
				<span class="langChange" lang="en">15. Have a vivid imagination.</span><span class="hidden langChange" lang="jp">15. 想像力が豊かである</span><br>
				<input type="radio" name="q15_o" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q15_o" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q15_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q15_o" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q15_o" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="16">
				<span class="langChange" lang="en">16. Keep in the background.</span><span class="hidden langChange" lang="jp">16. 引っ込み思案である</span><br>
				<input type="radio" name="q16_e" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q16_e" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q16_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q16_e" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q16_e" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="17">
				<span class="langChange" lang="en">17. Sympathize with others' feelings.</span><span class="hidden langChange" lang="jp">17. 人に共感しやすい</span><br>
				<input type="radio" name="q17_a" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q17_a" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q17_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q17_a" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q17_a" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="18">
				<span class="langChange" lang="en">18. Make a mess of things.</span><span class="hidden langChange" lang="jp">18. 無茶なことをする</span><br>
				<input type="radio" name="q18_c" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q18_c" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q18_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q18_c" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q18_c" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="19">
				<span class="langChange" lang="en">19. Seldom feel blue.</span><span class="hidden langChange" lang="jp">19. 落ち込むことはめったにない</span><br>
				<input type="radio" name="q19_n" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q19_n" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q19_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q19_n" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q19_n" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="20">
				<span class="langChange" lang="en">20. Am not interested in abstract ideas.</span><span class="hidden langChange" lang="jp">20. 抽象的な考えには興味がない</span><br>
				<input type="radio" name="q20_o" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q20_o" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q20_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q20_o" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q20_o" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="21">
				<span class="langChange" lang="en">21. Start conversations.</span><span class="hidden langChange" lang="jp">21. 自分から話しかけるほうである</span><br>
				<input type="radio" name="q21_e" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q21_e" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q21_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q21_e" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q21_e" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="22">
				<span class="langChange" lang="en">22. Am not interested in other people's problems.</span><span class="hidden langChange" lang="jp">22. 他人の問題には興味がない</span><br>
				<input type="radio" name="q22_a" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q22_a" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q22_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q22_a" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q22_a" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="23">
				<span class="langChange" lang="en">23. Get chores done right away.</span><span class="hidden langChange" lang="jp">23. すぐに雑用を済ませる</span><br>
				<input type="radio" name="q23_c" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q23_c" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q23_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q23_c" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q23_c" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="24">
				<span class="langChange" lang="en">24. Am easily disturbed.</span><span class="hidden langChange" lang="jp">24. 動揺しやすい</span><br>
				<input type="radio" name="q24_n" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q24_n" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q24_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q24_n" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q24_n" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="25">
				<span class="langChange" lang="en">25. Have excellent ideas.</span><span class="hidden langChange" lang="jp">25. 素晴らしいアイディアを持っている</span><br>
				<input type="radio" name="q25_o" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q25_o" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q25_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q25_o" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q25_o" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="26">
				<span class="langChange" lang="en">26. Have little to say.</span><span class="hidden langChange" lang="jp">26. あまり話すことがない</span><br>
				<input type="radio" name="q26_e" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q26_e" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q26_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q26_e" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q26_e" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="27">
				<span class="langChange" lang="en">27. Have a soft heart.</span><span class="hidden langChange" lang="jp">27. 優しい心を持っている</span><br>
				<input type="radio" name="q27_a" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q27_a" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q27_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q27_a" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q27_a" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="28">
				<span class="langChange" lang="en">28. Often forget to put things back in their proper place.</span><span class="hidden langChange" lang="jp">28. 整理整頓を怠りがち</span><br>
				<input type="radio" name="q28_c" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q28_c" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q28_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q28_c" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q28_c" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="29">
				<span class="langChange" lang="en">29. Get upset easily.</span><span class="hidden langChange" lang="jp">29. 慌てやすい</span><br>
				<input type="radio" name="q29_n" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q29_n" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q29_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q29_n" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q29_n" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="30">
				<span class="langChange" lang="en">30. Do not have a good imagination.</span><span class="hidden langChange" lang="jp">30. アイディアが乏しいほうだ</span><br>
				<input type="radio" name="q30_o" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q30_o" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q30_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q30_o" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q30_o" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="31">
				<span class="langChange" lang="en">31. Talk to a lot of different people at parties.</span>
				<span class="hidden langChange" lang="jp">31. パーティでは色々な人と話すほうだ</span><br>
				<input type="radio" name="q31_e" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q31_e" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q31_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q31_e" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q31_e" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="32">
				<span class="langChange" lang="en">32. Am not really interested in others.</span><span class="hidden langChange" lang="jp">32. 他人には全く興味がない</span><br>
				<input type="radio" name="q32_a" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q32_a" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q32_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q32_a" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q32_a" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="33">
				<span class="langChange" lang="en">33. Like order.</span><span class="hidden langChange" lang="jp">33. 整頓するのが好きである</span><br>
				<input type="radio" name="q33_c" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q33_c" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q33_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q33_c" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q33_c" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="34">
				<span class="langChange" lang="en">34. Change my mood a lot.</span><span class="hidden langChange" lang="jp">34. 気分をコロコロ変える</span><br>
				<input type="radio" name="q34_n" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q34_n" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q34_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q34_n" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q34_n" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="35">
				<span class="langChange" lang="en">35. Am quick to understand things.</span><span class="hidden langChange" lang="jp">35. ものわかりが良いほうだ</span><br>
				<input type="radio" name="q35_o" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q35_o" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q35_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q35_o" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q35_o" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="36">
				<span class="langChange" lang="en">36. Don't like to draw attention to myself.</span><span class="hidden langChange" lang="jp">36. 人から注目を浴びるのは好きではない</span><br>
				<input type="radio" name="q36_e" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q36_e" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q36_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q36_e" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q36_e" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="37">
				<span class="langChange" lang="en">37. Take time out for others.</span><span class="hidden langChange" lang="jp">37. 他の人のために時間を割くほうだ</span><br>
				<input type="radio" name="q37_a" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q37_a" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q37_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q37_a" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q37_a" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="38">
				<span class="langChange" lang="en">38. Shirk my duties.</span><span class="hidden langChange" lang="jp">38. 仕事や学習をサボることが多い</span><br>
				<input type="radio" name="q38_c" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q38_c" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q38_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q38_c" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q38_c" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="39">
				<span class="langChange" lang="en">39. Have frequent mood swings.</span><span class="hidden langChange" lang="jp">39. 気分が著しく変化するほうだ</span><br>
				<input type="radio" name="q39_n" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q39_n" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q39_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q39_n" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q39_n" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="40">
				<span class="langChange" lang="en">40. Use difficult words.</span><span class="hidden langChange" lang="jp">40. 難しい言葉を使うほうだ</span><br>
				<input type="radio" name="q40_o" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q40_o" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q40_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q40_o" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q40_o" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="41">
				<span class="langChange" lang="en">41. Don't mind being the center of attention.</span><span class="hidden langChange" lang="jp">41. 注目の的になるのは嫌ではない</span><br>
				<input type="radio" name="q41_e" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q41_e" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q41_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q41_e" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q41_e" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="42">
				<span class="langChange" lang="en">42. Feel others' emotions.</span><span class="hidden langChange" lang="jp">42. 他の人の気持ちがわかる</span><br>
				<input type="radio" name="q42_a" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q42_a" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q42_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q42_a" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q42_a" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="43">
				<span class="langChange" lang="en">43. Follow a schedule.</span><span class="hidden langChange" lang="jp">43. 予定に従うほうだ</span><br>
				<input type="radio" name="q43_c" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q43_c" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q43_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q43_c" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q43_c" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="44">
				<span class="langChange" lang="en">44. Get irritated easily.</span><span class="hidden langChange" lang="jp">44. イライラしやすい</span><br>
				<input type="radio" name="q44_n" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q44_n" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q44_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q44_n" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q44_n" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="45">
				<span class="langChange" lang="en">45. Spend time reflecting on things.</span><span class="hidden langChange" lang="jp">45. いろんなことを反省しては時間を過ごす</span><br>
				<input type="radio" name="q45_o" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q45_o" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q45_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q45_o" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q45_o" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="46">
				<span class="langChange" lang="en">46. Am quiet around strangers.</span><span class="hidden langChange" lang="jp">46. 人見知りする</span><br>
				<input type="radio" name="q46_e" value="5"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q46_e" value="4"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q46_e" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q46_e" value="2"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q46_e" value="1"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="47">
				<span class="langChange" lang="en">47. Make people feel at ease.</span><span class="hidden langChange" lang="jp">47. 人を安心させる</span><br>
				<input type="radio" name="q47_a" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q47_a" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q47_a" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q47_a" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q47_a" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="48">
				<span class="langChange" lang="en">48. Am exacting in my work.</span><span class="hidden langChange" lang="jp">48. 張り切って仕事や学習に取り組むほうだ</span><br>
				<input type="radio" name="q48_c" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q48_c" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q48_c" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q48_c" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q48_c" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="49">
				<span class="langChange" lang="en">49. Often feel blue.</span><span class="hidden langChange" lang="jp">49. 落ち込むことが多い</span><br>
				<input type="radio" name="q49_n" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q49_n" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q49_n" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q49_n" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q49_n" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div class="questionArea" id="50">
				<span class="langChange" lang="en">50. Am full of ideas.</span><span class="hidden langChange" lang="jp">50. アイディアが豊富である</span><br>
				<input type="radio" name="q50_o" value="1"><label><span class="langChange" lang="en">Very Inaccurate</span><span class="hidden langChange" lang="jp">まったく当てはまらない</span></label>
				<input type="radio" name="q50_o" value="2"><label><span class="langChange" lang="en">Moderately Inaccurate</span><span class="hidden langChange" lang="jp">あまり当てはまらない</span></label>
				<input type="radio" name="q50_o" value="3"><label><span class="langChange" lang="en">Neither Accurate Nor Inaccurate</span><span class="hidden langChange" lang="jp">どちらともいえない</span></label>
				<input type="radio" name="q50_o" value="4"><label><span class="langChange" lang="en">Moderately Accurate</span><span class="hidden langChange" lang="jp">やや当てはまる</span></label>
				<input type="radio" name="q50_o" value="5"><label><span class="langChange" lang="en">Very Accurate</span><span class="hidden langChange" lang="jp">よく当てはまる</span></label>
				<br><br>
			</div>

			<div>
				<div class="hidden" id="warning">
					<strong>
						<span class="langChange" lang="en">Please answer all questions.</span>
						<span class="hidden langChange" lang="jp">すべての設問に回答してください.</span>
					</strong>
				</div>
				<div class="hidden" id="finish">
					<strong>
						<span class="langChange" lang="en">Please click the button at the bottom of the page.</span>
						<span class="hidden langChange" lang="jp">ページ下部のボタンをクリックしてください.</span>
					</strong>
				</div>
			</div>


			<div class="buttonWrapper">
				<button type="button" id="butQuestionnaireDone" class="questionnaireButton">
					<label for="butQuestionnaireDone">
						<span class="langChange" lang="en">Done</span>
						<span class="hidden langChange" lang="jp">終了</span>
					</label>
				</button>
			</div>
			<hr>
		</div>
	</div>
	<div class="hid" id="mainpage">
		<div class="welcome instructions">

		</div>
		<div class="reminder instructions">
			<p/>
			<span class="langChange" lang="en">
				Please read the instructions below. <strong>Pay special attention to the bold or <span class="attention">red</span> sections!</strong><br>
				The underlines correspond to <strong>the areas highlighted in the image</strong>.
			</span>
			<span class="hidden langChange" lang="jp">
				以下の説明を読んでください．<strong>太字や<span class="attention">赤字</span>の部分は重要なので特に注意して読んでください! </strong>
				下線部分は<strong>画像で強調している部分</strong>と対応しています．<br>
			</span>
		</div>
	
		<hr>
		<div class="reminder instructions">
			<div id="standardText">
				<h1>
					<span class="langChange" lang="en">Overview:</span>
					<span class="hidden langChange" lang="jp">概要:</span>
				</h1><br><br>
				<div id="pva1">
					<span class="langChange" lang="en">
						You are about to engage in a series of negotiation games with a partner. <br>
						The objective is to decide how to divide a set of items.<br>
						If you can agree with your partner, you'll receive the points allocated on your side when you agreed.<br>
						You will be playing <strong>3 games</strong>.<br>
					</span>
					<span class="hidden langChange" lang="jp">
						あなたは相手との交渉ゲームに参加しようとしています．
						交渉の目的は各アイテムをどのように分けるかを決定することです．
						相手と合意に至ることができれば，合意したときに自分の側に割り当てられたポイントを受け取ることができます．
						交渉は全部で<strong>3回</strong>あります．<br>
					</span>

				</div>
				<br>
				<span class="langChange" lang="en">
					The <strong>first of 3 games</strong> consists of 4 items: record crates, antique lamps, Art Deco paintings, and cuckoo clocks. <br>
					<strong><span class="attention">Later games may have different items, and they may be worth more or less!  Pay close attention.</span></strong><br>
					In the first game, you get <strong>4 points for each box of records, 3 points for each of the paintings, 2 points for each of the lamps, and only 1 point for each cuckoo clock</strong>.<br>
					This means that the records are worth the most to you! <br>
					Your opponent may want the same items you do, <strong><span class="attention">or they may not</span></strong>. <br>
					Talking to your partner can help reveal what items they may want.<br>
				</span>

				<span class="hidden langChange" lang="jp">
					<strong>最初のゲーム</strong>はレコード，アンティークランプ，絵画，鳩時計の4つのアイテムで構成されています．
					<strong><span class="attention">2回目以降のゲームは別のアイテムについて交渉する可能性があり、それぞれのアイテムには異なるポイントが割り当てられています! </span></strong><br>
					最初のゲームでは，<strong>レコード1つにつき4ポイント，絵画1つにつき3ポイント，ランプ1つにつき2ポイント，鳩時計1つにつき1ポイント</strong>を獲得できます．
					すなわち，このゲームではあなたにとってレコードが一番価値があるということになります．<br>
					相手はあなたと同じアイテムを<strong><span class="attention">欲しがっているかもしれないし，そうでないかもしれません</span></strong>．
					相手に尋ねることで，相手が欲しがっているであろうアイテムを知ることができます．<br>
				</span>

				<br><hr>
				<h1>
					<span class="langChange" lang="en">
						About the Game Board:
					</span>
					<span class="hidden langChange" lang="jp">
						ゲームボード(画面構成)について:
					</span>
				</h1><br><br>
				<span class="langChange" lang="en">
					Below is a picture of the game board. <br>
					The chat log is on the right, and a picture of your partner on the left. <br>
					In the bottom half, there is a trade table and buttons.  Near your partner's picture, you may see tips appear to help guide you!<br>
					In the game, you can send messages and questions to your opponent.<br>
					You can also move items around on the game board, and send offers. <br>
					Everything you do will appear in the chat log on the right side of the screen so you can look it over.<br>
				</span>
				<span class="hidden langChange" lang="jp">
					下図はゲームボードの画像です．
					右側にチャットログ，左側に相手の写真があります．
					下半分にはトレードテーブルとボタンがあります．相手の画像の近くにはヒントが表示されています．
					ゲーム中には相手にメッセージや質問などを送ることができます．<br>
					また，ゲームボード上でアイテムを移動させたり，相手に提案を送ったりすることもできます．<br>
					あなたと相手がしたことはすべて画面右側のチャットログに表示されるので，過去の行動を振り返ることもできます．<br><br>
				</span>

				<div>
					<img class="instruction-pic" id="instr_whole" alt="Picture of the game board" src="img/instruction_game_board.png" width="650"/>
				</div>
			</div>

			<div>
				<hr>
				<h1>
					<span class="langChange" lang="en">
						About the Trade Table:
					</span>
					<span class="hidden langChange" lang="jp">
						トレードテーブルについて:
					</span>
				</h1> <br><br>
				<span class="langChange" lang="en">
					Below is the trade table. <br>
					With the trade table <strong>you are able to send offers to your partner</strong>. <br>
					It will start grayed out.  Click <span class="underLine colorBlue">"Start an Offer"</span> to enable it. <br>
					You can <span class="underLine colorRed">click any item to pick it up</span>, then <span class="underLine colorRed">click again to place it</span>.<br>
					For example, you can click one of the lamps in the middle and then click it to your side.<br>
					You can <span class="underLine colorRed">click multiple times for more items</span>. <br>
					Nothing sends until you click <span class="underLine colorBlue">"Send your Offer"</span>.<br><br>
				</span>
				<span class="hidden langChange" lang="jp">
					下図はトレードテーブルです．
					トレードテーブルを使って，<strong>交渉相手に提案を送ることができます</strong>．
					最初はグレーで表示されていますが，<span class="underLine colorBlue">"Start an Offer"</span> をクリックすることでアイテムを配置することが可能になります．<br>
					アイテムを<span class="underLine colorRed">クリックするとアイテムを掴み</span>，<span class="underLine colorRed">再度クリックすることで配置</span>することができます．
					例えば，真ん中にあるランプを1回クリックしてから，自分が相手側のランプをクリックすることでランプを割り当てることができます．
					同じアイテムを<span class="underLine colorRed">複数回クリックすることで複数個掴む</span>ことができ，アイテムを1度に複数個割り当てることができます．<br>
					<span class="underLine colorBlue">"Send your Offer"</span> をクリックすると相手に提案が送信されますが，クリックするまで何度でもアイテムを再配置することができます．<br><br>
				</span>

				<div>
					<img class="instruction-pic" id="instr_table1" alt="Picture of the table" src="img/instruction_offer_start.png" width="400" />
					<span class="langChange" lang="en">
						<img class="instruction-pic" id="instr_table2" alt="Picture of the table" src="img/instruction_offer_send.png" width="500" style="margin-left: 50px;"/>
					</span>
					<span class="hidden langChange" lang="jp">
						<img class="instruction-pic" id="instr_table2_jp" alt="Picture of the table" src="img/instruction_offer_send_jp.png" width="520" style="margin-left: 50px;"/>
					</span>
				</div>
				<br>
				<span class="langChange" lang="en">
					You can also <span class="underLine colorGreen">accept</span> or <span class="underLine colorRed">reject</span> <strong>PARTIAL</strong> offers that your partner sends you as shown in the figure on the left below. <br>
					<strong>These offers aren't binding, but are helpful in building towards a full offer</strong>.<br>
					Pressing <span class="underLine colorBlue">"Formal Accept"</span> is only possible if ALL items are either on your side or your partner's. <br>
					If you both agree, the game is finished! <br>
					<strong><span class="attention">Formal Accept is possible when there are no undecided items</span></strong>, as shown in the figure on the right below.<br><br>
				</span>
				<span class="hidden langChange" lang="jp">
					また，下図左に示すように相手があなたに送ってくる<strong>部分的な</strong>提案を<span class="underLine colorGreen">受け入れたり(Accept)</span>，<span class="underLine colorRed">拒否したり(Reject)</span>することもできます．
					<strong>これらの提案を受け入れたりすることによる拘束は特にありませんが，最終的な提案を構築するのに役立ちます</strong>．<br>
					<span class="underLine colorBlue">"Formal Accept"</span> をすることができるのは，すべてのアイテムがあなたもしくは相手に割り当てられているときのみです．
					両者が "Formal Accept" によって同意した場合，ゲームは終了です．
					"Formal Accept" は下図右のように，<strong><span class="attention">まだ決まっていない(Undecidedに配置されている)アイテムがない場合に可能です</span></strong>．<br><br>
				</span>
				<div>
					<img class="instruction-pic" id="instr_table3" alt="Picture of the table" src="img/instruction_offer_reaction.png" width="400" />
					<img class="instruction-pic" id="instr_table4" alt="Picture of the table" src="img/instruction_formal_accept.png" width="400" style="margin-left: 50px;"/>
				</div>
			</div>

			<div>
				<hr>
				<h1>
					<span class="langChange" lang="en">
						About Emoticons and Avatars:
					</span>
					<span class="hidden langChange" lang="jp">
						顔文字とアバターについて:
					</span>
				</h1><br><br>
				<span class="langChange" lang="en">
					The buttons you see below can be used to send emoticons in chat!<br>
					The blinking emoticon is representing your current emotional state. <br>
					Use it to communicate how you feel about the negotiation!<br>
					Depending on how you feel about the your opponent's messages or offers, there's a possibility that they may also change their behavior towards you.<br>
					<strong><span class="attention">Knowing the partner's emotions is also important in negotiations</span></strong>.<br>
					So <strong>be proactive in expressing your emotions!</strong><br><br>
				</span>
				<span class="hidden langChange" lang="jp">
					下図は，顔文字を送信するボタンです．
					点滅している顔文字は，あなたが最後に送信した感情を表しています．
					交渉や相手の行動に対する気持ちを伝えるのに使いましょう．<br>
					相手のメッセージや提案に対する気持ち次第で，相手もあなたに対する行動を変更する可能性があります．
					<strong><span class="attention">交渉では，相手の感情を知ることも重要になります</span></strong>．
					そのため，<strong>積極的に相手に対して顔文字を送信しましょう!</strong><br><br>
				</span>

				<div>
					<img class="instruction-pic" id="instr_emo" alt="Picture of the emotion buttons" src="img/instruction_emotion.png" width="300"/>
				</div>
				<br>
				<span class="langChange" lang="en">
					You will be assigned an avatar that will be visible to your partner.  <br>
					Your avatar will change facial expressions when you send an emoticon. <br>
					You will be able to see your opponent's avatar across from the chat box.  <br>
					Their avatar's facial expression will also change when they send emoticons. <br>
					Below is an example of what your avatar could look like.<br><br>
				</span>
				<span class="hidden langChange" lang="jp">
					あなたには固有のアバターが割り当てられており，相手のゲームボードに表示されています．
					アバターは顔文字を送るとそれに応じて表情が変わります．<br>
					あなたのゲームボードには相手のアバターが表示されています．
					相手が顔文字を送ると，あなたのゲームボード上のアバターの表情が変わります．
					下図は，自分のアバターがどのように見えているかの例です．<br><br>
				</span>

				<div>
					<img class="instruction-pic" id="instr_avatar" alt="Picture of an avatar" src="img/ChrRens/ChrRens_Smile.jpg" width="200"/>
				</div>
			</div>

			<div>
				<hr>
				<h1>
					<span class="langChange" lang="en">
						About Sending Messages:
					</span>
					<span class="hidden langChange" lang="jp">
						メッセージ送信について:
					</span>
				</h1><br><br>
				<span class="langChange" lang="en">
					You can send a message using the button at the bottom right of the screen.<br>
					Sending messages <strong> lets them know how you feel about the negotiations and their offers</strong>.<br>
					Therefore, <strong><span class="attention">the sending of messages is an important element in facilitating negotiations</span></strong>.Take advantage of it!<br>
					You can send the following types of messages to your partner.<br>
					<ul>
						<li>Asking your partner's preferences</li>
						<li>Expressing your preferences</li>
						<li>Reacting to the their behavior</li>
						<li>Making even better agreement</li>
					</ul>
				</span>
				<span class="hidden langChange" lang="jp">
					画面右下のボタンでメッセージを送ることができます．
					メッセージを送ることで<strong>交渉や相手の提案に対してあなたがどう思っているかを相手に伝えることができます</strong>．
					そのため，<strong><span class="attention">メッセージの送信は交渉を円滑に進める上で重要な要素となります</span></strong>．ぜひ活用しましょう．
					相手には，以下のようなタイプのメッセージを送ることができます，<br>
					<ul>
						<li>相手の好みを聞く</li>
						<li>あなたの好みを伝える</li>
						<li>相手の行動に反応する</li>
						<li>より良い合意案を探る</li>
					</ul>
				</span>
				<div>
					<img class="instruction-pic" id="instr_message" alt="Picture of the message button" src="img/instruction_message_button.png" width="400" />
				</div>
			</div>

			<div>
				<hr>
				<h1>
					<span class="langChange" lang="en">
						About Expressing and Asking Preferences:
					</span>
					<span class="hidden langChange" lang="jp">
						好みを聞く・伝える方法について:
					</span>
				</h1><br><br>
				<span class="langChange" lang="en">
					Below you can find an image of the preference menu. <br>
					During the negotiation you can <strong>express your own preferences for items and ask your opponent specific questions about their preferences</strong>.<br>
					Telling your opponent your preferences or asking to their preferences can have <stong> a positive outcome for both parties.</stong><br>
					Clicking either of the first two buttons on the right side will let you <strong>express your preferences for items</strong>. <br>
					Just <span class="underLine colorRed">click the item you want to talk about once</span>, then <span class="underLine colorBlue">click again in one of the boxes</span>.<br>
					Here, you can see that you're about to ask that your opponent like "paintings" "more than" "cuckoo clocks".<br>
					You can also <span class="underLine colorGreen">click the "more than" symbol</span> to turn it into different options, like "equal" or "best".<br><br>
				</span>
				<span class="hidden langChange" lang="jp">
					下図に好みについてのメッセージを作成する場面の画像を示します．
					交渉の際には<strong>あなたの好みを伝えたり，相手の好みについて質問をしたりすることができます</strong>．
					相手にあなたの好みを伝えたり相手の好みを聞いたりすることで，<strong>双方にとってプラスになることがあります</strong>．<br>
					ゲームボードの右下にあるボタンをクリックすることで好みについてのメッセージを送信することができます．
					"Ask your opponent's preferences"で相手の好み，"Tell your own preferences"であなたの好みに関するメッセージを送信できます．<br>
					<span class="underLine colorRed">上側のアイテムを一度クリック</span>してから，<span class="underLine colorBlue">下側のボックスをクリック</span>すると相手に送る内容を変更することができます．
					下図では，欲しがっているアイテムは "絵画" ">" "鳩時計" ですか？と相手に聞こうとしています．
					また．<span class="underLine colorGreen"> ">" のマークをクリック</span>すると "=" や "一番好き" のように別の聞き方をすることができます．<br><br>
				</span>
				<div>
					<span class="langChange" lang="en">
						<img class="instruction-pic" id="instr_relation" alt="Picture of the relation options" src="img/instruction_preference.png" width="450" />
					</span>
					<span class="hidden langChange" lang="jp">
						<img class="instruction-pic" id="instr_relation_jp" alt="Picture of the relation options" src="img/instruction_preference_jp.png" width="450" />
					</span>
				</div>
			</div>

			<div>
				<hr>
				<h1>
					<span class="langChange" lang="en">
						About Sending Friendly/Unfriendly/Neutral Messages:
					</span>
					<span class="hidden langChange" lang="jp">
						ポジティブ・ネガティブ・ニュートラルメッセージの送信について:
					</span>
				</h1><br><br>
				<span class="langChange" lang="en">
					You can tell your partner how you feel by sending each message.<br>
					There are positive, negative and neutral messages.<br>
					You can send these messages by clicking the third button from the top on the right side of the game board.<br>
					Many messages convey your feelings in the moment to them, but some of these messages may prompt them to take certain actions.<br>
					There are messages that <strong>request them to send an offer</strong>, respectively, as folllows.<br>
					<ul>
						<li>Friendly options: <span class="underLine colorBlue"><strong>Would you please make an offer?</strong></span></li>
						<li>Unfriendly options: <span class="underLine colorRed"><strong>What wrong with you? Hurry up and make an offer!</strong></span></li>
					</ul>

					There are also messages that threaten the opponent.<br>
					<ul>
						<li>Friendly options: <span class="underLine colorBlue">I'm sorry but I think I may walk away.</span></li>
						<li>Unfriendly options: <span class="underLine colorRed">You making me want to walk away from this!</span></li>
					</ul>

					The following message also <strong>asks them to tell you about them preferences</strong>.<br>
					<ul>
						<li>Neutral options: <span class="underLine colorGreen"><strong>So could you tell me about your preferences?</strong></span></li>
					</ul>
				</span>
				<span class="hidden langChange" lang="jp">
					各メッセージを送ることで，相手に自分の気持ちなどを伝えることができます．
					ポジティブなメッセージ，ネガティブなメッセージ，中立なメッセージなどがあります．
					これらのメッセージはゲームボード右下の "Use emotion to influence your opponent" をクリックすることで送信できます．<br>
					多くのメッセージはそのときの気持ちを相手に伝えるものですが，中には相手に特定の行動を促すメッセージもあります．
					<strong>相手に提案してもらうように促す</strong>メッセージは以下の通りです．<br>
					<ul>
						<li>Friendly options: <span class="underLine colorBlue"><strong>Would you please make an offer?</strong></span></li>
						<li>Unfriendly options: <span class="underLine colorRed"><strong>What wrong with you? Hurry up and make an offer!</strong></span></li>
					</ul>
					相手を脅すメッセージもあります．<br>
					<ul>
						<li>Friendly options: <span class="underLine colorBlue">I'm sorry but I think I may walk away.</span></li>
						<li>Unfriendly options: <span class="underLine colorRed">You making me want to walk away from this!</span></li>
					</ul>
					<strong>相手の好みを何でもいいから知りたいとき</strong>は以下のメッセージを使用してください．<br>
					<ul>
						<li>Neutral options: <span class="underLine colorGreen"><strong>So could you tell me about your preferences?</strong></span></li>
					</ul>
				</span>
				<div>
					<span class="langChange" lang="en">
						<img class="instruction-pic" id="instr_message_friendly" alt="Picture of the friendly message button" src="img/instruction_message_friendly.png" width="490"/>
					</span>
					<span class="hidden langChange" lang="jp">
						<img class="instruction-pic" id="instr_message_friendly_jp" alt="Picture of the friendly message button" src="img/instruction_message_friendly_jp.png" width="490"/>
					</span>
					<span class="langChange" lang="en">
						<img class="instruction-pic" id="instr_message_unfriendly" alt="Picture of the unfriendly message button" src="img/instruction_message_unfriendly.png" width="490" style="margin-left: 30px;"/><br><br>
					</span>
					<span class="hidden langChange" lang="jp">
						<img class="instruction-pic" id="instr_message_unfriendly_jp" alt="Picture of the unfriendly message button" src="img/instruction_message_unfriendly_jp.png" width="490" style="margin-left: 30px;"/><br><br>
					</span>
					<span class="langChange" lang="en">
						<img class="instruction-pic" id="instr_message_neutral" alt="Picture of the neutral message button" src="img/instruction_message_other.png" width="370"/>
					</span>
					<span class="hidden langChange" lang="jp">
						<img class="instruction-pic" id="instr_message_neutral_jp" alt="Picture of the neutral message button" src="img/instruction_message_other_jp.png" width="370"/>
					</span>
					<img class="instruction-pic" id="instr_message_general" alt="Picture of the message button" src="img/instruction_message.png" width="380" style="margin-left: 150px; margin-bottom: 10px" />
				</div>
			</div>

			<div>
				<hr>
				<h1>
					<span class="langChange" lang="en">
						About Sending Other Messages:
					</span>
					<span class="hidden langChange" lang="jp">
						その他のメッセージ送信について:
					</span>
				</h1><br><br>
				<span class="langChange" lang="en">
					The green button in the lower right of the screen allows you to exchange information with your partner, including information that is more directly related to the outcome of the negotiation.<br>
					You can use the following messages to <strong>convey the points you already have at the start of the negotiation (called BATNA)</strong> to them.<br>
					You can change what you tell them by moving the slider.<br>
					<ul>
						<li><span class="underLine colorRed">My bottom line is...</span></li>
					</ul>
					If you want to know their BATNA, please send the following message.<br>
					<ul>
						<li><span class="underLine colorBlue">So could you tell me what's your bottom line?</span></li>
					</ul>
					Use the message below to ask them to send you a good offer.<br>
					<ul>
						<li><span class="underLine colorGreen">Would you please send a good deal in exchange for a favor?</span></li>
					</ul>
					If they send you a good offer by the above message, give them return the favor and they will be happy too!<br>
					Let them know that you will repay them with the following message.<br>
					<ul>
						<li><span class="underLine colorOrange">I'm returning the favor to you! Give me a deal good for you.</span></li>
					</ul>
				</span>
				<span class="hidden langChange" lang="jp">
					画面右下の緑色のボタンを押すと，交渉の結果に直結する情報などを相手とやりとりすることができます．
					<strong>あなたが交渉開始時点ですでに持っているポイント(BATNAといいます)</strong>を相手に伝えるには，以下のようなメッセージを使用します．
					スライダーを動かすことで相手に伝える内容を変えることができます．
					<ul>
						<li><span class="underLine colorRed">My bottom line is...</span></li>
					</ul>
					相手のBATNAを知りたい場合は，以下のメッセージを送ってください．<br>
					<ul>
						<li><span class="underLine colorBlue">So could you tell me what's your bottom line?</span></li>
					</ul>
					あなたにとって良い提案をしてもらいたい場合は以下のメッセージを使用します．<br>
					<ul>
						<li><span class="underLine colorGreen">Would you please send a good deal in exchange for a favor?</span></li>
					</ul>
					上記のメッセージで相手が良い提案を送ってくれたら，あなたもお返しをしてあげると相手も喜んでくれるはずです！
					以下のメッセージを使って，お返しをする旨を伝えましょう．<br>
					<ul>
						<li><span class="underLine colorOrange">I'm returning the favor to you! Give me a deal good for you.</span></li>
					</ul>
				</span>

				<div>
					<span class="langChange" lang="en">
						<img class="instruction-pic" id="instr_message_info" alt="Picture of the other message button" src="img/instruction_other_info.png" width="600" />
					</span>
					<span class="hidden langChange" lang="jp">
						<img class="instruction-pic" id="instr_message_info_jp" alt="Picture of the other message button" src="img/instruction_other_info_jp.png" width="580" />
					</span>
					<img class="instruction-pic" id="instr_message_batna" alt="Picture of the batna" src="img/instruction_batna.png" width="400" style="margin-left: 50px;" /><br>
				</div>
			</div>

			<div>
				<hr>
				<h1>
					<span class="langChange" lang="en">
						Some Final Important Notes:
					</span>
					<span class="hidden langChange" lang="jp">
						その他重要事項:
					</span>
				</h1><br>
				<span class="langChange" lang="en">
					<ul>
						<li>The items and your preferences WILL CHANGE for each game if you are playing multiple games, so make sure to click the <span class="underLine colorBlue"><strong>"View Payoffs"</strong></span> button at the bottom left of the screen if you need a reminder what you're looking for.
							A pop-up will appear, as shown below.
							You can see the value you get with each item and your <span class="underLine colorRed">BATNA</span>. Negotiate to get a better value than BATNA!</li>
						<li>The ONLY way to finish the game is to press "Formal Accept" and have your partner also press it, or for time to run out.  Pressing "Accept (non-binding)" will not work.</li>
						<li>When negotiation time is run out, you get the points you have as BATNA. <strong>Since BATNA is considerably less value, <span class="attention">try to reach a "Formal Accept" in time as much as possible!</span></strong></li>
						<li><strong>You don't necessarily have to tell the truth about preferences or BATNA</strong>. However, be aware that <strong>lying can be difficult to negotiate</strong>.</li>
						<li>Your partner may lie as well.</li>
						<li>In rare cases, <strong>the screen at the start of the negotiation may be mis-sized and cut off</strong>. Please <span class="attention"><strong>reload the page</strong></span> in that case.</li>
						<li><strong>We recommend that you finish the three negotiations at once</strong>. However, if you want to interrupt the negotiations, such as when it is difficult to do so continuously, <strong>close the browser tab</strong> and end the negotiations.
							To resume negotiations, enter your ID in the form at the top of the page.</li>
						<li>If you have any trouble or questions about negotiations or experiments, please contact <span class="attention"><strong><a href="mailto:s198680w@st.go.tuat.ac.jp">s198680w@st.go.tuat.ac.jp</a></strong></span>.</li>
					</ul>
				</span>

				<span class="hidden langChange" lang="jp">
					<ul>
						<li>ゲームが複数回あるときは各ゲームでアイテムや好みが変わります，あなたが何を好んでいるのか確認したい場合は，画面左下の <span class="underLine colorBlue"><strong>"View Payoffs"</strong></span> ボタンをクリックしてください．
							下図のようなポップアップが表示されます．
							各アイテムのポイントと<span class="underLine colorRed">BATNA</span>で得られるポイントを確認することができます．上手に交渉してBATNAよりも良いポイントを手に入れましょう!</li>
						<li>ゲームは "Formal Accept" を両者が押すか，時間切れになると終了します．"Accept (non-binding)" を押しても交渉が終了するわけではありません．</li>
						<li>交渉時間がなくなるとBATNAとして持っているポイントが獲得できます．<strong>しかしBATNAの価値はかなり低いので，<span class="attention">できるだけ時間内に "Formal Accept" にたどり着くようにしましょう!</span></strong></li>
						<li>好みやBATNAについて<strong>必ずしも本当のことを言わなければならないわけではありません</strong>．しかし嘘をつくと<strong>交渉が難しくなる場合がある</strong>ので注意しましょう．</li>
						<li>同様に相手も嘘をつく可能性があります．</li>
						<li>まれに<strong>交渉開始時の画面サイズがおかしくなり画面が見切れる</strong>場合があります．その場合は<span class="attention"><strong>ページをリロード</strong></span>してください．</li>
						<li><strong>交渉は3回一気に終わらせてしまうことをおすすめします</strong>．しかし，時間的に連続で行うのが難しい場合など交渉を中断する場合は<strong>ブラウザのタブを閉じて</strong>交渉を終了してください．
							交渉を再開する場合はページ上部のフォームにIDを入力してください．</li>
						<li>交渉や実験についてトラブルが発生したり質問がある場合は<span class="attention"><strong><a href="mailto:s198680w@st.go.tuat.ac.jp">s198680w@st.go.tuat.ac.jp</a></strong></span>までご連絡ください．</li>
					</ul>
				</span>

				<div>
					<img class="instruction-pic" id="instr_payoff_place" alt="Picture of the view payoff" src="img/instruction_payoff_place.png" width="400" />
					<img class="instruction-pic" id="instr_payoff" alt="Picture of the view payoff" src="img/instruction_payoff.png" width="400" style="margin-left: 50px;"/>
				</div>
			</div>
			<hr>
		</div>

		<div id="quiz" align="left">
			<span class="langChange" lang="en">
				<br>Answer the below questions for the 'start' button to appear!<br><br><br>
				1) What item is worth the most to you?
			</span>
			<span class="hidden langChange" lang="jp">
				<br>以下の質問に答えて "スタート" ボタンを押してください!<br><br><br>
				1) あなたにとって最も価値のあるアイテムは何ですか?
			</span>

			<form action="">
				<input type="radio" name="item" value="wrong"> <label><span class="langChange" lang="en">lamps</span><span class="hidden langChange" lang="jp">ランプ</span></label><br>
				<input type="radio" name="item" value="wrong"> <label><span class="langChange" lang="en">paintings</span><span class="hidden langChange" lang="jp">絵画</span></label><br>
				<input id="ans1" type="radio" name="item" value="right"> <label><span class="langChange" lang="en">boxes of records</span><span class="hidden langChange" lang="jp">レコード</span></label><br>
				<input type="radio" name="item" value="wrong"> <label><span class="langChange" lang="en">cuckoo clocks</span><span class="hidden langChange" lang="jp">鳩時計</span></label><br>
			</form>
			<div class="hidden wrong" id="wrong1"><em>
				<span class="langChange" lang="en">
					Oops!  Scroll back up and check the bold sections!
				</span>
				<span class="hidden langChange" lang="jp">
					違います!スクロールして太字部分をチェックしてみてください!
				</span>
			</em></div>

			<br><br>
			<span class="langChange" lang="en">
				2) What item is worth the least to you?
			</span>
			<span class="hidden langChange" lang="jp">
				2) あなたにとって最も価値のないアイテムは何ですか?
			</span>
			<form action="">
				<input id="ans2" type="radio" name="pref" value="right"> <label><span class="langChange" lang="en">cuckoo clocks</span><span class="hidden langChange" lang="jp">鳩時計</span></label><br>
				<input type="radio" name="pref" value="wrong"> <label><span class="langChange" lang="en">paintings</span><span class="hidden langChange" lang="jp">絵画</span></label><br>
				<input type="radio" name="pref" value="wrong"> <label><span class="langChange" lang="en">lamps</span><span class="hidden langChange" lang="jp">ランプ</span></label><br>
				<input type="radio" name="pref" value="wrong"> <label><span class="langChange" lang="en">boxes of records</span><span class="hidden langChange" lang="jp">レコード</span></label>
			</form>

			<div class="hidden wrong" id="wrong2"><em>
				<span class="langChange" lang="en">
					Oops!  Scroll back up and check the bold sections!
				</span>
				<span class="hidden langChange" lang="jp">
					違います!スクロールして太字部分をチェックしてみてください!
				</span>
			</em></div>


			<br><br>
			<span class="langChange" lang="en">
				3) Which item is worth the same amount of points to you and your opponent?
			</span>
			<span class="hidden langChange" lang="jp">
				3) あなたと相手，両者にとって同じ価値があるアイテムはどれですか?
			</span>
			<form action="">
				<input type="radio" name="offer" value="wrong"> <label><span class="langChange" lang="en">lamps</span><span class="hidden langChange" lang="jp">ランプ</span></label><br>
				<input type="radio" name="offer" value="wrong"> <label><span class="langChange" lang="en">paintings</span><span class="hidden langChange" lang="jp">絵画</span></label><br>
				<input type="radio" name="offer" value="wrong"> <label><span class="langChange" lang="en">It's impossible to know this.</span><span class="hidden langChange" lang="jp">相手の好みを知ることは不可能なのでわからない</span></label><br>
				<input id="ans3" type="radio" name="offer" value="right"> <label><span class="langChange" lang="en">It depends on the opponent's preferences which can be discovered by asking them questions.</span><span class="hidden langChange" lang="jp">質問して知った相手の好みによる</span></label>
			</form>
			<div class="hidden wrong" id="wrong3"><em>
				<span class="langChange" lang="en">
					Oops!  Scroll back up and check the bold sections!
				</span>
				<span class="hidden langChange" lang="jp">
					違います!スクロールして太字部分をチェックしてみてください!
				</span>
			</em></div>


			<br><br>
			<span class="langChange" lang="en">
				4) Which statement is correct in the use of trade tables?
			</span>
			<span class="hidden langChange" lang="jp">
				4) トレードテーブルの使い方で正しい記述はどれですか?
			</span>
			<form action="">
				<input type="radio" name="trade" value="wrong"> <label><span class="langChange" lang="en">Drag and drop items to move them</span><span class="hidden langChange" lang="jp">ドラッグアンドドロップでアイテムを移動させる</span></label><br>
				<input id="ans4" type="radio" name="trade" value="right"> <label><span class="langChange" lang="en">Click to move the item</span><span class="hidden langChange" lang="jp">クリックしてアイテムを移動させる</span></label><br>
				<input type="radio" name="trade" value="wrong"> <label><span class="langChange" lang="en">Items once assigned cannot be moved</span><span class="hidden langChange" lang="jp">一度割り当てられたアイテムは動かすことはできない</span></label><br>
				<input type="radio" name="trade" value="wrong"> <label><span class="langChange" lang="en">Can not move the item by yourself</span><span class="hidden langChange" lang="jp">自分でアイテムを動かすことはできない</span></label>
			</form>
			<div class="hidden wrong" id="wrong4"><em>
				<span class="langChange" lang="en">
					Oops!  Scroll back up and check the bold sections!
				</span>
				<span class="hidden langChange" lang="jp">
					違います!スクロールして太字部分をチェックしてみてください!
				</span>
			</em></div>


			<br><br>
			<span class="langChange" lang="en">
				5) Which statement is <strong>WRONG</strong> about BATNA?
			</span>
			<span class="hidden langChange" lang="jp">
				5) BATNAについて<strong>間違っている</strong>記述はどれですか?
			</span>
			<form action="">
				<input type="radio" name="batna" value="wrong"> <label><span class="langChange" lang="en">Click the "View Payoffs" button and you can check BATNA</span><span class="hidden langChange" lang="jp">"View Payoffs" ボタンをクリックすると確認できる</span></label><br>
				<input id="ans5" type="radio" name="batna" value="right"> <label><span class="langChange" lang="en">You can not know your partner's BATNA</span><span class="hidden langChange" lang="jp">相手のBATNAを知ることはできない</span></label><br>
				<input type="radio" name="batna" value="wrong"> <label><span class="langChange" lang="en">If time runs out, you can only get points that are set in BATNA</span><span class="hidden langChange" lang="jp">時間切れになった場合はBATNAに設定されているポイントしか獲得できない</span></label><br>
				<input type="radio" name="batna" value="wrong"> <label><span class="langChange" lang="en">You can tell your partner a lie BATNA</span><span class="hidden langChange" lang="jp">相手に嘘のBATNAを教えても良い</span></label>
			</form>
			<div class="hidden wrong" id="wrong5"><em>
				<span class="langChange" lang="en">
					Oops!  Scroll back up and check the bold sections!
				</span>
				<span class="hidden langChange" lang="jp">
					違います!スクロールして太字部分をチェックしてみてください!
				</span>
			</em></div>


			<br><br>
			<span class="langChange" lang="en">
				6) Which is correct about messages and emoticons?
			</span>
			<span class="hidden langChange" lang="jp">
				6) メッセージや顔文字について正しいものはどれですか?
			</span>
			<form action="">
				<input type="radio" name="behavior" value="wrong"> <label><span class="langChange" lang="en">There is no particular meaning to send a message or emoticons</span><span class="hidden langChange" lang="jp">メッセージや顔文字を送信する意味は特にない</span></label><br>
				<input type="radio" name="behavior" value="wrong"> <label><span class="langChange" lang="en">You can never know your partner's preferences</span><span class="hidden langChange" lang="jp">相手の好みを聞く方法はない</span></label><br>
				<input type="radio" name="behavior" value="wrong"> <label><span class="langChange" lang="en">There is no message to encourage partners to act</span><span class="hidden langChange" lang="jp">相手に行動を促すようなメッセージはない</span></label><br>
				<input id="ans6" type="radio" name="behavior" value="right"> <label><span class="langChange" lang="en">You can tell the your partner your feeling by sending emoticons and messages</span><span class="hidden langChange" lang="jp">顔文字やメッセージを送信するとあなたの様子を相手に伝えることができる</span></label><br>
			</form>
			<div class="hidden wrong" id="wrong6"><em>
				<span class="langChange" lang="en">
					Oops!  Scroll back up and check the bold sections!
				</span>
				<span class="hidden langChange" lang="jp">
					違います!スクロールして太字部分をチェックしてみてください!
				</span>
			</em></div>
		</div>
		<form id="formUserData" target="_self" action="searching.jsp" method="POST">
			<br><br>
			<!--The below is for internal testing -->
			<div id="qualtricsHide">
			<h2>Testing Agent vs Agent Form Section:</h2>
			<p>Note: The following form is intended for internal testing.  This form can be submitted to create an agent as if the user had come from Qualtrics.</p>
				<input type="radio" name="gameChoice" value="self" onclick="hideDiv()"> I want to play the game for myself<br>
				<input type="radio" name="gameChoice" value="agent" onclick="showDiv()"> I want to make an agent to play for me (click to fill in form)<br>
				<br><br>
				<div id="survey" style="display:none;">
				Select expression:
				<select name="expression">
				  <option value="PosNeg">Positive Negative</option>
				  <option value="Neutral">Neutral</option>
				  <option value="Happy">Happy</option>
				  <option value="Angry">Angry</option>
				</select>
				<br><br>
				Select behavior:
				<select name="behavior">
				  <option value="Building">Building</option>
				  <option value="Competitive">Competitive</option>
				</select>
				<br><br>
				Select messages:
				<select name="message">
				  <option value="Negative">Negative</option>
				  <option value="Neutral">Neutral</option>
				  <option value="Positive">Positive</option>
				  <option value="PosNeg">Positive Negative</option>
				</select>
				<br><br>
				Select withholding information:
				<select name="withhold">
				  <option value="Withholding">Able to withhold</option>
				  <option value="Open">Unable to withhold</option>
				</select>
				<br><br>
				Select honesty:
				<select name="honesty">
				  <option value="Honest">Honest</option>
				  <option value="Lying">Lying</option>
				</select>
				</div>
			</div>
			<br><br>
			<input id="qualtricsQ1" name="qualtricsQ1" type="hidden" value="">
			<input id="qualtricsQ2" name="qualtricsQ2" type="hidden" value="">
			<input id="qualtricsQ3" name="qualtricsQ3" type="hidden" value="">
			<input id="qualtricsQ4" name="qualtricsQ4" type="hidden" value="">
			<input id="qualtricsFlag" name="qualtricsFlag" type="hidden" value="ON">
			<input id="gameChoice" name="gameChoice" type="hidden" value="">
			<input id="gameMode" name="gameMode" type="hidden" value="">
			<input id="condition" name="condition" type="hidden" value="">

			<input id="neuroticism" name="neuroticism" type="hidden" value="">
			<input id="extraversion" name="extraversion" type="hidden" value="">
			<input id="openness" name="openness" type="hidden" value="">
			<input id="conscientiousness" name="conscientiousness" type="hidden" value="">
			<input id="agreeableness" name="agreeableness" type="hidden" value="">

			<div class="post instructions hidden" style="margin: auto;">
				<span class="langChange" lang="en">
					However, we <strong>highly</strong> recommend to check the operation <a href="https://myiago.com/apps/sandbox/searching.jsp" target="_blank"><span style="font-size: x-large; font-weight: bolder">here</span></a> first.  <br>
					This is only a confirmation of the operation, <strong>not an action from your partner</strong>.<br><br>
					Be sure to <span class="attention"><strong>confirm that the ID displayed to the right of your partner's avatar is yours</strong></span> when you start negotiating.<br>
					We recommend <span class="attention"><strong>duplicating this page</strong></span> so you can play it while checking the operation.<br>
					When you're ready, click the 'Start!' button below.<br>
				</span>
				<span class="hidden langChange" lang="jp">
					まずは<a href="https://myiago.com/apps/sandbox/searching.jsp" target="_blank"><span style="font-size: x-large; font-weight: bolder">ここ</span></a>で操作の確認をしてみることを<strong>強く</strong>おすすめします．
					あくまで操作の確認なので<strong>相手から何か行動してくるわけではありません</strong>．<br><br>
					交渉が開始したら必ず<span class="attention"><strong>相手のアバターの右に表示されているIDがあなたのものか確認</strong></span>してください．
					<strong>交渉は英語で行われます! 適宜</strong><span class="attention"><strong>説明賞を確認</strong></span>しながら交渉を行うと操作を確認しながらプレイできるのでおすすめです．<br>
					準備ができましたら以下の "スタート" ボタンをクリックしてください．<br>
				</span>
				<br>
				<br>
				<div id="reminderText"></div>
				<br>
				<div id="reminderPhoto"></div>
				<div class="hidden" id="IDWarning">
					<strong>
						<span class="langChange" lang="en">Please enter a string consisting of only numbers.</span>
						<span class="hidden langChange" lang="jp">数値のみで構成される文字列を入力してください.</span>
					</strong>
				</div>
				<strong>Your ID: </strong><input id="MTurkID" name="MTurkID" type="text" value=""><br><br>
				<div class="buttonWrapper">
					<button id="IDSubmitButton" type="button" value="Start"/>
					<label for="IDSubmitButton">
						<span class="langChange" lang="en">Start</span>
						<span class="hidden langChange" lang="jp">スタート</span>
					</label>
				</div>
			</div>
		</form>
	</div>
	<div id="wait" class="hidden">
		<h1>Please waiting for others to join...</h1>
	</div>
	<div id="in-use" class="hidden">
		<h1>You are already in a room.</h1>
	</div>
</body>
</html>