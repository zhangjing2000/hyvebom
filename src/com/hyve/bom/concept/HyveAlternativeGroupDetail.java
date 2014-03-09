package com.hyve.bom.concept;

import java.util.UUID;

public interface HyveAlternativeGroupDetail extends HyveProductGroupDetail {
	MemberType getMemberType();
	UUID getSubGroupID();
	int getSkuNo();
}
