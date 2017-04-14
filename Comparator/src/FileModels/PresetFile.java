package FileModels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Control.Controller;
import Enumerations.FileType;
import Enumerations.ProgramState;
import Exceptions.InvalidFileTypeException;

public class PresetFile implements FileFilter {
	//the path and name of the file. Name includes file extension
	protected String path,name;
	
	//the file object
	protected File file;
	
	//The type of preset file
	protected FileType type;
	
	//The Object for writing to a file
	protected PrintWriter write;
	
	//The Object for reading to a file
	protected BufferedReader read;
	
//=======================================================================================================================================
//CONSTRUCTORs
//=======================================================================================================================================
	
	public PresetFile(File file){
		try{
			this.file = file;
			path = file.getAbsolutePath();
			name = file.getName();
			type = parseFileType(path);	//throws the exception
		}catch(InvalidFileTypeException ifte){
			this.type = FileType.INVALID;
			Controller.getInstance().setState(ProgramState.ERRONEOUS).caughtException = ifte;
			ifte.printStackTrace();
		}
	}
	
	public PresetFile(String fileName,FileType type){
		this.name = fileName;
		this.type = type;
		file = null;
		write = null;
		read = null;
	}
	
//=======================================================================================================================================
//CLASS BEHAVIOR METHODS
//=======================================================================================================================================

	@Override
	public boolean accept(File file) {
		boolean result = true,isDirectory = file.isDirectory();
		String path = file.getAbsolutePath();		

		if(isDirectory) {
			File[] files = file.listFiles();			
			int length = files.length,index = 0;
			
			while(!files[index].getAbsolutePath().endsWith(".pst")){
				result = false;
				if(++index >= length)
					break;
			}
		}
		else
			if(! ( path.endsWith(".pst") || path.endsWith(".pspi") ) )
				result = false;
		
		return result;
	}
	
	public boolean closeReader(){
		boolean result = true;
		try {
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	/**
	 * Determines whether the given string is preset. This method is only used inside parsePreset method
	 * @param line The string being checked
	 * @return A boolean telling whether the line is a preset or not*/
	protected boolean isPreset(String line){
		int colonCount = 0;
		int equalsCount = 0;
		
		if(line.contains(":") && line.contains("=")){
			for(int i = 0 ; i < line.length() ; i++){
				if(colonCount > 1 || equalsCount > 1)
					return false;
				if(line.charAt(i) == ':')
					colonCount++;
				if(line.charAt(i) == '=')
					equalsCount++;
			}
			return true;
		}
				
		return false;
	}
	
	public boolean openWriter(String path){
		boolean result = true;
		try{
			write = new PrintWriter(new BufferedWriter(new FileWriter(new File(path))));
		}catch(IOException ioe){
			ioe.printStackTrace();
			result = false;
		}
		return result;
	}
	
//=======================================================================================================================================
//GETTERS
//=======================================================================================================================================
	
	public File getFile() {
		return file;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPath() {
		return path;
	}
		
	public FileType getType() {
		return type;
	}

	public String toString(){
		if(path != null)
			return path;
		else
			return name;
	}

//=======================================================================================================================================	
//SETTERS
//=======================================================================================================================================
	
	public void setFile(File file){
		this.file = file;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setPath(String filePath){
		this.path = filePath;
	}
	
	public void setType(FileType type){
		this.type = type;
	}
		
//=======================================================================================================================================
// PRIVATE METHODS 
//=======================================================================================================================================
	
	private FileType parseFileType(String filePath) throws InvalidFileTypeException{
		FileType result;		
		File file = new File(filePath);
		
		if(file.isDirectory() && isPstFolder(file))
			result = FileType.FOLDER;
		
		else if(!accept(file)){
			throw new InvalidFileTypeException(file);
		}

		else if(filePath.endsWith(".pst"))
			result = FileType.PST;
			
		else
			result = FileType.PSPI;
				
		return result;
	}
	
	private boolean isPstFolder(File file){		
		return file.listFiles(this).length > 0;
	}
		
}//EOF
