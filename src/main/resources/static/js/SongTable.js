const TableCols = {
	CANCEL: 0,
	SONG: 1,
	KEY: 2,
	BUTTON: 4
};
// no "3", since that's the UP/DOWN or +/-

export default class SongTable {
	/*
		Some things to consider:
		-	"nextRow" is merely a unique number and may not reflect the number of rows,
			especially if deletes occur
	*/
	constructor(el) {
		this.table = document.createElement('table');
		this.nextRow = 0;
		this.rows = [];

		let tr = document.createElement('tr');

		// header row
		let th = document.createElement('th');
		tr.appendChild(th);
		th = document.createElement('th');
		let text = document.createTextNode('Select song');
		th.appendChild(text);
		tr.appendChild(th);
		th = document.createElement('th');
		text = document.createTextNode('Key');
		th.appendChild(text);
		tr.appendChild(th);
		th = document.createElement('th');
		text = document.createTextNode('Edit');
		th.appendChild(text);
		tr.appendChild(th);
		th = document.createElement('th');
		th.innerHTML = '&nbsp;';
		tr.appendChild(th);
		this.table.appendChild(tr);

		document.getElementById(el).appendChild(this.table);

		let input = document.createElement('input');
		input.setAttribute('class', 'dropdown_modern');
		input.setAttribute('list', 'songlistDD');
		input.addEventListener('change', function() {
			let tuple = makelist.getSelected(input.value);
			let keycell = input.parentNode.parentNode.childNodes[TableCols.KEY]; // nav up to td, then tr, then go to 3rd td which is the key
			if(keycell.childNodes.length == 1) {
				// what if the leader's key is different?
				let child = keycell.childNodes[0];
				keycell.removeChild(child);
			}
			let newkey = document.createTextNode(tuple.key || '');
			keycell.appendChild(newkey);
		});
		this.input = input;

		let cancel = document.createElement('faux');
		cancel.setAttribute('id', 'cancel');
		cancel.setAttribute('class', 'fa fa-times');
		cancel.addEventListener('click', function() {
			input.value = '';
		});
		this.cancel = cancel;
	}

	editRow(songRow) {
		let cell = document.getElementById(`songCell${songRow.guid}`);
		if(this.input.parentNode != null) {
			this.input.parentNode.removeChild(this.input);
		}
		cell.appendChild(this.input);
		let check = document.getElementById(`row${songRow.guid}`).childNodes[TableCols.CANCEL];
		check.appendChild(this.cancel);
		this.editing = songRow;
	}

	newRow() {
		let _row = new SongRow(this);
		this.table.appendChild(_row.node);
		return _row;
	}

	/*
		"Save" can be one of two things: append the list, or update an existing entry.
		In either case, the common operations are:
		1. remove the "ok" button
		2. detach drop-downs
		3. convert the drop-downs to static text
		4. either add event handlers or a button to go into edit mode
	*/
	saveRow() {
		this.editing.saveRow();
		return;
		// first ensure that a selection was made
		if(this.input.value == '') {
			alert('No song selected');
			return;
		}
		let guid = this.editing.guid;
		let keyCell = $(`#keyCell${guid}`).text();

		// 1. remove the ok button
		let buttonCell = document.getElementById(`button${guid}`);
		buttonCell.removeChild(buttonCell.childNodes[0]);

		//let cancel = document.getElementById('cancel');
		this.cancel.parentNode.removeChild(this.cancel);

		// 2. detach the drop-downs; already captured selected song
		this.input.parentNode.removeChild(this.input);

		// 3. convert the drop-downs to static text
		let statik = document.getElementById(`songCell${guid}`);
		statik.setAttribute('class', 'fakebox');
		let text = document.createTextNode(this.input.value);
		statik.appendChild(text);

		let tuple = makelist.getSelected(this.input.value);
		this.editing.songName = this.input.value;
		this.editing.songId = tuple.id;
		this.editing.songKey = keyCell;
		this.input.value = '';

		let tableRows = this.table.children.length - 1;
		// 4. either append or update
		var rowIdx;
		for(rowIdx = 1; rowIdx <= tableRows; rowIdx++) {
			if(this.table.childNodes[rowIdx].getAttribute('id') == `row${guid}`) {
				console.log(`rowIdx: ${rowIdx}, tableRows: ${tableRows}`);
				break;
			}
		}

		if(tableRows == rowIdx) {
			// do an auto-add of next row
			var newRow = new SongRow(this); // a row needs to be attached to a table
//			console.log(newRow);
		}

		this.rows.push(this.editing); // commit the edit
		console.log(this.editing);
		this.editing = null;
/*

		let node = document.getElementById('songCell' + num);
		if(num == this.rows.length) { // last row
			let nextRow = new SongRow(1, 'Every Praise', 'Gb');
			this.rows.push(nextRow);

			let newRow = this.newRow(); // don't have to specify the position for append
			console.log(this.table.childNodes[1]);
			this.table.appendChild(newRow); // appends to "tbody"

			// detach from parent
			let node = document.getElementById('selectedSong');
			node.parentNode.removeChild(node);
			newRow.childNodes[0].appendChild(node); // first child should be <td>
			node.value = '';*/
	}
}
