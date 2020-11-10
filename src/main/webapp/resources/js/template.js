var prevScroll = window.pageYOffset;

window.onscroll = function() {
	var currentScroll = window.pageYOffset;
	if (prevScroll - 5 > currentScroll) {
		$('#menuTop').fadeIn(500);
	} else if (prevScroll + 5 < currentScroll) {
		$('#menuTop').fadeOut(500);
	}
	prevScroll = currentScroll;
}