package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.Shipment;
import com.krishagni.catissueplus.core.administrative.domain.Shipment.Status;
import com.krishagni.catissueplus.core.administrative.events.ShipmentDetail;

public interface ShipmentFactory {
	Shipment createShipment(ShipmentDetail detail, Status status);
}
