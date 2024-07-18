package com.example.Wanted.Market.API.service;

import com.example.Wanted.Market.API.domain.Item;
import com.example.Wanted.Market.API.domain.Member;
import com.example.Wanted.Market.API.domain.Orders;
import com.example.Wanted.Market.API.domain.ProductStatus;
import com.example.Wanted.Market.API.repository.ItemRepository;
import com.example.Wanted.Market.API.repository.MemberRepository;
import com.example.Wanted.Market.API.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testCreateItem() {
        Item item = new Item();
        item.setStatus(ProductStatus.AVAILABLE); // Enum 값으로 설정

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item createdItem = itemService.createItem(item);

        assertNotNull(createdItem);
        assertEquals(ProductStatus.AVAILABLE, createdItem.getStatus()); // ProductStatus Enum 값 비교
        verify(itemRepository, times(1)).save(item);
    }


    @Test
    public void testGetAllItems() {
        itemService.getAllItems();
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    public void testGetItemById() {
        String itemId = "testItemId";
        Item item = new Item();
        item.setItemId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Item foundItem = itemService.getItemById(itemId);

        assertNotNull(foundItem);
        assertEquals(itemId, foundItem.getItemId());
    }
}