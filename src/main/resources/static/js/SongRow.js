/*function uuidv4() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	let r = Math.random() * 16 | 0;
	let v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}*/
let rowId = 1;

export default class SongRow {
	constructor(parent) { // pass a handle to the table
		this.parent = parent;
		/*
			The template for a row is:
			  <tr id="row1">
			    <td class="songCell" id="songCell1"><select class="dropdown_modern" id="songlistDD"></select></td>
			    <td class="keyCell" id="keyCell1">Eb</td>
			    <td><faux onclick="insertSong(1)"> + </faux> / <faux onclick="deleteSong(1)">-</faux></td>
			    <td><input type="button" value="ok" id="ok1" onclick="makelist.commit('1')"/></td>
			  </tr>
		*/
//		let pos = uuidv4();

		let tr = document.createElement('tr');
		tr.setAttribute('id', `row${rowId}`);

		let td = document.createElement('td');
		tr.appendChild(td); // for the "x"

		td = document.createElement('td');
		td.setAttribute('class', 'songCell');
		td.setAttribute('id', `songCell${rowId}`);
		tr.appendChild(td);

		td = document.createElement('td');
		td.setAttribute('class', 'keyCell');
		td.setAttribute('id', `keyCell${rowId}`);
		tr.appendChild(td);

		td = document.createElement('td');
		let faux = document.createElement('faux');
		faux.setAttribute('onclick', `insertSong(${rowId})`);
		let text = document.createTextNode(' + ');
		faux.appendChild(text);
		td.appendChild(faux);
		// ---
		text = document.createTextNode(' / ');
		td.appendChild(text);
		// ---
		faux = document.createElement('faux');
		faux.setAttribute('onclick', `deleteSong('${rowId}')`);
		text = document.createTextNode(' - ');
		faux.appendChild(text);
		td.appendChild(faux);
		tr.appendChild(td);

		// <td><input type="button" value="ok" onclick="makelist.commit('1')"/></td>
		td = document.createElement('td');
		td.setAttribute('id', `button${rowId}`);
		let input = document.createElement('input');
		input.setAttribute('type', 'button');
		input.setAttribute('value', 'ok');
		input.setAttribute('onclick', 'songTable.saveRow()');
		td.appendChild(input);
		tr.appendChild(td);

		this.node = tr;
		this.songId = null;
		this.songName = null;
		this.songKey = null; // only if it differs from the template
		this.guid = rowId;

		rowId++;
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
		// first ensure that a selection was made
		var inputValue = this.parent.input.value; // shorthand
		if(inputValue == '') {
			alert('No song selected');
			return;
		}
		let keyCell = $(`#keyCell${this.guid}`).text();

		// 1. remove the ok button
		let buttonCell = document.getElementById(`button${this.guid}`);
		buttonCell.removeChild(buttonCell.childNodes[0]);

		//let cancel = document.getElementById('cancel');
		this.parent.cancel.parentNode.removeChild(this.parent.cancel);

		// 2. detach the drop-downs; already captured selected song
		this.parent.input.parentNode.removeChild(this.parent.input);

		// 3. convert the drop-downs to static text
		let statik = document.getElementById(`songCell${this.guid}`);
		statik.setAttribute('class', 'fakebox');
		let text = document.createTextNode(inputValue);
		statik.appendChild(text);

		let tuple = makelist.getSelected(inputValue);
//		this.editing.songName = input;
//		this.editing.songId = tuple.id;
//		this.editing.songKey = keyCell;
//		this.input.value = '';

		var tableRows = this.parent.table.children.length - 1;
		// 4. either append or update
		var rowIdx;
		for(rowIdx = 1; rowIdx <= tableRows; rowIdx++) {
			if(this.parent.table.childNodes[rowIdx].getAttribute('id') == `row${this.guid}`) {
				console.log(`rowIdx: ${rowIdx}, tableRows: ${tableRows}`);
				break;
			}
		}

		if(tableRows == rowIdx) {
			// do an auto-add of next row
			var newRow = new SongRow(this); // a row needs to be attached to a table
//			console.log(newRow);
		}

//		this.rows.push(this.editing); // commit the edit
//		console.log(this.editing);
//		this.editing = null;
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
