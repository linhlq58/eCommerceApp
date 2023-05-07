package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_items_by_name() {
        given(itemController.getItemsByName("test").getBody()).willReturn(getListTestItem());

        ResponseEntity<List<Item>> getItemsByNameResponse = itemController.getItemsByName("test");

        assertNotNull(getItemsByNameResponse);
        assertEquals(200, getItemsByNameResponse.getStatusCodeValue());

        List<Item> listItem = getItemsByNameResponse.getBody();
        assertEquals(listItem.get(0).getName(), getListTestItem().get(0).getName());
        assertEquals(listItem.get(0).getPrice(), getListTestItem().get(0).getPrice());
    }

    @Test
    public void get_item_by_id() {
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(getTestItem()));

        ResponseEntity<Item> getItemByIdResponse = itemController.getItemById(1L);

        assertNotNull(getItemByIdResponse);
        assertEquals(200, getItemByIdResponse.getStatusCodeValue());

        Item item = getItemByIdResponse.getBody();
        assertEquals(item.getName(), getTestItem().getName());
        assertEquals(item.getPrice(), getTestItem().getPrice());
    }

    @Test
    public void get_all_items() {
        given(itemController.getItems().getBody()).willReturn(getListTestItem());

        ResponseEntity<List<Item>> getItemsResponse = itemController.getItems();

        assertNotNull(getItemsResponse);
        assertEquals(200, getItemsResponse.getStatusCodeValue());

        List<Item> listItem = getItemsResponse.getBody();
        assertEquals(listItem.get(0).getName(), getListTestItem().get(0).getName());
        assertEquals(listItem.get(0).getPrice(), getListTestItem().get(0).getPrice());
    }

    public List<Item> getListTestItem() {
        List<Item> listItem = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Car");
        item1.setPrice(BigDecimal.valueOf(3.56));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Motorbike");
        item2.setPrice(BigDecimal.valueOf(2.87));

        Item item3 = new Item();
        item3.setId(3L);
        item3.setName("Bicycle");
        item3.setPrice(BigDecimal.valueOf(1.32));

        listItem.add(item1);
        listItem.add(item2);
        listItem.add(item3);

        return listItem;
    }

    public Item getTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Pie");
        item.setPrice(BigDecimal.valueOf(4.44));

        return item;
    }
}
