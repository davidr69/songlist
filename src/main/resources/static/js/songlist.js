export default class SongList {
	cache;

	constructor() {
		this.cache = {};
		this.#init();
	}

	#init = () => {
		fetch('api/v1/songs').then(resp => {
			resp.json().then(data => {
				this.cache = data;
				this.#drawTable('*');
			});
		});

		fetch('api/v1/songs/languages').then(resp => {
			resp.json().then(data => {
				this.#showLanguages(data);
			});
		});
	}

	#showLanguages = (data) => {
		let dd = document.getElementById('languages');
		for(let language of data) {
			let opt = new Option(language.language, language.id)
			dd.options.add(opt);
		}
	}

	changeLanguage = (obj) => {
		let language = obj[obj.selectedIndex].value;
		this.#drawTable(language);
	}

	#drawTable = (language) => {
		function notNull(param) {
			return (param == null ? "\u00A0" : param); // character 160
		}

		/*
			{
				"id":-8,
				"title":"Altar call",
				"key":null,
				"tempo":0,
				"author":null,
				"praise":false,
				"marker":true,
				"alias":null
			}
		*/
		let table = document.createElement('table');
		table.setAttribute('cellspacing', '1');
		table.setAttribute('cellpadding', '3');
		table.setAttribute('border', '0');
		table.setAttribute('id', 'songTable');

		let tr = document.createElement('tr');

		let th = document.createElement('th');
		let text = document.createTextNode('Song');
		th.appendChild(text);
		tr.appendChild(th);

		th = document.createElement('th');
		text = document.createTextNode('aka');
		th.appendChild(text);
		tr.appendChild(th);

		th = document.createElement('th');
		text = document.createTextNode('Tempo');
		th.appendChild(text);
		tr.appendChild(th);

		th = document.createElement('th');
		text = document.createTextNode('Key');
		th.appendChild(text);
		tr.appendChild(th);

		th = document.createElement('th');
		text = document.createTextNode('Author(s)');
		th.appendChild(text);
		tr.appendChild(th);

		table.appendChild(tr);

		for(let song of this.cache) {
			if(song.marker) {
				continue;
			}
			if(!(language === '*' || language === song.language?.toString())) {
				continue;
			}
			tr = document.createElement('tr');

			let td = document.createElement('td');
			text = document.createTextNode(song.title);
			td.appendChild(text);
			tr.appendChild(td);

			td = document.createElement('td');
			text = document.createTextNode(notNull(song.alias));
			td.appendChild(text);
			tr.appendChild(td);

			td = document.createElement('td');
			if(song.tempo === 0) {
				td.innerHtml = '&nbsp;';
			} else {
				td.setAttribute('style', 'text-align: right');
				text = document.createTextNode(notNull(song.tempo));
				td.appendChild(text);
			}
			tr.appendChild(td);

			td = document.createElement('td');
			text = document.createTextNode(notNull(song.key));
			td.appendChild(text);
			tr.appendChild(td);

			td = document.createElement('td');
			text = document.createTextNode(notNull(song.author));
			td.appendChild(text);
			tr.appendChild(td);

			table.appendChild(tr);
		}
		let content = document.getElementById('content');
		while(content.childNodes.length > 0) {
			content.removeChild(content.childNodes[0]);
		}
		content.appendChild(table);
	}
}
