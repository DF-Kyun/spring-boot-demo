package com.dfkyun.kettleserver.ext.trans.steps;

import com.dfkyun.kettleserver.ext.utils.JSONArray;
import com.dfkyun.kettleserver.ext.utils.JSONObject;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import com.dfkyun.kettleserver.ext.core.PropsUI;
import com.dfkyun.kettleserver.ext.trans.step.AbstractStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.systemdata.SystemDataMeta;
import org.pentaho.di.trans.steps.systemdata.SystemDataTypes;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

@Component("SystemInfo")
@Scope("prototype")
public class SystemInfo extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
		SystemDataMeta systemDataMeta = (SystemDataMeta) stepMetaInterface;
		
		String fields = cell.getAttribute("fields");
		if(StringUtils.hasText(fields)) {
			JSONArray jsonArray = JSONArray.fromObject(fields);
			
			String[] fieldName = new String[jsonArray.size()];
			SystemDataTypes[] fieldType = new SystemDataTypes[jsonArray.size()];
//			for(int i=0; i<jsonArray.size(); i++) {
//				JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//				fieldName[i] = jsonObject.optString("name");
//				//jsonObject.optString("type")
//				fieldType[i] = systemDataMeta.getFieldType();
//			}
			
			systemDataMeta.setFieldName(fieldName);
			systemDataMeta.setFieldType(systemDataMeta.getFieldType());
		}
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface) throws Exception {
		SystemDataMeta systemDataMeta = (SystemDataMeta) stepMetaInterface;
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		
		JSONArray jsonArray = new JSONArray();
		if(systemDataMeta.getFieldName() != null) {
			for(int i=0; i<systemDataMeta.getFieldName().length; i++) {
				String fieldName = systemDataMeta.getFieldName()[i];
				SystemDataTypes fieldType = systemDataMeta.getFieldType()[i];
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", fieldName);
				jsonObject.put("type", fieldType != null ? fieldType.getCode() : "");
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("fields", jsonArray.toString());
		
		return e;
	}

}
