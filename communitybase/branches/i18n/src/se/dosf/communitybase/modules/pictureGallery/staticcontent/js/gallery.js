function viewComments(showAll){
	document.viewComments.viewComments.value=showAll;
	document.viewComments.submit();
}

function checkFlashSupport(url){
	
	// Major version of Flash required
	var requiredMajorVersion = 8;
	// Minor version of Flash required
	var requiredMinorVersion = 0;
	// Minor revision of Flash required
	var requiredRevision = 0;

	
	var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);
	
	if(!hasRequestedVersion){
		window.location = url;
	}
	
}