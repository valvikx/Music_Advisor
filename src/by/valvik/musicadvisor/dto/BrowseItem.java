package by.valvik.musicadvisor.dto;

import by.valvik.musicadvisor.domain.Item;

import java.util.List;

public record BrowseItem(List<? extends Item> items, int totalPages, int currentPage) {

}
