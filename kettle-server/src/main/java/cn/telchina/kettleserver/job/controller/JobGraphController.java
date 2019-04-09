package cn.telchina.kettleserver.job.controller;

import cn.telchina.kettleserver.ext.vo.KettleServerResponse;
import cn.telchina.kettleserver.job.service.JobGraphService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobGraphController {
	
	@Autowired
	private JobGraphService jobGraphService;

	@PostMapping("/save")
	protected KettleServerResponse save(@RequestParam String paramJson) throws Exception{

		JSONObject jo = (JSONObject) JSONObject.parse(paramJson);
		String type = (String) jo.get("type");
		String jobName = (String) jo.get("jobName");

		KettleServerResponse result = new KettleServerResponse();

		KettleServerResponse createResult = jobGraphService.createJob(jobName);
		if(createResult.isStatus() == true){
			KettleServerResponse saveResult = jobGraphService.saveJob(jobName, type, jo);
			result = saveResult;
		}else{
			result = createResult;
		}

		return result;
	}
	
}
