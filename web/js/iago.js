//////////FOLLOWING RUNS ASAP \\\\\\\\\\



//globals
var neutralTimeout;
var artChar = "Brad";
var globalTimer = 0;
var timer = 0;
var timerID;
var angerGlow = false;
var happyGlow = false;
var sadGlow = false;
var surprisedGlow = false;
var neutralGlow = true;
var webSocket;
var maxTime = 0;
var closeSafe = false;
var readyToRestart = false;
var holdingForUser = false;
var playerBATNA = 0;
var playerPresentedBATNA = 0;
var totalPlayerPoints = 0;
var disable = false;
var disableStarted = false;
var qualtricsData = "";
var disableFunction = function disabler(e){
    e.stopPropagation();
    e.preventDefault();
}


const EMOTIONS = {
	NEUTRAL: "neutral",
	ANGER: "anger",
	HAPPY: "happy",
	SAD: "sad",
	SURPRISED: "surprised"
}

const BUTTON_LANGUAGE_LIST = new Map([
    ["Ask your opponent's preferences >", "相手の好みを聞く >"],
    ["Tell your own preferences >", "自分の好みを教える >"],
    ["Use emotion to influence your opponent >", "感情を伝えるメッセージ >"],
    ["Get favorable deals and explore alternatives! >", "より良い交渉を行うためのメッセージ >"],
    ["We should try to split things evenly.", "均等に分けるべきですね."],
    ["We should each get our most valuable item.", "お互いに一番価値のあるアイテムを手に入れましょう."],
    ["We should try harder to find a deal that benefits us both.", "お互いにメリットのある取引ができるように努力しましょう."],
    ["I wish we could reach something a little more fair.", "より公平な合意案にたどりつきたいですね."],
    ["Would you please make an offer?", "新しい提案を送信してもらえませんか?"],
    ["I'm sorry but I think I may walk away.", "申し訳ありませんが, 交渉を打ち切るかもしれません."],
    ["If you don't split this evenly there will be consequences!", "均等に分けないと後悔しますよ?"],
    ["I just want us to get our most valuable items so this can be over with!", "一番価値のあるアイテムを手に入れてさっさと終わりにしたいんだよ!"],
    ["You need to try a LOT harder to find a deal that benefits us both!", "もっと頑張ってお互いにメリットのある合意案を探してください!"],
    ["You not even trying to find something remotely fair to both of us!", "公平な合意案を探そうとしてないですよね?"],
    ["What wrong with you? Hurry up and make an offer!", "どうしました? 早く提案を送信してくださいよ!"],
    ["You making me want to walk away from this!", "あなたのせいで交渉をやめたくなったよ!"],
    ["So could you tell me what's your bottom line?", "あなたの最低ラインを教えてくれますか?"],
    ["My bottom line is...  >", "私の最低ラインは... >"],
    ["Would you please send a good deal in exchange for a favor?", "私にとって良い提案を送っていただけないでしょうか?"],
    ["I'm returning the favor to you!  Give me a deal good for you.", "お返しをしたいです! あなたにとって良い提案を送信してください."],
    ["I'm thinking...", "今考え中です..."],
    ["I'm happy with this so far!", "今のところ満足しています!"],
    ["I'm not happy with this...", "これでは満足できません..."],
    ["I don't think that makes sense with previous statements.", "以前の発言は意味をなさないと思うのですが."],
    ["So could you tell me about your preferences?", "あなたの好みを教えてくれますか?"],
    ["Friendly options >", "友好的なメッセージ >"],
    ["Unfriendly options>", "敵対的なメッセージ >"],
    ["Neutral options >", "ニュートラルなメッセージ >"],
    ["<Confirm>", "<送信>"],
    ["<Back>", "<戻る>"],
    ["<Send>", "<送信>"]
]);

const LANGUAGE_LIST = new Map([
	["Ask your opponent's preferences >", "相手の好みを聞く >"],
	["Tell your own preferences >", "自分の好みを教える >"],
    ["Use emotion to influence your opponent >", "感情を伝えるメッセージ >"],
    ["Get favorable deals and explore alternatives! >", "より良い交渉を行うためのメッセージ >"],
	["We should try to split things evenly.", "均等に分けるべきですね."],
    ["We should each get our most valuable item.", "お互いに一番価値のあるアイテムを手に入れましょう."],
    ["We should try harder to find a deal that benefits us both.", "お互いにメリットのある取引ができるように努力しましょう."],
    ["I wish we could reach something a little more fair.", "より公平な合意案にたどりつきたいですね."],
    ["Would you please make an offer?", "新しい提案を送信してもらえませんか?"],
    ["I'm sorry but I think I may walk away.", "申し訳ありませんが, 交渉を打ち切るかもしれません."],
    ["If you don't split this evenly there will be consequences!", "均等に分けないと後悔しますよ?"],
    ["I just want us to get our most valuable items so this can be over with!", "一番価値のあるアイテムを手に入れてさっさと終わりにしたいんだよ!"],
    ["You need to try a LOT harder to find a deal that benefits us both!", "もっと頑張ってお互いにメリットのある合意案を探してください!"],
    ["You not even trying to find something remotely fair to both of us!", "公平な合意案を探そうとしてないですよね?"],
    ["What wrong with you? Hurry up and make an offer!", "どうしました? 早く提案を送信してくださいよ!"],
    ["You making me want to walk away from this!", "あなたのせいで交渉をやめたくなったよ!"],
    ["So could you tell me what's your bottom line?", "あなたの最低ラインを教えてくれますか?"],
	["My bottom line is...  >", "私の最低ラインは... >"],
    ["Would you please send a good deal in exchange for a favor\\?", "私にとって良い提案を送っていただけないでしょうか?"],
    ["I'm returning the favor to you!  Give me a deal good for you.", "お返しをしたいです! あなたにとって良い提案を送信してください."],
    ["I'm thinking...", "今考え中です..."],
    ["I'm happy with this so far!", "今のところ満足しています!"],
	["I'm not happy with this...", "これでは満足できません..."],
    ["I don't think that makes sense with previous statements.", "以前の発言は意味をなさないと思うのですが."],
    ["So could you tell me about your preferences\\?", "あなたの好みを教えてくれますか?"],
    ["Friendly options >", "友好的なメッセージ >"],
    ["Unfriendly options>", "敵対的なメッセージ >"],
    ["Neutral options >", "ニュートラルなメッセージ >"],
	["Just so you know, I already have an offer for ", "知っておいて欲しいのですが, 私はすでに"],
    [" points, so I won't accept anything less.", "ポイント持っているので, それ以下の提案は受け取りかねます."],
    ["No, I can't accept that offer.", "この提案は承諾できかねます."],
    ["Yes, I accept that offer.", "この提案を承諾します!"],
	["Hello.", "こんにちは."],
    ["I think this deal is good for the both of us.", "この提案内容はお互いにとって良いものだと思っています."],
    ["I think you'll find this offer to be satisfactory.", "この提案なら納得いただけると思います."],
    ["I think this arrangement would be beneficial to both.", "この提案は両者に有益だと思います."],
    ["I think this deal will interest you.", "この提案なら興味を持っていただけると思います."],
    ["Please consider this deal?", "この提案を検討してみてください."],
    ["I think this arrangement is fair.", "この内容は公平だと思います."],
    ["How about a deal like this?", "このような内容はいかがですか?"],
    ["What do you think of this?", "これなんかはどう思われますか?"],
    ["What do you think about this arrangement?", "このような提案についてどう思われますか?"],
    ["I think this deal is the best idea ever.", "この内容は今までで一番良いと思っています."],
   	["I would be very happy if you accept this deal.", "この提案を承諾していただけるととても嬉しいです."],
    ["This is a very good agreement for me, but what about you?", "この提案内容は私にとってとても良いものですがあなたにとっても良いものなら幸いです."],
    ["Wonderful!", "素晴らしい!"],
    ["I'm glad we could come to an agreement!", "承諾してくれてよかった!"],
    ["Sounds good!", "いいですね!"],
    ["Thank you!", "ありがとうございます!"],
    ["Let's keep looking for the best agreement at this rate!", "この調子で最適な合意案を探していきましょう!"],
    ["I have a feeling this negotiation with you is going to end well!", "あなたとの交渉はうまくいきそうです!"],
    ["Thank you for accepting my offer!", "提案を承諾していただいてありがとうございます!"],
    ["That's nice!", "よかったです!"],
    ["You are the best!", "最高です!"],
    ["Oh that's too bad.", "そうですか..."],
    ["Ah well, perhaps another time.", "承諾はまたの機会にお願いします."],
    ["Ok, maybe something different next time.", "次は違う提案をしてみますね."],
    ["Alright.", "わかりました."],
    ["I'll consider another offer.", "別の提案を考えてみます."],
    ["I'll think about it some more...", "もう少し考えてみます..."],
    ["Well... I'd like to know your opinion and offer too.", "そうですか... あなたの意見や提案も聞きたいです."],
    ["I don't have enough information to make a good offer.", "十分な情報がないので良い提案をできませんでした."],
    ["I'll do my best to suggest the offer that is attractive to you.", "あなたにとって良い提案をできるように頑張ります."],
    ["What kind of offer do you think is best for you?", "あなたはどんな提案が良いと思っていますか?"],
    ["I'm sorry, but I don't think that's fair to me.", "申し訳ないけどその提案は私にとって公平じゃないと思います."],
    ["Apologies, but that won't work for me.", "すみません. それは承諾できません."],
    ["Perhaps we should spend more time finding a solution that's good for us both...", "お互いにとって良い合意案を探すのにもっと時間をかけるべきかもしれませんね..."],
    ["I won't be able to accept that.  So sorry.", "その内容では承諾しかねます. ごめんなさい."],
    ["I could accept your offer if you'd more concede, but...", "もう少し譲歩していただければ承諾できると思います."],
    ["Could you concede a little more?", "もう少し譲っていただけませんか?"],
    ["I'm sorry, but this offer is unacceptable.", "すみませんが, この提案は拒否させていただきます."],
    ["There has to be a better agreement... Let's work together to find the best deal!", "もっと良い合意案があるはずです... 一緒にベストな合意案を探しましょう!"],
    ["Could you assign a few more items to me?", "私にもう少しアイテムを割り当てていただけますか?"],
    ["I'm sorry I can't accept it. I'll try to think of another offer instead.", "承諾できなくてすみません. 代わりに別の提案を考えてみます. "],
    ["Your offer is good!", "その提案良いですね!"],
    ["That seems like a good deal.", "良い提案だと思います!"],
    ["That will work for me.", "この提案は私にとって良い結果をもたらすと思います."],
    ["Yes. This deal will work.", "この提案内容は良い結果になると思います."],
    ["Thank you for conceding!", "譲っていただいてありがとうございます!"],
    ["Let's keep the negotiation going at this rate!", "この調子で交渉を続けましょう!"],
    ["This offer is great!", "この提案は素晴らしいです!"],
    ["Let's find a good agreement while making concessions to each other!", "お互いに譲り合いながら良い合意案を探りましょう!"],
    ["This is a good offer for me. I hope it is good for you as well.", "これは私にとって良い内容でした. あなたにっとても良い内容だと幸いです."],
    ["Let's continue to negotiate.", "交渉を続けましょう."],
    ["Hello? Are you still there?", "あの... まだ居ますよね?"],
    ["No rush, but are you going to send me an offer?", "急いではないですが, なにか提案をしていただけませんか?"],
    ["Can I do anything to help us reach a deal that's good for us both?", "お互いのためになる交渉をするために何かできることはありますか?"],
    ["I'm sorry, but are you still there?", "すみません. まだいらっしゃいますか?"],
    ["Can I provide more information to help us reach consensus?", "合意案を探すためにもっと情報を提供してもよろしいですか?"],
    ["If you're still here, please respond in some way...", "もしまだいるならなにかしら反応をください..."],
	["I don't have enough information about you... please provide some information to facilitate the negotiations.", "あなたに関する情報が少ないと思います... 交渉を円滑に進めるためにもう少し情報をください."],
    ["I'd like to know a little more about you so that we can successfully negotiate.", "交渉を成功させるためにもあなたの情報をもう少し知りたいです."],
    ["If you can't think of a good offer, please send a message \"Would you please make an offer?\" or \"What wrong with you? Hurry up and make an offer!\". I would think of an offer instead.", "良い提案が思いつかない場合は\"Would you please make an offer?\"か\"What wrong with you? Hurry up and make an offer!\"のメッセージを送信してください. 私が代わりに提案を考えます."],
    ["If you want me to send you an Offer, please send me a message saying \"Would you please make an offer?\" or \"What wrong with you? Hurry up and make an offer!\".", "私から提案を送信してほしい場合は\"Would you please make an offer?\"か\"What wrong with you? Hurry up and make an offer!\"のメッセージを送信してみてください."],
    ["I got it.", "わかりました."],
    ["Thank you for sharing your preference.", "好みを教えていただきありがとうございます."],
    ["Thank you! It's going to make the negotiations go smoothly!", "ありがとうございます! これで交渉をスムーズに進められると思います!"],
    ["I see... I'll refer to it when I think about the offer.", "なるほど. 提案を考える際に反映したいと思います."],
    ["This is very informative! Thank you!", "貴重な情報ありがとうございます!"],
    ["I'm starting to get to know you a little better.", "少しずつあなたのことが分かってきました."],
    ["Should I share my preferences with you as well? If you want to know, ask from \"Ask your opponent's preference\" or \"So could you tell me about your preferences?\".", "私の好みも教えた方がよろしいですか? 知りたい場合は\"Ask your opponent's preference\"か\"So could you tell me about your preferences?\"から聞いてください."],
    ["This is useful information for me as we negotiate!", "交渉するためにすごく有用な情報です!"],
    ["Thanks for the information. You can ask for my preference via \"Ask your opponent's preference\" or \"So could you tell me about your preferences?\".", "ありがとうございます. 私の好みは\"Ask your opponent's preference\"か\"So could you tell me about your preferences?\"から聞くことができます."],
    ["Thank you! If you want to know my preference, use the \"Ask your opponent's preference\" or \"So could you tell me about your preferences?\".", "ありがとうございます! 私の好みを知りたい場合は\"Ask your opponent's preference\"か\"So could you tell me about your preferences?\"を使って聞いてくださいね!"],
    ["Why don't we make sure you get your favorite item, and I get mine?", "お互いに一番好きなアイテムを獲得できるようにしませんか?"],
    ["I think so, too!", "私もそう思います!"],
    ["I'll do my best to make a fair offer as possible!", "できるだけ公平な提案をできるように努力します!"],
    ["I agree!", "賛成です!"],
    ["I want to make a mutually acceptable deal.", "お互いに納得できる交渉をしたいですね."],
    ["Let's keep each other informed and try to get a fair deal!", "お互いに情報交換して平等な取引をできるように頑張りましょう!"],
    ["I need the information to make a fair deal... Could you tell me your preferences via \"Tell your own preferences\"?", "平等な取引をするために情報が必要ですね... あなたの好みを\"Tell your own preferences\"から教えてくれませんか?"],
    ["If you tell me your information using \"Tell your own preferences\", perhaps I can make better offer.", "あなたの好みを\"Tell your own preferences\"を使って教えていただければ, もっと良い提案をできるかもしれません."],
    ["Agreed! If you want to know my information, using \"Ask your opponent's preference\" or \"So could you tell me about your preferences?\".", "賛成です! 私の好みを聞きたい場合は\"Ask your opponent's preference\"か\"So could you tell me about your preferences?\"を使用してください."],
    ["If we have more information about each other, we have a fair deal. You can know my preferences via \"Ask your opponent's preference\" or \"So could you tell me about your preferences?\".", "お互いの情報がもっとあれば公平な取引ができると思います. 私の好みは\"Ask your opponent's preference\"か\"So could you tell me about your preferences?\"で知ることができます."],
	["I'm sorry, have I done something wrong?  I'm just trying to make sure we both get the things that make us the most happy.", "何か悪いことしましたか? 私はお互いが一番得をするものを分けようとしているだけなんです."],
	["Sorry... I'm trying to do my best...", "ごめんなさい... もっと努力します..."],
	["I think we need to be a little more cooperative or it won't be good for both of us.", "お互いにもう少し協調的にならないとお互いのためにならないと思います."],
	["Forgive me, let's pull ourselves together and work together from now on to get the best deal possible.", "許してください. これからは最高の取引ができるように身を引き締めていきたいと思います."],
	["Let's work together to negotiate, because I don't think it will benefit either of us if we don't.", "お互いの利益にならないと思うので, 協力して交渉しましょう."],
	["I'm sure there are ideas that are fair and mutually beneficial. Let's not give up and keep going.", "お互いにメリットのある合意案があると思います. 諦めずに協力して交渉を進めていきましょう."],
	["Okay... I don't know what kind of offer you want so can you send it to me?", "そうですか... どんな提案があなたにとっていいのかわからないので, あなたから送ってもらえますか？"],
	["Well... I think we can negotiate better if we have more information... Tell me your preference using \"Tell your own preferences\".", "なるほど... もっと情報があればより良い交渉ができると思います... \"Tell your own preferences\"でお好みを教えてください."],
	["It's going to be hard to negotiate without a little more information about each other... Please tell me your preferences using the \"Tell your own preferences\" or hear my preferences via \"Ask your opponent's preference\" or \"So could you tell me about your preferences?\".", "もう少しお互いの情報がないと交渉は難しそうですね... あなたの好みを\"Tell your own preferences\"で教えていただくか, 私の好みを\"Ask your opponent's preference\"か\"So could you tell me about your preferences?\"で聞いてください."],
	["I'm sorry... could you send me an offer so I can know what you think?", "すみません... あなたの考えを知りたいので提案を送ってくれませんか?"],
	["What's wrong?", "どうしました?"],
	["I'm sorry... Did I do something wrong?", "すみません... なにか悪いことしましたか...?"],
	["What's going on?", "どうかしましたか?"],
	["What's the matter?", "何事ですか?"],
	["I'm sorry... I have no animosity towards you...", "ごめんなさい... あなたに敵意はないんですが..."],
	["I'm sorry. I didn't mean to provoke you.", "すみません. 挑発するつもりはなかったんです."],
	["What's the problem?", "なにか問題ありましたか?"],
	["I don't know why, but I'm sorry anyway...", "理由はわかりませんが, とにかくすみません..."],
	["Let's keep negotiating together without that negativity!", "そんなネガティブにならずに交渉を続けましょうよ!"],
	["Was there anything about my behavior that you didn't like...?", "私の行動でなにか気に入らないことがありましたか...?"],
	["By the way, will you tell me a little about your preferences?", "あなたの好みをなにか教えてくれませんか?"],
	["Well, at least you're happy!", "少なくとも喜んでいるみたいでよかったです!"],
	["I'm glad you're happy, too.", "あなたが喜んでいるようで私も嬉しいです!"],
	["Let's have fun!", "楽しんでいきましょう!"],
	["Let's continue to negotiate positively.", "ポジティブに交渉を続けていきましょう!"],
	["Great!", "よかったです!"],
	["I'm so glad you're pleased.", "喜んでもらえてとても嬉しいです."],
	["I'm happy too!", "私も嬉しいです!"],
	["Good!", "よかった!"],
	["What, did I surprise you?", "え, 驚いてるんですか?"],
	["Why are you surprised?", "なぜ驚いているんです?"],
	["Wow!", "えっ!"],
	["Okay.", "わかりました."],
	["I see.", "了解です."],
	["All right.", "はい，わかりました."],
	["Is everything ok?", "大丈夫ですか?"],
	["I already have an offer for ", "私はすでにBATNAとして"],
	[" points, so anything that gets me more than ", "ポイント持っているので, "],
	[" points will do.", "ポイント以上ならなんでもいいですよ."],
    [" points. In case you forgot, ", "ポイント以上必要なのかと思ってました. 忘れているかもしれませんが, "],
	["Well, since you can't accept anything less than ", "あなたは"],
	[" points and I can't accept anything that gets me less than ", "ポイントより低い提案は承諾しないし私は"],
	[" points, I don't think we'll be able to make a deal. Maybe we should just walk away.", "ポイントより低い提案は承諾しないということは交渉が成立しないと思います. このまま立ち去った方がいいのかもしれないですね."],
	["Oh it is? I thought you needed more than ", "そうなんですか? てっきりあなたは"],
    ["In case you forgot, ", "忘れているかもしれませんが, "],
    ["I'm sorry.  I must be misunderstanding.  Earlier, you said: ", "すみません, 私は誤解しているかもしれません. 先ほど, あなたは次のように言っていました. :"],
	[" Was that not correct? Once again, tell me your correct preferences!", "これらは正しくなかったということですか? 改めて正しい好みを教えてください!"],
	["Well, maybe I can accept a little less. Would you be able to give me at least ", "もう少し少ないポイントでも承諾するべきでしたね. 最低でも"],
	["Please don't go yet! Maybe we can still make a deal. Would you mind reminding me what you would like?", "まだ交渉できるかもしれないので立ち去らないでください! 何が欲しいか教えてくれませんか?"],
	["Oh well, I guess we really should walk away. Are you sure you can't accept anything less than ", "確かに, 立ち去るべきかもしれないですね. あなたは本当に"],
	["We're almost out of time!  Accept this quickly!", "もう時間がありません! 早急にこの提案を承諾してください!"],
	["I'm glad to see you! Let's start negotiating!", "会えて嬉しいです! では, 交渉を始めましょう."],
	["I'm sorry, have I said something confusing? I didn't mean to.", "混乱させるようなことを言ってしまいましたか? そんなつもりはありませんでした."],
	["Can you be a little more specific? Saying \"something\" is confusing.", "もう少し具体的に言ってくれませんか? \"なにか\"というのは曖昧すぎます."],
	["No way! You still owe me from before...", "いやいや! まだ私に対して借りがあるじゃないですか..."],
	["Well, you're welcome I guess... but I don't think you really owed me!", "ありがとうございます. でも... あなたは私に対して借りがないし遠慮しときます!"],
	["Sure, but you'll owe me one, ok?", "いいですよ. でも借りは返してくださいね?"],
	["Thanks for returning the favor from before!", "借りを返してくれてありがとうございます!"],
	["Hmm.  I don't really feel like it.", "んー私, 今受け入れる気分じゃないんですよね."],
	["Sure, since you did me that favor before, I'm happy to help this round.", "前に借りを作ったし, 今回は喜んで手伝わせてもらうよ."],
	["Oh I'm sorry, but items this game are worth so much to me...", "すみません... このゲームのアイテムは私にとても価値があるので..."],
	["I don't really do favors.", "私は今回恩返しできないのでお断りします."],
	["Oh blast!  And this was so important to me this round too...", "そうですか... 私にとってすごく重要なものだったのに..."],
	["Oh wonderful!  I will make sure to pay you back in the next game!", "素晴らしい！ 次のゲームでは必ずお返しします!"],
	["I don't think it best to reveal my intentions yet. Maybe if you did first...", "私はまだ好みを明らかにするべきではないと思っています. あなたが先に教えてくれたら考えます."],
	["Actually, I won't be able to offer you anything that gives you ", "あなたが"],
	[" points. I think I'm going to have to walk away, unless you were lying.", "ポイント獲得できるような提案を作成することができません. そのBATNAが本当なら立ち去るしかないかもしれません."]
]);


const REPLACE_LIST = new Map([
    ["lamps", "ランプ"],
    ["lamp", "ランプ"],
    ["paintings", "絵画"],
    ["painting", "絵画"],
    ["boxes of records", "レコード"],
    ["box of records", "レコード"],
    ["cuckoo clocks", "鳩時計"],
    ["cuckoo clock", "鳩時計"],
    ["bars of iron", "鉄塊"],
    ["bar of iron", "鉄塊"],
    ["bars of gold", "金塊"],
    ["bar of gold", "金塊"],
    ["barrels of oil", "石油"],
    ["barrel of oil", "石油"],
    ["shipments of spices", "香辛料"],
    ["shipment of spices", "香辛料"],
    ["apples", "りんご"],
    ["apple", "りんご"],
    ["oranges", "オレンジ"],
    ["orange", "オレンジ"],
	["pears", "洋梨"],
    ["pear", "洋梨"],
    ["bananas", "バナナ"],
    ["banana", "バナナ"],
    ["something else", "なにか"],
    ["something ", "なにか"],
    ["something", "なにか"],
    ["You already have a deal for 12 points. Try to get more!", "あなたのBATNAは12ポイントです. より高得点を目指しましょう!"],
    [" points each", "ポイント"],
    ["If you want to share your actual walk-away value, press \"Send\" now. If you'd like to send a higher walk-away value, move the slider to the right!", "実際のBATNAの値を共有したい場合は, \"送信\"を押してください. より高い値を送信したい場合は, スライダーを右に動かしてください!"]
]);

const REPLACE_DIALOG = new Map([
	["The items and your preferences have changed!", "アイテムとあなたの好みが変わりました!"],
    ["You need to negotiate a trade deal!", "今回は貿易交渉をしてください!"],
    ["Click \"View Payoffs\" to view these again.", "\"報酬を見る\"でアイテムなどをもう一度確認できます."],
    ["You have an alternative deal for 12 points!", "今回, あなたのBATNAは12ポイントです!"],
    ["Time need to negotiate a for some antiques!", "骨董品について交渉してください!"],
    ["You need to negotiate at a market stall!", "今回は市場の屋台で交渉を行います!"],
    ["You now get 4 points for each banana, 3 points for each orange, 2 points for each pear, and <strong>only 1 point for each apple.</strong>", "あなたはバナナ1個ごとに4ポイント, オレンジ1個ごとに3ポイント, 洋梨1個ごとに2ポイント, <strong>りんご1個ごとに1ポイント</strong>獲得できます."],
    ["You now get 4 points for each bar of iron, 3 points for each bar of gold, 2 points for each barrel of oil, and <strong>only 1 points for each shipment of spices.</strong>", "あなたは鉄塊1個ごとに4ポイント, 金塊1個ごとに3ポイント, 石油1個ごとに2ポイント, <strong>香辛料1個ごとに1ポイント</strong>獲得できます."],
    ["You now get 4 points for each box of records, 3 points for each painting, 2 points for each lamp, and <strong>only 1 point for each cuckoo clock.</strong>", "あなたはレコード1個ごとに4ポイント, 絵画1個ごとに3ポイント, ランプ1個ごとに2ポイント, <strong>鳩時計1個ごとに1ポイント</strong>獲得できます."],
    ["<p>The game has ended because your opponent has accepted your offer!  Both players receive the points agreed upon on in their final offers. </p><br><br>", "<p>相手があなたの提案を受け入れたためゲームは終了しました!  両プレイヤーは合意案によるポイントを獲得します. </p><br><br>"],
    ["Your score was", "あなたのスコア"],
    ["<p>The game has ended because time has expired.  Both players receive their BATNA.  </p><br><br>", "<p>時間切れのため終了しました. 両プレイヤーはBATNAを受け取ります.  </p><br><br>"],
    ["The game has ended!  Be prepared, a new game will start with the other opponent soon!", "交渉は終了しました!  もうすぐ新しい相手との交渉ゲームが始まります！"],
    ["The game has ended!  You'll be redirected to finish soon.", "交渉は終了しました!  終了ページへリダイレクトされます."],
    ["Only one minute remains in this negotiation!", "あと1分で交渉が終了します!"],
    ["You are not allowed to use that form yet!", "あなたはまだあのフォームを使用できません!"],
    ["Thanks for playing!  Please click ok to be redirected to the finish page!", "プレイしていただきありがとうございます! OKをクリックすると終了ページへリダイレクトされます."],
    ["The connection was terminated early. Your opponent may have left. Please reload the page. ", "接続が切れてしまいました. 相手が立ち去った可能性があります. ページをリロードしてください. "]
]);

function feelEmotion(emotion) {
	neutralGlow = emotion === EMOTIONS.NEUTRAL
	angerGlow = emotion === EMOTIONS.ANGER
	happyGlow = emotion === EMOTIONS.HAPPY
	sadGlow = emotion === EMOTIONS.SAD
	surprisedGlow = emotion === EMOTIONS.SURPRISED
}

//called when data comes into the WebSocket
function requestAsyncInfo(incomingEvent, socket) {
	var event = $.parseJSON(incomingEvent);

	if (event.tag == "points-vh") {
		total = 0;
		for (i = 0; i < event.data.length; i++) {
			$("#labelVHPoints" + i).text(event.data[i]);
			total += event.data[i];
		}
		$("#labelVHPointsTotal").text(total);
	} else if(event.tag == "visibility-points-vh") {
		if(event.data == true)
			$(".labelVHPoints").removeClass("hidden");//insecure
		else
			$(".labelVHPoints").addClass("hidden");
	} else if(event.tag == "agent-description") {
		if(event.data != null) {
			if($("input[name=lang]:checked").val() == "0"){
                $("#vhDescription").html(event.data)
			}
			else{
                $("#vhDescription").html("<h1>交渉相手</h1><p>相手はワクワクしながら交渉に臨んでいます!</p>");
            }
			$("#vhDescription").removeClass("hidden");
		}
	} else if(event.tag == "visibility-timer") {
		if(event.data == true)
			$("#negoTimer").removeClass("hidden");
		else
			$("#negoTimer").addClass("hidden");
	} else if(event.tag == "max-time") {
		maxTime = event.data;
	} else if (event.tag == "cursors-ready") {
		$("#butCol0Row0").attr('src', "img/item0.png" + "?" + Math.random()); //force a recache.  Ew, I know.
		$("#butCol0Row1").attr('src', "img/item0.png" + "?" + Math.random());
		$("#butCol0Row2").attr('src', "img/item0.png" + "?" + Math.random());

		$("#butCol1Row0").attr('src', "img/item1.png" + "?" + Math.random());
		$("#butCol1Row1").attr('src', "img/item1.png" + "?" + Math.random());
		$("#butCol1Row2").attr('src', "img/item1.png" + "?" + Math.random());

		$("#butCol2Row0").attr('src', "img/item2.png" + "?" + Math.random());
		$("#butCol2Row1").attr('src', "img/item2.png" + "?" + Math.random());
		$("#butCol2Row2").attr('src', "img/item2.png" + "?" + Math.random());

		$("#butCol3Row0").attr('src', "img/item3.png" + "?" + Math.random());
		$("#butCol3Row1").attr('src', "img/item3.png" + "?" + Math.random());
		$("#butCol3Row2").attr('src', "img/item3.png" + "?" + Math.random());

		$("#butCol4Row0").attr('src', "img/item4.png" + "?" + Math.random());
		$("#butCol4Row1").attr('src', "img/item4.png" + "?" + Math.random());
		$("#butCol4Row2").attr('src', "img/item4.png" + "?" + Math.random());

		$("#butItem0").attr('src', "img/item0.png" + "?" + Math.random());
		$("#butItem1").attr('src', "img/item1.png" + "?" + Math.random());
		$("#butItem2").attr('src', "img/item2.png" + "?" + Math.random());
		$("#butItem3").attr('src', "img/item3.png" + "?" + Math.random());
		$("#butItem4").attr('src', "img/item4.png" + "?" + Math.random());

		$("#butItemFirst").attr('src', "img/white.png");
		$("#butItemSecond").attr('src', "img/white.png");
	} else if(event.tag =="config-character-art") {
		switch (event.data) {
			case "Brad":
				artChar = "Brad"
				break
			case "Ellie":
				artChar = "Ellie"
				break
			case "Rens":
				artChar = "Rens"
				break
			default:
				artChar = "Laura"
		}

		if (artChar == "Brad")
			$("#vhImg").attr('src', "img/ChrBrad/ChrBrad_Neutral.png");
		else if (artChar == "Ellie")
			$("#vhImg").attr('src', "img/ChrEllie/ChrEllie_Neutral.png");
		else if  (artChar == "Rens")
			$("#vhImg").attr('src', "img/ChrRens/ChrRens_Neutral.jpg");
		else
			$("#vhImg").attr('src', "img/ChrLaura/ChrLaura_Neutral.jpg");
	} else if(event.tag == "player-BATNA") {
		playerBATNA = event.data;
		$("#mySlider").val(event.data);
		$("#mySlider").css("background","green");
		playerPresentedBATNA = event.data;
		$("#batnaValue").text("I already have an offer for " + playerPresentedBATNA + " points.");
	} else if(event.tag == "total-player-points") {
		totalPlayerPoints = event.data;
		$( "#mySlider" ).attr({
			max: event.data
		});
	} else if(event.tag == "hide-quit") {
		updateButtonPanelItem("butFormalQuit", false, true)
	}
	else if (event.tag == "open-survey") {
		survey = event.data;
		if (survey == null || survey == "") {
			submitAll(false);
		} else {
			//This block does not currently check to see if the survey URL is valid.
			text = $.get(survey, function(data) {
                toggleSurveyDiag(true, data)
			}, 'html')
		}
	}
	else
		handleEvent(event, socket)
}
function printMenu(json) {
	// First hide the <div>'s elements completely 
	$(".butText").addClass("hidden");
	$(".butContainer").addClass("hidden");
	$(".compare-item").addClass("hidden");
	$(".compare-panel-message").addClass("hidden");
	$(".buffer").addClass("hidden");
	//$(".compare-panel-message").text("");

	minHeight = 0;
	// Then add every name contained in the list.
	$.each(json, function(id, text) {
		minHeight += 30;
		$("#" + id).toggleClass("hidden");
		if($("#" + id).hasClass("butText"))
			changeLanguageChatButton(id, text);
	});
	
	minPrefHeight = $("#butPrefDiv").height() + 30*3;//accounts for two buttons
	// Then add every name contained in the list.	

	if ($(".messages").height() < minHeight)
		$("#messageBuffer").css("padding-top", minHeight - $(".messages").height());

	if ($(".messages").height() < minPrefHeight)
		$("#buttonBuffer").css("padding-top", minPrefHeight - $(".messages").height());
	
	//browser-specific kludges
	var OSName="Unknown OS";
	if (navigator.appVersion.indexOf("Win")!=-1) OSName="Windows";
	if (navigator.appVersion.indexOf("Mac")!=-1) OSName="MacOS";
	if (navigator.appVersion.indexOf("X11")!=-1) OSName="UNIX";
	if (navigator.appVersion.indexOf("Linux")!=-1) OSName="Linux";

	//this is a kludgy fix for people with small screens--this code needs a full css rework to better support odd browsers
	//there may also be some Mac/PC differences--never mind about mobile browsers
	//I suppose I'll keep supporting IE 11 until 2025, when it reaches extended EoL
	if((navigator.userAgent.indexOf("Opera") != -1 || navigator.userAgent.indexOf('OPR') != -1) && OSName != "Windows")//seems to be a mac problem 
	{
		//$(".butText").css("background-color", "#FFFFFE"); //must do this.  FFFFFF won't work--won't cause rerender
		$(".butText").css("flex", "1 0 auto !important"); //Mac does not respect min-height so must disable shrinkage
	}
	else if(navigator.userAgent.indexOf("Chrome") != -1 && OSName != "Windows")//seems to be a mac problem
	{
		//$(".butText").css("background-color", "#FFFFFE"); //must do this.  FFFFFF won't work--won't cause rerender
		$(".butText").css("flex", "1 0 auto !important"); //Mac does not respect min-height so must disable shrinkage
	}
	else if(navigator.userAgent.indexOf("Safari") != -1 && OSName != "Windows")//seems to be a mac problem)
	{
		//$(".butText").css("background-color", "#FFFFFE"); //must do this.  FFFFFF won't work--won't cause rerender
		$(".butText").css("flex", "1 0 auto !important"); //Mac does not respect min-height so must disable shrinkage
	}
	else if(navigator.userAgent.indexOf("Firefox") != -1 ) 
	{
		//leave
	}
	else if((navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true )) //IF IE > 10 or Edge
	{
		$(".buffer").addClass("hidden");
		$("#gameContainer").css("margin", "0");
		$(".gameWrapper").css("margin", "0");
		$("#gameContainer").css("height", "800");
		$("#gameContainer").css("width", "1200");
		$(".menu-side").css("width", "720");
		$(".vh-side").css("width", "480");
		$(".flex-child").css("margin", "0");
		$(".flex-child-no-grow").css("margin", "0");
		$(".flex-child-no-shrink").css("margin", "0");
	}  
	else 
	{
		//leave
	}
}

function openSocket(){
	// Ensures only one connection is open at a time
	if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
		writeResponse("WebSocket is already opened.");
		return;
	}

	var loc = window.location, new_uri;
	
	//NOTE: This is the proper way of ensuring the correct protocols in server setup that does not rely on reverse proxying.
	//Often, Tomcat or similar servers will be set up (perfectly safely) unencrypted behind an encrypted static server.
	//In this case, particularly with Apache, the URLs can become mangled because the ASF is dumb.
	//While you can rectify this mangling manually, it will confuse the code below, which does not know the presence
	//of a proxy, and will therefore use the wrong protocol.
	//Therefore, please ONLY use this code if you know what you're doing.
	
	if (loc.protocol === "https:") {
		new_uri = "wss:";
	} else {
		new_uri = "ws:";
	}
	
	//In most setups, use the following code for unencrypted Tomcat servers behind reverse proxies.  This can, however,
	//cause problems with mixed content errors.
	//new_uri = "ws:";
	
	//Now continue
	new_uri += "//" + loc.host;
	new_uri += loc.pathname + "/ws";
	// Create a new instance of the websocket
	webSocket = new WebSocket(new_uri);

	/**
	 * Binds functions to the listeners for the websocket.
	 */
	webSocket.onopen = function(event) {
		startup(event)
	}

	webSocket.onmessage = function(event) {
		requestAsyncInfo(event.data, webSocket);
	}

	webSocket.onclose = function(event) {
		clearInterval(timerID);

		if(closeSafe == false) {
            if($("input[name=lang]:checked").val() == "0") {
                dialog("The connection was terminated early. Your opponent may have left. Please reload the page. ");
            }
            else{
                dialog(REPLACE_DIALOG.get("The connection was terminated early. Your opponent may have left. Please reload the page. "));
            }

			// This block isn't working. Intended to allow us to write final data on an unsafe close
			var obj = new Object();
			obj.tag = "unsafe-close";
			obj.data = "";
			socket.send(JSON.stringify(obj));
		}
		console.log('Onclose called' + JSON.stringify(event));
		console.log('code is' + event.code);
		console.log('reason is ' + event.reason);
		console.log('wasClean  is' + event.wasClean);
		$("input").off("click");
		$("button").off("click");
		$(".tableSlot").off("click");
	}
}

function closeSocket() {
	closeSafe = true;
	webSocket.close();
}

function startup(event) {
	startup(event, true);
}

function startup() {
	startup(null, false);
}

function startup(event, useEvent) {

	if (window.performance.navigation.type == 1) {
		var refreshed = new Object();
		refreshed.tag = "page-refreshed";
		refreshed.data = "";
		webSocket.send(JSON.stringify(refreshed));
	}
	if (useEvent) {
		var debugdata = 'Onopen called' + JSON.stringify(event) + '\ncode is' + event.code + '\nreason is ' + event.reason + '\nwasClean  is' + event.wasClean;
		console.log(debugdata);
		var debug = new Object();
		debug.tag = "debug";
		debug.data = debugdata;
		webSocket.send(JSON.stringify(debug));
	}
	var obj0 = new Object();
	obj0.tag = "experimentFirstCheck";
	obj0.data = "";
    webSocket.send(JSON.stringify(obj0));

    var obj = new Object();
	obj.tag = "button";
	obj.data = "root";
	webSocket.send(JSON.stringify(obj));
	var obj2 = new Object();
	obj2.tag = "button";
	obj2.data = "butCol0Row0";
	webSocket.send(JSON.stringify(obj2));
	var obj3 = new Object();
	obj3.tag = "butCol0Row0";
	obj3.data = "root";
	webSocket.send(JSON.stringify(obj3));
    showChatMessage(agentMessage("Hello!")) //this forces IE to refresh the formatting
    flashMessage("Hello!")
	var obj4 = new Object();
	obj4.tag = "request-visibility";
	obj4.data = "vh-points";
	webSocket.send(JSON.stringify(obj4));
	var obj5 = new Object();
	obj5.tag = "request-max-time";
	obj5.data = "";
	webSocket.send(JSON.stringify(obj5));
	var obj6 = new Object();
	obj6.tag = "request-visibility";
	obj6.data = "timer";
	webSocket.send(JSON.stringify(obj6));
	var obj7 = new Object();
	obj7.tag = "request-agent-description";
	obj7.data = "";
	console.log(obj7)
	webSocket.send(JSON.stringify(obj7));
	var obj8 = new Object();
	obj8.tag = "request-agent-art";
	obj8.data = "";
	webSocket.send(JSON.stringify(obj8));
	var obj9 = new Object();	//get the actual value of the user's BATNA
	obj9.tag = "request-player-batna";
	obj9.data = "";
	webSocket.send(JSON.stringify(obj9));
	var obj10 = new Object();	//get the total possible points for the player.
	obj10.tag = "request-total-player-points";
	obj10.data = "";
	webSocket.send(JSON.stringify(obj10));
	var obj11 = new Object();	
	obj11.tag = "start-threads";
	obj11.data = "";
	webSocket.send(JSON.stringify(obj11));
	var obj12 = new Object();	
	obj12.tag = "request-disable";
	obj12.data = "";
	webSocket.send(JSON.stringify(obj12));

}

function buttonHandler(button) {
	//some of this hiding does not conform to code-hiding standards/encapsulation.  Will be revised to server-side.
	if(button.data == "butStartOffer") {
		$(".itemButton").prop("disabled", false);
		updateButtonPanelItem("butStartOffer", false, undefined)
		updateButtonPanelItem("butSendOffer", true, undefined)
		updateButtonPanelItem("butAccept", false, undefined)
		updateButtonPanelItem("butReject", false, undefined)
		updateButtonPanelItem("butAcceptFavor", false, undefined)
		updateButtonPanelItem("butRejectFavor", false, undefined)
		updateButtonPanelItem("butFormalAccept", false, undefined)

		disableExchangeTable(false)
	}

	if(button.data == "butSendOffer") {
		$(".itemButton").prop("disabled", true);
		updateButtonPanelItem("butSendOffer", false, undefined)
		updateButtonPanelItem("butStartOffer", true, undefined)

		disableExchangeTable(true)
	}

	if(button.data == "butAccept" || button.data == "butReject") {
		updateButtonPanelItem("butAccept", false, undefined)
		updateButtonPanelItem("butReject", false, undefined)
	}
	
	if(button.data == "butAccepFavor" || button.data == "butRejectFavor") {
		updateButtonPanelItem("butAcceptFavor", false, undefined)
		updateButtonPanelItem("butRejectFavor", false, undefined)
	}

	if(button.data == "butSend") {
		var obj = new Object();
		obj.tag = "sendBATNA";
		obj.data = ""+playerPresentedBATNA; //This needs to be a String for GameBridgeUtils to parse it correctly.
		webSocket.send(JSON.stringify(obj));
	}

	if(button.data == "butItemComparison") {
		if($("#butItemComparison").attr('src') == "img/ltsymbol.jpg") {
			$("#butItemComparison").attr('src', 'img/gtsymbol.jpg');
			$("#butItemSecond").removeClass("reallyHidden");
			button.data += "GT";
		} else if($("#butItemComparison").attr('src') == "img/gtsymbol.jpg") {
			$("#butItemComparison").attr('src', 'img/best.png');
			$("#butItemSecond").addClass("reallyHidden");
			button.data += "BEST";
		} else if($("#butItemComparison").attr('src') == "img/best.png") {
			$("#butItemComparison").attr('src', 'img/least.png');
			$("#butItemSecond").addClass("reallyHidden");
			button.data += "LEAST";
		} else if($("#butItemComparison").attr('src') == "img/least.png") {
			$("#butItemComparison").attr('src', 'img/eqsymbol.jpg');
			$("#butItemSecond").removeClass("reallyHidden");
			button.data += "EQUAL";
		} else if($("#butItemComparison").attr('src') == "img/eqsymbol.jpg") {
			$("#butItemComparison").attr('src', 'img/ltsymbol.jpg');
			$("#butItemSecond").removeClass("reallyHidden");
			button.data += "LT";
		}
	}

	switch(button.data) {
		case "butAnger":
			feelEmotion(EMOTIONS.ANGER)
			break
		case "butSad":
			feelEmotion(EMOTIONS.SAD)
			break
		case "butNeutral":
			feelEmotion(EMOTIONS.NEUTRAL)
			break
		case "butSurprised":
			feelEmotion(EMOTIONS.SURPRISED)
			break
		case "butHappy":
			feelEmotion(EMOTIONS.HAPPY)
			break
		default:
	}

	webSocket.send(JSON.stringify(button));

	//this needs to be done immediately, as it is simply waiting for input from the servlet
	if(button.data.indexOf("butExpl") > -1) {
		if($("input[name=lang]:checked").val() == "0") {
            showChatMessage(userMessage($("#" + button.data).val()))
        }
        else{
            showChatMessage(userMessage(Array.from(BUTTON_LANGUAGE_LIST.keys()).filter(function(key) { return BUTTON_LANGUAGE_LIST.get(key) == $("#" + button.data).val() })[0]))
        }
		updateButtonPanelItem("butAcceptFavor", false, undefined)
		updateButtonPanelItem("butRejectFavor", false, undefined)
		scrollChatToTop()
	}	
}

function submitAll() {
    submitAll(true);
}

function submitAll(validSurvey) {
    if(allChecked()) {
        if (validSurvey === undefined || validSurvey) {
//		if ($('input:iagoCheckableQuestion', this).is(':checked')){
//			console.log("something was checked");
            var survey = "{" + $(".iagoCheckableQuestion:checked").map(function () {
                return ("\"" + $(this).attr("id") + "\":\"" + $(this).val() + "\"");
            }).get() + "}";
            var surveyObj = new Object();
            surveyObj.data = JSON.parse(survey);
            surveyObj.tag = "survey";
            webSocket.send(JSON.stringify(surveyObj));
//			
//		    } else {
//		    	console.log("nothing was checked");
//		        alert("Please answer all the questions on the survey and press the submit button when you're finished making your selections.");
//		        return false;
//		    }
        }

        webSocket.send(JSON.stringify({tag: "dialogClosed", data: ""}))
        $(".gameWrapper").removeClass("hidden")
        toggleSurveyDiag(false, "")
    }
    else {
        $("#warning").removeClass("hidden");
        promptNotYet();
    }
}

function allChecked(){
    var element = $("input[name^=q]:checked");
    if(element.length == 13)
        return true;
    else
        return false;
}

function promptNotYet(){
    var names = $("input[name^=q]:checked").map(function(index, element){return element.name});
    if(names.length != 0) {
        for (var i = 1; i <= 13; i++) {
            if (names.filter(function(index, value){ return value.match(new RegExp("^q" + i + "_.*$")); }).length > 0) {
                $(".questionArea#" + i).removeClass("notYet");
            }
            else {
                $(".questionArea#" + i).addClass("notYet");
                if(i >= 4 && i <= 9)
                    $(".questionArea#0").addClass("notYet");
            }
        }
    }
    else {
        $(".questionArea").addClass("notYet");
    }
}
function radioChecked(){
    if($("input[name^=q]:checked")) {
        $("input[name^=q]:checked").parent().parent().removeClass("notYet");

        var names = $("input[name^=q]:checked").map(function(index, element){return element.name});
        var i = 4;
        for (; i <= 9; i++) {
            if (names.filter(function(index, value){ return value.match(new RegExp("^q" + i + "_.*$")); }).length == 0) {
                break;
            }
        }
        if(i == 10){
            $(".questionArea#0").removeClass("notYet");
        }
        if (allChecked()) {
            $("#warning").addClass("hidden");
            $("#finish").removeClass("hidden");
        }
    }

    if($("input[name=lang]:checked").val() == "0"){
        $(".langChange[lang=en]").removeClass("hidden");
        $(".langChange[lang=jp]").addClass("hidden");
    }
    else{
        $(".langChange[lang=en]").addClass("hidden");
        $(".langChange[lang=jp]").removeClass("hidden");
    }

    if($("input[name=lang_survey]:checked").val() == "0"){
        $(".langChange[lang=en]").removeClass("hidden");
        $(".langChange[lang=jp]").addClass("hidden");
    }
    else{
        $(".langChange[lang=en]").addClass("hidden");
        $(".langChange[lang=jp]").removeClass("hidden");
    }
}


////////////////////////////FOLLOWING RUNS AFTER ELEMENTS LOADED \\\\\\\\\\\\\\\\\\\\\\\\\\\\
$(document).ready(function() 
		{
	
	openSocket();
	
	//scaling to more reasonable size
	var starterData = { 
			  size: {
			    width: $wrapper.width(),
			    height: $wrapper.height()
			  }
			}
	doResize(null, starterData);

	var tempID = "root";
	
	timerID = setInterval(function(){

		globalTimer++;
		if(globalTimer == 10) {
			webSocket.send(JSON.stringify({ tag: "notify-thread", data: "" }));
			globalTimer = 0;
		}

		if(holdingForUser) {
			return;
		}

		timeRemaining = maxTime - timer;
		
		timer++;

		if(timeRemaining < 0)
			timeRemaining = 0;
		//If the timer just started (which is 420 seconds) and two agents are playing each other, then activate the UI disable:
		if(disable === true && disableStarted === false){
				disableStarted = true;
				document.addEventListener("click",disableFunction,true);
				console.log("stopped interraction")
		}
		//Reactivate the UI at the end of the game if two agents are playing each other so the user can proceed to the next section:
		if(timeRemaining === 1 && disable === true){
			document.removeEventListener("click", disableFunction, true)
			disable = false;
			console.log("got this far")
		}
		mins = Math.floor(timeRemaining / 60);
		secs = Math.floor(timeRemaining % 60);
		trueSecs = secs + "";
		if(secs < 10)
			trueSecs = "0" + secs;

        if($("input[name=lang]:checked").val() == "0"){
            $("#negoTimer").text("Time Remaining: " + mins + ":" + trueSecs);
        }
        else{
            $("#negoTimer").text("残り時間: " + mins + ":" + trueSecs);

        }

		if(readyToRestart) {
			readyToRestart = false;
			holdingForUser = true;
			webSocket.send(JSON.stringify({ tag: "openSurvey", data: "" }));
			$(".gameWrapper").addClass("hidden");
		}

		if(timer % 5 == 0) {
			webSocket.send(JSON.stringify({ tag: "time", data: timer }));
		}

		glowEmotionItem("butAnger", timer % 2 == 0 && angerGlow)
		glowEmotionItem("butSad", timer % 2 == 0 && sadGlow)
		glowEmotionItem("butNeutral", timer % 2 == 0 && neutralGlow)
		glowEmotionItem("butSurprised", timer % 2 == 0 && surprisedGlow)
		glowEmotionItem("butHappy", timer % 2 == 0 && happyGlow)
	}, 1000);


	//disable the offer grid to start
	$(".issueButton").prop("disabled", true);
	disableExchangeTable(true)

	//dialogStartGame("Welcome to IAGO! Prepare to begin!"); //TODO remove deprecated code no longer relevant (intermediate now used)						

	//specialty event to capture grid buttons with larger area
	$(".tableSlot").click(function(event)
			{
		var button = new Object();
		button.tag = "button";
		if(event.target.id.indexOf("divCol") >= 0) {
			button.data = "but" + event.target.id.substring(event.target.id.indexOf("Col"));
			buttonHandler(button);
		}
			});

	// Add an event that triggers when ANY button
	// on the page is clicked...
	$("input").click(function(event) {
		//send a message in the socket
		var button = new Object();
		button.tag = "button";
		button.data = event.target.id;
		buttonHandler(button);
	})

	$("button").click(function(event) {
		//send a message in the socket
		var button = new Object();
		button.tag = "button";
		button.data = event.target.id;
		buttonHandler(button);
	})

	$("#mySlider").on('input', function () {
		playerPresentedBATNA = $("#mySlider").val();
        if($("input[name=lang]:checked").val() == "0"){
            $("#batnaValue").text("I already have an offer for " + playerPresentedBATNA + " points.");
            $("#batnaDescription").text("This is the truth.");
        }
        else{
            $("#batnaValue").text("私のBATNAは" + playerPresentedBATNA + "ポイントです.");
            $("#batnaDescription").text("相手に本当の値を伝えます.");
        }
		trueBATNASpan = totalPlayerPoints * .05;

		lowerBATNARange = playerBATNA-trueBATNASpan;	//Allow +- 5% for a "truthful" BATNA.
		higherBATNARange = playerBATNA+trueBATNASpan;
		
		if (playerPresentedBATNA < lowerBATNARange) {
			//user is lying about BATNA
			$("#mySlider").css("background","red");
            if($("input[name=lang]:checked").val() == "0")
				$("#batnaDescription").text("This form of lying makes you look weak.");
            else
                $("#batnaDescription").text("嘘をつくと交渉がうまくいかない可能性があります.");
        } else if (playerPresentedBATNA >= lowerBATNARange && playerPresentedBATNA < higherBATNARange) {
			//user is telling the truth (first condition isn't strictly necessary but is included for clarity)
			$("#mySlider").css("background","green");
            if($("input[name=lang]:checked").val() == "0")
                $("#batnaDescription").text("This is the truth.");
            else
                $("#batnaDescription").text("相手に本当の値を伝えます.");
        } else {
			//user is lying about BATNA
			$("#mySlider").css("background","red");
            if($("input[name=lang]:checked").val() == "0")
                $("#batnaDescription").text("This form of lying makes you look strong.");
			else
            	$("#batnaDescription").text("嘘をつくと交渉がうまくいかない可能性があります.");
		}
	});

	$("#mySlider").mouseup(function()
			{
		trueBATNASpan = totalPlayerPoints * .05;
		lowerBATNARange = playerBATNA-trueBATNASpan;
		higherBATNARange = playerBATNA+trueBATNASpan;
		//user is telling the truth about his/her BATNA.
		if(playerPresentedBATNA >= lowerBATNARange && playerPresentedBATNA < higherBATNARange){
			$("#mySlider").val(playerBATNA);
			playerPresentedBATNA = playerBATNA;
            if($("input[name=lang]:checked").val() == "0"){
                $("#batnaValue").text("I already have an offer for " + playerPresentedBATNA + " points.");
                $("#batnaDescription").text("This is the truth.");
			}
			else{
                $("#batnaValue").text("私のBATNAは" + playerPresentedBATNA + "ポイントです.");
                $("#batnaDescription").text("相手に本当の値を伝えます.");
			}

		}
			});

	$(function () {
		$('[data-toggle="tooltip"]').tooltip()
	})
});


function changeLanguage(){
    if($("input[name=lang]:checked").val() == "0"){
        dispEnglish();
    }
    else{
        dispJapanese();
    }
}

function dispJapanese(){
    $(".langChange[lang=en]").addClass("hidden");
    $(".langChange[lang=jp]").removeClass("hidden");

    $(".messageHistory").addClass("hidden");
	$(".messageHistoryJp").removeClass("hidden");

    $("#vhDescription").html("<h1>交渉相手</h1><p>相手はワクワクしながら交渉に臨んでいます!</p>");

    $("#butStartOffer").html("提案を作成する");
    $("#butSendOffer").html("提案を送る");
    $("#butFormalAccept").html("提案を正式に承諾する");
    $("#butFormalQuit").html("ゲームを終える");
    $("#butAccept").html("提案を承諾する (拘束力はありません)");
    $("#butReject").html("提案を拒否する (拘束力はありません)");
    $("#butAcceptFavor").html("お願いを承諾する (拘束力はありません)");
    $("#butRejectFavor").html("お願いを拒否する (拘束力はありません)");
    $("#butViewPayoffs").html("報酬を見る");

    playerPresentedBATNA = $("#mySlider").val();
	$("#batnaValue").text("私のBATNAは" + playerPresentedBATNA + "ポイントです.");
	$("#batnaDescription").text("相手に本当の値を伝えます.");

    if (playerPresentedBATNA < lowerBATNARange) {
    	$("#batnaDescription").text("嘘をつくと交渉がうまくいかない可能性があります.");
    } else if (playerPresentedBATNA >= lowerBATNARange && playerPresentedBATNA < higherBATNARange) {
    	$("#batnaDescription").text("相手に本当の値を伝えます.");
    } else {
    	$("#batnaDescription").text("嘘をつくと交渉がうまくいかない可能性があります.");
    }

    if($("#butItemsComparison").hasClass("hidden")){
        $(".compare-panel-message").text("実際のBATNAの値を共有したい場合は, \"送信\"を押してください. より高い値を送信したい場合は, スライダーを右に動かしてください!");
    }
}

function dispEnglish() {
    $(".langChange[lang=en]").removeClass("hidden");
    $(".langChange[lang=jp]").addClass("hidden");

    $(".messageHistory").removeClass("hidden");
    $(".messageHistoryJp").addClass("hidden");

    $("#vhDescription").html("<h1>Opponent</h1><p>They are excited to begin negotiating!</p>");

    $("#butStartOffer").html("Start an offer");
    $("#butSendOffer").html("Send your offer");
    $("#butFormalAccept").html("Formally accept offer");
    $("#butFormalQuit").html("Formally quit game");
    $("#butAccept").html("Accept offer (non-binding)");
    $("#butReject").html("Reject offer (non-binding)");
    $("#butAcceptFavor").html("Accept favor (non-binding)");
    $("#butRejectFavor").html("Reject favor (non-binding)");
    $("#butViewPayoffs").html("View Payoffs");

    playerPresentedBATNA = $("#mySlider").val();
    $("#batnaValue").text("I already have an offer for " + playerPresentedBATNA + " points.");
    $("#batnaDescription").text("This is the truth.");

    if (playerPresentedBATNA < lowerBATNARange) {
        $("#batnaDescription").text("This form of lying makes you look weak.");
    } else if (playerPresentedBATNA >= lowerBATNARange && playerPresentedBATNA < higherBATNARange) {
        $("#batnaDescription").text("This is the truth.");
    } else {
        $("#batnaDescription").text("This form of lying makes you look strong.");
    }

    if ($("#butItemsComparison").hasClass("hidden")) {
        $(".compare-panel-message").text("If you want to share your actual walk-away value, press \"Send\" now. If you'd like to send a higher walk-away value, move the slider to the right!");
    }
}


function changeLanguageChatButton(id, text){

    if($("input[name=lang]:checked").val() == "0"){
        $("#" + id).val(text);
    }
    else{
		if(BUTTON_LANGUAGE_LIST.has(text)){
            $("#" + id).val(BUTTON_LANGUAGE_LIST.get(text));
        }
		else{
            $("#" + id).val(text);
        }
	}
}

function transJp(text){
	var isReplaced = false;
    for(key of LANGUAGE_LIST.keys()){
    	var regKey = key.replace(/\?/, "\\?");
		if(text.match(new RegExp(regKey))) {
            text = text.replace(new RegExp(regKey, "g"), LANGUAGE_LIST.get(key));
            isReplaced = true;
        }
    }

    if(isReplaced && text.match(/ points\?/)){
    	if(text.match(new RegExp("最低でも"))){
            text = text.replace(new RegExp(" points\\?", "g"), "ポイントはいただけませんか?");
        }
        else{
            text = text.replace(new RegExp(" points\\?", "g"), "ポイント以上ないと承諾しないのですか?");
        }
    }
    else if(!isReplaced){
        if(text.match(new RegExp("(Do you like|I like)"))){
            if(text.match(new RegExp("Do you like"))){
                text = text.replace(new RegExp("Do you like ", "g"), "あなたは");
                text = text.replace(new RegExp("\\?", "g"), "ですか?");
            }
            else{
                text = text.replace(new RegExp("I like ", "g"), "私は");
                text = text.replace(new RegExp("\\.", "g"), "です.");
            }

    		if(text.match(new RegExp("best"))){
                text = text.replace(new RegExp("( best| the best)", "g"), "が一番ほしい");
                for(key of REPLACE_LIST.keys()){
                	text = text.replace(new RegExp(key), REPLACE_LIST.get(key));
                }
			}
			else if(text.match(new RegExp("least"))){
                text = text.replace(new RegExp("( least| the least)", "g"), "が一番いらない");
                for(key of REPLACE_LIST.keys()){
                    text = text.replace(new RegExp(key), REPLACE_LIST.get(key));
                }
            }
            else {
                if (text.match(new RegExp("the same as"))) {
                    text = text.replace(new RegExp(" the same as ", "g"), "と同じくらい");
                    text = text.replace(new RegExp("です", "g"), "がほしいです");
                }
                else if (text.match(new RegExp("more than"))) {
                    text = text.replace(new RegExp(" more than ", "g"), "より");
                    text = text.replace(new RegExp("です", "g"), "がほしいです");
                }
                else if (text.match(new RegExp("less than"))) {
                    text = text.replace(new RegExp(" less than ", "g"), "より");
                    text = text.replace(new RegExp("です", "g"), "がいりません");
                }
                var matches = text.match(new RegExp("[^\\\x01-\\\x7E\\\xA1-\\\xDF]+[\\\s\\\w]+[^\\\x01-\\\x7E\\\xA1-\\\xDF]", "g"));
                matches = matches.map(function(string){return string.replace(new RegExp("[^\\\x01-\\\x7E\\\xA1-\\\xDF]+", "g"), "")});
                text = text.replace(new RegExp(matches[0]), REPLACE_LIST.get(matches[1]));
                text = text.replace(new RegExp(matches[1]), REPLACE_LIST.get(matches[0]));
            }

        }
        else if(text.match(new RegExp("(I'll get|You'll get)"))){
			text = text.replace(new RegExp("You'll get ", "g"), "あなたは");
			text = text.replace(new RegExp("I'll get ", "g"), "私は");
			text = text.replace(new RegExp(", and", "g"), "獲得し, ");
			text = text.replace(new RegExp("\\.", "g"), "獲得します.");


			var matches = text.match(new RegExp("[^\\\x01-\\\x7E\\\xA1-\\\xDF]+(all the |[0-9] )([\\s\\w]+)[^\\\x01-\\\x7E\\\xA1-\\\xDF]", "g"));
            matches = matches.map(function(string){return string.replace(new RegExp("[^\\\x01-\\\x7E\\\xA1-\\\xDF]+", "g"), "")});
            var item = new Array();
            var num = new Array();
            matches.forEach(function(data){
				if(data.match(/[0-9]/)){
					num.push(data.match(/[0-9]/) + "個");
					item.push(data.replace(/[0-9] /g, ""));
                }
                else{
                    num.push("全て");
                    item.push(data.replace(/all the /g, ""));
				}
			});
            for(var i = 0; i < num.length; i++){
                text = text.replace(new RegExp("(all the |[0-9] )([\\\s\\\w]+)"), REPLACE_LIST.get(item[i]) + "を" + num[i]);
            }

		}
	}

    return text;
}
