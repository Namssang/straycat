package com.straycat.cat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.straycat.service.CatService;

@Controller
public class CatController
{

	@Autowired
	private CatService service;

	
	////////////////////////////////// 고양이 리스트 부분 ////////////////////////////////////////////////////////////////////////////////////////
	
	// 길냥이관리 게시판 리스트 조회
	@RequestMapping(value="/cat") 
	public String selectList(Model model
			, @RequestParam(name="searchGu", defaultValue = "") String searchGu
			, @RequestParam(name="searchDong", defaultValue = "") String searchDong
			, HttpServletRequest request) 
	{
		List<Map<String, Object>> location = null;
		Map<String, Object> map = new HashMap<>();
		int dataCount = 0;
		
		try
		{
			map.put("gu", request.getParameter("gu"));
			map.put("dong", request.getParameter("dong"));
			
			location = service.listCat(map);
			
			dataCount = service.dataCount(searchGu, searchDong);
			
			List<Map<String, Object>> gu = service.listGu();

			model.addAttribute("list", location);
			model.addAttribute("gu", gu);
			model.addAttribute("dataCount", dataCount);
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return "Cat_List"; 
	}
	
	
	
	
	////////////////////////////////// 고양이 리스트 부분 ////////////////////////////////////////////////////////////////////////////////////////

	
	////////////////////////////////// 고상페 페이지부분 ////////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping("/catdetail")
	public String selectList(Model model, @RequestParam String id, @RequestParam(name="user_code", defaultValue="") String user_code)
	{
		Map<String, Object> catInfo = service.catInfo(id);
		List<Map<String, Object>> catLocation = service.catLocation(id);
		List<Map<String, Object>> catActReg = service.catActReg(id);

		model.addAttribute("catInfo", catInfo);
		model.addAttribute("catActReg", catActReg);
		model.addAttribute("catLocation", catLocation);

		return "Cat_Detail";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/catLocation_ajax")
	@ResponseBody
	public ResponseEntity catLocation(@RequestParam(name = "cat_id") String cat_id)
	{
		HttpHeaders headers = new HttpHeaders();
		ArrayList<HashMap> hmlist = new ArrayList<HashMap>();

		List<Map<String, Object>> commList = service.catLocation(cat_id);

		if (commList.size() > 0)
		{
			for (int i = 0; i < commList.size(); i++)
			{
				HashMap hm = new HashMap();

				hm.put("LATITUDE", commList.get(i).get("LATITUDE"));
				hm.put("LONGITUDE", commList.get(i).get("LONGITUDE"));
				

				hmlist.add(hm);
			}
		}

		JSONArray json = new JSONArray(hmlist);

		return new ResponseEntity(json.toString(), headers, HttpStatus.CREATED);
	}
	
	//////////////////////////////////고상페 페이지부분 ////////////////////////////////////////////////////////////////////////////////////////
	
	
	  // 길고양이 등록 컨트롤러
	  
	  @RequestMapping(value="/registration") 
	  public String catregistration(@RequestParam Map<String,Object> param, HttpServletRequest request, Model model) 
	  {
			
		  	service.addCat(param);
		  
			model.addAttribute("gu", request.getParameter("gu"));
			model.addAttribute("dong", request.getParameter("dong"));
			
		
			
			return "redirect:cat";
		  	
	  }
	  
	  
	  // 길고양이 등록 폼 컨트롤러
	  @RequestMapping(value="/catregistration") 
	  public String catregistrationForm(Model model) 
	  {
		// 구 셀렉트박스 값 넘기기
			List<Map<String, Object>> gu = service.listGu();
			model.addAttribute("gu", gu);

			return "Cat_Registration";
	  }
	  
	  
	  // 고상페 활동 날짜 변경 
	 @RequestMapping(value="/act_date")
	 @ResponseBody
	 public List<Map<String, Object>> actDate(@RequestParam(name="id") String id,
			 			 @RequestParam(name="act_date") String act_date, 
			 			 HttpServletResponse response) throws IOException
	 {
		 List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		 
		 act_date=act_date.replaceAll("-", "");
		 
		 Map<String, String> map = new HashMap<String, String>();
		 map.put("cat_code", id);
		 map.put("act_date", act_date);
		 
		 service.actDate(map);
		 
		 //JSONArray json = new JSONArray(service.actDate(id,act_date));
		 
		 //response.getWriter().print(json.toString());	
		 
		 return result;
	 }
}

