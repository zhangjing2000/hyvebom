package com.hyve.bom.snapshot;

import java.util.Date;
import java.util.UUID;

import com.hyve.bom.concept.HyveProductGroup;

public interface SnapshotedHyveProductGroup extends HyveProductGroup {
	UUID getLoggedGroupID();
	Date getSnapshotDateTime();
}
