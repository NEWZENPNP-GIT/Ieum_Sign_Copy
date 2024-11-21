(function ($) {
	$.fn.stepIndex=0;
	$.fn._menu=""
	$.fn._con=""
	$.fn._prevMove=function(){

		if(this.stepIndex <= this._menu.find(".nav_group > li").length-1){
			this.stepIndex--;
			this.move_step(this.stepIndex);
		}
	}
	
	$.fn._nextMove=function(){
			
		if(this.stepIndex < this._menu.find(".nav_group > li").length-1){
			this.stepIndex++;
			this.move_step(this.stepIndex);
		}
	}
	
	$.fn.move_step=function(go){
		//하단 이전, 다음 버튼 셋팅

		if(go==0){
			this._con.find(".btn_prev").removeClass("able");
			if(!this._con.find(".btn_next").hasClass("able")){
				this._con.find(".btn_next").addClass("able");
			}
		}else if(go == this._menu.find(".nav_group > li").length-1){
			if(!this._con.find(".btn_prev").hasClass("able")){
				this._con.find(".btn_prev").addClass("able");
			}
			if(this._con.find(".btn_next").hasClass("able")){
				this._con.find(".btn_next").removeClass("able");
			}
		}else{
			if(!this._con.find(".btn_prev").hasClass("able")){
				this._con.find(".btn_prev").addClass("able");
			}
			if(!this._con.find(".btn_next").hasClass("able")){
				this._con.find(".btn_next").addClass("able");
			}
		}

		//기존 클릭 버튼 초기화
		this._menu.find(".nav_group").find(".btn_type").removeClass("active");
		this._menu.find(".number_group").find(".num").removeClass("active");
		this._menu.find(".line_group > li").removeClass("active");
		this._menu.find(".line_group > li").removeClass("clear");
		
		//우측 네비바의 이동
		this._menu.find(".nav_group > li").eq(go).find(".btn_type").addClass("active");
		this._menu.find(".number_group > li").eq(go).find(".num").addClass("active");
		
		this._menu.find(".line_group > li").each(function(idx){

			if(idx < go){
				$(this).addClass("clear");
			}else if(idx == go){

				if($(this).parent().find("li").length-1 == go){
					$(this).addClass("clear");
				}else{
					$(this).addClass("active");
				}
			}
		})
		
		//content교체
		/*var goURL="resource/userGuide_step0"+(go+1)+".gif"
		this._con.find(".contents").find("img").attr("src",goURL);*/
	}
	
	$.fn.guideSetting=function(con,menu){
		$.fn._menu=$('[class*="'+menu+'"]');
		$.fn._con=$('[class*="'+con+'"]');

		$('[class*="'+menu+'"]').prepend('<ul class="number_group">');
		$('[class*="'+menu+'"]').prepend('<ul class="line_group">');
		
		$('[class*="'+menu+'"]').find(".nav_group > li").each(function(idx){
			$('[class*="'+menu+'"]').find(".line_group").append('<li class="ver_line"></li>');
			$('[class*="'+menu+'"]').find(".number_group").append('<li><div class="num"><span class="NanumSquare_R">'+(idx+1)+'</span></div></li>');
		});
		
		//zoom in - out
		$('[class*="'+con+'"]').find(".btn_zoom").find(".btn_type").click(function(){
			if($(this).hasClass("zoom_in")){
				$(this).addClass("zoom_out").removeClass("zoom_in");
				$(this).text("zoom_out");
				$('[class*="'+con+'"]').find(".contents").addClass("fullScreen");
			}else{
				$(this).addClass("zoom_in").removeClass("zoom_out");
				$(this).text("zoom_in");
				$('[class*="'+con+'"]').find(".contents").removeClass("fullScreen");
			}
		});
		
		//prev, next
		$('[class*="'+con+'"]').find(".btn_group").find(".btn_type").click(function(){
			if($(this).hasClass("btn_prev")){
				//"이전 버튼
				if($(this).hasClass("able")){
					$.fn._prevMove();
				}
			}else{
				if($(this).hasClass("able")){
				//다음 버튼 
					$.fn._nextMove();
				}
			}
		});
		
		//우측 네비버튼 EVENT
		$('[class*="'+menu+'"]').find(".nav_group").find(".btn_type").mouseover(function(){
			var index=$(this).parent().parent().index();
			
			$('[class*="'+menu+'"]').find(".number_group > li ").eq(index).find(".num").addClass("hover");
			$(this).addClass("hover");
		});
		
		$('[class*="'+menu+'"]').find(".nav_group").find(".btn_type").mouseout(function(){
			var index=$(this).parent().parent().index();
			
			$('[class*="'+menu+'"]').find(".number_group > li ").eq(index).find(".num").removeClass("hover");
			$(this).removeClass("hover");
		});
		
		$('[class*="'+menu+'"]').find(".nav_group").find(".btn_type").click(function(){
			var index=$(this).parent().parent().index();

			//버튼영역
			if(!$(this).hasClass("active")){
				$.fn.move_step(index);
				$.fn.stepIndex=index;
			}
		});
		
		//첫 페이지 로드
		$.fn.move_step(0);
	}
	/*$.fn.displaySize=function(){

		if(822 < $(window).width() ){
			$("#guide_popup_bg").css("top", Math.max(0, (($(window).height() - $("#guide_popup_bg").outerHeight()) / 2) + $(window).scrollTop()) + "px");
	        $("#guide_popup_bg").css("left", Math.max(0, (($(window).width() - $("#guide_popup_bg").outerWidth()) / 2) + $(window).scrollLeft()) + "px");
	        $("#guide_popup_bg").css("margin",0);
		}
		
	}
	$(window).resize(function() {
        $.fn.displaySize();
    });
	$(window).ready(function() {
        $.fn.displaySize();
    });*/
}($));