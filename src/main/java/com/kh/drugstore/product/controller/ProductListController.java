package com.kh.drugstore.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.drugstore.common.DrugstoreUtils;
import com.kh.drugstore.product.model.dto.Product;
import com.kh.drugstore.product.model.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/product")
public class ProductListController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/productList.do")
	public void productList(@RequestParam(defaultValue = "1") int cPage, Model model, HttpServletRequest request) {
		//페이징 시작. 콘텐츠 영역
		Map<String, Integer> param = new HashMap<>();
		
		int limit = 15;
		param.put("cPage", cPage);
		param.put("limit", limit);
		
		List<Product> list = productService.findAllProduct(param); //모든 제품찾기 
		log.debug("list = {}", list);
		model.addAttribute("list", list);
		
		//2. 페이지바
		int totalContent = productService.getTotalContent();
		log.debug("totalContent = {}", totalContent);
		String url = request.getRequestURI();
		String pagebar = DrugstoreUtils.getPagebar(cPage, limit, totalContent, url);
		model.addAttribute("pagebar", pagebar);
		
		log.debug("list = {}", list);
		
	}
	
//	@GetMapping("/category")
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
