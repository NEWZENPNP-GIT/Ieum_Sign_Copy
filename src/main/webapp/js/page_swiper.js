swiperPage=function(){
	this.current=1;
	this.total;
}
swiperPage.prototype.init=function(){
	var _this=this;
	
	console.log($(".pinch-zoom").length)
	this.total=$(".swiper-slide").length;
	var innerHTML="<span class='swiper-pagination-current'>1</span> / "
		+"<span class='swiper-pagination-total'>"+$(".swiper-slide").length+"</span>"
	$(".pageNav").find(".swiper-pagination").append(innerHTML);
	
	this.imgdiv = document.getElementsByClassName('pinch-zoom-image');
	this.img=this.imgdiv[0];
	/*22.08.17 주석처리 > open소스 19.05.22 zoom 삭제*/
    /*_zoom= new Zoom(this.img, {pan: true,rotate: false});
	_zoom.orientListener(_orient);*/
	//var el = document.querySelector('div.pinch-zoom');
	//var el =$(".pinch-zoom-container > .pinch-zoom").eq(0);
	//new PinchZoom.default(el, {});

	this.btnSetting();

	$(".btn_prev").click(function(){
		_this.swiperEvent("prev");
	});
	
	$(".btn_next").click(function(){
		_this.swiperEvent("next");
	});
}	
	
swiperPage.prototype.btnSetting=function(){
	var _this=this;

	if(this.total==1){
		$('.btn_next.swiper-button-next').css("display","none");
		$('.btn_prev.swiper-button-prev').css("display","none");
	}
	
	if(this.current==this.total){
		if(this.total == 1){
			if(!$(".btn_prev").hasClass("disabled")){
				$(".btn_prev").addClass("disabled")
			}
		}else{
			if($(".btn_prev").hasClass("disabled")){
				$(".btn_prev").removeClass("disabled")
			}
		}
		
		if(!$(".btn_next").hasClass("disabled")){
			$(".btn_next").addClass("disabled")
		}
	}else if(this.current== 1){
		if(!$(".btn_prev").hasClass("disabled")){
			$(".btn_prev").addClass("disabled")
		}
		
		if($(".btn_next").hasClass("disabled")){
			$(".btn_next").removeClass("disabled")
		}
	}else{
		if($(".btn_prev").hasClass("disabled")){
			$(".btn_prev").removeClass("disabled")
		}
		if($(".btn_next").hasClass("disabled")){
			$(".btn_next").removeClass("disabled")
		}
	}

	$(".swiper-pagination-current").text(this.current);
}

swiperPage.prototype.swiperEvent=function(arrow){
	var _this=this;
	
	//zm.reset();
	if(arrow=="prev"){
		if(this.current <= this.total && this.current !== 1){
			this.current--;
		}else{
			return;
		}
		
	}else{
		if(this.current < this.total){
			this.current++;
		}else{
			return;
		}
	}
	
	this.imgdiv = document.getElementsByClassName('pinch-zoom-image');
	this.img=this.imgdiv[this.current-1];

	/*22.08.17 주석처리 > open소스 19.05.22 zoom 삭제*/
	//_zoom.orientEventDel();

	//_zoom=new Zoom(this.img, {pan: true,rotate: false});
	//_zoom.orientListener(_orient);
	 
	$(".swiper-wrapper > .swiper-slide").each(function(idx){
		if(_this.current != (idx +1)){
			$(this).css("display","none");
		}else{
			$(this).css("display","block");
		}
		
	})
    $(".swiper-wrapper > .swiper-slide").eq(this.current-1).addClass("active");
	this.btnSetting();
}