-- drop table tbl_ye006;

CREATE TABLE tbl_ye006 (
  계약ID varchar(30) NOT NULL,
  사용자ID char(15) NOT NULL,
  원천명세ID char(15) NOT NULL,
  m1 char(1) DEFAULT 'N',
  m1금액 decimal(10,0) DEFAULT 0,
  m2 char(1) DEFAULT 'N',
  m2금액 decimal(10,0) DEFAULT 0,
  m3 char(1) DEFAULT 'N',
  m3금액 decimal(10,0) DEFAULT 0,
  m4 char(1) DEFAULT 'N',
  m4금액 decimal(10,0) DEFAULT 0,
  m5 char(1) DEFAULT 'N',
  m5금액 decimal(10,0) DEFAULT 0,
  m6 char(1) DEFAULT 'N',
  m6금액 decimal(10,0) DEFAULT 0,
  m7 char(1) DEFAULT 'N',
  m7금액 decimal(10,0) DEFAULT 0,
  m8 char(1) DEFAULT 'N',
  m8금액 decimal(10,0) DEFAULT 0,
  m9 char(1) DEFAULT 'N',
  m9금액 decimal(10,0) DEFAULT 0,
  m10 char(1) DEFAULT 'N',
  m10금액 decimal(10,0) DEFAULT 0,
  m11 char(1) DEFAULT 'N',
  m11금액 decimal(10,0) DEFAULT 0,
  m12 char(1) DEFAULT 'N',
  m12금액 decimal(10,0) DEFAULT 0,
  추가제출서류번호 char(15) DEFAULT NULL,
  등록일시 char(14) DEFAULT NULL,
  수정일시 char(14) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='차량비과세';

ALTER TABLE tbl_ye006 ADD CONSTRAINT pk_tbl_ye006 PRIMARY KEY (계약ID, 사용자ID, 원천명세ID);









