package com.dfkyun.kettleserver.ext;

import java.util.ArrayList;

import com.dfkyun.kettleserver.ext.core.database.ConfigBean;
import com.dfkyun.kettleserver.ext.utils.SpringUtil;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.pentaho.metastore.stores.delegate.DelegatingMetaStore;

public class App {

	private static App app;
	
	public static KettleDatabaseRepositoryMeta meta;

	private Repository repository;
	
	
	public static App getInstance() {
		if (app == null) {
			app = new App();
		}
		return app;
	}


	public Repository getRepository() {

		if(repository == null){
			try {
				
				ConfigBean dataSource = SpringUtil.getBean(ConfigBean.class);
				
				String url = dataSource.getUrl();
				String hostname = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
				String port = url.substring(url.lastIndexOf(":") + 1, url.lastIndexOf("/"));
				String dbName = url.substring(url.lastIndexOf("/") + 1);
				
				DatabaseMeta dbMeta = new DatabaseMeta();
				dbMeta.setName("webkettle");
				dbMeta.setDBName(dbName);
				dbMeta.setDatabaseType("MYSQL");
				dbMeta.setAccessType(0);
				dbMeta.setHostname(hostname);
				dbMeta.setServername(hostname);
				dbMeta.setDBPort(port);
				dbMeta.setUsername(dataSource.getUsername());
				dbMeta.setPassword(dataSource.getPassword());
				ObjectId objectId = new LongObjectId(100);
				dbMeta.setObjectId(objectId);
				dbMeta.setShared(true);
	            dbMeta.setSupportsBooleanDataType(true);
				dbMeta.addExtraOption(dbMeta.getPluginId(), "characterEncoding", "utf8");
				dbMeta.addExtraOption(dbMeta.getPluginId(), "useUnicode", "true");
				dbMeta.addExtraOption(dbMeta.getPluginId(), "autoReconnect", "true");
				meta = new KettleDatabaseRepositoryMeta();
				meta.setName("webkettle");
				meta.setId("KettleDatabaseRepository");
				meta.setConnection(dbMeta);
				meta.setDescription("webkettle");
				KettleDatabaseRepository repository = new KettleDatabaseRepository();
				repository.init(meta);
				repository.connect("admin", "admin");
				this.repository = repository;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return repository;
	}
	
	private Repository defaultRepository;
	
	public Repository getDefaultRepository() {
		return this.defaultRepository;
	}
	
	public void selectRepository(Repository repo) {
		if(repository != null) {
			repository.disconnect();
		}
		repository = repo;
	}

	private DelegatingMetaStore metaStore;

	public DelegatingMetaStore getMetaStore() {
		return metaStore;
	}
	
	private RowMetaAndData variables = null;
	private ArrayList<String> arguments = new ArrayList<String>();
	
	public String[] getArguments() {
		return arguments.toArray(new String[arguments.size()]);
	}
	
	public RowMetaAndData getVariables() {
		return variables;
	}

}
