
// Pager draw module
Pager = function() {
	var num = 0;
	var color = '#000000';
	var lineWidth = 1;
	var canvas = null;
	var context = null;
	var dataURL = null;
	var forms = [];
	var items = [];
	var initX = 480;
	var initY = 1100;
	var initScale = 0.5;
	var initPoint = 140/72.0;
	
	// public variables & methods..
	return {
		initialize: function() {
			canvas = document.getElementById('drawPager');
			if (!canvas) {
				alert('해당 브라우져는 수기서명을 지원하지 않습니다.');
				return;
			}
			if (!canvas.getContext) {
				alert('해당 브라우져[Context]는 수기서명을 지원하지 않습니다.');
				return;
			}
			context = canvas.getContext('2d');
			if (!context) {
				alert('해당 브라우져[2d Canvas]는 수기서명을 지원하지 않습니다.');
				return;
			}
			
			context.imageSmoothingEnabled = true;
			context.webkitImageSmoothingEnabled = true;
			context.mozImageSmoothingEnabled = true;
			
		},
		clear: function() {
			context.clearRect(0, 0, canvas.width, canvas.height);
		},
		getPage: function() {
			return num;
		},
		loadPage: function(page, URL) {
			num = page;
			if(URL) dataURL = URL;
			
			var page = new Image();
			
			page.onload = function() {
				var imgWidth = page.naturalWidth;
				var screenWidth  = canvas.width;
				var scaleX = 1;
				if (imgWidth > screenWidth)
					scaleX = screenWidth/imgWidth;
				
				var imgHeight = page.naturalHeight;
				var screenHeight = canvas.height;
				
				var scaleY = 1;
				if (imgHeight > screenHeight)
					scaleY = screenHeight/imgHeight;
				
				var scale = scaleY;
				if(scaleX < scaleY)
					scale = scaleX;
				/*
				if(scale<1)
					scale = 1.0;
				*/
				
				imgHeight = imgHeight*scale;
				imgWidth = imgWidth*scale;
				
				canvas.height = imgHeight;
				canvas.width = imgWidth;
				
				context.drawImage(page, 0, 0, page.naturalWidth, page.naturalHeight, 0,0, imgWidth, imgHeight);
				//context.drawImage(page, 0, 0, 896, 1270);
				Pager.drawAll();
				//context.drawImage(page, 0, 0, canvas.width, canvas.height);
			}
			page.src = dataURL;
		},
		savePage: function() {
			return JSON.stringify({kform: forms});
		},
		changeColor: function(hexColor) {
			color = hexColor;
		},
		getColor: function() {
			return color;
		},
		setItem: function(key, line) {

			// 이전 서명은 삭제처리
			for (var i in forms) {
				if(forms[i].key == key) {
					forms.splice(i, 1);
					break;
				}
			}
			
			formdata = {
					key: key,
					page: num,
					value: '',
					image: '',
					path: []
				}
			
			for(var j=0;j<line.length;j++) {
				for (var k=0; k<line[j].data.length;k++) {
					var point = line[j].data[k];
					var lineX = (point.x*initScale);
					var lineY = (point.y*initScale);
					line[j].data[k].x = lineX;
					line[j].data[k].y = lineY;
				}
			}
			
			formdata.path = line;
			forms.push(formdata);
			//console.log(forms);
		},
		getItem: function() {
			return forms;
		},
		changeLineWidth: function(width) {
			lineWidth = width;
			context.lineWidth = width;
		},
		drawLine: function(color, width, data) {
			var lineX = 0;
			var lineY = 0;
			if (data.length > 0) {
				var preLineWidth = context.lineWidth;
				var preColor = context.strokeStyle;
				
				context.beginPath();
				context.lineWidth = width;
				context.strokeStyle = color;
				lineX = initX + data[0].x;
				lineY = initY + data[0].y;
				context.moveTo(lineX, lineY);
				for (var i = 1; i < data.length; i++) {
					lineX = initX + data[i].x;
					lineY = initY + data[i].y;
					context.lineTo(lineX, lineY);
				}
				context.stroke();
				context.closePath();
				
				context.lineWidth = preLineWidth;
				context.strokeStyle = preColor;
			}
		},
		drawAll: function() {
			for (var i in forms) {
				for (var j in forms[i].path) {					
					if (forms[i].path[j].undo == false)
						// 초기 좌표값 설정
						var id = forms[i].key;
						for (var k in jsonData.form) {
							if(jsonData.form[k].id==id) {
								initX=jsonData.form[k].dispX*initPoint;
								initY=(jsonData.form[k].dispY*initPoint)-(jsonData.form[k].height*initPoint);
								//console.log(initX + "-"+ initY);
							}
						}
						Pager.drawLine(color, lineWidth, forms[i].path[j].data);
				}
			}
		}
	};
}();

