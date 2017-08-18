package com.ucsmy.aos.api.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	public static final ObjectMapper mapper = new ObjectMapper();

	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	public static List<Map> jsonToList(String jsonStr) throws IOException {
		return mapper.readValue(jsonStr, new TypeReference<List>() {
		});
	}

	public static List jsonToList(String jsonStr, Class clazz) throws IOException {
		JavaType javaType = getCollectionType(ArrayList.class, clazz);
		return mapper.readValue(jsonStr, javaType);
	}

	public static Object jsonToObject(String jsonStr, Class clazz) throws IOException {
		return mapper.readValue(jsonStr, clazz);
	}

	public static JsonNode jsonToJsonNode(String jsonStr) throws IOException {
		return mapper.readTree(jsonStr);
	}

	public static String formatObjectToJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}

	public static JsonNode formatObjectToJsonNode(Object object) {
		return mapper.valueToTree(object);
	}

	/**
	 * * @Title: strJson2Map * @Description: json串转map
	 * 
	 * @param json
	 * @return 入参 * @return Map<String,Object> 返回类型 * @author （作者） * @throws *
	 * @date 2017-3-9 上午11:49:16 * @version V1.0
	 */
	public static Map<String, Object> strJson2Map(String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		Map<String, Object> resMap = new HashMap<String, Object>();
		Iterator<Entry<String, Object>> it = jsonObject.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> param = (Entry<String, Object>) it.next();
			if (param.getValue() instanceof JSONObject) {
				resMap.put(param.getKey(), strJson2Map(param.getValue().toString()));
			} else if (param.getValue() instanceof JSONArray) {
				resMap.put(param.getKey(), json2List(param.getValue()));
			} else {
				resMap.put(param.getKey(), JSONObject.toJSONString(param.getValue(), SerializerFeature.WriteClassName));
			}
		}
		return resMap;
	}

	private static List<Map<String, Object>> json2List(Object json) {
		JSONArray jsonArr = (JSONArray) json;
		List<Map<String, Object>> arrList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < jsonArr.size(); ++i) {
			arrList.add(strJson2Map(jsonArr.getString(i)));
		}
		return arrList;
	}
}
