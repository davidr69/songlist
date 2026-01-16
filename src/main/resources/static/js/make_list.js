export default class MakeList {
	origData;
	selections;

	constructor() {
		this.origData = [];// when rows are inserted, the increment sequence is lost
		this.selections = [];
	}

	#formatItem = (item) => {
		let datum = item['title'];
		if(item['marker'] === true) {
			datum = `-----${datum}-----`;
		} else if(item['author'] != null) {
			datum = `${datum} (${item['author']})`;
		}
		return datum;
	}

	getSelectedSongs = () => {
		let service = document.getElementById('serviceId').value;
		if(service === 0) {
			return;
		}

		fetch(`rest/selectedSongs/${service}`).then(resp => {
			resp.json().then(data => {
				data.forEach(((item, idx) => {
					this.selections[item['id']] = idx;
				}))
			});
		}).catch(err => {
			alert(`Cannot get "selected songs": ${err}`);
		});
	};

	#getAllSongs = () => {
		fetch(`rest/allSongs`).then(resp => {
			resp.json().then(data => {
				let sel = document.getElementById('songlistDD');
				this.origData = [];

				for(let item of data) {
					let formatted = this.#formatItem(item);
					let opt = new Option(formatted);
					item['formatted'] = formatted;
					this.origData.push(item);
					sel.appendChild(opt);
				}
				common.handle('allSongsLoaded');

			});
		}).catch(err => {
			alert(`Cannot load song list: ${err}`);
		});
	};

	getSingerSongs = () => {
		let leader = document.getElementById('worshipLeader').value;
		fetch(`rest/singerSongs/${leader}`).then(resp => {
			resp.json().then(data => {
				let sel = document.getElementById('songlistDD');
				this.origData = [];
				for(let item of data) {
					let formatted = this.#formatItem(item);
					let opt = new Option(formatted);
					item['formatted'] = formatted;
					this.origData.push(item);
					sel.appendChild(opt);
				}
				// TODO: are there existing songs selected? Should keys change?
				common.handle('allSongsLoaded');

			});
		}).catch(err => {
			alert(`Cannot load song list for ${leader}: ${err}`);
		});
	};

	update = () => {
		if(document.getElementById('data option').length === 0) {
			alert('No selections made');
			return;
		}
		let songs = [];

		$('#data option').each(function(idx, item) {
			songs.push(item.value);
		})

		if($('#serviceId').val() == 0) {
			new_list(songs);
		} else {
			update_list(songs);
		}
	}

	updateList = (songlist) => {
		fetch({
			url: 'api/v1/songs/list',
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				songs: songlist,
				service: document.getElementById('serviceId').value
			})
		}).then(resp => {
			resp.json().then(data => {
				if(data.success === false) {
					alert(data.message);
				}
			});
		}).catch(err => {
			alert(`Error updating song list: ${err}`);
		})
	}

	newList = (songlist) => {
		fetch({
			url: `rest/songs/${songlist}`,
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				serviceType: document.getElementById('serviceType').val(),
				month: document.getElementById('month').val(),
				day: document.getElementById('day').val(),
				year: document.getElementById('year').val()
			})
		}).then(resp => {
			resp.json().then(data => {
				if(data.succes === true) {
					document.getElementById('serviceId').value = data.newListId;
				} else {
					alert(data.message);
				}
			});
		}).catch(err => {
			alert(`sad day! ${err}`);
		});
	}

	print = () => {
		let service = document.getElementById('serviceId').value;
		if(service !== 0) {
			let url = `print.html?serviceId=${service}`;
			window.open(url, 'Service', 'width=750,height=600');
		}
	};

	newSelect = () => {
		// [{id: -8, title: "Altar call", key: null, tempo: null, author: null, praise: null, marker: true, alias: null}]
		let html = '<select class="dropdown_modern">';
		for(let item of this.origData) {
			html += `<option value=${item.id}>${item.title}</option>`;
		}
		html += '</select><br/>';
		$('#selections').append(html);
	};

	getSelected = (selected) => {
		let tuple = this.origData.filter(item => item.formatted === selected);
		return tuple[0];
	}
}
