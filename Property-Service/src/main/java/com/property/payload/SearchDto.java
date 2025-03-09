package com.property.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchDto {
	private String searchQuery;
	private Integer pageNo;
	private Integer pageSize;
	private Long categoryId;
	private Double minPrice;
	private Double maxPrice;
	private Integer rating;
	private Boolean isOnlyPopuler;
}
