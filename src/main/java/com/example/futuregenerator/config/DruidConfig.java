package com.example.futuregenerator.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Druid数据源配置
 */

@Configuration
public class DruidConfig {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	DataSource dataSource ;
	
	@Bean
	public ServletRegistrationBean druidServlet() {
		log.info("init Druid Servlet Configuration ");
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		// IP白名单
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
		// IP黑名单(共同存在时，deny优先于allow)
//		servletRegistrationBean.addInitParameter("deny", "192.168.27.26");
//		// 控制台管理用户
//		servletRegistrationBean.addInitParameter("loginUsername", "admin");
//		servletRegistrationBean.addInitParameter("loginPassword", "admin");
//		// 是否能够重置数据 禁用HTML页面上的“Reset All”功能
//		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}
	/**
     * 自动识别使用的数据库类型
     * 在mapper.xml中databaseId的值就是跟这里对应，
     * 如果没有databaseId选择则说明该sql适用所有数据库
     * */
    @Bean
    public DatabaseIdProvider getDatabaseIdProvider(){
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("Oracle","oracle");
        properties.setProperty("MySQL","mysql");
        properties.setProperty("DB2","db2");
        properties.setProperty("Derby","derby");
        properties.setProperty("H2","h2");
        properties.setProperty("HSQL","hsql");
        properties.setProperty("Informix","informix");
        properties.setProperty("MS-SQL","ms-sql");
        properties.setProperty("PostgreSQL","postgresql");
        properties.setProperty("Sybase","sybase");
        properties.setProperty("Hana","hana");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
	
    @Bean(name="dbType")
    public String getDatabaseType(){
    	try {
			return getDatabaseIdProvider().getDatabaseId(dataSource);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    
	/**
	 * 数据源配置
	 *
	 */
	@ConfigurationProperties(prefix = "spring.datasource")
	public class DataSourceProperties {
		private String url;
		private String username;
		private String password;
		private String driverClassName;
		private int initialSize;
		private int minIdle;
		private int maxActive;
		private int maxWait;
		private int timeBetweenEvictionRunsMillis;
		private int minEvictableIdleTimeMillis;
		private String validationQuery;
		private boolean testWhileIdle;
		private boolean testOnBorrow;
		private boolean testOnReturn;
		private boolean poolPreparedStatements;
		private int maxPoolPreparedStatementPerConnectionSize;
		private String filters;
		private String connectionProperties;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getDriverClassName() {
			return driverClassName;
		}

		public void setDriverClassName(String driverClassName) {
			this.driverClassName = driverClassName;
		}

		public int getInitialSize() {
			return initialSize;
		}

		public void setInitialSize(int initialSize) {
			this.initialSize = initialSize;
		}

		public int getMinIdle() {
			return minIdle;
		}

		public void setMinIdle(int minIdle) {
			this.minIdle = minIdle;
		}

		public int getMaxActive() {
			return maxActive;
		}

		public void setMaxActive(int maxActive) {
			this.maxActive = maxActive;
		}

		public int getMaxWait() {
			return maxWait;
		}

		public void setMaxWait(int maxWait) {
			this.maxWait = maxWait;
		}

		public int getTimeBetweenEvictionRunsMillis() {
			return timeBetweenEvictionRunsMillis;
		}

		public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
			this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
		}

		public int getMinEvictableIdleTimeMillis() {
			return minEvictableIdleTimeMillis;
		}

		public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
			this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
		}

		public String getValidationQuery() {
			return validationQuery;
		}

		public void setValidationQuery(String validationQuery) {
			this.validationQuery = validationQuery;
		}

		public boolean isTestWhileIdle() {
			return testWhileIdle;
		}

		public void setTestWhileIdle(boolean testWhileIdle) {
			this.testWhileIdle = testWhileIdle;
		}

		public boolean isTestOnBorrow() {
			return testOnBorrow;
		}

		public void setTestOnBorrow(boolean testOnBorrow) {
			this.testOnBorrow = testOnBorrow;
		}

		public boolean isTestOnReturn() {
			return testOnReturn;
		}

		public void setTestOnReturn(boolean testOnReturn) {
			this.testOnReturn = testOnReturn;
		}

		public boolean isPoolPreparedStatements() {
			return poolPreparedStatements;
		}

		public void setPoolPreparedStatements(boolean poolPreparedStatements) {
			this.poolPreparedStatements = poolPreparedStatements;
		}

		public int getMaxPoolPreparedStatementPerConnectionSize() {
			return maxPoolPreparedStatementPerConnectionSize;
		}

		public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
			this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
		}

		public String getFilters() {
			return filters;
		}

		public void setFilters(String filters) {
			this.filters = filters;
		}

		public String getConnectionProperties() {
			return connectionProperties;
		}

		public void setConnectionProperties(String connectionProperties) {
			this.connectionProperties = connectionProperties;
		}

		@Bean
		@Primary
		public DataSource dataSource() {
			DruidDataSource datasource = new DruidDataSource();
			datasource.setUrl(url);
			datasource.setUsername(username);
			datasource.setPassword(password);
			datasource.setDriverClassName(driverClassName);

			datasource.setInitialSize(initialSize);
			datasource.setMinIdle(minIdle);
			datasource.setMaxActive(maxActive);
			datasource.setMaxWait(maxWait);
			datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
			datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
			datasource.setValidationQuery(validationQuery);
			datasource.setTestWhileIdle(testWhileIdle);
			datasource.setTestOnBorrow(testOnBorrow);
			datasource.setTestOnReturn(testOnReturn);
			datasource.setPoolPreparedStatements(poolPreparedStatements);
			datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
			try {
				datasource.setFilters(filters);
			} catch (SQLException e) {
				log.error("异常", e);
			}
			datasource.setConnectionProperties(connectionProperties);
			return datasource;
		}
	}
}
