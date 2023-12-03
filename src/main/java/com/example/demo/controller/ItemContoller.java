package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

@Controller
public class ItemContoller {

	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ItemRepository itemRepository;
	
	// 商品一覧表示
	@GetMapping("/items")
	public String index(
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "maxPrice", defaultValue = "") Integer maxPrice,
			@RequestParam(name = "categoryId", defaultValue = "") Integer categoryId,
			Model model) {
		
		// 全商品表示の場合：初期表示またはサイトタイトルのリンク押下時 
		// すべてのカテゴリーを種痘
		List<Category> categoryList = categoryRepository.findAll();
		
		// リクエストパラメータの有無によって商品リストの取得を分岐
		// TODO: この商品リスト取得はオブジェクト指向の立場からサービスとして分割するのが適切
		List<Item> itemList = null;
		if (!keyword.isEmpty() && maxPrice != null) {
			// キーワードが入力されかつ価格検索上限値が入力されている場合：キーワード検索かつ価格上限値検索
			itemList = itemRepository.findByNameContainingAndPriceLessThanEqual(keyword, maxPrice);
		} else if (!keyword.isEmpty() && maxPrice == null) {
			// キーワードが入力されかつ価格上限値が入力されていない場合：キーワード検索
			itemList = itemRepository.findByNameContaining(keyword);
		} else if (keyword.isEmpty() && maxPrice != null) {
			// キーワードが入力されずかつ価格上限値が入力されている場合：価格上限値以下検索
			itemList = itemRepository.findByPriceLessThanEqual(maxPrice);
		} else if (keyword.isEmpty() && maxPrice == null && categoryId != null) {
			// キーワードも価格上限値も入力されていない場合でかつカテゴリーのリンクが押下された場合：カテゴリ検索
			itemList = itemRepository.findByCategoryId(categoryId);
		} else {
			// 上記以外の場合（初期表示およびサイト名のリンクが押下された場合）：全検索
			itemList = itemRepository.findAll();
		}
		
		// 取得したカテゴリーリストと取得した商品をスコープに登録
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("itemList", itemList);
		// 画面遷移
		return "items";
	}
	
}
