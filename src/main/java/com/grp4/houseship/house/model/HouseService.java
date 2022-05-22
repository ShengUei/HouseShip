package com.grp4.houseship.house.model;

import com.grp4.houseship.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
		return houseRepository.findAll();
	}

	public Page<HouseInfo> searchAll(Pageable pageable) {
		return houseRepository.findByStatusIsTrueOrderByCreatedDateDesc(pageable);
	}

	public Page<HouseInfo> searchAllByCity(String city, Pageable pageable) {
		return houseRepository.findByCityAndStatusIsTrueOrderByCreatedDateDesc(city, pageable);
	}

	public HouseInfo searchById(int id) {
		Optional<HouseInfo> optional = houseRepository.findByHouseNoAndStatusIsTrue(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return optional.orElse(null);
	}

	public Page<HouseInfo> advanceSearch(String str, Pageable pageable) {
		String sqlStr = "select i.* from houseinfo i join houseOffers o on i.offersNo = o.offersNo" +
				" join houseRules r on i.rulesNo = r.rulesNo" +
				" join (select houseNo ,min(photoPath) photoPath from housePhotos group by houseNo) p on i.houseNo = p.houseNo" +
				" where ";
		Query query = entityManager.createNativeQuery(sqlStr + str, HouseInfo.class);
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		Page<HouseInfo> pageOfHouseInfo = new PageImpl<>(query.getResultList(), pageable, getCountForAdvanceSearch(str));
		return pageOfHouseInfo;
	}

	private Integer getCountForAdvanceSearch(String str) {
		String sqlStr = "select count(*) from houseinfo i join houseOffers o on i.offersNo = o.offersNo" +
				" join houseRules r on i.rulesNo = r.rulesNo" +
				" join (select houseNo ,min(photoPath) photoPath from housePhotos group by houseNo) p on i.houseNo = p.houseNo" +
				" where ";
		return (Integer) entityManager.createNativeQuery(sqlStr + str).getSingleResult();
	}

	public List<HouseInfo> searchByAccount(Member member) {
		return houseRepository.findByMemberOrderByCreatedDateDesc(member);
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
				houseInfo.setStatus(true);
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
	
	//空房查詢
	public Page<HouseInfo> searchByDateBetween(String date1, String date2, Pageable pageable){
		return houseRepository.findByDateBetween(date1, date2, pageable);
	}

	public Page<HouseInfo> searchByDateAndCity(String date1, String date2, String city, Pageable pageable){
		return houseRepository.findByDateBetweenAndCity(date1, date2, city, pageable);
	}

	//確認房源與日期可提供
	public HouseInfo findByDateAndHouseNo(String date1, String date2, int houseNo){
		return houseRepository.findByDateAndHouseNo(date1, date2, houseNo);
	}
	
}
