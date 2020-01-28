function isDirectory(){
	function isDirectory(){
		var refTab = document.getElementById("tableDataUser")
		var  ttl;
		// Loop through all rows and columns of the table and popup alert with the value
		// /content of each cell.
		for ( var i = 0; row = refTab.rows[i]; i++ ) {
		   row = refTab.rows[i];
		   for ( var j = 0; col = row.cells[j]; j++ ) {
		      alert(row.firstChild.nodeValue);
		   }
		}
		}
}