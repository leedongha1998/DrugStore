package com.kh.drugstore.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DrugstoreUtils {
	
	
	/**
	 * totalPage 전체페이지수
	 * pagebarSize 표시할 페이지 수 5
	 * pageNo 페이지 번호(증감변수)
	 * pagebarStart 첫페이지
	 * pagebarEnd 마지막 페이지
	 * numPerPage 한 페이지만 표시할 컨텐츠 수 
	 * cPage 현재 페이지 
	 * offset = (현재페이지-1)*한 페이지에 표시할 컨텐츠 수 
	 * int limit = 한 페이지에 표시할 컨텐츠 수 
	 * 
	 */
	public static String getPagebar(int cPage, int limit, int totalContent, String url) {
		StringBuffer pagebar = new StringBuffer();
		url += "?cPage="; 		
		
		final int pagebarSize = 5; // 페이지바 버튼 수
		final int totalPage = (int) Math.ceil((double) totalContent / limit);
		final int pagebarStart = ((cPage - 1) / pagebarSize) * pagebarSize + 1;
		final int pagebarEnd = pagebarStart + pagebarSize - 1;
		int pageNo = pagebarStart;
		
		pagebar.append("<ul class=\"pagination justify-content-center\">\r\n");
		
		// 1. previous 
		if(pageNo == 1) {
			pagebar.append("<li class=\"page-item disabled\">\r\n"
					+ "	      <a class=\"page-link\" href=\"#\" aria-label=\"Previous\">\r\n"
					+ "	        <span aria-hidden=\"true\">&laquo;</span>\r\n"
					+ "	        <span class=\"sr-only\">Previous</span>\r\n"
					+ "	      </a>\r\n"
					+ "	    </li>\r\n");
		}
		else {
			pagebar.append("<li class=\"page-item\">\r\n"
					+ "	      <a class=\"page-link\" href=\"" + url + (pageNo - 1) + "\" aria-label=\"Previous\">\r\n"
					+ "	        <span aria-hidden=\"true\">&laquo;</span>\r\n"
					+ "	        <span class=\"sr-only\">Previous</span>\r\n"
					+ "	      </a>\r\n"
					+ "	    </li>\r\n");
		}
		
		// 2. pageNo
		while(pageNo <= pagebarEnd && pageNo <= totalPage) {
			if(pageNo == cPage) {
				pagebar.append("<li class=\"page-item active\"><a class=\"page-link\" href=\"#\">" + pageNo + "</a></li>\r\n");
			}
			else {
				pagebar.append("<li class=\"page-item\"><a class=\"page-link\" href=\"" + url + pageNo + "\">" + pageNo + "</a></li>\r\n");
			}
			
			pageNo++;
		}
		
		
		// 3. next
		if(pageNo > totalPage) {
			pagebar.append("<li class=\"page-item disabled\">\r\n"
					+ "	      <a class=\"page-link\" href=\"#\" aria-label=\"Next\">\r\n"
					+ "	        <span aria-hidden=\"true\">&raquo;</span>\r\n"
					+ "	        <span class=\"sr-only\">Next</span>\r\n"
					+ "	      </a>\r\n"
					+ "	    </li>\r\n");
		}
		else {
			pagebar.append("<li class=\"page-item\">\r\n"
					+ "	      <a class=\"page-link\" href=\"" + url + pageNo + "\" aria-label=\"Next\">\r\n"
					+ "	        <span aria-hidden=\"true\">&raquo;</span>\r\n"
					+ "	        <span class=\"sr-only\">Next</span>\r\n"
					+ "	      </a>\r\n"
					+ "	    </li>\r\n");
		}
		
		
		pagebar.append("</ul>");
		return pagebar.toString();
	}

	public static String getRenamedFilename(String originalFilename) {
		// 확장자추출
		int beginIndex = originalFilename.lastIndexOf(".");
		String ext = "";
		if(beginIndex > -1) 
			ext = originalFilename.substring(beginIndex); // .txt
		
		// 새이름 생성
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS_");
		DecimalFormat df = new DecimalFormat("000");
		return sdf.format(new Date()) + df.format(Math.random() * 1000) + ext;
	}
	

}
