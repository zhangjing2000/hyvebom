package com.hyve.bom.concept;

import java.util.UUID;

public interface HyveAssemblyGroupDetail extends HyveProductGroupDetail {
	MemberType getMemberType();
	UUID getSubGroupID();
	int getMinBOMQty();
	int getMaxBOMQty();
}
