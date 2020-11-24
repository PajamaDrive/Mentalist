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
	<div id="big5">

		<div id="questionnaire">
			<div id="big5Instruction">
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

				<p>
					<span class="langChange" lang="en">Please fill out the questionnaire. <strong>If you answered before</strong>, please enter your ID in the form below.</span>
					<span class="hidden langChange" lang="jp">事前アンケートにご協力ください．<strong>以前回答した方</strong>は以下のフォームにIDを入力してください．</span>
				</p>

				<form id="formBefore" target="_self" action="searching.jsp" method="POST">
					<div style="margin: auto;">
						<span class="hidden" id="beforeIDwarning">数値のみで構成される文字列を入力してください.</span><br>
						<strong>Your ID:</strong><input id="beforeMTurkID" name="beforeMTurkID" type="text" value=""><br><br>
						<input id="beforeIDSubmitButton" type="button" value="Start!" style="height:35px; width:70px;"/>
					</div>
				</form>
			</div>
			<br>
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
				<button type="button" id="butQuestionnaireDone" class="questionnaireButton" style="height:35px; width:70px;">
					<label for="butQuestionnaireDone">
						<span class="langChange" lang="en">Done</span>
						<span class="hidden langChange" lang="jp">終了</span>
					</label>
				</button>
			</div>
			<hr>

		</div>

	</div>
	<div class="hidden" id="mainpage">
	<div class="welcome instructions">
		
	</div>
	<div class="reminder instructions">
		<p/>
		Please read the instructions below. <strong>Pay special attention to the bold sections!</strong>
	</div>
	
	<hr>
	<div class="reminder instructions">
		<div id="standardText">
		
			<h1>Overview:</h1>
			<br>
			<div id="ava1">
			Your virtual agent is about to engage in a series of negotiation games with a partner.  The objective is to decide how to divide a set of items.<br>
			If your virtual agent can agree with your partner, you'll receive the points allocated on your side when you agreed.<br>
			Keep in mind that your agent be playing <strong>3 games</strong>, so watch out for opportunities to do well in the long-run, such as by <strong>exchanging favors!</strong><br>
			</div>
			<div id="pva1">
			You are about to engage in a series of negotiation games with a partner. The objective is to decide how to divide a set of items.<br>
			If you can agree with your partner, you'll receive the points allocated on your side when you agreed.<br>
			Keep in mind that you will be playing <strong>3 games</strong>, so watch out for opportunities to do well in the long-run, such as by <strong>exchanging favors!</strong><br>
			</div>
			<br>
			The <strong>first of 3 games</strong> consists of 4 items: record crates, antique lamps, Art Deco paintings, and cuckoo clocks. <br>
			<strong>Later games may have different items, and they may be worth more or less!  Pay close attention.</strong>
			<br>
			<div id="ava2">
			In the first game, your agent gets <strong>4 points for each box of records, 3 points for each of the paintings, 2 points for each of the lamps, and only 1 point for each cuckoo clock</strong>.  <br>
			This means that the records are worth the most to it!  Your opponent may want the same items, <strong>or they may not</strong>.<br>
			</div>
			<div id="pva2">
			In the first game, you get <strong>4 points for each box of records, 3 points for each of the paintings, 2 points for each of the lamps, and only 1 point for each cuckoo clock</strong>.<br>
			This means that the records are worth the most to you!  Your opponent may want the same items you do, <strong>or they may not</strong>.  Talking to your partner can help reveal what items they may want.<br>
			</div>
			<br>
			<hr>
			<h1>About the Game Board:</h1>
			<br>
			Below is a picture of the game board. The chat log is on the right, and a picture of your partner on the left. In the bottom half, there is a trade table and buttons.  Near your partner's picture, you may see tips appear to help guide you!
			<br>
			<div id="pva3">
			In the game, you can send messages and questions to your opponent. You can also move items around on the game board, and send offers. <br>
			Everything you do will appear in the chat log on the right side of the screen so you can look it over.<br>
			</div>
			
			<div id="ava3">
			In the game, your agent can send messages and questions to your opponent. It can also move items around on the game board, and send offers. <br>
			Everything it does will appear in the chat log on the right side of the screen so you can look it over.<br>
			</div>
		    <br>
		</div>
		<div>
			<img class="instruction-pic" id="instr_whole" alt="Picture of the game board" src="img/instr_whole.PNG" width="590" height="426" />
		</div>

		<hr>
		<br>
		<h1>About the Trade Table:</h1> <br>
		<div id="ava4">
		Below is the trade table. With the trade table <strong>your agent can send offers to your partner</strong>.  You cannot interact with this interface and must watch your agent do so for you.
		    Your agent can click any item to pick it up, then click again to place it.
			For example, it can click one of the lamps in the middle and then click it to your side.  It can click multiple times for more items.  Nothing sends until it clicks "Send Offer".<br><br>
			It can also accept or reject <strong>PARTIAL</strong> offers that your partner sends it. <strong>These offers aren't binding, but are helpful in building towards a full offer</strong>.<br>
			Pressing "Formal Accept" is only possible if ALL items are either on its side or your partner's.  If they both agree, the game is finished!  <br><br>
		</div>
		<div id="pva4">
		Below is the trade table. With the trade table <strong>you are able to send offers to your partner</strong>.  It will start grayed out.  Click "Start Offer" to enable it.  You can click any item to pick it up, then click again to place it.<br>
			For example, you can click one of the lamps in the middle and then click it to your side.  You can click multiple times for more items.  Nothing sends until you click "Send Offer".<br><br>
			You can also accept or reject <strong>PARTIAL</strong> offers that your partner sends you. <strong>These offers aren't binding, but are helpful in building towards a full offer</strong>.<br>
			Pressing "Formal Accept" is only possible if ALL items are either on your side or your partner's.  If you both agree, the game is finished!  <br><br>
		</div>
			
		<div>
			<img class="instruction-pic" id="instr_table" alt="Picture of the table" src="img/instr_table.PNG" width="543" height="347" />
		</div>
		<br>
		<hr>
		<br>
		<h1>About Emoticons and Avatars:</h1>
		<br> 
		<div id="pva5">
		The buttons you see below can be used to send emoticons in chat! The blinking emoticon is representing your current emotional state. Use it to communicate how you feel about the negotiation!<br><br>
		</div>
		<div id="ava5">
		The buttons you see below can be used by your agent to send emoticons in chat! The blinking emoticon is representing your current emotional state. Your agent can use it to communicate how it feels about the negotiation!<br><br>
		</div>
		<div>
			<img class="instruction-pic" id="instr_emo" alt="Picture of the emotion buttons" src="img/instr_emo.PNG" width="316" height="90" />
		</div>
		<br>
		<div id="pva6">
		You will be assigned an avatar that will be visible to your partner.  Your avatar will change facial expressions when you send an emoticon.  You will be able
		to see your opponent's avatar across from the chat box.  <br> Their avatar's facial expression will also change when they send emoticons.  Below is an example of what your avatar could look like.
		</div>
		<div id="ava6">
		Your agent will be assigned an avatar that will be visible to your partner.  Your agent's avatar will change facial expressions when it sends an emoticon.  You will be able
		to see your opponent's avatar across from the chat box.  <br> Their avatar's facial expression will also change when they send emoticons.  Below is an example of what your agent's avatar could look like.
		</div>
		<br>
		<img class="instruction-pic" id="instr_table" alt="Picture of an avatar" src="img/ChrBrad/ChrBrad_SmallSmile.png"/>
		<div>
		<br>
		<hr>
		<br>	
		<h1>About Expressing Preferences:</h1>
		<br>
		<div id="pva7">
		<br>
		Below you can find an image of the preference menu. During the negotiation you can <strong>express your own preferences for items and ask your opponent specific questions about their preferences</strong>.
		<br>
		Clicking either of the first two buttons on the right side will let you <strong>express your preferences for items</strong>. Just click the item you want to talk about once,
		then click again in one of the boxes.  
		<br>
		Here, you can see that you're about to say that you like "lamps" "less than" "paintings".  You can also click the "less than" symbol to turn it into different
		options, like "equal" or "best".<br><br>	
		</div>				
		<div id="ava7">
		<br>
		Below you can find an image of the preference menu. During the negotiation your agent can <strong>express its preferences for items and ask your opponent specific questions about their preferences</strong>.
		<br>
		Clicking either of the first two buttons on the right side will let it <strong>express its preferences for items</strong>. 
		<br>
		Here, you can see that it's about to say that it likes "lamps" "less than" "paintings".  It can also click the "less than" symbol to turn it into different
		options, like "equal" or "best".<br><br>	
		</div>
		</div>
		<div>
			<img class="instruction-pic" id="instr_relation" alt="Picture of the relation options" src="img/instr_relation.PNG" width="560" height="276" />
		</div>
		<div>
		<br>
		<hr>
		<br>
		<h1>Some Final Important Notes:</h1>	
		<div id="pva8">
		<ul>
		<li>The ONLY way to finish the game is to press "Formal Accept" and have your partner also press it, or for time to run out.  Pressing "Accept (non-binding)" will not work.</li>
		<li>The items and your preferences WILL CHANGE for each game if you are playing multiple games, so make sure to click the "View Payoffs" button if you need a reminder what you're looking for.</li>
		</ul>
		</div>
		<div id="ava8">
		<ul>
		<li> <strong>Your interface will be disabled and you will watch your agent play for you against your opponent.</strong></li>
		<li>The ONLY way to finish the game is for your agent to press "Formal Accept" and have your partner also press it, or for time to run out.  Pressing "Accept (non-binding)" will not work.</li>
		<li>The items and your preferences WILL CHANGE for each game if you are playing multiple games, so make sure to click the "View Payoffs" button if you need a reminder what you're looking for.</li>
		</ul>
		</div>
		</div>
		<hr>
	</div>	
			<br><br><strong>If you are coming from Qualtrics, you only need to answer the 3 quiz questions below and click 'Start!' to begin the game.  If you are not coming from Qualtrics and do not want MTurk credit 
			then there will be a form below which you can fill in with your game preferences.<br> <br> <br> </strong>
			<div id="quiz" align="left">
				<br><br>Answer the below questions for the 'start' button to appear!<br><br>
				1) What item is worth the most to you?
				<form action="">
					<input type="radio" name="item" value="wrong"> lamps<br>
					<input type="radio" name="item" value="wrong"> paintings<br>
					<input id="ans1" type="radio" name="item" value="right"> boxes of records<br>
					<input type="radio" name="item" value="wrong"> cuckoo clocks<br>
				</form>
				<div class="hidden wrong" id="wrong1"><em>Oops!  Scroll back up and check the bold sections!</em></div>
		
				<br><br>
				2) What item is worth the least to you?
				<form action="">
					<input id="ans2" type="radio" name="pref" value="right"> cuckoo clocks<br>
		
					<input type="radio" name="pref" value="wrong"> paintings<br>
					<input type="radio" name="pref" value="wrong"> lamps<br>
					<input type="radio" name="pref" value="wrong"> boxes of records
				</form>
				
				<div class="hidden wrong" id="wrong2"><em>Oops!  Scroll back up and check the bold sections!</em></div>
				<br><br>
				3) Which item is worth the same amount of points to you and your opponent?
				<form action="">
					<input type="radio" name="offer" value="wrong"> lamps<br>
					<input type="radio" name="offer" value="wrong"> paintings<br>
					<input type="radio" name="offer" value="wrong"> It's impossible to know this.<br>
					<input id="ans3" type="radio" name="offer" value="right"> It depends on the opponent's preferences which can be discovered by asking them questions.
				</form>
				<div class="hidden wrong" id="wrong3"><em>Oops!  Scroll back up and check the bold sections!</em></div>
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
					Congratulations, you answered all three questions correctly.  The 'Start!' button below is now available.  However, we <strong>highly</strong>
					 recommend that you first experiment in the <a href="https://myiago.com/apps/sandbox/searching.jsp" target="_blank">sandbox</a> first.  You can experiment with the interface in a new window before matching with your opponent!
					<br>
					<br>
					<div id="reminderText"></div>
					<br>
					<div id="reminderPhoto"></div>
<!-- 					<script> -->
<!-- // 						var img = new Image(); -->
<!-- // 						var imgdiv = document.getElementById('reminderPhoto'); -->
<!-- // 						var remtxt = document.getElementById('reminderText'); -->
<!-- // 						var cond = decodeURI(getQueryVariable("condition")); -->
<!-- // 						//The followiung logic loads the correct opponent image depending on whether user is playing against agent or human: -->
<!-- // 						if(cond === "hpva") -->
<!-- // 						{ -->
<!-- // 							img.src = "img/pcondition.jpg"; -->
<!-- // 							img.width = 300; -->
<!-- // 							img.height = 300; -->
<!-- // 							remtxt.append("REMINDER: You will negotiate against another human.  You will be connected once a match is found. ") -->
<!-- // 							imgdiv.appendChild(img); -->
<!-- // 						} -->
<!-- // 						else if(cond === "hava") -->
<!-- // 						{ -->
<!-- // 							img.src = "img/pcondition.jpg"; -->
<!-- // 							img.width = 300; -->
<!-- // 							img.height = 300; -->
<!-- // 							remtxt.append("REMINDER: You will program a virtual agent to negotiate against a human.  You will be connected once a match is found. ") -->
<!-- // 							imgdiv.appendChild(img); -->
<!-- // 						} -->
<!-- // 						else if(cond === "apva" ) -->
<!-- // 						{ -->
<!-- // 							img.src = "img/acondition.png"; -->
<!-- // 							img.width = 300; -->
<!-- // 							img.height = 300; -->
<!-- // 							remtxt.append("REMINDER: You will negotiate against a virtual agent. You will be connected once a connection to the agent's server is established.") -->
<!-- // 							imgdiv.appendChild(img); -->
<!-- // 						} -->
<!-- // 						else if(cond === "aava" ) -->
<!-- // 						{ -->
<!-- // 							img.src = "img/acondition.png"; -->
<!-- // 							img.width = 300; -->
<!-- // 							img.height = 300; -->
<!-- // 							remtxt.append("REMINDER: You will program a virtual agent to negotiate against another virtual agent.  You will be connected once a connection to the agent's server is established.") -->
<!-- // 							imgdiv.appendChild(img); -->
<!-- // 						} -->
<!-- 				</script> -->
					<span class="hidden" id="IDwarning">数値のみで構成される文字列を入力してください.</span><br>
					<strong>Your ID:</strong><input id="MTurkID" name="MTurkID" type="text" value=""><br><br>
					<input id="IDSubmitButton" type="button" value="Start!" style="height:35px; width:70px;"/>
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