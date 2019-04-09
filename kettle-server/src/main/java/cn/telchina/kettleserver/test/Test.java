package cn.telchina.kettleserver.test;

import cn.telchina.kettleserver.ext.utils.TaskType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String args[]) throws Exception {

        System.out.print(createJobParameters());
    }

    // 创建central_repository测试参数
    public static String createCenterParameters(){

        String result = "";
        JSONObject jo = new JSONObject();
        Map<String,Object> database = new HashMap<String,Object>();
        database.put("name","orcl_57_test");
        database.put("type","ORACLE");
        database.put("access",0);
        database.put("hostname","10.200.19.57");
        database.put("databaseName","orcl");
        database.put("username","dires");
        database.put("password","diresZj_2019");
        database.put("port","1521");

        JSONArray ja = new JSONArray();
        ja.add(database);
        jo.put("databases", ja);

        Map<String,Object> step2 = new HashMap<String,Object>();
        step2.put("connection","orcl_57_test");
        step2.put("sql","SELECT * FROM LS.JGC_tb_Corp_CompanyInfo where sjzt='1'");
        jo.put("step2",step2);

        JSONArray fieldsJa = new JSONArray();
        Map<String, String> fields1 = new HashMap<String, String>();
        fields1.put("column_name","ID");
        fields1.put("stream_name","ID");
        fieldsJa.add(fields1);
        Map<String, String> fields2 = new HashMap<String, String>();
        fields2.put("column_name","CLASSID");
        fields2.put("stream_name","CLASSID");
        fieldsJa.add(fields2);
        Map<String,Object> step3 = new HashMap<String,Object>();
        step3.put("connection","orcl_57_test");
        step3.put("fields",fieldsJa);
        step3.put("table","QYK_tb_Corp_CompanyInfo)");
        jo.put("step3",step3);

        String column = "SOCIALCREDITCODE";
        String expStr = "[a-zA-Z0-9]{8}-[a-zA-Z0-9]|[A-Z0-9]{18}";
        String conJson =  "{\"negated\":false,\"operator\":0,\"conditions\":[{\"negated\":false,\"operator\":0,\"conditions\":[{\"negated\":false,\"operator\":2,\"left_valuename\":\""+
                column+"\",\"func\":6,\"right_exact\":{\"name\":\"constant\",\"type\":\"String\",\"text\":\""+
                expStr+"\",\"length\":-1,\"precision\":-1,\"isnull\":false}}]}]}";
        Map<String,Object> step5 = new HashMap<String,Object>();
        step5.put("condition",JSONObject.parse(conJson));
        jo.put("step5",step5);

        JSONArray failFieldsJa = new JSONArray();
        Map<String, String> failFields1 = new HashMap<String, String>();
        failFields1.put("column_name","ID");
        failFields1.put("stream_name","ID");
        failFieldsJa.add(failFields1);
        Map<String, String> failFields2 = new HashMap<String, String>();
        failFields2.put("column_name","CLASSID");
        failFields2.put("stream_name","CLASSID");
        failFieldsJa.add(failFields2);
        Map<String,Object> step6 = new HashMap<String,Object>();
        step6.put("connection","orcl_57_test");
        step6.put("fields",failFieldsJa);
        step6.put("table","QYK_tb_Corp_CompanyInfo)");
        jo.put("step6",step6);

        jo.put("type", TaskType.TASK_TYPE_CENTRAL_REPOSITORY);

        result = JSON.toJSONString(jo,SerializerFeature.DisableCircularReferenceDetect);
        return result;
    }

    // 创建clean_record测试参数
    public static String createCleanParameters(){

        String result = "";
        JSONObject jo = new JSONObject();

        JSONArray fieldsJa = new JSONArray();
        Map<String, String> fields1 = new HashMap<String, String>();
        fields1.put("column_name","BH");
        fields1.put("stream_name","BH");
        fieldsJa.add(fields1);
        Map<String, String> fields2 = new HashMap<String, String>();
        fields2.put("column_name","QXNR");
        fields2.put("stream_name","WTB_NAME");
        fieldsJa.add(fields2);
        Map<String,Object> step2 = new HashMap<String,Object>();
        step2.put("connection","orcl_57_test");
        step2.put("fields",fieldsJa);
        step2.put("table","SYS_SJQXGL)");
        jo.put("step2",step2);

        Map<String,Object> step4 = new HashMap<String,Object>();
        step4.put("connection","orcl_57_test");
        step4.put("sql","select sys_guid() as bh ,'QYK_tb_Corp_CompanyInfo_WT' as wtb_name ,sysdate as qxsj from dual");
        jo.put("step4",step4);

        jo.put("type", TaskType.TASK_TYPE_CLEAN_RECORD);

        result = JSON.toJSONString(jo,SerializerFeature.DisableCircularReferenceDetect);
        return result;
    }

    // 创建status_update测试参数
    public static String createStatusParameters(){

        String result = "";
        JSONObject jo = new JSONObject();

        Map<String,Object> step2 = new HashMap<String,Object>();
        step2.put("connection","orcl_57_test");
        step2.put("sql","update LS.JGC_tb_Corp_CompanyInfo set sjzt ='1' where sjzt='0'");
        jo.put("step2",step2);

        jo.put("type", TaskType.TASK_TYPE_STATUS_UPDATE);

        result = JSON.toJSONString(jo,SerializerFeature.DisableCircularReferenceDetect);
        return result;
    }

    // 创建job测试参数
    public static String createJobParameters(){

        String result = "";
        JSONObject jo = new JSONObject();

        Map<String,Object> step3 = new HashMap<String,Object>();
        step3.put("transname","7073837ee8bf4763827fb2a757a2512f");
        jo.put("step3",step3);

        Map<String,Object> step5 = new HashMap<String,Object>();
        step5.put("transname","ac57b75719294ad48eca22c6e316cd9b");
        jo.put("step5",step5);

        Map<String,Object> step6 = new HashMap<String,Object>();
        step6.put("transname","cd469ed345844eb9bc80144986b902ae");
        jo.put("step6",step6);

        jo.put("type", TaskType.TASK_TYPE_JOB);
        jo.put("jobName", "清洗1");

        result = JSON.toJSONString(jo,SerializerFeature.DisableCircularReferenceDetect);
        return result;
    }
}
