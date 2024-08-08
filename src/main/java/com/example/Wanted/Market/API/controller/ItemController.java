package com.example.Wanted.Market.API.controller;

import com.example.Wanted.Market.API.domain.Item;
import com.example.Wanted.Market.API.domain.Orders;
import com.example.Wanted.Market.API.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping
    public Item createItem(@RequestBody Item item, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원만 제품을 등록할 수 있습니다.");
        }
        return itemService.createItem(item);
    }

    @GetMapping
    public List<Item> getAllItems(){
        return itemService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @PostMapping("/{itemId}/purchase")
    public Orders purchaseItem(@PathVariable Long itemId, @RequestParam String buyerEmail) {
        return itemService.purchaseItem(itemId, buyerEmail);
    }
}
