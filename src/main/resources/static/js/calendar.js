const monarr = [
	"January", "February", "March", "April", "May", 'June', 'July',
	"August", "September", "October", "November", "December"
];
const keep = ["weekend", "weekday", "line_right"];

function find_pos(obj) {
	let curleft = 0;
	let curtop = 0;
	do {
		curleft += obj.offsetLeft;
		curtop += obj.offsetTop;
	} while(obj = obj.offsetParent);
	return [curleft, curtop];
}

class Calendar {
	constructor(id, monLabel, mydate, objName, dispDate) {
		this.id = id; // id name of table
		this.mydate = mydate;
		this.when = monLabel;
		this.myName = objName;
		this.dispDate = dispDate;
	}

	showDate() {
		let when = this.mydate;
		document.getElementById(this.dispDate).innerHTML = `${monarr[when.getMonth()]} ${when.getDate()}, ${when.getFullYear()}`;
	}

	show_cal() {
		let mydate = this.mydate;
		let month = mydate.getMonth();
		let year = mydate.getFullYear();
		let day = mydate.getDate();

		let now = new Date(year, month, 1);
		let dow = now.getDay(); // what day of week is the 1st?

		let before = new Date(now - 1); // object for last day of previous month
		let prevLastDay = before.getDate(); // actual day (numeric)

		let lastDay = findLastDay(month, year);

		document.getElementById(this.when).innerHTML = `${monarr[month]} ${year}`;
		let tbl = document.getElementById(this.id);
		let row = tbl.rows[2];
		// filler section
		let i = 0;
		if(dow !== 0) {
			let filler = prevLastDay - dow + 1;
			for(i=0; i<dow; i++) {
				row.cells[i].innerHTML = filler++;
				let attr = get_base(row.cells[i].className);
				attr += " fadeday";
				row.cells[i].setAttribute("class", attr);
				row.cells[i].removeAttribute("onMouseOver");
				row.cells[i].removeAttribute("onMouseOut");
				row.cells[i].removeAttribute("onClick");
			}
		}
		let today = 1;
		let rowidx = 2;
		let temp;
		// mid section
		while(today <= lastDay) {
			row = tbl.rows[rowidx++];
			while(i < 7 && today <= lastDay) {
				let attr = get_base(row.cells[i].className);
				if(today === day) {
					attr += " today";
				}
				row.cells[i].setAttribute("onMouseOver", this.myName + ".rollOver(this)");
				row.cells[i].setAttribute("onMouseOut", this.myName + ".rollOff(this)");
				row.cells[i].setAttribute("onClick", this.myName + ".setDay(" + today +")");
				row.cells[i].setAttribute("class", attr);
				row.cells[i].innerHTML = today++;
				i++;
				temp = i;
			}
			i = 0;
		}
		var filler = 1;
		for(i=temp; i<7; i++) {
			row.cells[i].innerHTML = filler++;
			let attr = get_base(row.cells[i].className);
			attr += " fadeday";
			row.cells[i].setAttribute("class", attr);
			row.cells[i].removeAttribute("onMouseOver");
			row.cells[i].removeAttribute("onMouseOut");
			row.cells[i].removeAttribute("onClick");
		}
		while(rowidx < 8) {
			row = tbl.rows[rowidx++];
			for(i=0; i<7; i++) {
				row.cells[i].innerHTML = filler++;
				let attr = get_base(row.cells[i].className);
				attr += " fadeday";
				row.cells[i].setAttribute("class", attr);
				row.cells[i].removeAttribute("onMouseOver");
				row.cells[i].removeAttribute("onMouseOut");
				row.cells[i].removeAttribute("onClick");
			}
		}
		this.showDate();
	}

	rollOver(obj) {
		this.preserve = obj.className;
		obj.className = "rollOver";
	}

	rollOff(obj) {
		obj.className = this.preserve;
	}

	setDay(day) {
		let when = this.mydate;
		when.setDate(day);
		this.mydate = when;
		this.showDate();
	}

	next_mon() {
		let when = this.mydate;
		let month = when.getMonth();
		let year = when.getFullYear();
		let day = when.getDate();
		month++;
		if(month === 12) {
			this.mydate = new Date(year + 1, 0, day);
		} else {
			this.mydate = new Date(year, month, day);
		}
		this.show_cal();
	}

	prev_mon() {
		let when = this.mydate;
		let month = when.getMonth();
		let year = when.getFullYear();
		let day = when.getDate();
		month--;
		if(month < 0) {
			this.mydate = new Date(year - 1, 11, day);
		} else {
			this.mydate = new Date(year, month, day);
		}
		this.show_cal();
	}

	curDate() {
		let when = this.mydate;
		return {"month": when.getMonth(), "day": when.getDate(), "year": when.getFullYear()};
	}

}

function get_base(classes)
{
	let newarr = [];
	let fields = classes.split(" ");
	for(let i=0; i<fields.length; i++) {
		let field = fields[i];
		for(let j=0; j<keep.length; j++) {
			let item = keep[j];
			if(field === item) {
				newarr[newarr.length] = field;
				break;
			}
		}
	}
	return newarr.join(" ");
}

function findLastDay(mon, yr)
{
	let monarr = [31,28,31,30,31,30,31,31,30,31,30,31];
// Rules:
//   1. Year is leap year if year is equally divisible by 4
//   2. Century years are not leap years (generally)
//   3. Century years equally divisible by 400 are leap years
	let leap = 0;
	if(mon === 1) {
		if(yr%4 === 0) {				// rule #1
			leap = 1;
			if (yr%100 === 0) {		// rule #2
				leap = 0;			// does not have 29th day
				if(yr%400 === 0) {	// rule #3
					leap = 1;
				}
			}
		}
	}
    monarr[1] += leap;
	return monarr[mon];
}
