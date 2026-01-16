var months = ["", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

Array.prototype.has = function(item)
{
	for(var i=0; i<this.length; i++) {
		if(this[i] === item) {
			return true;
		}
	}
	return false;
};

function stripz(val)
{
	if(val.length > 0)
		while(val.substr(0, 1) == "0")
			val = val.substr(1);
	return val;
}

// create namespace
window['common'] = {};
common.observers = {};

common.handle = function(event) {
	if(event in common.observers) {
		common.observers[event]();
	}
};

common.parseUrlParams = function() {
	let parts = window.location.href.split('?');
	if(parts.length == 2) {
		let arr = { };
		for(item of parts[1].split('&')) {
			let kvp = item.split('=');
			if(kvp.length == 2) {
				arr[kvp[0]] = kvp[1];
			}
		}
		return arr;
	} else {
		return null;
	}
}
