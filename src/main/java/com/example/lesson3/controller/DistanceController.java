package com.example.lesson3.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lesson3.model.Store;
import com.example.lesson3.service.StoreService;
import com.example.lesson3.utils.GeoUtil;

@RestController
@RequestMapping("/api")
public class DistanceController {

	@Autowired
	private StoreService storeService;

	@PostMapping("/distance")
	public ResponseEntity<Map<String, Object>> calculateDistance(@RequestBody Map<String, Object> request) {
		double latCurrent = (Double) request.get("latCurrent");
		double lonCurrent = (Double) request.get("lonCurrent");
		long storeId = Long.parseLong((String) request.get("storeId")); // ID cửa hàng được gửi từ frontend

		try {
			Store store = storeService.findById(storeId).orElse(null);
			String storeAddress = store.getAddress();

			// Lấy tọa độ của cửa hàng từ địa chỉ
			double[] storeLatLng = GeoUtil.getLatLngFromAddress(storeAddress);
			double storeLat = storeLatLng[0];
			double storeLon = storeLatLng[1];

			// Tính khoảng cách giữa vị trí hiện tại và cửa hàng
			double distance = GeoUtil.calculateDistance(latCurrent, lonCurrent, storeLat, storeLon);

			// Trả về kết quả dưới dạng JSON
			Map<String, Object> response = new HashMap<>();
			response.put("distance", String.format("%.2f", distance));
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Không thể lấy tọa độ cho địa chỉ cửa hàng.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@PostMapping("/list/distances")
	public Map<String, Object> getDistances(@RequestBody Map<String, Object> request) {
	    double latCurrent = Double.parseDouble(request.get("latCurrent").toString());
	    double lonCurrent = Double.parseDouble(request.get("lonCurrent").toString());

	    List<String> storeIdStrings = (List<String>) request.get("storeIds");
	    List<Long> storeIds = storeIdStrings.stream()
	        .map(Long::parseLong)
	        .collect(Collectors.toList());

	    Map<String, Double> distances = new HashMap<>();

	    for (Long storeId : storeIds) {
	        Store store = storeService.findById(storeId).orElse(null);
	        if (store != null) {
	            try {
	                double[] latLng = GeoUtil.getLatLngFromAddress(store.getAddress());
	                double distance = GeoUtil.calculateDistance(latCurrent, lonCurrent, latLng[0], latLng[1]);
	                distances.put(storeId.toString(), distance);
	            } catch (Exception e) {
	                e.printStackTrace();
	                //distances.put(storeId.toString(), -1.0); // hoặc bạn có thể bỏ qua, hoặc set -1 nếu lỗi
	            }
	        }
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("distances", distances);
	    return response;
	}


}
