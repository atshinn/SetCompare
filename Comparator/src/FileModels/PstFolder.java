package FileModels;
import java.io.File;
import java.util.ArrayList;

import Enumerations.FileType;
import Enumerations.PstFileType;

/**
 * The purpose of this class is to represent a preset file folder, store PstFile objects, and to handle comparisons between two PstFolder objects
 * 
 * @author Alec Shinn p68370
 * */
public class PstFolder extends PresetFile{
	private PstFile[] files;
	private short black,red,pc;

	
	/**
	 * Creates a PstFolder object given a File object.
	 * @param directory The directory containing the preset files
	 * @throws Exception 
	*/
	public PstFolder(File directory) throws Exception{
		super(directory);
		black = -1;
		red = -1;
		pc = -1;
		
		files = new PstFile[3];
		
		parseDirectory();
		
		
	}

	/**
	 * Creates a PstFolder object given only the name of the folder. This method is used when creating PstFolder objects from a pspi file.
	 * @param name The name of the folder*/
	public PstFolder(String name){
		super(name,FileType.FOLDER);		
		files = new PstFile[3];
		
		black = -1;
		red = -1;
		pc = -1;
	}
	
	/**
	 * Finds the PstFile object contained in this PstFolder
	 * @param file Is the preset file type, either red,black, or pc
	 * @return A PstFile object of the given preset type or null if this PstFolder does not have such an object*/
	public PstFile getFile(PstFileType type){
		PstFile result = null;
		
		if(has(type)){
			
			switch(type){
				case RED:
					result = files[red];
					break;
				
				case BLACK:
					result = files[black];
					break;
					
				case PC:
					result = files[pc];
					break;			
			}
			
		}
		
		return result;
	}

	/**
	 * Determines whether the PstFolder has a file of the given preset type
	 * @param fileType The preset file type, either red,black, or pc
	 * @return A boolean telling the folder contains the given file type*/
	public boolean has(PstFileType fileType){
		boolean result = false;
		
		if(fileType == PstFileType.BLACK && black > -1){
			result = true;
		}
		else if(fileType == PstFileType.RED && red > -1){
			result = true;
		}
		else if(fileType == PstFileType.PC && pc > -1){
			result = true;
		}
		
		return result;
	}

	/**
	 * Adds a given PstFile to this PstFolder object
	 * @param toAdd The PstFile being added
	 * @return A boolean telling whether the add was successful*/
	public boolean add(PstFile toAdd){
		PstFileType addType = toAdd.getPstType();
		boolean result = false;
		short index = 0;
		
		while(index < 3 && files[index] != null){
			index++;
		}
		
		if(index < 3){
			files[index] = toAdd;
			result = true;
			
			switch(addType){
				case BLACK:
					black = index;
					break;
				case RED:
					red = index;
					break;
				case PC:
					pc = index;
					break;
			}
			
		}		
		
		return result;
	}
	
	/**
	 * Checks whether two PstFolders are equivalent to one another. Equality is based off of the preset files contained in the folder having the same presets
	 * @param toCompare The other PstFolder object being compared
	 * @return A boolean telling whether the two PstFolder objects are equal*/
	public boolean equals(PstFolder toCompare){
		boolean black = false, red = false, pc = false;			
				
		black = this.getFile(PstFileType.BLACK).equals(toCompare.getFile(PstFileType.BLACK));
		red = this.getFile(PstFileType.RED).equals(toCompare.getFile(PstFileType.RED));
		pc = this.getFile(PstFileType.PC).equals(toCompare.getFile(PstFileType.PC));
		
		return black && red && pc;
		
	}
	
	/**
	 * Generates a Difference report for two PstFolder objects
	 * @param toCompare The other PstFolder object being compared*/
	public File generateComparisonReport(PresetFile file){
		PstFolder toCompare = (PstFolder) file;
		new File("Reports\\" + this.name + "__v__" + toCompare.getName()).mkdirs();		
		File reportDir = new File("Reports\\" + this.name + "__v__" + toCompare.getName());
	
		this.getFile(PstFileType.BLACK).generateReport(toCompare.getFile(PstFileType.BLACK),reportDir.getAbsolutePath());
		this.getFile(PstFileType.RED).generateReport(toCompare.getFile(PstFileType.RED),reportDir.getAbsolutePath());
		this.getFile(PstFileType.PC).generateReport(toCompare.getFile(PstFileType.PC),reportDir.getAbsolutePath());
		
		return reportDir;
	}	
	
	public PstFile[] getFiles(){
		return files;
	}
	
	private void parseDirectory() throws Exception{
		File[] dirFiles = super.file.listFiles();
		
		for(File pstFile: dirFiles){
			if(accept(pstFile)){	
				add(new PstFile(pstFile));
			}			
		}
	}
}
