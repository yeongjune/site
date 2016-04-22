package com.authority.service;

import java.util.List;
import java.util.Map;

public interface AuthorityService {

	List<Map<String, Object>> load();

	int update(String id, Integer checked);

	Map<String, Object> get(String id);

}
