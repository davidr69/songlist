export default class Print {
	params;
	active_calls;

	constructor() {
		this.params = common.parseUrlParams();
		this.active_calls = 2;
		this.#init();
	}

	#init = () => {
		this.#get_metadata();
	}

	#flat_sharp = (note) => {
		if(note == null || note === "null") {
			return "";
		}
		return note.replace(/#/, "&#9839;").replace(/b/, "&#9837;");
	}

	#callback = () => {
		if(--this.active_calls === 0) {
			document.body.style.display = 'inline';
		}
	}

	#draw = (data) => {
		let songSet = document.getElementById("songSet");
		for(let tuple of data) {
			let tr = document.createElement('tr');
			if(tuple.song.marker) {
				let td = document.createElement('td');
				td.setAttribute('class', 'list bold');
				td.setAttribute('colspan', '3');
				td.appendChild(document.createElement('br'));
				td.appendChild(document.createTextNode(tuple.song.title));
				td.appendChild(document.createElement('br'));
				tr.appendChild(td);
			} else {
				let td = document.createElement('td');
				td.setAttribute('class', 'list');
				let note = tuple.keyOverride == null ? tuple.song.note : tuple.keyOverride;
				td.innerHTML = this.#flat_sharp(note);
				tr.appendChild(td);

				let author = tuple.song.author == null ? '' : `<span class="slant">(${tuple.song.author})</span>`;
				let leader = tuple.leader == null ? '' : `<span class='fade'>&nbsp;&nbsp;. . . ${tuple.leader}</span>`;
				let aka = tuple.song.alias == null ? '' : `... <span class="aka">&laquo;${tuple.song.alias}&raquo;</span>`;
				let extra = '';
				if(tuple.memo != null) {
					extra = ` <span class='memo'>[${tuple.memo}]</span>`;
				}
				td = document.createElement('td');
				td.setAttribute('class', 'list');
				td.innerHTML = `${tuple.song.title} ${author} ${aka} ${leader}${extra}`;
				tr.appendChild(td);

				td = document.createElement('td');
				td.setAttribute('class', 'list');
				td.setAttribute('style', 'text-align: right');
				td.appendChild(document.createTextNode(tuple.tempo == null ? '' : tuple.tempo));
				tr.appendChild(td);
			}
			songSet.appendChild(tr);
		}
		this.#callback();
	}

	#draw_meta = (data) => {
		const theDate = data.mydate.substring(0, 10);
		document.title = `${data.service.description} - ${theDate} @ ${data.service.serviceTime}`;
		document.getElementById("serviceName").innerHTML = data.service.description;
		document.getElementById("serviceDate").innerHTML = data.formattedDateTime;
		if(data.leader != null) {
			document.getElementById("leader").innerHTML = data.leader.name;
		}
		this.#callback();
}

	#get_metadata = async() => {
		const resp = await fetch(`api/v1/services/active/${this.params.serviceId}`);
		if(resp.status === 200) {
			const data = await resp.json();
			this.#draw_meta(data);
			this.#draw(data.details);
		} else {
			alert('Could not obtain song data');
		}
	}
}
