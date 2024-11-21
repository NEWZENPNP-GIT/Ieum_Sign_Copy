package com.ezsign.feb.master.vo;

import java.util.List;

import lombok.Data;

@Data
public class ExcelDataRequest<T> {

    public String 계약ID;
    public String 근무시기;
    public List<T> data;
}
