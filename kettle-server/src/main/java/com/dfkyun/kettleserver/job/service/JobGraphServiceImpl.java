package com.dfkyun.kettleserver.job.service;

import com.dfkyun.kettleserver.ext.App;
import com.dfkyun.kettleserver.ext.PluginFactory;
import com.dfkyun.kettleserver.ext.base.GraphCodec;
import com.dfkyun.kettleserver.ext.utils.ResponseUtils;
import com.dfkyun.kettleserver.ext.utils.StringEscapeHelper;
import com.dfkyun.kettleserver.ext.vo.KettleServerResponse;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class JobGraphServiceImpl implements JobGraphService {

    @Override
    public KettleServerResponse createJob(String jobName){

        String dir = "/";
        Repository repository = App.getInstance().getRepository();
        RepositoryDirectoryInterface directory = null;
        try{

            directory = repository.findDirectory(dir);
            if(directory == null)
                directory = repository.getUserHomeDirectory();
            if(repository.exists(jobName, directory, RepositoryObjectType.JOB)) {

                return ResponseUtils.fail("","该作业已经存在，请重新输入！");
            }
            JobMeta jobMeta = new JobMeta();
            jobMeta.setRepository(App.getInstance().getRepository());
            jobMeta.setMetaStore(App.getInstance().getMetaStore());
            jobMeta.setName(jobName);
            jobMeta.setRepositoryDirectory(directory);
            repository.save(jobMeta, "add: " + new Date(), null);

            String jobPath = directory.getPath();
            if(!jobPath.endsWith("/"))
                jobPath = jobPath + '/';
            jobPath = jobPath + jobName;

            return ResponseUtils.success(jobPath, "作业创建成功");
        }catch (Exception e){

            e.printStackTrace();
            return ResponseUtils.fail("","作业创建失败!");
        }
    }

    @Override
    public KettleServerResponse saveJob(String jobName, String type, JSONObject jo) throws Exception{

        Repository repository = null;
        GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.JOB_CODEC);
        String graphXml = getJobXml(jobName, type, jo);
        System.out.println(StringEscapeHelper.decode(graphXml));
        JobMeta jobMeta = (JobMeta) codec.decode(StringEscapeHelper.decode(graphXml));
        repository = App.getInstance().getRepository();
        ObjectId existingId = repository.getJobId(jobMeta.getName(), jobMeta.getRepositoryDirectory());
        //repository.getTransformationID( jobMeta.getName(), jobMeta.getRepositoryDirectory());
        if(jobMeta.getCreatedDate() == null)
            jobMeta.setCreatedDate(new Date());
        if(jobMeta.getObjectId() == null)
            jobMeta.setObjectId(existingId);
        jobMeta.setModifiedDate(new Date());
        boolean versioningEnabled = true;
        boolean versionCommentsEnabled = true;
        String fullPath = jobMeta.getRepositoryDirectory() + "/" + jobMeta.getName() + jobMeta.getRepositoryElementType().getExtension();
        RepositorySecurityProvider repositorySecurityProvider = repository.getSecurityProvider() != null ? repository.getSecurityProvider() : null;
        if ( repositorySecurityProvider != null ) {
            versioningEnabled = repositorySecurityProvider.isVersioningEnabled( fullPath );
            versionCommentsEnabled = repositorySecurityProvider.allowsVersionComments( fullPath );
        }
        String versionComment = null;
        if (!versioningEnabled || !versionCommentsEnabled) {
            versionComment = "";
        } else {
            versionComment = "no comment";
        }
        repository.save(jobMeta, versionComment, null);

        return ResponseUtils.success(jobName,"作业保存成功！");
    }

    private String getJobXml(String jobName, String type, JSONObject jo) throws Exception {

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
                            attr.setValue(jobName);
                        }
                    }
                }
                // 设置步骤
                Attribute stepAttr = bookChild.attribute("id");
                if("JobEntry".equals(nodeName) && "3".equals(stepAttr.getValue())){
                    Map<String,Object> step3 = (Map<String, Object>) jo.get("step3");
                    List<Attribute> attrs3 = bookChild.attributes();
                    for (Attribute attr : attrs3) {
                        String attrName = attr.getName();
                        if("transname".equals(attrName)){
                            attr.setValue((String) step3.get("transname"));
                        }
                    }
                }

                if("JobEntry".equals(nodeName) && "5".equals(stepAttr.getValue())){
                    Map<String,Object> step5 = (Map<String, Object>) jo.get("step5");
                    List<Attribute> attrs5 = bookChild.attributes();
                    for (Attribute attr : attrs5) {
                        String attrName = attr.getName();
                        if("transname".equals(attrName)){
                            attr.setValue((String) step5.get("transname"));
                        }
                    }
                }

                if("JobEntry".equals(nodeName) && "6".equals(stepAttr.getValue())){
                    Map<String,Object> step6 = (Map<String, Object>) jo.get("step6");
                    List<Attribute> attrs6 = bookChild.attributes();
                    for (Attribute attr : attrs6) {
                        String attrName = attr.getName();
                        if("transname".equals(attrName)){
                            attr.setValue((String) step6.get("transname"));
                        }
                    }
                }
            }
        }

        xmlString = document.asXML();
        return xmlString;
    }
}
