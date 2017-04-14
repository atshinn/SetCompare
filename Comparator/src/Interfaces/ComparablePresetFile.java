package Interfaces;

import java.io.File;

import FileModels.PresetFile;

public interface ComparablePresetFile {
	public boolean equals(PresetFile otherFile);
	
	public boolean isCommon(PresetFile otherFile);
	
	public File generateReport(PresetFile otherFile);
}
