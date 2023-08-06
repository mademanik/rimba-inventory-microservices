package com.rimba.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimba.item.dto.request.ItemRequest;
import com.rimba.item.dto.response.ItemResponse;
import com.rimba.item.repository.ItemRepository;
import com.rimba.item.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/item")
@Slf4j
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestPart("item") String item, @RequestPart("barang") MultipartFile barang) {
        ItemRequest itemRequest = new ItemRequest();
        try {
            ObjectMapper mapper = new ObjectMapper();
            itemRequest = mapper.readValue(item, ItemRequest.class);

            ItemResponse itemResponse = itemService.createItem(itemRequest, barang);
            return new ResponseEntity<>(itemResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Error creating item {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        try {
            List<ItemResponse> items = itemService.getAllItems();
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error getAllItems {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ItemResponse>> getItemById(@PathVariable("id") Long id) {
        try {
            Optional<ItemResponse> itemResponse = itemService.getItemById(id);
            return new ResponseEntity<>(itemResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error getItemById {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteItemById(@PathVariable("id") Long id) {
        try {
            itemService.deleteItemById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error getItemById {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
