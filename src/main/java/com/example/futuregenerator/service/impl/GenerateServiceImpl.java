package com.example.futuregenerator.service.impl;

import com.example.futuregenerator.base.BeanField;
import com.example.futuregenerator.service.GenerateService;
import com.example.futuregenerator.utils.GenerateInput;
import com.example.futuregenerator.utils.StrUtil;
import com.example.futuregenerator.utils.TemplateUtil;
import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GenerateServiceImpl implements GenerateService {

	@Resource
	private String dbType;
	@Autowired
	private JdbcTemplate jdbcTemplate;
    @Value("${spring.datasource.url}")
    private String tableSchema;
	private RowMapper<BeanField> beanFieldMapper = new RowMapper<BeanField>() {

		@Override
		public BeanField mapRow(ResultSet rs, int paramInt) throws SQLException {
			BeanField beanField = new BeanField();
			beanField.setColumnName(rs.getString("column_name"));
			beanField.setColumnType(rs.getString("data_type"));
			beanField.setColumnComment(rs.getString("column_comment"));
			beanField.setColumnDefault(rs.getString("column_default"));

			return beanField;
		}
	};

	@Override
	public List<BeanField> listBeanField(String tableName) {
		String sql="select column_name, data_type, column_comment, column_default FROM information_schema.columns WHERE table_name= ? and table_schema = (select database())";
		if("oracle".equals(dbType)) {
			sql="select c.column_name,c.DATA_TYPE,(select cc.comments  from user_col_comments cc WHERE cc.table_name=c.TABLE_NAME and cc.column_name=c.COLUMN_NAME) column_comment , " + 
					"c.DATA_DEFAULT column_default " + 
					"from user_tab_columns c where c.TABLE_NAME=?";
		}
		
		List<BeanField> beanFields = jdbcTemplate.query(sql,
				new String[] { "oracle".equals(dbType)?tableName.toUpperCase():tableName }, beanFieldMapper);
		if (CollectionUtils.isEmpty(beanFields)) {
			throw new IllegalArgumentException("表" + tableName + "不存在");
		}
		if(map==null || map.size()==0) {
			initMap();
		}
		beanFields.parallelStream().forEach(b -> {
			b.setName(StrUtil.str2hump(b.getColumnName()));
			String type = map.get(b.getColumnType());
			if (type == null) {
				type = String.class.getSimpleName();
			}
			b.setType(type);
			if ("id".equals(b.getName())) {
				b.setType(Long.class.getSimpleName());
			}

			b.setColumnDefault(b.getColumnDefault() == null ? "" : b.getColumnDefault());
		});

		return beanFields;
	}
	@Override
	public String getTableDesc(String tableName) {
		String sql="select t.TABLE_COMMENT tableDesc from information_schema.TABLES t where TABLE_Name=? and table_schema=(select database())";
		Map map=jdbcTemplate.queryForMap(sql,new String[] { "oracle".equals(dbType)?tableName.toUpperCase():tableName });
		if(map.containsKey("tableDesc")) {
			return map.get("tableDesc").toString();
		}
		return null;
	}

	@Override
	public List<String> getTableNameList() {
        List<String> list=null;
		if(!"oracle".equals(dbType)){
            String schema = tableSchema.substring(0, tableSchema.indexOf('?')).split("/")[3];
			String sql="select table_name from information_schema.tables where table_schema=?";
            list=jdbcTemplate.queryForList(sql, new Object[]{schema},String.class);
		}
		return list;
	}

	/**
	 * mysql类型与java类型部分对应关系
	 */
	private static Map<String, String> map = Maps.newHashMap();
	public void initMap(){
		if( "oracle".equals(dbType)) {
			map.put("NUMBER", Integer.class.getSimpleName());
			map.put("DATE", Date.class.getSimpleName());
			map.put("VERCHAR2", String.class.getSimpleName());
			map.put("NVERCHAR2", String.class.getSimpleName());
			map.put("CHAR", String.class.getSimpleName());
			map.put("NCHAR", String.class.getSimpleName());
			map.put("CLOB", String.class.getSimpleName());
			map.put("NCLOB", String.class.getSimpleName());
			map.put("BLOB", byte[].class.getSimpleName());
		}else {
			map.put("int", Integer.class.getSimpleName());
			map.put("tinyint", Integer.class.getSimpleName());
			map.put("double", Double.class.getSimpleName());
			map.put("float", Float.class.getSimpleName());
			map.put("decimal", BigDecimal.class.getSimpleName());
			map.put("date", Date.class.getSimpleName());
			map.put("timestamp", Date.class.getSimpleName());
			map.put("datetime", Date.class.getSimpleName());
			map.put("varchar", String.class.getSimpleName());
			map.put("text", String.class.getSimpleName());
			map.put("longtext", String.class.getSimpleName());
		}
	}

	@Override
	public String upperFirstChar(String string) {
		String name = StrUtil.str2hump(string);
		String firstChar = name.substring(0, 1);
		name = name.replaceFirst(firstChar, firstChar.toUpperCase());

		return name;
	}

	@Override
	public void saveCode(GenerateInput input) {
		TemplateUtil.saveJava(input);
		TemplateUtil.saveJavaDao(input);
		TemplateUtil.saveService(input);
		TemplateUtil.saveController(input);
		//TemplateUtil.saveHtmlList(input);
	}

	

}
