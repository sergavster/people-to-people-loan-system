package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.AttestationType;

public interface AttestationService {

	public void updateAttestationTypeByList(List<AttestationType> list);

	public void addAttestationType(AttestationType attestationType);

//	public void deleteAttestationTypeByid(long id);

	public List getAttestationType();
	
}
