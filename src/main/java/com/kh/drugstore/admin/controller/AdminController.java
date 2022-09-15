package com.kh.drugstore.admin.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.kh.drugstore.admin.model.service.AdminService;
import com.kh.drugstore.common.DrugstoreUtils;
import com.kh.drugstore.member.model.dto.User;
import com.kh.drugstore.product.model.dto.Category;
import com.kh.drugstore.product.model.dto.Product;
import com.kh.drugstore.product.model.dto.ProductAttachment;
import com.kh.drugstore.product.model.dto.ProductEntity;
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
	
	@Autowired
	ServletContext application;

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
//		model.addAttribute("list", list);
		
		// 페이지바
		int totalContent = productService.getTotalContent();
		log.debug("totalContent = {}", totalContent);
		String url = request.getRequestURI();
		String pagebar = DrugstoreUtils.getPagebar(cPage, limit, totalContent, url);
//		model.addAttribute("pagebar", pagebar);
//		model.addAttribute("totalContent", totalContent);
	}
	
	@GetMapping("/product/productEnroll.do")
	public void productEnrollForm() {}
	
	@GetMapping("product/productUpdate.do")
	public void productUpdate(@RequestParam int pcode, Model model) {
		Product product = productService.selectOneProductCollection(pcode);
		log.debug("product = {}", product);
		model.addAttribute("product", product);
	}
	
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
	
	@GetMapping("/product/findByValues.do")
	public String findByValues(
							@RequestParam String pcode,
							@RequestParam String pname,
							@RequestParam String manu,
							@RequestParam String saleStatus,
							@RequestParam String categoryId,
							@RequestParam String toDate,
							@RequestParam String fromDate,
							Model model){
		
//		log.debug("pcode = {}", pcode);
//		log.debug("pname = {}", pname);
//		log.debug("manu = {}", manu);
//		log.debug("saleStatus = {}", saleStatus);
//		log.debug("categoryId = {}", categoryId);
//		log.debug("toDate = {}", toDate);
//		log.debug("fromDate = {}", fromDate);
		
		Map<String, Object> param = new HashMap<>();
		param.put("pcode", pcode);
		param.put("pname", pname);
		param.put("manu", manu);
		param.put("saleStatus", saleStatus);
		param.put("categoryId", categoryId);
		param.put("toDate", toDate);
		param.put("fromDate", fromDate);
		
		List<ProductEntity> list = productService.findByValues(param);
		log.debug("list = {}", list);
		model.addAttribute("list", list);
		
		return "/admin/product/productList";
	}
	
	@PostMapping("/product/productEnroll.do")
	public String productEnroll(
			@RequestParam(name = "upFile") List<MultipartFile> upFileList,
			@RequestParam(name = "categoryId") String categoryId,
			@RequestParam(name = "pname") String pname,
			@RequestParam(name = "manu") String manu,
			@RequestParam(name = "price") String price,
			@RequestParam(name = "discount") String discount,
			@RequestParam(name = "amount") String amount,
			RedirectAttributes redirectAttr) 
					throws IllegalStateException, IOException {
		
		log.debug("컨트롤러입니다");
		Product product = new Product();
		product.setCategoryId(Integer.parseInt(categoryId));
		product.setPname(pname);
		product.setManu(manu);
		product.setPrice(Integer.parseInt(price));
		product.setAmount(Integer.parseInt(amount));
		log.debug("product = {}", product);
		
		for(MultipartFile upFile : upFileList) {
			log.debug("upFile = {}", upFile);
			log.debug("upFile#name = {}", upFile.getName()); // upFile
			log.debug("upFile#name = {}", upFile.getOriginalFilename());
			log.debug("upFile#size = {}", upFile.getSize());
			
			if(!upFile.isEmpty()) {
				// a. 서버 컴퓨터에 저장
				String saveDirectory = application.getRealPath("resources/upload/product");
				String renamedFilename = DrugstoreUtils.getRenamedFilename(upFile.getOriginalFilename());
				File destFile = new File(saveDirectory, renamedFilename);
				upFile.transferTo(destFile);
				
				// b. DB저장을 위해 Attachment 객체 생성
				ProductAttachment attach = new ProductAttachment(upFile.getOriginalFilename(), renamedFilename);
				product.add(attach);
			}
		}
		log.debug("product = {}", product);
		
		// db 저장
		int result = adminService.insertProduct(product);
		redirectAttr.addFlashAttribute("msg", "상품을 성공적으로 등록했습니다.");
		return "/admin/product/productEnroll";
	}
	
	@GetMapping("getCategoryParentLevel.do")
	public ResponseEntity<?> getCategoryParentLevel(@RequestParam int categoryId) {
		log.debug("categoryId = {}", categoryId);
		Category category = adminService.getCategoryParentLevel(categoryId);
		log.debug("category={}", category);
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(category);
	}
	
	@PostMapping("/product/productUpdate.do")
	public String productUpdate(
			Product product,
			@RequestParam(name = "upFile") List<MultipartFile> upFileList,
			@RequestParam(name = "delFile", required = false) int[] delFiles,
			RedirectAttributes redirectAttr) throws IllegalStateException, IOException {
		
		log.debug("product = {}", product);
		String saveDirectory = application.getRealPath("resources/upload/product");
		int result = 0;
		log.debug("delFiles = {}", delFiles);
		
		// 첨부파일 삭제
		if(delFiles != null) {
			for(int attachNo : delFiles) {
				
				// 파일 삭제
				ProductAttachment attach = adminService.selectOneAttachment(attachNo);
				File delFile = new File(saveDirectory, attach.getRenamedFilename());
				boolean deleted = delFile.delete();
				log.debug("{} 파일 삭제 : {}", attach.getRenamedFilename(), deleted);
				
				// product_attachment row제거
				result = adminService.deleteAttachment(attachNo);
				log.debug("{}번 attachment record 삭제 완료!", attachNo);
			}
		}
			
		// 첨부파일 추가
		for(MultipartFile upFile : upFileList) {
			log.debug("upFile = {}", upFile);
			log.debug("upFile#name = {}", upFile.getName()); // upFile
			log.debug("upFile#name = {}", upFile.getOriginalFilename());
			log.debug("upFile#size = {}", upFile.getSize());
			
			if(!upFile.isEmpty()) {
				// a. 서버 컴퓨터에 저장
				String renamedFilename = DrugstoreUtils.getRenamedFilename(upFile.getOriginalFilename());
				File destFile = new File(saveDirectory, renamedFilename);
				upFile.transferTo(destFile);
				
				// b. DB저장을 위해 Attachment 객체 생성
				ProductAttachment attach = new ProductAttachment(upFile.getOriginalFilename(), renamedFilename);
				attach.setPcode(product.getPcode());
				product.add(attach);
			}
		}
		log.debug("product#after = {}", product);
		result = adminService.updateProduct(product);
		
		redirectAttr.addFlashAttribute("msg", "상품을 성공적으로 수정하였습니다.");
			
		
		return "redirect:/admin/product/productUpdate.do?pcode=" + product.getPcode();
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
