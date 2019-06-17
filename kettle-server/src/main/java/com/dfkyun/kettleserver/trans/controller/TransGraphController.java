package com.dfkyun.kettleserver.trans.controller;

import com.dfkyun.kettleserver.ext.vo.KettleServerResponse;
import com.dfkyun.kettleserver.trans.service.TransGraphService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trans")
public class TransGraphController {

    @Autowired
    private TransGraphService transGraphService;

    /**
     * 转化保存接口，更改之后，不每次都新建转换，改为判断新建，转换更改操作也可调用该接口
     * @param paramJson
     * @return
     * @throws Exception
     */
    @PostMapping("/save")
    protected KettleServerResponse save(@RequestParam String paramJson) throws Exception {

        JSONObject jo = (JSONObject) JSONObject.parse(paramJson);
        String type = (String) jo.get("type");

        KettleServerResponse result = new KettleServerResponse();

        String transName = (String) jo.get("transName");
        result = transGraphService.saveTrans(transName, type, jo);

        return result;
    }

    @PostMapping("/delete")
    protected KettleServerResponse delete(@RequestParam String transPath) throws Exception {

        KettleServerResponse result = transGraphService.deleteTransformation(transPath);

        return result;
    }

}
