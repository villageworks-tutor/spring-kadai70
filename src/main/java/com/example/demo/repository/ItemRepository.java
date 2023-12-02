package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	/**
	 * 商品のカテゴリ検索
	 * SELECT * FROM items WHERE category_id = ?
	 */
	List<Item> findByCategoryId(Integer categoryId);

	/**
	 * 商品名のキーワード検索
	 * SELECT * FROM items WHERE name LIKE ?
	 */
	List<Item> findByNameContaining(String keyword);

}
