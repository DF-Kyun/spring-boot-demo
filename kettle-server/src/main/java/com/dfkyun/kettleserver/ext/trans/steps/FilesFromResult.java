package com.dfkyun.kettleserver.ext.trans.steps;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import com.dfkyun.kettleserver.ext.core.PropsUI;
import com.dfkyun.kettleserver.ext.trans.step.AbstractStep;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

@Component("FilesFromResult")
@Scope("prototype")
public class FilesFromResult extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
		
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		return e;
	}

}
