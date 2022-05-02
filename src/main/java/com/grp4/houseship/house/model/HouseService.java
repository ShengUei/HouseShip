package com.grp4.houseship.house.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HouseService {

	@Autowired
	private HouseRepository houseRepository;

	public List<HouseInfo> searchAll() {
		return houseRepository.findAll();
	}

	public HouseInfo searchById(int id) {
		Optional<HouseInfo> optional = houseRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return optional.orElse(null);
	}

	public boolean insert(HouseInfo houseInfo) {
		if(houseInfo.getH_title() != null) {
			houseRepository.save(houseInfo);
			return true;			
		}
		return false;
	}

	public boolean update(int id, HouseInfo houseInfo) {
		Optional<HouseInfo> optional = houseRepository.findById(id);
		if(optional.isPresent()) {
			houseRepository.save(houseInfo);
			return true;
		}
		return false;
	}

	public boolean delete(int id) {
		if(houseRepository.existsById(id)) {
			houseRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public Long getDataCount() {
		return houseRepository.count();
	}

	public List<HouseInfo> findByPriceGreaterThan(double price) {
		return houseRepository.findByH_priceGreaterThan(price);
	}

	public List<HouseInfo> findByPriceBetween(double min, double max) {
		return houseRepository.findByH_priceBetween(min, max);
	}
	
}
