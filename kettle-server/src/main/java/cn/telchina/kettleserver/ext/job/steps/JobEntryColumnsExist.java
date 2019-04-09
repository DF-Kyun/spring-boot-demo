package cn.telchina.kettleserver.ext.job.steps;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import cn.telchina.kettleserver.ext.core.PropsUI;
import cn.telchina.kettleserver.ext.job.step.AbstractJobEntry;
import cn.telchina.kettleserver.ext.utils.JSONArray;
import cn.telchina.kettleserver.ext.utils.JSONObject;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;


@Component("COLUMNS_EXIST")
@Scope("prototype")
public class JobEntryColumnsExist extends AbstractJobEntry {

	@Override
	public void decode(JobEntryInterface jobEntry, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
		org.pentaho.di.job.entries.columnsexist.JobEntryColumnsExist jobEntryColumnsExist = (org.pentaho.di.job.entries.columnsexist.JobEntryColumnsExist) jobEntry;

		jobEntryColumnsExist.setDatabase(DatabaseMeta.findDatabase(databases, cell.getAttribute("connection")));
		jobEntryColumnsExist.setSchemaname(cell.getAttribute("schemaname"));
		jobEntryColumnsExist.setTablename(cell.getAttribute("tablename"));
		
		
		JSONArray jsonArray = JSONArray.fromObject(cell.getAttribute("fields"));
		jobEntryColumnsExist.setArguments(new String[jsonArray.size()]);
		for(int i=0; i<jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			jobEntryColumnsExist.getArguments()[i] = jsonObject.optString("field");
		}
	}

	@Override
	public Element encode(JobEntryInterface jobEntry) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.JOB_JOBENTRY_NAME);
		org.pentaho.di.job.entries.columnsexist.JobEntryColumnsExist jobEntryColumnsExist = (org.pentaho.di.job.entries.columnsexist.JobEntryColumnsExist) jobEntry;

		DatabaseMeta databaseMeta = jobEntryColumnsExist.getDatabase();
		e.setAttribute("connection", databaseMeta == null ? "" : databaseMeta.getName());
		e.setAttribute("schemaname", jobEntryColumnsExist.getSchemaname());
		e.setAttribute("tablename", jobEntryColumnsExist.getTablename());
		
		JSONArray jsonArray = new JSONArray();
		if(jobEntryColumnsExist.getArguments() != null) {
			for(int j=0; j< jobEntryColumnsExist.getArguments().length; j++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("field", jobEntryColumnsExist.getArguments()[j]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("fields", jsonArray.toString());
		
		return e;
	}


}
