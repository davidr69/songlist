print = {}

print.init = function() {
	let params = common.parseUrlParams();
	let active_calls = 2;

	function flat_sharp(note) {
		if(note == null || note == "null") {
			return "";
		}
		return note.replace(/#/, "&#9839;").replace(/b/, "&#9837;");
	}

	function formatTime(time) {
		// must be in the format hh:mm:ss
		let parts = time.split(':');
		if(parts[0] == 0) {
			return `12:${parts[1]}am`
		} else if(parts[0] < 12) {
			return `${parts[0]}:${parts[1]}am`;
		} else {
			return `${parts[0] - 12}:${parts[1]}pm`
		}
	}

	function callback() {
		if(--active_calls == 0) {
			document.body.style.display = 'inline';
		}
	}

	const draw = (data) => {
		let songSet = document.getElementById("songSet");
		for(let tuple of data) {
			let tr = document.createElement('tr');
			if(tuple.marker) {
				let td = document.createElement('td');
				td.setAttribute('class', 'list bold');
				td.setAttribute('colspan', '3');
				td.appendChild(document.createElement('br'));
				td.appendChild(document.createTextNode(tuple.title));
				td.appendChild(document.createElement('br'));
				tr.appendChild(td);
			} else {
				let td = document.createElement('td');
				td.setAttribute('class', 'list');
				td.innerHTML = flat_sharp(tuple.note);
				tr.appendChild(td);

				let author = tuple.author == null ? '' : `<span class="slant">(${tuple.author})</span>`;
				let leader = tuple.leader == null ? '' : `<span class='fade'>&nbsp;&nbsp;. . . ${tuple.leader}</span>`;
				let aka = tuple.aka == null ? '' : `... <span class="aka">&laquo;${tuple.aka}&raquo;</span>`;
				let extra = '';
				if(tuple.memo != null) {
					extra = ` <span class='memo'>[${tuple.memo}]</span>`;
				}
				td = document.createElement('td');
				td.setAttribute('class', 'list');
				td.innerHTML = `${tuple.title} ${author} ${aka} ${leader}${extra}`;
				tr.appendChild(td);

				td = document.createElement('td');
				td.setAttribute('class', 'list');
				td.setAttribute('style', 'text-align: right');
				td.appendChild(document.createTextNode(tuple.tempo == null ? '' : tuple.tempo));
				tr.appendChild(td);
			}
			songSet.appendChild(tr);
		}
		callback();
	}

	const draw_meta = (data) => {
		document.title = `${data.service} - ${data.formattedDate}`;
		document.getElementById("serviceName").innerHTML = data.service;
		if(data.time != null) {
			data.formattedDate += ` - ${formatTime(data.time)}`;
		}
		document.getElementById("serviceDate").innerHTML = data.formattedDate;
		if(data.leader != null) {
			document.getElementById("leader").innerHTML = data.leader;
		}
		callback();
}

	const get_songs = async() => {
		const resp = await fetch(`api/v1/songs/selected/${params.serviceId}`);
		if(resp.status === 200) {
			const data = await resp.json();
			draw(data);
		} else {
			alert('Could not obtain song data');
		}
	}

	const get_metadata = async() => {
		const resp = await fetch(`api/v1/services/active/${params.serviceId}`);
		if(resp.status === 200) {
			const data = await resp.json();
			draw_meta(data);
		} else {
			alert('Could not obtain song data');
		}
	}

	get_songs();
	get_metadata();
}
