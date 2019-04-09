package cn.telchina.kettleserver.ext.utils;

import cn.telchina.kettleserver.ext.vo.KettleServerResponse;

public class ResponseUtils {

    public static KettleServerResponse success(String data, String des) {
        return response(true, data, des);
    }

    public static KettleServerResponse fail(String data, String des) {
        return response(false, data, des);
    }

    private static KettleServerResponse response(boolean status, String data, String des) {

        KettleServerResponse response = new KettleServerResponse();
        response.setData(data);
        response.setDesc(des);
        response.setStatus(status);

        return response;
    }
}
