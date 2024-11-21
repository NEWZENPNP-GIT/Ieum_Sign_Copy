
$(document).ready(function(){
	var oEditors = [];

	nhn.husky.EZCreator.createInIFrame({
	    oAppRef: oEditors,
	    elPlaceHolder: "ir1",
	    sSkinURI: "smarteditor/SmartEditor2Skin.html",
	    fCreator: "createSEditor2"

	});
	
})
