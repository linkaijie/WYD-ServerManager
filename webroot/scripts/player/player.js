function bannedPlayer(playerId,logType){
	lhgdialog.opendlg('玩家封号',ctx+"/jsp/player/playerForm.jsp?playerId="+playerId+"&logType="+logType,500,240,true,true);
}
		
function gagPlayer(playerId,logType){
	lhgdialog.opendlg('玩家禁言',ctx+"/jsp/player/gagPlayerForm.jsp?playerId="+playerId+"&logType="+logType,500,240,true,true);
}