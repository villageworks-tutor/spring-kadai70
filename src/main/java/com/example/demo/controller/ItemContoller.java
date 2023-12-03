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
		if (!keyword.isEmpty()) {
			// キーワードが入力されている場合
			if (maxPrice != null) {
				// 価格検索上限が入力されている場合：キーワード検索かつ価格上限値以下検索
				itemList = itemRepository.findByNameContainingAndPriceLessThanEqual(keyword, maxPrice);
			} else {
				// 価格検索上限が入力されていない場合：キーワード検索（Step1のコーディングに対応）
				itemList = itemRepository.findByNameContaining(keyword);
			}
		} else {
			// キーワードが入力されていない場合
			if (maxPrice != null) {
				// 価格検索上限が入力されている場合：価格上限値以下検索
				itemList = itemRepository.findByPriceLessThanEqual(maxPrice);
			} else {
				// 価格検索上限が入力されていない場合
				if (categoryId != null) {
					// カテゴリーのリンクが押下された場合：カテゴリー検索
					itemList = itemRepository.findByCategoryId(categoryId);
				} else {
					// カテゴリーのリンクが押下されていない場合：全件検索（初期表示またはサイトタイトルのリンク押下時のコーディングに対応）
					itemList = itemRepository.findAll();
				}
			}
		}
		
		// 取得したカテゴリーリストと取得した商品をスコープに登録
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("itemList", itemList);
		// 画面遷移
		return "items";
	}
	
}
