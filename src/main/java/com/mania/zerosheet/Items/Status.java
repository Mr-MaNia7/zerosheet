package com.mania.zerosheet.Items;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    MAINTENANCE("M"), LOAN("L"), AVAILABLE("A");
    private String code;
}



