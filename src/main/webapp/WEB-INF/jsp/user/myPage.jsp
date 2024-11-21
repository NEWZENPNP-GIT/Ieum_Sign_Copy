
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
    <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, user-scalable=no">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">

    <link rel="stylesheet" type="text/css" href="/css/font_pc.css">
    <link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
    <link rel="stylesheet" type="text/css" href="/css/myPage.css">

    <script type="text/javascript" src="/js/jquery.min.js"></script>
    </head>

    <body>
    <!-- *********************************************  계약 관리  *********************************************  -->
    <div id="myPage_wrap" class="">
        <div class="container">
            <div class="location">
                <span class="loc_main NanumGothic">게시판 > </span>
                <span class="loc_sub NanumGothic">마이페이지</span>
            </div>
            <div class="pagtit">
                <span class="NanumGothic">마이페이지</span>
            </div>
            <div class="tit_dec NanumGothic">
                <span class="speaker"></span>
                <span>안녕하세요 관리자님 </span>
            </div>
            <div class="column" id="myReport">
                <div class="chart_wrap">
                    <ul>
                        <li class="coltit NanumGothic"><span>서비스별 이용현황</span></li>
                        <li class="subtit"><span class="NanumGothic">현재 사원수</span> <span>300명</span></li>
                        <li class="con">
                            <div id="legend"></div>
                            <div id="piechart"></div>
                        </li>
                        <li class="btn_foot"><a class="btn_type Material_icons">서비스 이용관리 ></a></li>
                    </ul>
                </div>
                <div class="point_wrap">

    <div class="coltit NanumGothic"><span class="icon"></span><span>포인트 이용 내역</span></div>
    <div class="subtit"><span class="NanumGothic">포인트</span></div>
        <ul class="list">
            <li>
                <div class="name">전자계약 서비스</div>
                <div class="point">
                    <div class="score">5,123</div>
                    <div class="p">p</div>
                </div>
            </li>
            <li>
                <div class="name">전자계약서 보관 서비스</div>
                <div class="point">
                    <div class="score">5,123</div>
                    <div class="p">p</div>
                </div>
            </li>
            <li>
                <div class="name">전자계약서 양식 등록 서비스</div>
                <div class="point">
                    <div class="score">5,123</div>
                    <div class="p">p</div>
                </div>
            </li>
            <li>
                <div class="name">전자계약 서비스</div>
                <div class="point">
                    <div class="score">5,123</div>
                    <div class="p">p</div>
                </div>
            </li>
            <li>
                <div class="name">전자계약서 보관 서비스</div>
                <div class="point">
                    <div class="score">5,123</div>
                    <div class="p">p</div>
                </div>
            </li>
            <li>
                <div class="name">전자계약서 보관 서비스</div>
                <div class="point">
                    <div class="score">15,123</div>
                    <div class="p">p</div>
                </div>
            </li>
            <li>
                <div class="name">전자계약서 양식 등록 서비스</div>
                <div class="point">
                    <div class="score">512,123</div>
                    <div class="p">p</div>
                </div>
            </li>
        </ul>
                </div>
            </div>
        </div>
    </div>
    </body>
    <script type="text/javascript">
    /* *************** 차트 셋팅  ***************  */
    var chartWrap=$("#piechart");
    var legendWrap=$("#legend");
    var chartColumn=['연차관리','전자문서보관','급여관리','전자계약'];
    var chartValue=[50,40,15,30];
    </script>
    <script type="text/javascript" src="/js/myPage.js"></script>

    </html>