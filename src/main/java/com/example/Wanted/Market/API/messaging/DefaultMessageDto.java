package com.example.Wanted.Market.API.messaging;

import lombok.Data;

@Data
public class DefaultMessageDto {
    private String objType;
    private String text;
    private String webUrl;
    private String mobileUrl;
    private String btnTitle;
}