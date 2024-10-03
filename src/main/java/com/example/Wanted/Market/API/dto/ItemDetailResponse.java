package com.example.Wanted.Market.API.dto;

import com.example.Wanted.Market.API.domain.Item;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ItemDetailResponse {
    private Item item;
    private LocalDate modificationAllowedDate;
    private boolean canBeModified;

    public ItemDetailResponse(Item item, LocalDate modificationAllowedDate, boolean canBeModified) {
        this.item = item;
        this.modificationAllowedDate = modificationAllowedDate;
        this.canBeModified = canBeModified;
    }
}