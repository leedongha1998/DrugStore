package com.kh.drugstore.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.drugstore.admin.model.service.AdminService;
import com.kh.drugstore.common.DrugstoreUtils;
import com.kh.drugstore.member.model.dto.User;
import com.kh.drugstore.product.model.dto.Category;
import com.kh.drugstore.product.model.dto.Product;
import com.kh.drugstore.product.model.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@Autowired
	ProductService productService;

	HttpSession session;
	
	@GetMapping("/header.do")
	public void toAdmin() {
		
	}

// 주희코드 시작	
	@GetMapping("/product/productList.do")
	public void productList(
			@RequestParam(defaultValue = "1") int cPage, 
			HttpServletRequest request, 
			Model model) {
		
		// 콘텐츠
		Map<String, Integer> param = new HashMap<>();
		
		int limit = 20;
		param.put("cPage", cPage);
		param.put("limit", limit);
		
		List<Product> list = productService.findAllProduct(param);
		log.debug("list = {}", list);
		model.addAttribute("list", list);
		
		// 페이지바
		int totalContent = productService.getTotalContent();
		log.debug("totalContent = {}", totalContent);
		String url = request.getRequestURI();
		String pagebar = DrugstoreUtils.getPagebar(cPage, limit, totalContent, url);
		model.addAttribute("pagebar", pagebar);
		model.addAttribute("totalContent", totalContent);
	}
	
	@GetMapping("/product/productEnroll.do")
	public void productEnroll() {}
	
	@GetMapping("/product/category.do")
	public ResponseEntity<?> categorySelect(@RequestParam int categoryId) {
		List<Category> categoryList = adminService.selectCategoryList(categoryId);
		log.debug("categoryList= {}", categoryList);
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(categoryList);
	}
	
	@GetMapping("/autocompletePname.do")
	public ResponseEntity<?> autocompletePname(@RequestParam String term){
		List<String> resultList = adminService.autocompletePname(term);
		log.debug("resultList = {}", resultList);
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(resultList);
	}
	
	@GetMapping("/autocompleteManu.do")
	public ResponseEntity<?> autocompleteManu(@RequestParam String term){
		List<String> resultList = adminService.autocompleteManu(term);
		log.debug("resultList = {}", resultList);
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(resultList);
	}
// 주희코드 끝	

	
// 태연코드 시작	
	@GetMapping("user/userList.do")
	public void userList(
			@RequestParam(defaultValue = "1") int cPage, 
			Model model, 
			HttpServletRequest request,
			@RequestParam(value = "searchType",required = false,defaultValue="") String searchType,
			@RequestParam(value = "keyword",required = false,defaultValue="") String keyword) {
		
		// 1. content영역
		Map<String, Integer> param = new HashMap<>();
		int limit = 10;
		param.put("cPage", cPage);
		param.put("limit", limit);
		List<User> list = adminService.userList(param,searchType,keyword);
		log.debug("list = {}", list);
		
		model.addAttribute("list", list);
		model.addAttribute("searchType",searchType);
		model.addAttribute("keyword",keyword);
		
		// 2. pagebar영역
		int totalContent =adminService.getTotalContent();
		log.debug("totalContent = {}", totalContent);
		String url = request.getRequestURI(); 
		String pagebar = DrugstoreUtils.getPagebar(cPage, limit, totalContent, url);
		model.addAttribute("pagebar", pagebar);
		model.addAttribute("totalContent", totalContent);
	}
	
	@GetMapping("/statis/enrollStatis.do")
	public void enrollStatis(Model model) {
			model.addAttribute("memMinus1", adminService.getMinus1Mem());
			model.addAttribute("memMinus2", adminService.getMinus2Mem());
			model.addAttribute("memMinus3", adminService.getMinus3Mem());
			model.addAttribute("memMinus4", adminService.getMinus4Mem());
			model.addAttribute("memMinus5", adminService.getMinus5Mem());
			model.addAttribute("memMinus6", adminService.getMinus6Mem());
			model.addAttribute("memMinus7", adminService.getMinus7Mem());
			model.addAttribute("memToday", adminService.getMemToday());
	}
	
	@GetMapping("/statis/visitStatis.do")
	public String visitStatis(Model model) {
		model.addAttribute("todayCount", adminService.getVisitTodayCount());
		model.addAttribute("totalCount", adminService.getVisitTotalCount());
		
		return "admin/statis/visitStatis";
	}
	
	
	
// 태연코드 끝
	
}	
