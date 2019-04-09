package cn.telchina.kettleserver.job.service;

import cn.telchina.kettleserver.ext.vo.KettleServerResponse;
import com.alibaba.fastjson.JSONObject;

public interface JobGraphService {

    public KettleServerResponse createJob(String jobName);

    public KettleServerResponse saveJob(String jobName, String type, JSONObject jo) throws Exception;

}
