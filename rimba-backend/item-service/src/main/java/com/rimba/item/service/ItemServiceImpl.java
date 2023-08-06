package com.rimba.item.service;

import com.rimba.item.dto.request.ItemRequest;
import com.rimba.item.dto.response.ItemResponse;
import com.rimba.item.model.Item;
import com.rimba.item.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Value("${fileUploadRoot}")
    private String fileUploadRoot;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public ItemResponse createItem(ItemRequest itemRequest, MultipartFile barang) {
        Long getDateName = new Date().getTime();

        //rename thumbnail
        String getFileName = FilenameUtils.removeExtension(barang.getOriginalFilename());
        String getFileExt = FilenameUtils.getExtension(barang.getOriginalFilename());
        String namaBarangThumbnail = getFileName + getDateName + "_barang." + getFileExt;

        String setRootPath = fileUploadRoot + "/";

        //upload barang thumbnail
        try {
            Path root = Paths.get(setRootPath);
            Files.createDirectories(root);
            Files.copy(barang.getInputStream(), root.resolve(namaBarangThumbnail));
        } catch (IOException e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A thumbnail of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }

        Item item = Item.builder()
                .namaItem(itemRequest.getNamaItem())
                .unit(itemRequest.getUnit())
                .stock(itemRequest.getStock())
                .hargaSatuan(itemRequest.getHargaSatuan())
                .barang(namaBarangThumbnail)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        itemRepository.save(item);
        log.info("Item with id : {} is successfully created", item.getId());
        return mapToItemResponse(item);
    }

    @Override
    public List<ItemResponse> getAllItems() {
        List<Item> items = itemRepository.findAll();
        log.info("getAllItems successfully retrieved");
        return items.stream().map(this::mapToItemResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<ItemResponse> getItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        log.info("getItemById: {} successfully retrieved", id);
        return item.map(this::mapToItemResponse);
    }

    @Override
    public void deleteItemById(Long id) {
        //get url file directory
        Optional<Item> item = itemRepository.findById(id);
        String filePath = fileUploadRoot + "/" + item.get().getBarang();

        try {
            FileSystemUtils.deleteRecursively(new File(filePath));
            itemRepository.deleteById(id);
            log.info("Item with id: {} is successfully deleted", id);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Failed Delete Item with id: {}", id);
        }

    }

    private ItemResponse mapToItemResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .namaItem(item.getNamaItem())
                .unit(item.getUnit())
                .stock(item.getStock())
                .hargaSatuan(item.getHargaSatuan())
                .barang(item.getBarang())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
