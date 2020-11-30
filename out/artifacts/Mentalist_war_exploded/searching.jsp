<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Searching for an opponent</title>
<style>
	div,h1{
	font-family: "Arial";
	}
	.loader {
	 margin: auto;
	  border-top: 10px solid blue;
	  border-right: 10px solid gold;
	  border-bottom: 10px solid blue;
	  border-radius: 50%;
	  width: 120px;
	  height: 120px;
	  animation: spin 2s linear infinite;
	  border-left: 12px solid gold;
	}
	
	@keyframes spin {
	  0% { transform: rotate(0deg); }
	  100% { transform: rotate(360deg); }
	}
	
</style>
<script>
	var gameType = "<%= request.getParameter("condition") %>";
	//Pass the data onto the server after 5 seconds if no other user is found, will return here if no other user found after server pass
	window.onload=function(){ 
		if(gameType === "hpva" || gameType === "hava"){
			window.setTimeout(document.agentdata.submit.bind(document.agentdata), 17000);
		}
		else{
			window.setTimeout(document.agentdata.submit.bind(document.agentdata), 10000);
		}
    };
	
	//Displays the 'waiting complete' text and hides loading animation once the game page opens
	function changePageContentOnLoad() {
		  document.getElementById("complete").style.visibility = "visible";
		  document.getElementById("loader").style.visibility = "hidden";
		}
	if(gameType === "hpva" || gameType === "hava"){
		setTimeout("changePageContentOnLoad()", 13000);	
	}
	else{
		setTimeout("changePageContentOnLoad()", 6000);
	}
</script>
</head>
<body>
<h1 align="center">IAGO Waiting Room</h1>
<hr>
<br>
<div class ="searchingcontent">
	<div class="loader" id="loader"></div>
	<br>
	<div id="searchText" style="text-align:center"></div>
	<script>
		if(gameType === "hpva" || gameType === "hava"){
			document.getElementById("searchText").innerHTML = "<h2>Searching for another user.  This may take a few seconds, please do not disconnect or reload the page!</h2>";
		}
		else{
			document.getElementById("searchText").innerHTML = "<h2>Connecting to agent server.  This may take a few seconds, please do not disconnect or reload the page!</h2>";
		}
	</script>
	<br>
	<div id="complete" style="visibility: hidden;text-align:center;color:green"><h2>Connected!  Please wait for game to load...</h2></div>
</div>
</body>
<form action="game" name="agentdata" method="POST">
	<input id="expression" name="expression" type="hidden" value=<%= request.getParameter("expression") == null ? "PosNeg" : request.getParameter("expression")%>>
	<input id="behavior" name="behavior" type="hidden" value=<%= request.getParameter("behavior") == null ? "Building" : request.getParameter("behavior")%>>
	<input id="message" name="message" type="hidden" value=<%= request.getParameter("message") == null ? "Negative" : request.getParameter("message") %>>
	<input id="withhold" name="withhold" type="hidden" value=<%= request.getParameter("withhold") == null ? "Withholding" : request.getParameter("withhold") %>>
	<input id="honesty" name="honesty" type="hidden" value=<%= request.getParameter("honesty") == null ? "Honest" : request.getParameter("honesty") %>>
	
	<input id="qualtricsQ1" name="qualtricsQ1" type="hidden" value=<%= request.getParameter("qualtricsQ1") == null ? "Undefined" : request.getParameter("qualtricsQ1") %>>
	<input id="qualtricsQ2" name="qualtricsQ2" type="hidden" value=<%= request.getParameter("qualtricsQ2") == null ? "Undefined" : request.getParameter("qualtricsQ2") %>>
	<input id="qualtricsQ3" name="qualtricsQ3" type="hidden" value=<%= request.getParameter("qualtricsQ3") == null ? "Undefined" : request.getParameter("qualtricsQ3") %>>
	<input id="qualtricsQ4" name="qualtricsQ4" type="hidden" value=<%= request.getParameter("qualtricsQ4") == null ? "Undefined" : request.getParameter("qualtricsQ4") %>>
	<input id="qualtricsFlag" name="qualtricsFlag" type="hidden" value=<%= request.getParameter("qualtricsFlag") == null ? "OFF" : request.getParameter("qualtricsFlag") %>>
	<input id="gameChoice" name="gameChoice" type="hidden" value=<%= request.getParameter("gameChoice") == null ? "agent" : request.getParameter("gameChoice") %>>
	<input id="condition" name="condition" type="hidden" value=<%= request.getParameter("condition") == null ? "false" : request.getParameter("condition") %>>
	<input id="MTurkID" name="MTurkID" type="hidden" value=<%= request.getParameter("MTurkID") == null ? request.getParameter("beforeMTurkID") : request.getParameter("MTurkID") %>>

	<input id="neuroticism" name="neuroticism" type="hidden" value=<%= request.getParameter("neuroticism") == null ? "-1" : request.getParameter("neuroticism") %>>
	<input id="extraversion" name="extraversion" type="hidden" value=<%= request.getParameter("extraversion") == null ? "-1" : request.getParameter("extraversion") %>>
	<input id="openness" name="openness" type="hidden" value=<%= request.getParameter("openness") == null ? "-1" : request.getParameter("openness") %>>
	<input id="conscientiousness" name="conscientiousness" type="hidden" value=<%= request.getParameter("conscientiousness") == null ? "-1" : request.getParameter("conscientiousness") %>>
	<input id="agreeableness" name="agreeableness" type="hidden" value=<%= request.getParameter("agreeableness") == null ? "-1" : request.getParameter("agreeableness") %>>

	<input id="languageChecked" name="languageChecked" type="hidden" value=<%= request.getParameter("languageChecked") == null ? request.getParameter("beforeLanguageChecked") : request.getParameter("languageChecked") %>>

</form>
</html>