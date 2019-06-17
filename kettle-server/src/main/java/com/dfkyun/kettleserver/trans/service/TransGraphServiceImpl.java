package com.dfkyun.kettleserver.trans.service;

import com.dfkyun.kettleserver.ext.App;
import com.dfkyun.kettleserver.ext.PluginFactory;
import com.dfkyun.kettleserver.ext.base.GraphCodec;
import com.dfkyun.kettleserver.ext.utils.ResponseUtils;
import com.dfkyun.kettleserver.ext.utils.StringEscapeHelper;
import com.dfkyun.kettleserver.ext.utils.TaskType;
import com.dfkyun.kettleserver.ext.vo.KettleServerResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.pentaho.di.base.AbstractMeta;
import org.pentaho.di.repository.*;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.metastore.stores.delegate.DelegatingMetaStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransGraphServiceImpl implements TransGraphService {

    @Override
    public KettleServerResponse createTrans(){

        Repository repository = App.getInstance().getRepository();
        RepositoryDirectoryInterface directory = null;
        TransMeta transMeta = null;
        // transName需要唯一值
        String transName = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            directory = repository.findDirectory("/");
            if(directory == null)
                directory = repository.getUserHomeDirectory();
            if(repository.exists(transName, directory, RepositoryObjectType.TRANSFORMATION)) {
                return ResponseUtils.fail("", "该转换已经存在，请重新输入！");
            }
            transMeta = new TransMeta();
            transMeta.setRepository(App.getInstance().getRepository());
            transMeta.setMetaStore(App.getInstance().getMetaStore());
            transMeta.setName(transName);
            transMeta.setRepositoryDirectory(directory);
            repository.save(transMeta, "add: " + new Date(), null,true);

            return ResponseUtils.success(transName, "创建成功");
        } catch (Exception e) {
            //出现异常回滚
            e.printStackTrace();

            return ResponseUtils.fail("", "创建失败!");
        }

    }

    @Override
    public KettleServerResponse saveTrans(String transName, String type, JSONObject jo) throws Exception {

        Repository repository = App.getInstance().getRepository();
        GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
        String graphXml = "";

        if(TaskType.TASK_TYPE_CENTRAL_REPOSITORY.equals(type)){
            graphXml = getTransCentralXml(transName, type, jo);
        }else if(TaskType.TASK_TYPE_CLEAN_RECORD.equals(type)){
            graphXml = getTransCleanXml(transName, type, jo);
        }else if(TaskType.TASK_TYPE_STATUS_UPDATE.equals(type)){
            graphXml = getTransStatusXml(transName, type, jo);
        }else if(TaskType.TASK_TYPE_JOB.equals(type)){

        }

        // System.out.println(StringEscapeHelper.decode(graphXml));
        AbstractMeta transMeta = codec.decode(StringEscapeHelper.decode(graphXml));
        repository = App.getInstance().getRepository();
        DelegatingMetaStore metaStore = App.getInstance().getMetaStore();
        ObjectId existingId = repository.getTransformationID(transMeta.getName(), transMeta.getRepositoryDirectory());
        if (transMeta.getCreatedDate() == null)
            transMeta.setCreatedDate(new Date());
        if (transMeta.getObjectId() == null)
            transMeta.setObjectId(existingId);
        transMeta.setModifiedDate(new Date());

        boolean versioningEnabled = true;
        boolean versionCommentsEnabled = true;
        String fullPath = transMeta.getRepositoryDirectory() + "/" + transMeta.getName() + transMeta.getRepositoryElementType().getExtension();
        RepositorySecurityProvider repositorySecurityProvider = repository.getSecurityProvider() != null ? repository.getSecurityProvider() : null;
        if (repositorySecurityProvider != null) {
            versioningEnabled = repositorySecurityProvider.isVersioningEnabled(fullPath);
            versionCommentsEnabled = repositorySecurityProvider.allowsVersionComments(fullPath);
        }
        String versionComment = null;
        if (!versioningEnabled || !versionCommentsEnabled) {
            versionComment = "";
        } else {
            versionComment = "no comment";
        }
        repository.save(transMeta, versionComment, null);

        return ResponseUtils.success(transName,"转换保存成功！");

    }

    private String getTransCentralXml(String transName, String type, JSONObject jo) throws Exception {

        String xmlString = "";
        ClassPathResource resource = new ClassPathResource("templates/"+type+".xml");

        SAXReader reader = new SAXReader();
        Document document = reader.read(resource.getInputStream());
        Element bookStore = document.getRootElement();
        Iterator it = bookStore.elementIterator();
        // 遍历迭代器，获取根节点中的信息
        while (it.hasNext()) {
            Element book = (Element) it.next();
            Iterator itt = book.elementIterator();
            while (itt.hasNext()) {
                // 节点名及节点值
                Element bookChild = (Element) itt.next();
                String nodeName = bookChild.getName();

                // 设置数据源及转换名
                if("Info".equals(nodeName)){
                    // 获取属性名以及 属性值
                    List<Attribute> bookAttrs = bookChild.attributes();
                    for (Attribute attr : bookAttrs) {
                        String attrName = attr.getName();
                        if("databases".equals(attrName)){
                            attr.setValue(((JSONArray) jo.get("databases")).toString());
                        }
                        if("name".equals(attrName)){
                            attr.setValue(transName);
                        }
                    }
                }
                // 设置步骤
                Attribute stepAttr = bookChild.attribute("id");
                if("Step".equals(nodeName) && "2".equals(stepAttr.getValue())){
                    Map<String,Object> step2 = (Map<String, Object>) jo.get("step2");
                    List<Attribute> attrs2 = bookChild.attributes();
                    for (Attribute attr : attrs2) {
                        String attrName = attr.getName();
                        if("connection".equals(attrName)){
                            attr.setValue((String) step2.get("connection"));
                        }
                        if("sql".equals(attrName)){
                            attr.setValue((String) step2.get("sql"));
                        }
                    }
                }

                if("Step".equals(nodeName) && "3".equals(stepAttr.getValue())){
                    Map<String,Object> step3 = (Map<String, Object>) jo.get("step3");
                    List<Attribute> attrs3 = bookChild.attributes();
                    for (Attribute attr : attrs3) {
                        String attrName = attr.getName();
                        if("connection".equals(attrName)){
                            attr.setValue((String) step3.get("connection"));
                        }
                        if("fields".equals(attrName)){
                            attr.setValue(((JSONArray) step3.get("fields")).toString());
                        }
                        if("table".equals(attrName)){
                            attr.setValue((String) step3.get("table"));
                        }
                        if("schema".equals(attrName)){
                            attr.setValue((String) step3.get("schema"));
                        }
                    }
                }

                if("Step".equals(nodeName) && "5".equals(stepAttr.getValue())){
                    Map<String,Object> step5 = (Map<String, Object>) jo.get("step5");
                    List<Attribute> attrs5 = bookChild.attributes();
                    for (Attribute attr : attrs5) {
                        String attrName = attr.getName();
                        if("condition".equals(attrName)){
                            attr.setValue(((JSONObject) step5.get("condition")).toString());
                        }
                    }
                }

                if("Step".equals(nodeName) && "6".equals(stepAttr.getValue())){
                    Map<String,Object> step6 = (Map<String, Object>) jo.get("step3");
                    List<Attribute> attrs6 = bookChild.attributes();
                    for (Attribute attr : attrs6) {
                        String attrName = attr.getName();
                        if("connection".equals(attrName)){
                            attr.setValue((String) step6.get("connection"));
                        }
                        if("fields".equals(attrName)){
                            attr.setValue(((JSONArray) step6.get("fields")).toString());
                        }
                        if("table".equals(attrName)){
                            attr.setValue((String) step6.get("table"));
                        }
                        if("schema".equals(attrName)){
                            attr.setValue((String) step6.get("schema"));
                        }
                    }
                }
            }
        }
        xmlString = document.asXML();

        return xmlString;
    }

    private String getTransCleanXml(String transName, String type, JSONObject jo) throws Exception {

        String xmlString = "";
        ClassPathResource resource = new ClassPathResource("templates/"+type+".xml");

        SAXReader reader = new SAXReader();
        Document document = reader.read(resource.getInputStream());
        Element bookStore = document.getRootElement();
        Iterator it = bookStore.elementIterator();
        // 遍历迭代器，获取根节点中的信息
        while (it.hasNext()) {
            Element book = (Element) it.next();
            Iterator itt = book.elementIterator();
            while (itt.hasNext()) {
                // 节点名及节点值
                Element bookChild = (Element) itt.next();
                String nodeName = bookChild.getName();
                // 设置数据源及转换名
                if("Info".equals(nodeName)){
                    // 获取属性名以及 属性值
                    List<Attribute> bookAttrs = bookChild.attributes();
                    for (Attribute attr : bookAttrs) {
                        String attrName = attr.getName();
                        if("name".equals(attrName)){
                            attr.setValue(transName);
                        }
                    }
                }
                // 设置步骤
                Attribute stepAttr = bookChild.attribute("id");
                if("Step".equals(nodeName) && "2".equals(stepAttr.getValue())){
                    Map<String,Object> step2 = (Map<String, Object>) jo.get("step2");
                    List<Attribute> attrs2 = bookChild.attributes();
                    for (Attribute attr : attrs2) {
                        String attrName = attr.getName();
                        if("connection".equals(attrName)){
                            attr.setValue((String) step2.get("connection"));
                        }
                        if("fields".equals(attrName)){
                            attr.setValue(((JSONArray) step2.get("fields")).toString());
                        }
                        if("table".equals(attrName)){
                            attr.setValue((String) step2.get("table"));
                        }
                        if("schema".equals(attrName)){
                            attr.setValue((String) step2.get("schema"));
                        }
                    }
                }

                if("Step".equals(nodeName) && "4".equals(stepAttr.getValue())){
                    Map<String,Object> step4 = (Map<String, Object>) jo.get("step4");
                    List<Attribute> attrs4 = bookChild.attributes();
                    for (Attribute attr : attrs4) {
                        String attrName = attr.getName();
                        if("connection".equals(attrName)){
                            attr.setValue((String) step4.get("connection"));
                        }
                        if("sql".equals(attrName)){
                            attr.setValue((String) step4.get("sql"));
                        }
                    }
                }
            }
        }

        xmlString = document.asXML();
        return xmlString;
    }

    private String getTransStatusXml(String transName, String type, JSONObject jo) throws Exception {

        String xmlString = "";
        ClassPathResource resource = new ClassPathResource("templates/"+type+".xml");

        SAXReader reader = new SAXReader();
        Document document = reader.read(resource.getInputStream());
        Element bookStore = document.getRootElement();
        Iterator it = bookStore.elementIterator();
        // 遍历迭代器，获取根节点中的信息
        while (it.hasNext()) {
            Element book = (Element) it.next();
            Iterator itt = book.elementIterator();
            while (itt.hasNext()) {
                // 节点名及节点值
                Element bookChild = (Element) itt.next();
                String nodeName = bookChild.getName();
                // 设置数据源及转换名
                if("Info".equals(nodeName)){
                    // 获取属性名以及 属性值
                    List<Attribute> bookAttrs = bookChild.attributes();
                    for (Attribute attr : bookAttrs) {
                        String attrName = attr.getName();
                        if("name".equals(attrName)){
                            attr.setValue(transName);
                        }
                    }
                }
                // 设置步骤
                Attribute stepAttr = bookChild.attribute("id");
                if("Step".equals(nodeName) && "2".equals(stepAttr.getValue())){
                    Map<String,Object> step2 = (Map<String, Object>) jo.get("step2");
                    List<Attribute> attrs2 = bookChild.attributes();
                    for (Attribute attr : attrs2) {
                        String attrName = attr.getName();
                        if("connection".equals(attrName)){
                            attr.setValue((String) step2.get("connection"));
                        }
                        if("sql".equals(attrName)){
                            attr.setValue((String) step2.get("sql"));
                        }
                    }
                }
            }
        }

        xmlString = document.asXML();
        return xmlString;
    }

    @Override
    public KettleServerResponse deleteTransformation(String transPath) throws Exception {
        Repository repository = App.getInstance().getRepository();
        String dir = transPath.substring(0, transPath.lastIndexOf("/"));
        String name = transPath.substring(transPath.lastIndexOf("/") + 1);
        // 删除转换
        RepositoryDirectoryInterface directory = repository.findDirectory(dir);
        if(directory == null){
            directory = repository.getUserHomeDirectory();
        }
        ObjectId id = repository.getTransformationID(name, directory);
        repository.deleteTransformation(id);

        return ResponseUtils.success("success", "转化删除成功");
    }

    }
