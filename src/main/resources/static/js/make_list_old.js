createNamespace('makelist.old');

makelist.old.init = function() {
	let indexes = [];
	let selections = [];

	function format_item(item) {
		let datum = item['title'];
		if(item['marker'] === true) {
			datum = '----- ' + datum + ' -----';
		} else if(item['author'] != null) {
			datum = datum + ' (' + item['author'] + ')';
		}
		return datum;
	}

	function get_selected_songs() {
		let service = document.getElementById('serviceId').value;
		if(service == 0) {
			return;
		}

		fetch(`api/v1/songs/selected/${service}`).then(resp => {
			resp.json().then(data => {
				let f = document.forms[0];
				data.forEach(function(item, idx) {
					selections[item['id']] = idx;
					f.data.options[idx] = new Option(format_item(item), item['id']);
				});
			});
		}).catch(e => {
			alert(`Could not get "selected songs": ${e}`);
		});
	}

	function get_all_songs() {
		fetch('api/v1/songs').then(resp => {
			resp.json().then(data => {
				let f = document.forms[0];
				data.forEach(function(item, idx) {
					indexes[item['id']] = idx;
					f.songlist.options[idx] = new Option(format_item(item), item['id']);
				});
			});
		}).catch(e => {
			alert(`Could not get "all songs": ${e}`);
		});
	}

    get_selected_songs();
	get_all_songs();
}

makelist.old.add_data = function() {
	const f = document.forms[0];
	const si = f.songlist.options.selectedIndex;
	if(si == -1) {
		alert("You must select a song");
		return;
	}
	const songid = f.songlist.options[si].value;
	const song = f.songlist.options[si].text;
	f.data.options[f.data.options.length] = new Option(song, songid);
}

makelist.old.del_data = function() {
	const f = document.forms[0];
	const si = f.data.options.selectedIndex;
	if(si == -1) {
		alert("You must first select an item from the list");
	} else {
		f.data.options[si] = null;
	}
}

makelist.old.move_up = function() {
	const f = document.forms[0];
	const si = f.data.options.selectedIndex;
	if(si==0) {
		alert("Already at top.");
		return;
	}
	if(si<0) {
		alert("You must first select an item from the list");
		return;
	}
	const prev_val = f.data.options[si-1].value;
	const prev_text = f.data.options[si-1].text;
	f.data.options[si-1].value = f.data.options[si].value;
	f.data.options[si-1].text = f.data.options[si].text;
	f.data.options[si].value = prev_val;
	f.data.options[si].text = prev_text;
	f.data.options.selectedIndex = si-1;
}

makelist.old.move_down = function() {
	const f = document.forms[0];
	const si = f.data.options.selectedIndex;
	if(si==(f.data.length-1)) {
		alert("Already at bottom.");
		return;
	}
	if(si<0) {
		alert("You must first select an item from the list");
		return;
	}
	const next_val = f.data.options[si+1].value;
	const next_text = f.data.options[si+1].text;
	f.data.options[si+1].value = f.data.options[si].value;
	f.data.options[si+1].text = f.data.options[si].text;
	f.data.options[si].value = next_val;
	f.data.options[si].text = next_text;
	f.data.options.selectedIndex = si+1;
}

makelist.old.update = function() {
	const post_form = (data) => Object.keys(data).map(key => `${encodeURIComponent(key)}=${encodeURIComponent(data[key])}`).join('&');
	const post_headers = {
		'Content-Type': 'application/x-www-form-urlencoded',
		'Accept': 'application/json'
	};

	function update_list(songlist) {
		const data = {
			songs:	songlist,
			service: document.getElementById('serviceId').value
		};
		fetch('api/v1/songs/list', {
			method:	'PUT',
			headers: post_headers,
			body: post_form(data)
		}).then(resp => {
			resp.json().then(data => {
				if(data.success === false) {
					alert(data.message);
				}
			})
		}).catch(e => {
			alert(`Coud not create list: ${e}`);
		});
	}

	function new_list(songlist) {
		let data = {
			songs: songlist,
			serviceType: document.getElementById('serviceType').value,
			month: document.getElementById('month').value,
			day: document.getElementById('day').value,
			year: document.getElementById('year').value,
			hour: document.getElementById('hour').value,
			minute: document.getElementById('minute').value
		};

		fetch('api/v1/songs/list', {
			method:	'POST',
			headers: post_headers,
			body: post_form(data)
		}).then(resp => {
			resp.json().then(data => {
				if(data.success === true) {
					document.getElementById('serviceId').value = data.newListId;
				} else {
					alert(data.message);
				}
			});
		}).catch(e => {
			alert(`Coud not create list: ${e}`);
		});
	}

	let data = document.getElementById('data');
	if(data.options.length == 0) {
		alert('No selections made');
		return;
	}
	let songs = [];

	for(let item of data.options) {
		songs.push(item.value);
	}

	if(document.getElementById('serviceId').value == 0) {
		new_list(songs);
	} else {
		update_list(songs);
	}
}
