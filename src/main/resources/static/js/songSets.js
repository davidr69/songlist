export default class SongSets {
	constructor() {
		this.#init();
	}

	#init = () => {
		this.redraw();
		this.#get_songs();
		this.#get_leaders();
	}

	#flat_sharp = (note) =>{
		if(note == null || note === "null") {
			return "";
		}
		return note.replace(/#/, "&#9839;").replace(/b/, "&#9837;");
	}

	#get_service_id = () => {
		let service_dd = document.getElementById('selectService');
		return service_dd[service_dd.selectedIndex].value;
	}

	#get_songs = () => {
		const sel = document.getElementById('selectSong');

		fetch(`api/v1/songs/service/${this.#get_service_id()}`).then(resp => {
			resp.json().then(data => {
				for(let song of data) {
					if(song.id > 0) {
						let desc = song.title;
						if(song.author != null) {
							desc += ` (${song.author})`;
						}
						sel[sel.length] = new Option(desc, song.id);
					}
				}
			})
		});
	}

	#get_leaders = () => {
		const sel = document.getElementById('selectLeader');

		fetch(`api/v1/leaders?service=${this.#get_service_id()}`).then(resp => {
			resp.json().then(data => {
				for(let leader of data) {
					sel[sel.length] = new Option(leader.name, leader.id);
				}
			});
		});
	}

	#build_table = (data) => {
		let table = document.createElement('table');
		table.setAttribute('style', 'padding: 2px; border-spacing: 0px;');

		for(let item of data) {
			let tr = document.createElement('tr');
			let td = document.createElement('td');
			td.setAttribute('colspan', '2');

			let date;
			if(item.service.video == null) {
				date = document.createTextNode(item.service.formattedDate);
			} else {
				date = document.createElement('a');
				date.setAttribute('href', item.service.video);
				date.setAttribute('target', '_blank');
				let innerDate = document.createTextNode(item.service.formattedDate);
				date.appendChild(innerDate);
			}

			let time = document.createTextNode(item.service.formattedTime || '');

			td.appendChild(date);
			td.appendChild(document.createTextNode(' '));
			td.appendChild(time);
			td.appendChild(document.createTextNode(' '));

			if(item.service.leader != null) {
				td.appendChild(document.createTextNode(' - '));
				let span = document.createElement('span');
				span.setAttribute('class', 'leader');
				span.appendChild(document.createTextNode(item.service.leader));
				td.appendChild(span);
			}
			tr.appendChild(td);

			// print icon

			let printIcon = document.createElement('i'); // <i class="fa-solid fa-print"></i>
			printIcon.setAttribute('class', 'fa-solid fa-print');

			let printLink = document.createElement('a');	// <a href="javascript:makelist.old.print();">Printable</a> version
			printLink.setAttribute('href', `javascript:songsets.print(${item.service.id});`);
			printLink.appendChild(printIcon);

			td.appendChild(document.createTextNode('\u00A0'));
			td.appendChild(document.createTextNode('\u00A0'));
			td.appendChild(document.createTextNode('\u00A0'));
			td.appendChild(printLink);

			tr.appendChild(td);

			table.appendChild(tr);

			for(let song of item.songs) {
				let row = document.createElement('tr');
				let cell = document.createElement('td');

				let title = document.createTextNode(song.title);

				if(song.marker === true) {
					cell.setAttribute('colspan', '2');
					let span = document.createElement('span');
					span.setAttribute('class', 'marker');
					span.appendChild(title);
					cell.appendChild(span);
					row.appendChild(cell);
				} else {
					cell.innerHTML = this.#flat_sharp(song.key);
					row.appendChild(cell);
					cell = document.createElement('td');
					cell.appendChild(title);
					if(song.author != null) {
						let slant = document.createElement('i');
						slant.appendChild(document.createTextNode(` (${song.author})`));
						cell.appendChild(slant);
					}
					row.appendChild(cell);
				}
				table.appendChild(row);
			}

			tr = document.createElement('tr');
			td = document.createElement('td');
			td.setAttribute('colspan', '2');
			td.innerHTML = '&nbsp;';
			tr.appendChild(td);
			table.appendChild(tr);
		}
		let div = document.getElementById('song_sets');
		while(div.childNodes.length > 0) {
			div.removeChild(div.childNodes[0]);
		}
		div.appendChild(table);
	};

	redraw = () => {
		let song_dd = document.getElementById('selectSong');
		let song = song_dd[song_dd.selectedIndex].value;

		let leader_dd = document.getElementById('selectLeader');
		let leader = leader_dd[leader_dd.selectedIndex].value;

		fetch(`api/v1/songs/sets?service=${this.#get_service_id()}&song=${song}&leader=${leader}`).then(resp => {
			resp.json().then(data => {
				this.#build_table(data);
			});
		});
	};

	redir = () => {
		window.history.replaceState(null, '', `setList?service=${this.#get_service_id()}`);
		document.getElementById('selectSong').options.length = 1;
		document.getElementById('selectLeader').options.length = 1;
		this.#init();
	}

	print = (serviceId) => {
		let url = "print.html?serviceId=" + serviceId;
		window.open(url, "SetList", "width=750,height=600");
	}
}
