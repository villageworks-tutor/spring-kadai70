package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Account;

import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
	
	@Autowired
	HttpSession session;
	@Autowired
	Account account;
	
	// ログイン画面表示およびログアウト処理
	@GetMapping({"/", "/login", "/logout"})
	public String index() {
		// セッションスコープの破棄
		session.invalidate();
		// 画面遷移
		return "login";
	}
	
	// ログイン処理
	@PostMapping("/login")
	public String login(
			@RequestParam("name") String name,
			Model model) {
		// セッションスコープに登録されているアカウント情報にリクエストパラメータを登録
		account.setName(name);
		// 画面遷移
		return "redirect:/items";
	}
	
}
