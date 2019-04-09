package com.dfkyun.kettleserver.job.service;

import com.dfkyun.kettleserver.ext.vo.KettleServerResponse;
import com.alibaba.fastjson.JSONObject;

public interface JobGraphService {

    public KettleServerResponse createJob(String jobName);

    public KettleServerResponse saveJob(String jobName, String type, JSONObject jo) throws Exception;

}
