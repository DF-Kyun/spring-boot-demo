package com.dfkyun.kettleserver.job.controller;

import com.dfkyun.kettleserver.ext.vo.KettleServerResponse;
import com.dfkyun.kettleserver.job.service.JobGraphService;
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

	@PostMapping("update")
	protected KettleServerResponse update(@RequestParam String paramJson) throws Exception{

		JSONObject jo = (JSONObject) JSONObject.parse(paramJson);
		String type = (String) jo.get("type");
		String jobName = (String) jo.get("jobName");
		String extdes = (String) jo.get("extended_description");

		KettleServerResponse result = jobGraphService.saveJob(jobName, extdes, jo);

		return result;
	}

	@PostMapping("delete")
	protected KettleServerResponse delete(@RequestParam String jobPath) throws Exception{

		KettleServerResponse result =  jobGraphService.deleteJob(jobPath);

		return result;
	}
	
}
