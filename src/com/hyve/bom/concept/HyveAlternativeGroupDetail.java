package com.hyve.bom.concept;

import java.util.UUID;

public interface HyveAlternativeGroupDetail extends HyveProductGroupDetail {
	HyveProductGroupMemberType getMemberType();
	UUID getSubGroupID();
	int getSkuNo();
}
