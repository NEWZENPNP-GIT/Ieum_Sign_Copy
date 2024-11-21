function DrawUtils() {
}

DrawUtils.drawRect = function (p1, p2, gl, pLoc) {
	var vertices = [
		p1.x, p1.y, 0,
		p1.x, p2.y, 0,
		p2.x, p1.y, 0,
		p2.x, p2.y, 0
	];
	
	var itemSize = 3;
	var numItems = 4;
	
	var posBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, posBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);	
	gl.vertexAttribPointer(pLoc, itemSize, gl.FLOAT, false, 0, 0);
	
	gl.drawArrays(gl.TRIANGLE_STRIP, 0, numItems);
}

DrawUtils.drawDiamond = function (center, radius, gl, pLoc) {
	var vertices = [
		center.x, center.y + radius, 0,
		center.x - radius, center.y, 0,
		center.x + radius, center.y, 0,
		center.x, center.y - radius, 0
	];
	
	var itemSize = 3;
	var numItems = 4;
	
	var posBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, posBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);	
	gl.vertexAttribPointer(pLoc, itemSize, gl.FLOAT, false, 0, 0);
	
	gl.drawArrays(gl.TRIANGLE_STRIP, 0, numItems);
}

DrawUtils.drawPolygon = function (points, gl, pLoc) {
	var vertices = [];
	
	for (var i = 0; i < points.length; i++) {
		var p = points[i];
		vertices.push(p.x, p.y, p.z);
	}
	
	var itemSize = 3;
	var numItems = vertices.length / itemSize;
	
	var posBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, posBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);	
	gl.vertexAttribPointer(pLoc, itemSize, gl.FLOAT, false, 0, 0);
	
	gl.drawArrays(gl.TRIANGLE_FAN, 0, numItems);
}

DrawUtils.drawPoints = function (points, gl, pLoc) {
	var vertices = [];
	
	for (var i = 0; i < points.length; i++) {
		var p = points[i];
		vertices.push(p.x, p.y, p.z);
	}
	
	var itemSize = 3;
	var numItems = vertices.length / itemSize;
	
	var posBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, posBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);	
	gl.vertexAttribPointer(pLoc, itemSize, gl.FLOAT, false, 0, 0);
	
	gl.drawArrays(gl.POINTS, 0, numItems);
}

DrawUtils.drawPolyline = function (points, gl, pLoc) {
	if (points.length==0) return;
	var vertices = [];
	
	for (var i = 0; i < points.length; i++) {
		var p = points[i];
		vertices.push(p.x, p.y, p.z);
		if(i==points.length-1 && points.length ==2) {
			vertices.push(p.x, p.y, p.z);
		}
	}
	
	var itemSize = 3;
	var numItems = vertices.length / itemSize;
	
	var posBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, posBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);	
	gl.vertexAttribPointer(pLoc, itemSize, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(pLoc);
	
	gl.drawArrays(gl.LINE_STRIP, 0, numItems);
	
}