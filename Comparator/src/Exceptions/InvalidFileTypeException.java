package Exceptions;

import java.io.File;

public class InvalidFileTypeException extends Exception {
	String message;
	public InvalidFileTypeException(File file){
		super("[ERROR]: File type is not supported. Only .pst,.pspi, or a directory containting at least one .pst files are acceptable\n" + "CAUSE: " + file.getAbsolutePath());
	}
}
