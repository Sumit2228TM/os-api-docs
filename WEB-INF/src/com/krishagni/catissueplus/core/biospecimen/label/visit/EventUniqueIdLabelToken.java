package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.domain.AbstractUniqueIdToken;

public class EventUniqueIdLabelToken extends AbstractUniqueIdToken<Visit> {

	@Autowired
	private DaoFactory daoFactory;

	public EventUniqueIdLabelToken() {
		this.name = "EVENT_UID";
	}

	@Override
	public Number getUniqueId(Visit visit, String... args) {
		Long id = visit.getCpEvent() != null ? visit.getCpEvent().getId() : -1L;
		String key = visit.getRegistration().getPpid() + "_" + id;
		return daoFactory.getUniqueIdGenerator().getUniqueId(name, key);
	}
}
