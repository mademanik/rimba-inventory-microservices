package com.rimba.item.service;

import com.rimba.item.dto.request.ItemRequest;
import com.rimba.item.dto.response.ItemResponse;
import com.rimba.item.dto.response.ItemStockResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    public ItemResponse createItem(ItemRequest itemRequest, MultipartFile barang);

    public ItemResponse updateItemById(Long id, ItemRequest itemRequest, MultipartFile barang);

    public List<ItemResponse> getAllItems();

    public Optional<ItemResponse> getItemById(Long id);

    public void deleteItemById(Long id);

    public ItemStockResponse getItemStock(Long id);

    public Boolean updateItemStock(Long id, Double reqStock);

    public Boolean rollbackItemStock(Long id, Double reqStock);

    public Resource load(Long id, String filename);
}
