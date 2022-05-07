package com.grp4.houseship.house.model;

import com.grp4.houseship.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HouseService {

	@Autowired
	private HouseRepository houseRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public List<HouseInfo> searchAll() {
//		return houseRepository.findAll();
		return houseRepository.findByStatusIsTrueOrderByCreatedDateDesc();
	}

	public List<HouseInfo> searchAllByCity(String city) {
		return houseRepository.findByCityAndStatusIsTrueOrderByCreatedDateDesc(city);
	}

	public HouseInfo searchById(int id) {
//		Optional<HouseInfo> optional = houseRepository.findById(id);
		Optional<HouseInfo> optional = houseRepository.findByHouseNoAndStatusIsTrue(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return optional.orElse(null);
	}

	public List<HouseInfo> advanceSearch(String str) {
		String sqlStr = "select * from houseinfo i join houseOffers o on i.offersNo = o.offersNo" +
				" join houseRules r on i.rulesNo = r.rulesNo" +
				" join housePhotos p on i.houseNo = p.houseNo" +
				" where ";
		return entityManager.createNativeQuery(sqlStr + str, HouseInfo.class).getResultList();
	}

	public List<HouseInfo> searchByAccount(Member member) {
		return houseRepository.findByMemberAndStatusIsTrueOrderByCreatedDateDesc(member);
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

	public boolean enable(int id) {
		Optional<HouseInfo> optional = houseRepository.findById(id);
		if(optional.isPresent()) {
			HouseInfo houseInfo = optional.get();
			if (!houseInfo.isStatus()) {
				houseInfo.setStatus(false);
				houseRepository.save(houseInfo);
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean cancel(int id) {
		Optional<HouseInfo> optional = houseRepository.findById(id);
		if(optional.isPresent()) {
			HouseInfo houseInfo = optional.get();
			houseInfo.setStatus(false);
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
