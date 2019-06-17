package com.dfkyun.kettleserver.trans.service;

import com.dfkyun.kettleserver.ext.vo.KettleServerResponse;
import com.alibaba.fastjson.JSONObject;

public interface TransGraphService {

    public KettleServerResponse createTrans();

    public KettleServerResponse saveTrans(String transName, String type, JSONObject jo) throws Exception;

    public KettleServerResponse deleteTransformation(String transPath) throws Exception;
}
