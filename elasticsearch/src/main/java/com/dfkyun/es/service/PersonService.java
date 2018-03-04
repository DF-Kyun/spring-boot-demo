package com.dfkyun.es.service;

import java.util.Map;

import com.dfkyun.es.entity.Person;

public interface PersonService {
	public Map<String, Object> savePerson(Person p);
	public Map<String, Object> updatePerson(Person p);
	public Map<String, Object> delPerson(String id);
	public Map<String, Object> findPerson(String id);
	public Map<String, Object> queryPerson(Person p);
}
