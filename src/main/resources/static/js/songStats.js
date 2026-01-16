export default class SongStats {
	constructor() {
		this.#init();
	}

	#init = () => {
		// determine what service id is selected
		let serviceId;
		let q = (window.location + '').split('?'); // not splitting on '=' to make no assumptions
		if(q.length === 2) {
			let svcArr = q[1].split('=');
			if(svcArr.length === 2) {
				if(svcArr[0] === 'service') { // properly formed URL
					serviceId = svcArr[1];
				}
			}
		}

		let f = document.getElementById('services');

		fetch('api/v1/services').then( resp => {
			resp.json().then( data => {
				let count = 0;
				for(let service of data) {
					f.options[count++] = new Option(service['description'], service['id'], false, service['id'] == serviceId);
				}
			});
		});
	}

	redir = (obj) => {
		let val = obj[obj.selectedIndex].value;
		window.location = `songStats?service=${val}`;
	}
}
