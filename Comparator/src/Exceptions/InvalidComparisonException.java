package Exceptions;

import FileModels.PresetFile;

@SuppressWarnings("serial")
public class InvalidComparisonException extends Exception {
	public InvalidComparisonException(PresetFile comparer,PresetFile comparee){
		super("[ERROR]: Cannot perform comparison due to incompatible types between files.\n" + comparer.getType() + " vs " + comparee.getType());
	}
}
