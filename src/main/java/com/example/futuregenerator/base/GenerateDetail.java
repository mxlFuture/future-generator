package com.example.futuregenerator.base;

import java.io.Serializable;
import java.util.List;

public class GenerateDetail implements Serializable {

	private static final long serialVersionUID = -164567294469931676L;

	private String beanName;
	private List<BeanField> fields;
	private String beanDesc; //表或实体注释

	public String getBeanDesc() {
		return beanDesc;
	}

	public void setBeanDesc(String beanDesc) {
		this.beanDesc = beanDesc;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getPath() {
		return System.getProperty("user.dir");
	}
	public List<BeanField> getFields() {
		return fields;
	}

	public void setFields(List<BeanField> fields) {
		this.fields = fields;
	}
}
