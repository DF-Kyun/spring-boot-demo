package cn.telchina.kettleserver.trans.controller;

import cn.telchina.kettleserver.ext.vo.KettleServerResponse;
import cn.telchina.kettleserver.trans.service.TransGraphService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trans")
public class TransGraphController {

    @Autowired
    private TransGraphService transGraphService;

    @PostMapping("/save")
    protected KettleServerResponse save(@RequestParam String paramJson) throws Exception {

        JSONObject jo = (JSONObject) JSONObject.parse(paramJson);
        String type = (String) jo.get("type");

        KettleServerResponse result = new KettleServerResponse();

        KettleServerResponse createResult = transGraphService.createTrans();
        if(createResult.isStatus() == true){
            String transName = createResult.getData();
            KettleServerResponse saveResult = transGraphService.saveTrans(transName, type, jo);
            result = saveResult;
        }else{
            result = createResult;
        }

        return result;
    }

}
