package com.example.futuregenerator.utils;

import com.example.futuregenerator.base.BeanField;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TemplateUtil {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	public static String getTemplete(String fileName) {
		return FileUtil.getText(TemplateUtil.class.getClassLoader().getResourceAsStream("generate/" + fileName));
	}

	public static void saveJava(GenerateInput input) {
		String path = input.getPath();
		String beanPackageName = input.getBeanPackageName();
		String beanName = input.getBeanName();
		List<String> beanFieldName = input.getBeanFieldName();
		List<String> beanFieldType = input.getBeanFieldType();
		List<String> beanFieldValue = input.getBeanFieldValue();
		

		String text = getTemplete("java.txt");
		text = text.replace("{beanPackageName}", beanPackageName).replace("{beanName}", beanName);

		String imports = "";
		if (beanFieldType.contains(BigDecimal.class.getSimpleName())) {
			imports += "import " + BigDecimal.class.getName() + ";\n";
		}
		if (beanFieldType.contains(Date.class.getSimpleName())) {
			imports += "import " + Date.class.getName() + ";";
		}

		text = text.replace("{import}", imports);
		text = text.replace("{beanDesc}", input.getBeanDesc());
		text = text.replace("{date1}", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		String filelds = getFields(beanFieldName, beanFieldType, beanFieldValue,input.getBeanFieldDesc());
		text = text.replace("{filelds}", filelds);
		text = text.replace("{getset}", getset(beanFieldName, beanFieldType));

		FileUtil.saveTextFile(text, path + File.separator +"src\\main\\java\\"+ getPackagePath(beanPackageName) + beanName + ".java");
		log.debug("生成java model：{}模板", beanName);
	}

	private static String getFields(List<String> beanFieldName, List<String> beanFieldType,
			List<String> beanFieldValue,List<String> desc) {
		StringBuffer buffer = new StringBuffer();
		int size = beanFieldName.size();
		for (int i = 0; i < size; i++) {
			String name = beanFieldName.get(i);
			if ("id".equals(name) || "createTime".equals(name) || "updateTime".equals(name)) {
				continue;
			}
			String type = beanFieldType.get(i);
			buffer.append("\t/**"+desc.get(i)+"*/\n");
			buffer.append("\tprivate ").append(type).append(" ").append(name);
			// 默认值
//			String value = beanFieldValue.get(i);
//			if (!StringUtils.isEmpty(value)) {
//				buffer.append(" = ");
//				if (type.equals(String.class.getSimpleName())) {
//					value = "\"" + value + "\"";
//				} else if (type.equals(Double.class.getSimpleName())) {
//					value = value + "D";
//				} else if (type.equals(Float.class.getSimpleName())) {
//					value = value + "F";
//				} else if (type.equals(BigDecimal.class.getSimpleName())) {
//					value = "new BigDecimal(" + value + ")";
//				}
//
//				buffer.append(value);
//			}
			buffer.append(";\n");
		}

		return buffer.toString();
	}

	private static String getset(List<String> beanFieldName, List<String> beanFieldType) {
		StringBuffer buffer = new StringBuffer();
		int size = beanFieldName.size();
		for (int i = 0; i < size; i++) {
			String name = beanFieldName.get(i);
			if ("id".equals(name) || "createTime".equals(name) || "updateTime".equals(name)) {
				continue;
			}

			String type = beanFieldType.get(i);
			buffer.append("\tpublic ").append(type).append(" get")
					.append(StringUtils.substring(name, 0, 1).toUpperCase() + name.substring(1, name.length()))
					.append("() {\n");
			buffer.append("\t\treturn ").append(name).append(";\n");
			buffer.append("\t}\n");
			buffer.append("\tpublic void set")
					.append(StringUtils.substring(name, 0, 1).toUpperCase() + name.substring(1, name.length()))
					.append("(").append(type).append(" ").append(name).append(") {\n");
			buffer.append("\t\tthis.").append(name).append(" = ").append(name).append(";\n");
			buffer.append("\t}\n");
		}

		return buffer.toString();
	}

	public static void saveJavaDao(GenerateInput input, List<BeanField> list) {
		String path = input.getPath();
		String tableName = input.getTableName();
		String beanPackageName = input.getBeanPackageName();
		String beanName = input.getBeanName();
		String daoPackageName = input.getDaoPackageName();
		String daoName = input.getDaoName();

		String text = getTemplete("dao.txt");
		text = text.replace("{daoPackageName}", daoPackageName);
		text = text.replace("{beanPackageName}", beanPackageName);
		text = text.replace("{daoName}", daoName);
		text = text.replace("{table_name}", tableName);
		text = text.replace("{beanName}", beanName);
		text = text.replace("{beanParamName}", lowerFirstChar(beanName));

		String insertColumns = getInsertColumns(input.getColumnNames());
		text = text.replace("{insert_columns}", insertColumns);
		String insertValues = getInsertValues(input.getColumnNames(), input.getBeanFieldName());
		text = text.replace("{insert_values}", insertValues);
		FileUtil.saveTextFile(text, path + File.separator +"src\\main\\java\\"+ getPackagePath(daoPackageName) + daoName + ".java");
		log.debug("生成java dao：{}模板", beanName);

		text = getTemplete("mapper.xml");
		String resMap = getResMap(input.getColumnNames(), input.getBeanFieldName(),list);
		text = text.replace("{resMap}",resMap);
		text = text.replace("{daoPackageName}", daoPackageName);
		text = text.replace("{beanPackageName}", beanPackageName);
		text = text.replace("{daoName}", daoName);
		text = text.replace("{insert_columns}", insertColumns);
		text = text.replace("{insert_values}", insertValues);
		text = text.replace("{table_name}", tableName);
		text = text.replace("{beanName}", beanName);
		String sets = getUpdateSets(input.getColumnNames(), input.getBeanFieldName());
		text = text.replace("{update_sets}", sets);
		String where = getWhere(input.getColumnNames(), input.getBeanFieldName());
		text = text.replace("{where}", where);
		FileUtil.saveTextFile(text, path + File.separator + "src\\main\\java\\"+ getPackagePath(daoPackageName) + beanName + "Mapper.xml");
	}
	private static String getResMap(List<String> columnNameList, List<String> beanFieldNameList,List<BeanField> list){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < columnNameList.size(); i++) {
			String beanFieldName = beanFieldNameList.get(i);
			String column = columnNameList.get(i);
			BeanField beanField = list.get(i);
			if(i==0){
				buffer.append("\t\t<id column=\"").append(column).append("\" property=\"").append(beanFieldName).append("\" jdbcType=\"");
				if(beanField.getColumnType().equals("int")){
					buffer.append("INTEGER\" />\n");
				}else if(beanField.getColumnType().equals("bigint")){
					buffer.append("BIGINT\" />\n");
				}else{
					buffer.append("VARCHAR\" />\n");
				}
			}else{
				buffer.append("\t\t<result column=\"").append(column).append("\" property=\"").append(beanFieldName).append("\" jdbcType=\"");
				if(beanField.getColumnType().equals("int")){
					buffer.append("INTEGER\" />\n");
				}else if(beanField.getColumnType().equals("bigint")){
					buffer.append("BIGINT\" />\n");
				}else{
					buffer.append("VARCHAR\" />\n");
				}
			}

		}

		String str = StringUtils.substringBeforeLast(buffer.toString(), ",");

		return str;
	}
	private static String getInsertValues(List<String> columnNames, List<String> beanFieldName) {
		StringBuffer buffer = new StringBuffer();
		int size = columnNames.size();
		for (int i = 0; i < size; i++) {
			String column = columnNames.get(i);
			if (!"id".equals(column)) {
				buffer.append("#{").append(beanFieldName.get(i)).append("}, ");
			}
		}

		String sets = StringUtils.substringBeforeLast(buffer.toString(), ",");
		return sets;
	}

	private static String getInsertColumns(List<String> columnNames) {
		StringBuffer buffer = new StringBuffer();
		int size = columnNames.size();
		for (int i = 0; i < size; i++) {
			String column = columnNames.get(i);
			if (!"id".equals(column)) {
				buffer.append(column).append(", ");
			}
		}

		String insertColumns = StringUtils.substringBeforeLast(buffer.toString(), ",");
		return insertColumns;
	}

	private static String getUpdateSets(List<String> columnNames, List<String> beanFieldName) {
		StringBuffer buffer = new StringBuffer();
		int size = columnNames.size();
		for (int i = 0; i < size; i++) {
			String column = columnNames.get(i);
			if (!"id".equals(column)) {
				buffer.append("\t\t\t<if test=\"params." + beanFieldName.get(i) + " != null and params." + beanFieldName.get(i) + " != ''\">\n");
				buffer.append("\t\t\t\t" + column).append(" = ").append("#{").append(beanFieldName.get(i))
						.append("}, \n");
				buffer.append("\t\t\t</if>\n");
			}
		}

		return buffer.toString();
	}

	private static String getWhere(List<String> columnNames, List<String> beanFieldName) {
		StringBuffer buffer = new StringBuffer();
		int size = beanFieldName.size();
		for (int i = 0; i < size; i++) {
			String column = columnNames.get(i);
			buffer.append("\t\t\t<if test=\"params." + beanFieldName.get(i) + " != null and params." + beanFieldName.get(i) + " != ''\">\n");
			buffer.append("\t\t\t\tand " + column).append(" = ").append("#{params.").append(beanFieldName.get(i))
					.append("} \n");
			buffer.append("\t\t\t</if>\n");
		}

		return buffer.toString();
	}

	/**
	 * 变量名
	 * 
	 * @param beanName
	 * @return
	 */
	public static String lowerFirstChar(String beanName) {
		String name = StrUtil.str2hump(beanName);
		String firstChar = name.substring(0, 1);
		name = name.replaceFirst(firstChar, firstChar.toLowerCase());

		return name;
	}

	private static String getPackagePath(String packageName) {
		String packagePath = packageName.replace(".", "/");
		if (!packagePath.endsWith("/")) {
			packagePath = packagePath + "/";
		}

		return packagePath;
	}

	public static void saveController(GenerateInput input) {
		String path = input.getPath();
		String beanPackageName = input.getBeanPackageName();
		String beanName = input.getBeanName();
		String daoPackageName = input.getDaoPackageName();
		String daoName = input.getDaoName();

		String text = getTemplete("controller.txt");
		text = text.replace("{daoPackageName}", daoPackageName);
		text = text.replace("{beanPackageName}", beanPackageName);
		text = text.replace("{daoName}", daoName);
		text = text.replace("{daoParamName}", lowerFirstChar(daoName));
		text = text.replace("{beanName}", beanName);
		text = text.replace("{beanParamName}", lowerFirstChar(beanName));
		text = text.replace("{controllerPkgName}", input.getControllerPkgName());
		text = text.replace("{controllerName}", input.getControllerName());
		text = text.replace("{servicePkgName}", input.getServicePkgName());
		text = text.replace("{serviceName}", input.getServiceName());
		
		FileUtil.saveTextFile(text, path + File.separator +"src\\main\\java\\"+ getPackagePath(input.getControllerPkgName())
				+ input.getControllerName() + ".java");
		log.debug("生成controller：{}模板", beanName);
	}

	public static void saveHtmlList(GenerateInput input) {
		String path = input.getPath();
		String beanName = input.getBeanName();
		String beanParamName = lowerFirstChar(beanName);

		String text = getTemplete("htmlList.txt");
		text = text.replace("{beanParamName}", beanParamName);
		text = text.replace("{beanName}", beanName);
		List<String> beanFieldNames = input.getBeanFieldName();
		text = text.replace("{columnsDatas}", getHtmlColumnsDatas(beanFieldNames));
		text = text.replace("{ths}", getHtmlThs(beanFieldNames));

		FileUtil.saveTextFile(text, path + File.separator +"src\\main\\resources\\static\\pages\\"+beanParamName+File.separator+ beanParamName + "List.html");
		log.debug("生成查询页面：{}模板", beanName);

		text = getTemplete("htmlAdd.txt");
		text = text.replace("{beanParamName}", beanParamName);
		text = text.replace("{addDivs}", getAddDivs(beanFieldNames));
		FileUtil.saveTextFile(text, path + File.separator +"src\\main\\resources\\static\\pages\\"+beanParamName+File.separator+ "add" + beanName + ".html");
		log.debug("生成添加页面：{}模板", beanName);

		text = getTemplete("htmlUpdate.txt");
		text = text.replace("{beanParamName}", beanParamName);
		text = text.replace("{addDivs}", getAddDivs(beanFieldNames));
		text = text.replace("{initData}", getInitData(beanFieldNames));
		FileUtil.saveTextFile(text, path + File.separator +"src\\main\\resources\\static\\pages\\"+beanParamName+File.separator+ "update" + beanName + ".html");
		log.debug("生成修改页面：{}模板", beanName);
	}

	private static CharSequence getInitData(List<String> beanFieldNames) {
		StringBuilder builder = new StringBuilder();
		beanFieldNames.forEach(b -> {
			builder.append("\t\t\t\t\t\t$('#" + b + "').val(data." + b + ");\n");
		});

		return builder.toString();
	}

	private static String getAddDivs(List<String> beanFieldNames) {
		StringBuilder builder = new StringBuilder();
		beanFieldNames.forEach(b -> {
			if (!"id".equals(b) && !"createTime".equals(b) && !"updateTime".equals(b)) {
				builder.append("\t\t\t<div class='form-group'>\n");
				builder.append("\t\t\t\t<label class='col-md-2 control-label'>" + b + "</label>\n");
				builder.append("\t\t\t\t<div class='col-md-10'>\n");
				builder.append("\t\t\t\t\t<input class='form-control' placeholder='" + b + "' type='text' name='" + b
						+ "' id='" + b + "' data-bv-notempty='true' data-bv-notempty-message='" + b + " 不能为空'>\n");
				builder.append("\t\t\t\t</div>\n");
				builder.append("\t\t\t</div>\n");
			}
		});
		return builder.toString();
	}

	private static String getHtmlThs(List<String> beanFieldNames) {
		StringBuilder builder = new StringBuilder();
		beanFieldNames.forEach(b -> {
			builder.append("\t\t\t\t\t\t\t\t\t<th>{beanFieldName}</th>\n".replace("{beanFieldName}", b));
		});
		return builder.toString();
	}

	private static String getHtmlColumnsDatas(List<String> beanFieldNames) {
		StringBuilder builder = new StringBuilder();
		beanFieldNames.forEach(b -> {
			builder.append("\t\t\t\t{\"data\" : \"{beanFieldName}\", \"defaultContent\" : \"\"},\n"
					.replace("{beanFieldName}", b));
		});

		builder.append("");
		return builder.toString();
	}

	public static void saveService(GenerateInput input) {
		// TODO Auto-generated method stub
		String path = input.getPath();
		String beanPackageName = input.getBeanPackageName();
		String beanName = input.getBeanName();
		String daoPackageName = input.getDaoPackageName();
		String daoName = input.getDaoName();
			
		String text = getTemplete("service.txt");
		text = text.replace("{servicePkgName}", input.getServicePkgName());
		text = text.replace("{serviceName}", input.getServiceName());
		text = text.replace("{daoPackageName}", daoPackageName);
		text = text.replace("{beanPackageName}", beanPackageName);
		text = text.replace("{daoName}", daoName);
		text = text.replace("{daoParamName}", lowerFirstChar(daoName));
		text = text.replace("{beanName}", beanName);
		text = text.replace("{beanParamName}", lowerFirstChar(beanName));

		FileUtil.saveTextFile(text, path + File.separator +"src\\main\\java\\"+ getPackagePath(input.getServicePkgName())
				+ input.getServiceName() + ".java");
		log.debug("生成Service：{}模板", path + File.separator + getPackagePath(input.getServicePkgName())
		+ input.getServiceName() + ".java");
		
		
		
		text = getTemplete("serviceImpl.txt");
		text = text.replace("{serviceImplPkgName}", input.getServiceImplPkgName());
		text = text.replace("{servicePkgName}", input.getServicePkgName());
		text = text.replace("{serviceName}", input.getServiceName());
		text = text.replace("{daoName}", daoName);
		text = text.replace("{daoParamName}", lowerFirstChar(daoName));
		text = text.replace("{beanName}", beanName);
		text = text.replace("{beanParamName}", lowerFirstChar(beanName));
		text = text.replace("{beanPackageName}", beanPackageName);
		text = text.replace("{daoPackageName}", daoPackageName);
		text = text.replace("{beanPackageName}", beanPackageName);
		
		
		
		FileUtil.saveTextFile(text, path + File.separator +"src\\main\\java\\"+ getPackagePath(input.getServiceImplPkgName())
				+ input.getServiceName() + "Impl.java");
		log.debug("生成ServiceImpl：{}模板",path + File.separator +"src\\main\\java\\"+ getPackagePath(input.getServiceImplPkgName())
		+ input.getServiceName() + "Impl.java");
		
		
	}

}
