package com.rimba.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimba.item.dto.request.ItemRequest;
import com.rimba.item.dto.response.ItemResponse;
import com.rimba.item.dto.response.ItemStockResponse;
import com.rimba.item.dto.response.ItemStockUpdate;
import com.rimba.item.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItemById(@PathVariable("id") Long id, @RequestPart("item") String item, @RequestPart(value = "barang", required = false) MultipartFile barang) {
        ItemRequest itemRequest = new ItemRequest();
        try {
            ObjectMapper mapper = new ObjectMapper();
            itemRequest = mapper.readValue(item, ItemRequest.class);

            ItemResponse itemResponse = itemService.updateItemById(id, itemRequest, barang);
            return new ResponseEntity<>(itemResponse, HttpStatus.OK);
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

    @GetMapping("/stock/{id}")
    public ResponseEntity<ItemStockResponse> getItemStock(@PathVariable("id") Long id) {
        try {
            ItemStockResponse itemStockResponse = itemService.getItemStock(id);
            return new ResponseEntity<>(itemStockResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error getItemStock {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/updateStock/{id}/{stock}")
    public ResponseEntity<ItemStockUpdate> updateItemStock(@PathVariable("id") Long id, @PathVariable("stock") Double stock) {
        ItemStockUpdate itemStockUpdate = new ItemStockUpdate();
        itemStockUpdate.setIsStockUpdated(false);
        try {
            Boolean isUpdated = itemService.updateItemStock(id, stock);
            if (isUpdated) {
                itemStockUpdate.setIsStockUpdated(isUpdated);
            }
            return new ResponseEntity<>(itemStockUpdate, HttpStatus.OK);
        } catch (Exception e) {
            log.info("Error updateItemStock {}", e.getMessage());
            return new ResponseEntity<>(itemStockUpdate, HttpStatus.OK);
        }
    }

    @GetMapping("/download/{id}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getDownload(@PathVariable Long id, @PathVariable String filename) {
        log.info("Download Request: " + filename + " with id " + id);
        Resource file = itemService.load(id, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
