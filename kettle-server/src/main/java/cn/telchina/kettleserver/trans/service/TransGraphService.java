package cn.telchina.kettleserver.trans.service;

import cn.telchina.kettleserver.ext.vo.KettleServerResponse;
import com.alibaba.fastjson.JSONObject;

public interface TransGraphService {

    public KettleServerResponse createTrans();

    public KettleServerResponse saveTrans(String transName, String type, JSONObject jo) throws Exception;

}
