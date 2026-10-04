package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;

public class ContainerSpecimenDetail {
	private StorageLocationSummary location;

	private SpecimenDetail specimen;

	public ContainerSpecimenDetail() {

	}

	public ContainerSpecimenDetail(StorageLocationSummary location, SpecimenDetail specimen) {
		this.location = location;
		this.specimen = specimen;
	}

	public StorageLocationSummary getLocation() {
		return location;
	}

	public void setLocation(StorageLocationSummary location) {
		this.location = location;
	}

	public SpecimenDetail getSpecimen() {
		return specimen;
	}

	public void setSpecimen(SpecimenDetail specimen) {
		this.specimen = specimen;
	}
}
