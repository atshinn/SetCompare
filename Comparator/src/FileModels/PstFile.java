package FileModels;
import java.util.*;

import Enumerations.FileType;
import Enumerations.PstFileType;
import Interfaces.ComparablePresetFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * The purpose of this class is to read, hold, and manipulate the data inside the pst files before checking for equality. Initially the original pst file
 * is represented but after the constructor calls generateComments this class will parse a temporary version of the file that has had all unnecessary
 * lines removed. If this file has been created from a pspi file then this object will not actually store the location to this file because the file is contained inside the pspi
 * file.
 * @author Alec Shinn p68370
 **/
public class PstFile extends PresetFile implements ComparablePresetFile{
	//All the preset objects that were read from this file
	private ArrayList<Preset> presets;
	
	//the number of presets in the file
	private int presetCount;
	
	//The type of preset file
	private PstFileType pstType;
	
//=======================================================================================================================================
//CONSTRUCTORs
//=======================================================================================================================================
	
	/**
	 * Creates a PstFile object
	 * @param file The path to the .pst file
	 * @throws Exception */
	public PstFile(File file) throws Exception{ 
		super(file);
		presetCount = 0;
		presets = new ArrayList<Preset>();		
			
		try {			
			super.read = new BufferedReader(new FileReader(super.file));			
			parsePresets();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		super.closeReader();				
	}
	
	/**
	 * Creates a PstFile object without having access to the particular file. Used when reading pspi file
	 * @param name The name of the file*/
	public PstFile(String name){
		super(name,FileType.PST);
		presetCount = 0;
		presets = new ArrayList<Preset>();	
	}	
	
//=======================================================================================================================================
//CLASS BEHAVIOR METHODS
//=======================================================================================================================================

	/**
	 * Compares one pst file to another to check for equality
	 * @param otherFile The file being compared
	 * @return A boolean telling whether the two files are equal*/
	public boolean equals(PresetFile file){
		PstFile otherFile = (PstFile) file;
		
		if(presetCount != otherFile.getPresetCount()){
			System.out.println("Preset counts dont match: " + presetCount + " != " + otherFile.getPresetCount());
			return false;
		}
		for(Preset preset : presets){
			if(otherFile.hasEqualPreset(preset.getPreset())){
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Finds the preset with the given name.
	 * @param presetName The name of the preset
	 * @return the preset object with the given name*/
	public Preset findPreset(String presetName){
		for(Preset prst : presets){
			if(prst.getPresetName().equalsIgnoreCase(presetName))
				return prst;
		}		
		return null;
	}
	
	/**
	 * Reads the file and creates the preset objects
	 * @exception IOException*/		
	public void parsePresets() throws IOException{
		String line;		
		while((line = read.readLine()) != null){
			line = line.trim();
			if(isPreset(line)){
				presets.add(new Preset(line));
				presetCount++;
			}
		}
	}
	
	/** 
	 * Checks the two files for commonalities. If any two files have nothing in common then they cannot be checked for equality
	 * @param otherFile The other file being checked for equality
	 * @return A boolean telling whether the two files have a preset in common*/
	public boolean isCommon(PresetFile file){
		PstFile otherFile = (PstFile) file;
		
		for(Preset preset: presets){
			Preset otherPreset = otherFile.findPreset(preset.getPresetName());
			if(otherPreset != null){						
				return true;
			}
		}				
		return false;
	}
	
	/**
	 * Creates a difference report for the two pst files. This method is used when comparing two pst files outside of a pst folder object.
	 *@param toCompare The other file to compare against*/
	public File generateReport(PresetFile file){
		PstFile toCompare = (PstFile) file;		
		String name1,name2,reportName;
		name1 = name.replaceAll(".pst","");
		name2 = toCompare.getName().replaceAll(".pst","");
		reportName = name1 + "__v__" + name2 + ".txt";
		
		new File("Reports\\").mkdir();		
		try {
			//open a writer to the comparison report between the two given files
			this.write = new PrintWriter(new BufferedWriter(new FileWriter(new File("Reports" + "\\" + reportName))));
			
			//write the header for the output file
			write.println("==================================================================================================================================");
			write.println( type + " FILE COMPARISON REPORT FOR THE COMPARISON: " + reportName);
			write.println("==================================================================================================================================");			
			
			generateDifferenceSummary(toCompare);
					
			generatePresetDifferencesSummary(toCompare);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		write.close();
		
		return new File("Reports\\" + reportName);
	}
	
	/**
	 * Creates a difference report for the two pst files. This method is used when comparing files inside a PstFolder object.
	 * @param toCompare The other PstFile being compared
	 * @param folderName The name of the folder the report will go into
	 * */ 
	public void generateReport(PstFile toCompare,String folderName){
		try {
			//open a writer to the comparison report between the two given files
			this.write = new PrintWriter(new BufferedWriter(new FileWriter(new File(folderName + "\\" + type + "Report.txt"))));
			
			//write the header for the output file
			write.println("==================================================================================================================================");
			write.println( type + " FILE COMPARISON REPORT FOR THE COMPARISON: " + folderName.split("\\\\")[folderName.split("\\\\").length - 1].toUpperCase());
			write.println("==================================================================================================================================");			
			
			generateDifferenceSummary(toCompare);
					
			generatePresetDifferencesSummary(toCompare);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		write.close();
	}

//=======================================================================================================================================
//GETTERS
//=======================================================================================================================================

	public PstFileType getPstType(){
		return pstType;
	}

	public ArrayList<Preset> getPresets(){
		return presets;
	}
	
	public int getPresetCount(){
		return presetCount;
	}

//=======================================================================================================================================	
//SETTERS
//=======================================================================================================================================
	
	public void setPstType(PstFileType pType){
		pstType = pType;
	}
	
	public void setPresetCount(int count){
		presetCount = count;
	}
	
//=======================================================================================================================================
// PRIVATE METHODS 
//=======================================================================================================================================
	
	/**
	 * The purpose of this method is to find the an equal preset in a different pst file. This method is only used inside the equals method.
	 * @param presetString The string representation of the preset
	 * @return A boolean noting whether the other file has the exact same preset*/
	private boolean hasEqualPreset(String presetString){
		for(Preset preset : presets){
			if(preset.getPreset().equalsIgnoreCase(presetString)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Generates the difference summary that goes at the top of the difference report. This method is used inside of generateReport method
	 * @param otherFile The file being compared against this object*/
	private void generateDifferenceSummary(PstFile otherFile){
		int uniquePresetCountThisFile = findUniquePresetCount(otherFile);
		int uniquePresetCountOtherFile = otherFile.findUniquePresetCount(this);
		int differenceOfPresetValues = this.findDifferenceOfPresetValueCount(otherFile);
		
		write.println("\n\nSUMMARY OF DIFFERENCES" + " ------------------------------");
		
		//next document number of presets
		if(this.presetCount - otherFile.presetCount != 1 && this.presetCount - otherFile.presetCount > -1)
			write.println(this.getName().replaceAll(".pst", "") + " has " + Integer.toString(this.presetCount - otherFile.presetCount) 
							+ " more presets than " + otherFile.getName().replaceAll("pst", ""));		
		
		else if(this.presetCount - otherFile.presetCount == 1)
			write.println(this.getName().replaceAll(".pst", "") + " has " + Integer.toString(this.presetCount - otherFile.presetCount) 
							+ " more preset than " + otherFile.getName().replaceAll("pst", ""));
		
		else if(otherFile.presetCount - this.presetCount != 1)
			write.println(otherFile.getName().replaceAll(".pst", "") + " has " + Integer.toString(otherFile.presetCount - this.presetCount) 
							+ " more presets than " + this.getName().replaceAll("pst", ""));
				
		else
			write.println(otherFile.getName().replaceAll(".pst", "") + " has " + Integer.toString(otherFile.presetCount - this.presetCount) 
							+ " more presets than " + this.getName().replaceAll("pst", ""));
			
		//document number of unique presets
		if(uniquePresetCountThisFile != 1)
			write.println(this.getName().replaceAll(".pst", "") + " has " + Integer.toString(uniquePresetCountThisFile) + " unique presets.");
		else
			write.println(this.getName().replaceAll(".pst", "") + " has " + Integer.toString(uniquePresetCountThisFile) + " unique preset.");
		
		if(uniquePresetCountOtherFile != 1)
			write.println(otherFile.getName().replaceAll(".pst", "") + " has " + Integer.toString(uniquePresetCountOtherFile) + " unique presets.");
		else
			write.println(otherFile.getName().replaceAll(".pst", "") + " has " + Integer.toString(uniquePresetCountOtherFile) + " unique preset.");
			
		
		//finally document the number of matching presets with different values
		if(differenceOfPresetValues != 1)
			write.println("There are " + differenceOfPresetValues + " matching presets with different values.");
		else
			write.println("There is " + differenceOfPresetValues + " matching preset with a different value.");
	
	}
	
	/**
	 * Generates the Preset Differences Summary that shows results of the comparisons between the presets of the files. This method is only used in generateDifferenceReport.
	 * @param otherFIle The file being compared against this object*/
	private void generatePresetDifferencesSummary(PstFile otherFile){
		write.println("\n\nPRESET DIFFERENCES" + " ------------------------------");
		
		write.println("The following presets are found in both files with different values:");
		
		ArrayList<Preset> differentValuedPresets = getDifferentValuedPresets(otherFile);
		
		for(int i = 0; i < differentValuedPresets.size(); i++){
			printPD(this,otherFile,differentValuedPresets.get(i).toString(),differentValuedPresets.get(++i).toString());
		}
			
		write.println("\n\nMATCHING PRESETS" + " ------------------------------");
		write.println("The following presets are the same in both files\n");
		
		ArrayList<Preset> equivalentPresets = matchingPresets(otherFile); 
		for(Preset preset : equivalentPresets)
			write.println(preset.toString());
		
		write.println("\n");
		
		write.println("\n\nUNIQUE PRESETS " + this.name.split("\\\\")[this.name.split("\\\\").length - 1] + " ------------------------------");
		write.println("The following presets are only in " + this.name.split("\\\\")[this.name.split("\\\\").length - 1] + "\n");
		
		ArrayList<Preset> uniquePresets = uniquePresets(otherFile); 
		for(Preset preset : uniquePresets)
			write.println(preset.toString());
		
		write.println("\n\nUNIQUE PRESETS " + otherFile.name.split("\\\\")[otherFile.name.split("\\\\").length - 1] + " ------------------------------");
		write.println("The following presets are only in " + otherFile.name.split("\\\\")[otherFile.name.split("\\\\").length - 1] + "\n");
		
		uniquePresets = otherFile.uniquePresets(this); 
		for(Preset preset : uniquePresets)
			write.println(preset.toString());
	}
	
	/**
	 * Finds all the presets that have different values. This method is only used in the generatePresetDifferenceSummary method.
	 *@param otherFile The file being compared against this file object
	 *@return An ArrayList of all the presets with different values*/
	private ArrayList<Preset> getDifferentValuedPresets(PstFile otherFile){
		ArrayList<Preset> differentPresets = new ArrayList<Preset>();		
		
		for(Preset preset : presets){
			Preset presetToCompare = otherFile.findPreset(preset.getPresetName());
			if(presetToCompare == null)
				continue;
			if(preset.getPresetName().equalsIgnoreCase(presetToCompare.getPresetName())){
				if(preset.getPresetType().equalsIgnoreCase(presetToCompare.getPresetType())){
					if(!preset.getPresetValue().equalsIgnoreCase(presetToCompare.getPresetValue())){
						differentPresets.add(preset);
						differentPresets.add(presetToCompare);
					}
				}						
			}
		}
		
		differentPresets.trimToSize();
		return differentPresets;
	}
	
	/**
	 * Finds all the presets who have the same value. This method is only used in generatePresetDifferenceSummary method
	 * @param otherFile The other PstFile file being compared against this object
	 * @return An ArrayList containing all the matching preset*/
	private ArrayList<Preset> matchingPresets(PstFile otherFile){
		ArrayList<Preset> matchingPresets = new ArrayList<Preset>();
		
			for(Preset preset : presets){
				Preset toCompare = otherFile.findPreset(preset.getPresetName());
				if(toCompare != null && preset.equals(toCompare))
					matchingPresets.add(toCompare);
			}
		matchingPresets.trimToSize();
		return presets;
	}
	
	/**
	 * Finds all the presets that are unique to one file or the other. This method is only used in in generatePresetDiffencesSummary.
	 * @param otherFile The other pst file this object is being compared to
	 * @return An ArrayList of all the unique presets*/
	private ArrayList<Preset> uniquePresets(PstFile otherFile){
		ArrayList<Preset> uniques = new ArrayList<Preset>();		
		
		for(Preset preset : presets){
			if(otherFile.findPreset(preset.getPresetName()) == null){
				uniques.add(preset);
			}
		}		
		
		uniques.trimToSize();
		return uniques;
	}
	
	/**
	 * Finds the number of presets that are in one file and not the other. This method is only used in the uniquePresets method.
	 * @param otherFile The other pst file this object is being compared to
	 * @return An integer value representing the number of unique presets*/
	private int findUniquePresetCount(PstFile otherFile){
		int uniquePresetCount = 0;
		
		for(Preset prst : presets){
			if(otherFile.findPreset(prst.getPresetName()) == null)
				uniquePresetCount++;
		}
		
		return uniquePresetCount;
	}
	
	/**
	 * Finds the number of presets with different values. This method is only used inside the getDifferentValuedPresets method
	 * @param otherFile The other pst file this object is being compared with
	 * @return An integer representing the number of presets with different values */
	private int findDifferenceOfPresetValueCount(PstFile otherFile){
		int differenceOfPresetValueCount = 0;		
		
		for(Preset preset : presets){
			Preset presetToCompare = otherFile.findPreset(preset.getPresetName());
			if(presetToCompare == null)
				continue;
			if(preset.getPresetName().equalsIgnoreCase(presetToCompare.getPresetName())){
				if(preset.getPresetType().equalsIgnoreCase(presetToCompare.getPresetType())){
					if(!preset.getPresetValue().equalsIgnoreCase(presetToCompare.getPresetValue())){
						differenceOfPresetValueCount++;
					}
				}						
			}
		}
		
		return differenceOfPresetValueCount;
	}
	
	/**
	 * This method prints the differences between the two presets from the two given files in the Difference Report. This method is only used in generatePresetDifferencesSummary method
	 * @param thisFile Is this object
	 * @param otherFile The other file is the file this object is being compared against
	 * @param firstPreset The string representation of the preset from this object
	 * @param secondPreset The string representation of the preset from the otherFile object*/
	private void printPD(PstFile thisFile,PstFile otherFile,String firstPreset,String secondPreset){
		String ogname = thisFile.name.replaceAll(".pst","").split("\\\\")[thisFile.name.split("\\\\").length - 1];
		String othername = otherFile.getName().replaceAll(".pst","").split("\\\\")[otherFile.getName().split("\\\\").length - 1];
		
		write.println(ogname + " " + firstPreset);
		write.println(othername + " " + secondPreset + "\n");
	}
	
}//EOF