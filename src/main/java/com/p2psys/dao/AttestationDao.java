package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Attestation;
import com.p2psys.domain.AttestationType;

public interface AttestationDao extends BaseDao {
	public List<AttestationType> getAllList();

	public List<Attestation> getListByUserid(long user_id);

	public Attestation addAttestation(Attestation att);

	/**
	 
	 * @param user_id
	 * @param status
	 *            用户iD,证件有效状态
	 * @return 指定用户证件列表
	 */
	public List<Attestation> getListByUserid(long user_id, int status);

	/**
	 * 获取用户指定证件类型列表
	 * 
	 
	 * @param namm
	 * @param status
	 * @return
	 */
	public List<Attestation> getListByUserName(String name, int status);

	/**
	 * 添加证件类型
	 * 
	 * @param attestationType
	 */
	public void addAttestationType(AttestationType attestationType);

	/**
	 * 根据证件类型号删除证件
	 * 
	 * @param type_id
	 */
	public void deleteAttestationType(long type_id);

	public void updateAttestationTypeByList(List<AttestationType> list);

	// public List<Attestation> getUserAttestationListByPageNumber(int page,int
	// max);

}
