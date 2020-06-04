package com.example.futuregenerator.controller;


import com.example.futuregenerator.base.BeanField;
import com.example.futuregenerator.base.GenerateDetail;
import com.example.futuregenerator.service.GenerateService;
import com.example.futuregenerator.utils.GenerateInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代码生成接口
 */
@RestController
@RequestMapping("/generate")
public class GenerateController {

	@Autowired
	private GenerateService generateService;


	@GetMapping("/getTableNameList")
	public List<String> getTableNameList(){
		List<String> list=generateService.getTableNameList();
		return list;
	}

	@GetMapping(params = { "tableName" })
	public GenerateDetail generateByTableName(String tableName) {
		GenerateDetail detail = new GenerateDetail();
		detail.setBeanName(generateService.upperFirstChar(tableName));
		List<BeanField> fields = generateService.listBeanField(tableName);
		String desc=generateService.getTableDesc(tableName);
		detail.setBeanDesc(desc);
		detail.setFields(fields);
		return detail;
	}



	@PostMapping
	public void save(@RequestBody GenerateInput input) {
		generateService.saveCode(input);
	}

}
