package by.valvik.musicadvisor.dto;

import by.valvik.musicadvisor.domain.Item;

import java.util.List;

public record ItemDto(List<? extends Item> items, int totalPages, int currentPage) {

}
