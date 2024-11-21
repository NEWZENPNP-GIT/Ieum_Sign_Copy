
var shaderProgram;
var pMatrix = mat4.create();
var bWebGLMode = true;
var sigContext = null;

var actionData = null;
var idx = 0;

var color = '#000000';
var lineWidth = 1;

	function setArray(type, x, y) {
		if(type==0) {
			if(actionData!=null) {
				History.addAction(actionData);			// add to History			
				actionData = null;
			}
			
			actionData = {
				id: idx++,
				key: 'aaa',
				action: 'pencil',
				undo: false,
				linewidth: lineWidth,
				color: color,
				data: [{x: x, y: y}]
			}
		} else if (type==1) {			
			actionData.data.push({x: x, y: y});		// add a (x,y) to actionData
		} else {
			actionData.data.push({x: x, y: y});		// add a (x,y) to actionData
			History.addAction(actionData);			// add to History			
			actionData = null;
		}
	}
	

	function relativeMouseCoords(event, currentElement) {
	    var totalOffsetX = 0;
	    var totalOffsetY = 0;
	    var canvasX = 0;
	    var canvasY = 0;
	
	    do {
	        totalOffsetX += currentElement.offsetLeft;
	        totalOffsetY += currentElement.offsetTop;
	    } while (currentElement = currentElement.offsetParent)
	
		if (event.pageX || event.pageY) { 
			canvasX = event.pageX;
			canvasY = event.pageY;
		} else { 
			canvasX = event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft; 
			canvasY = event.clientY + document.body.scrollTop + document.documentElement.scrollTop; 
		}
		
	    canvasX -= totalOffsetX;
	    canvasY -= totalOffsetY;
	    	
	    return {X:Math.round(canvasX), Y:Math.round(canvasY)};
	}
	
  // works out the X, Y position of the click inside the canvas from the X, Y position on the page
  function getPosition(mouseEvent, sigCanvas) {
     var x, y;
     if (mouseEvent.pageX != undefined && mouseEvent.pageY != undefined) {
        x = mouseEvent.pageX;
        y = mouseEvent.pageY;
     } else {
        x = mouseEvent.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
        y = mouseEvent.clientY + document.body.scrollTop + document.documentElement.scrollTop;
     }
     
     return { X: Math.round(x - sigCanvas.offsetLeft), Y: Math.round(y - sigCanvas.offsetTop) };
  }
  function removeMode(mode) {
	var is_touch_device = 'ontouchstart' in document.documentElement;
			
	if (is_touch_device) {
		if (mode==true) {
			sigCanvas.removeEventListener("touchstart", draw);
			sigCanvas.removeEventListener("touchmove", draw);
		} else {
		    sigCanvas.addEventListener('touchstart', draw, false);
		    sigCanvas.addEventListener('touchmove', draw, false);
		    sigCanvas.addEventListener('touchmove', function (event) {
		       event.preventDefault();
		    }, false);
		}
	}
  }
  // works out the X, Y position of the click inside the canvas from the X, Y position on the page
  function getTouchPosition(coors, sigCanvas) {
    var x, y;

    return { X: Math.ceil(coors.x), Y: Math.ceil(coors.y) };
  }
  function initialize() {
	 var sigCanvas = document.getElementById("drawCanvas");

	 try {
		 sigContext = initGL(sigCanvas);
	 } catch (e) {
		 bWebGLMode = false;
	 }


	 //webGL 초기화
	 if (bWebGLMode==true) {
		 //sigContext = initGL(sigCanvas);
		 //alert("webGL Serviced");
		 console.log("WebGL Service Start.!");
		 initShaders();
	 } else {
		 //alert("HTML5 Serviced");
		 sigContext = sigCanvas.getContext("2d");
		 sigContext.strokeStyle = color;
		 sigContext.lineWidth = lineWidth;
		 sigContext.lineCap = 'round';
		 sigContext.lineJoin = 'bevel';
	 }

	 // This will be defined on a TOUCH device such as iPad or Android, etc.
	 var is_touch_device = 'ontouchstart' in document.documentElement;
	
	 if (is_touch_device) {
	    // create a drawer which tracks touch movements
	    var drawer = {
	       touchstart: function (coors) {
		       var position = getTouchPosition(coors, sigCanvas);
		       setArray(0, position.X, position.Y);

		  	 	if (bWebGLMode) {
				    points = [];
	  	 			sigContext.lineWidth(1);
		  	 		if (color=="#000000") {
		 				sigContext.uniform4f(shaderProgram.colorLoc, 0.0, 0.0, 0.0, 1.0);
					} else {
		 				sigContext.uniform4f(shaderProgram.colorLoc, 1.0, 0.0, 0.0, 1.0);
					}
		  	 		points.push(new Point(position.X, sigContext.viewportHeight - position.Y));
		  	 	} else {
		  	 		sigContext.stroke();
		  	 		sigContext.moveTo(position.X, position.Y);
		  	 		sigContext.strokeStyle = color;
		  	 		sigContext.lineWidth = lineWidth;
		  	 		sigContext.beginPath();
		  	 		ptVector = [];
		  	 		ptVector.push(new Point(position.X, position.Y));
		  	 	}
		       /*
		       if (coors.finger >1) {
		       	alert("fingers"+coors.finger);
		       }
		       */
	       },
	       touchmove: function (coors) {
	    	   var position = getTouchPosition(coors, sigCanvas);
	    	   setArray(1, position.X, position.Y);
	    	   
	    	   if (bWebGLMode) {
	    		   // webGL signature
		  		   points.push(new Point(position.X, sigContext.viewportHeight - position.Y));
		  		   
		  		   if (points.length > 2) {
			  		   DrawUtils.drawPolyline(points, sigContext, shaderProgram.vertexPositionLoc);
			  		   points=[];
			  		   points.push(new Point(position.X, sigContext.viewportHeight - position.Y));
		  		   }
	    	   } else {
	    	 		ptVector.push(new Point(position.X, position.Y));
	    	 		
	    	 		if (ptVector.length > 2) {
	    	 			var ptLen = ptVector.length;
	    	 			var p1 = ptVector[ptLen-3];
	    	 			var p2 = ptVector[ptLen-2];
	    	 			var p3 = ptVector[ptLen-1];
	    	 			sigContext.quadraticCurveTo(p1.x, p1.y, (p1.x+p2.x)/2, (p1.y+p2.y)/2);
	    	 			sigContext.quadraticCurveTo(p2.x, p2.y, p3.x, p3.y);
	    	 			sigContext.stroke();
	    	 			ptVector=[];
	    	  	 		ptVector.push(new Point(position.X, position.Y));
	    	 		}
	    	 		
	    		    //sigContext.lineTo(position.X, position.Y);
		    	    //sigContext.stroke();
	    	   }
	       },
	       touchend: function (coors) {
	    	   var position = getTouchPosition(coors, sigCanvas);
	    	   setArray(9, position.X, position.Y);

	    	   if (bWebGLMode) {
	    		   // webGL signature
		  		   points.push(new Point(position.X, sigContext.viewportHeight - position.Y));
		  		   
		  		   DrawUtils.drawPolyline(points, sigContext, shaderProgram.vertexPositionLoc);
		  		   points = [];
	    	   } else {
	    		   sigContext.lineTo(position.X, position.Y);
		    	   sigContext.stroke();
	    	   }
	       },
	       touchcancel: function (coors) {
	    	   var position = getTouchPosition(coors, sigCanvas);
	    	   setArray(9, position.X, position.Y);

	    	   if (bWebGLMode) {
	    		   // webGL signature
		  		   points.push(new Point(position.X, sigContext.viewportHeight - position.Y));
		  		   
		  		   DrawUtils.drawPolyline(points, sigContext, shaderProgram.vertexPositionLoc);
		  		   points = [];
	    	   } else {
	    		   sigContext.lineTo(position.X, position.Y);
		    	   sigContext.stroke();
	    	   }
	       }
	    };
	    
	    // create a function to pass touch events and coordinates to drawer
	    function draw(event) {
	       // get the touch coordinates.  Using the first touch in case of multi-touch
	       var coors = {
	          x: event.targetTouches[0].pageX,
	          y: event.targetTouches[0].pageY,
	          finger: event.targetTouches.length
	       };
	
	       // Now we need to get the offset of the canvas location
	       var obj = sigCanvas;
	
	       if (obj.offsetParent) {
	          // Every time we find a new object, we add its offsetLeft and offsetTop to curleft and curtop.
	          do {
	             coors.x -= obj.offsetLeft;
	             coors.y -= obj.offsetTop;
	          }
			  // The while loop can be "while (obj = obj.offsetParent)" only, which does return null
			  // when null is passed back, but that creates a warning in some editors (i.e. VS2010).
	          while ((obj = obj.offsetParent) != null);
	       }
	
	       // pass the coordinates to the appropriate handler
	       drawer[event.type](coors);
	    }

 		$(sigCanvas).unbind("touchstart");
 		$(sigCanvas).unbind("touchmove");
 		$(sigCanvas).unbind("touchend");
 		$(sigCanvas).unbind("touchcancel");
	    sigCanvas.addEventListener('touchstart', draw, false);
	    sigCanvas.addEventListener('touchmove', draw, false);
	    sigCanvas.addEventListener('touchend', draw, false);
	    sigCanvas.addEventListener('touchcancel', draw, false);
	    
	    // prevent elastic scrolling
	    sigCanvas.addEventListener('touchstart', function (event) {
	       event.preventDefault();
	    }, false);
	    
        if (window.navigator.msPointerEnabled) {
	 		$(sigCanvas).unbind("MSPointerDown");
	 		$(sigCanvas).unbind("MSPointerMove");
	 		$(sigCanvas).unbind("MSPointerUp");
	 		$(sigCanvas).unbind("MSPointerCancel");
	 		sigCanvas.addEventListener('MSPointerDown', draw, false);
	 		sigCanvas.addEventListener("MSPointerMove", draw, false);
	 		sigCanvas.addEventListener('MSPointerUp', draw, false);
	 		sigCanvas.addEventListener('MSPointerCancel', draw, false);
	 		
	 		console.log("MSPointerDown");
		    sigCanvas.addEventListener('MSPointerDown', function (event) {
		       event.preventDefault();
		    }, false);
        }
        
	    var touchStarted = false, // detect if a touch event is sarted
	    	currX = 0,
	    	currY = 0,
	    	cachedX = 0,
	    	cachedY = 0;
	    
	 }
	 else {

 		$(sigCanvas).unbind("mousedown");
 		$(sigCanvas).unbind("mousemove");
 		$(sigCanvas).unbind("mouseup");
 		$(sigCanvas).unbind("mouseout");
	    // start drawing when the mousedown event fires, and attach handlers to
	    // draw a line to wherever the mouse moves to
		$("#drawCanvas").mousedown(function (mouseEvent) {
			var position = relativeMouseCoords(mouseEvent, sigCanvas);

			 if (bWebGLMode) {
	  	 		if (color=="#000000") {
			  	 	sigContext.lineWidth(1);
	 				sigContext.uniform4f(shaderProgram.colorLoc, 0.0, 0.0, 0.0, 1.0);
				} else {
			  	 	sigContext.lineWidth(1);
	 				sigContext.uniform4f(shaderProgram.colorLoc, 1.0, 0.0, 0.0, 1.0);
				}
				setArray(0, position.X, position.Y);
				points=[];
			    points.push(new Point(position.X, sigContext.viewportHeight - position.Y));
				
			 } else {
				sigContext.moveTo(position.X, position.Y);
				sigContext.beginPath();
				sigContext.strokeStyle = color;
				sigContext.lineWidth = lineWidth;
				setArray(0, position.X, position.Y);
	  	 		ptVector = [];
	  	 		ptVector.push(new Point(position.X, position.Y));
			 }
	
			// attach event handlers
			$(this).mousemove(function (mouseEvent) {
				drawLine(mouseEvent, sigCanvas, sigContext);
			}).mouseup(function (mouseEvent) {
				finishDrawing(mouseEvent, sigCanvas, sigContext);
			}).mouseout(function (mouseEvent) {
				finishDrawing(mouseEvent, sigCanvas, sigContext);
			});
			
	     });	
	 }
	 
	 // webGL canvas 초기화
	 /*
	 if (bWebGLMode) {

		 sigContext.enable(sigContext.DEPTH_TEST);
		 sigContext.clear(sigContext.COLOR_BUFFER_BIT | sigContext.DEPTH_BUFFER_BIT);
		 sigContext.uniformMatrix4fv(shaderProgram.pMatrixLoc, false, pMatrix);
		 sigContext.uniform4f(shaderProgram.colorLoc, 1.0, 0.0, 0.0, 1.0);
	 }
	 */
  }
  
  function drawLine(mouseEvent, canvas, context) {

     var position = relativeMouseCoords(mouseEvent, canvas);
     setArray(1, position.X, position.Y);
     
	 if (bWebGLMode) {
	     points.push(new Point(position.X, context.viewportHeight - position.Y));
	     	     
	     if (points.length > 2) {
	    	 DrawUtils.drawPolyline(points, context, shaderProgram.vertexPositionLoc);
	    	 points=[];
		     points.push(new Point(position.X, context.viewportHeight - position.Y));
	     }
	 } else {
 		ptVector.push(new Point(position.X, position.Y));
 		
 		if (ptVector.length > 2) {
 			var ptLen = ptVector.length;
 			var p1 = ptVector[ptLen-3];
 			var p2 = ptVector[ptLen-2];
 			var p3 = ptVector[ptLen-1];
 			context.quadraticCurveTo(p1.x, p1.y, (p1.x+p2.x)/2, (p1.y+p2.y)/2);
 			context.quadraticCurveTo(p2.x, p2.y, p3.x, p3.y);
			//context.bezierCurveTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
 		    context.stroke();
 			ptVector=[];
  	 		ptVector.push(new Point(position.X, position.Y));
 		}
	    //context.lineTo(position.X, position.Y);
 		//context.stroke();
	 }
     
  }

  function drawLineEnd(mouseEvent, canvas, context) {

     var position = relativeMouseCoords(mouseEvent, canvas);
     setArray(9, position.X, position.Y);

	 if (bWebGLMode) {
	     points.push(new Point(position.X, context.viewportHeight - position.Y));	     
	     
	     DrawUtils.drawPolyline(points, context, shaderProgram.vertexPositionLoc);
	     
	 } else {
	     context.lineTo(position.X, position.Y);
	     context.stroke();
	 }
  }

  // draws a line from the last coordiantes in the path to the finishing
  // coordinates and unbind any event handlers which need to be preceded
  // by the mouse down event
  function finishDrawing(mouseEvent, canvas, context) {
     // draw the line to the finishing coordinates
	 drawLineEnd(mouseEvent, canvas, context);

	 /*
	 if (!bWebGLMode)
		 context.closePath();
	 */
	 
     points = [];
     ptVector = [];
	
	 // unbind any events which could draw
	 $(canvas).unbind("mousemove")
	             .unbind("mouseup")
	             .unbind("mouseout");
  }


  //webGL contractor
  function initGL(canvas) {
	var gl = WebGLUtils.setupWebGL(canvas, {preserveDrawingBuffer:true});
  	  	
  	if (!gl) {
  		return null;
  	}
  	
  	gl.clearColor(0.0, 0.0, 0.0, 0.0);
  	
  	gl.viewportWidth = canvas.width;
  	gl.viewportHeight = canvas.height;
  	  	
  	mat4.ortho(0, gl.viewportWidth, 0, gl.viewportHeight, -1, 1, pMatrix);
  	
  	return gl;
  }
  

  function getShader(gl, id) {
      var shaderScript = document.getElementById(id);
      
      if (!shaderScript) {
          return null;
      }

      var str = "";
      var k = shaderScript.firstChild;

      while (k) {
      	if (k.nodeType == 3) {
      		str += k.textContent;
      	}
      
      	k = k.nextSibling;
      }

      var shader;

      if (shaderScript.type == "x-shader/x-fragment") {
      	shader = gl.createShader(gl.FRAGMENT_SHADER);
      } else if (shaderScript.type == "x-shader/x-vertex") {
          shader = gl.createShader(gl.VERTEX_SHADER);
      } else {
          return null;
      }

      gl.shaderSource(shader, str);
      gl.compileShader(shader);

      if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
          alert(gl.getShaderInfoLog(shader));
          return null;
      }

      return shader;
  }

  function initShaders() {
      var fragmentShader = getShader(sigContext, "shader-fs");
      var vertexShader = getShader(sigContext, "shader-vs");

      shaderProgram = sigContext.createProgram();
      sigContext.attachShader(shaderProgram, vertexShader);
      sigContext.attachShader(shaderProgram, fragmentShader);
      sigContext.linkProgram(shaderProgram);

      if (!sigContext.getProgramParameter(shaderProgram, sigContext.LINK_STATUS)) {
      	alert("Could not initialise shaders");
      }

      sigContext.useProgram(shaderProgram);
      
      shaderProgram.vertexPositionLoc = sigContext.getAttribLocation(shaderProgram, "aVertexPosition");
      sigContext.enableVertexAttribArray(shaderProgram.vertexPositionLoc);
      
      shaderProgram.colorLoc = sigContext.getUniformLocation(shaderProgram, "uColor");
      shaderProgram.pMatrixLoc = sigContext.getUniformLocation(shaderProgram, "uPMatrix");
  }
  

  function initCanvas() {
  	var sigCanvas = document.getElementById("drawCanvas");
	var sigWidth = sigCanvas.width;
	var sigHeight = sigCanvas.height;
	
  	
  	//bWebGLMode = true;
    initialize();
    
  	if (bWebGLMode) {
  		sigContext.clear(sigContext.COLOR_BUFFER_BIT);
  		sigContext.uniformMatrix4fv(shaderProgram.pMatrixLoc, false, pMatrix);
  		sigContext.lineWidth(1);
  		sigContext.uniform4f(shaderProgram.colorLoc, 0.0, 0.0, 0.0, 1.0);
  	} else {
  		sigContext.clearRect(0, 0, sigWidth, sigHeight);
  	}
  }
  