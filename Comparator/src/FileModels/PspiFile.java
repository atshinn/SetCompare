package FileModels;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * The purpose of this class is read a pspi file, create all the PstFolder objects within the file, and then create all the PstFile objects that are contained
 * in the pspi file and add them to their respective PstFolder.
 * 
 * @author Alec Shinn p68370
 * */
public class PspiFile extends PresetFile {
	//The path to a temporary file used so that the original pspi is left unaltered
	private File temp;	
	
	//The PstFolder objects that were created by the pst files contained inside the pspi
	private ArrayList<PstFolder> folders;
	
	//The number of folders contained in the pspi file
	private int folderCount;
	
//=======================================================================================================================================
//CONSTRUCTORs
//=======================================================================================================================================
	
	/**
	 * Creates a PspiFile object given the path to the file
	 * @param filePath The path to pspi file
	 * @throws Exception */
	public PspiFile(File file) {
		super(file);
		
		folders = new ArrayList<PstFolder>();
		folderCount = 0;
		
		try {
			temp = File.createTempFile(this.getName() + "_Temp", ".pspi", new File("temp\\"));
			
			temp.deleteOnExit();
			
			super.read = new BufferedReader(new FileReader(super.file));
			
			//needs to create a temp directory in case one has not been made
			new File("utility").mkdir();
			
			super.openWriter(temp.getAbsolutePath());
			//remove all the unnecessary lines
			removeGarbage();
			
			//now close the writer and reader
			super.write.close();
			
			super.read.close();
			
			//reopen reader to read the temp file
			super.read = new BufferedReader(new FileReader(temp));
		
			//parse the file folders 
			parseFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
//=======================================================================================================================================
//CLASS BEHAVIOR METHODS
//=======================================================================================================================================
	
	/**
	 * Finds the PstFile object with the given name
	 * @param fileName The name of the file being searched for
	 * @return PstFile The PstFile object with the given name or null if the object does not exist*/
	public PstFile findFile(String fileName){
		PstFile result = null;
		PstFile lookup = new PstFile(fileName);
		
		String folderName = fileName.split("\\\\")[fileName.split("\\\\").length - 1].replaceAll("(_black|_red|_pc|.pst)","");
		
		PstFolder folder = this.findFolder(folderName);
		
		result = folder.getFile(lookup.getPstType());
		
		return result;
	}
	
	/**
	 * Finds the PstFolder object with the given folder name.
	 * @param folderName The name of folder being searched for
	 * @return PstFolder The PstFolder object with the given name or null if the object does not exist*/
	public PstFolder findFolder(String folderName){
		PstFolder result = null;
		PstFolder[] _folders = (PstFolder[]) folders.toArray();
		
		int index = 0,maxIndex = _folders.length;
		
		while(index < maxIndex && !_folders[index].getName().equalsIgnoreCase(folderName)){
			index++;
		}
		
		if(index < maxIndex){
			result = _folders[index];
		}
		return result;
	}
	
	/**
	 * Reads the pspi file and creates all PstFolder and PstFile objects
	 * @throws IOException*/
	public void parseFile() throws IOException{
		String currentLine;
		PstFolder currentFolder = null;
		PstFile currentFile = null;
		
		while((currentLine = read.readLine()) != null){
						
			if(currentLine.isEmpty())
				continue;			
			
			if(isStartOfFile(currentLine)){
				currentLine = currentLine.split(":")[currentLine.split(":").length - 1];
				String folderName = currentLine.split("\\\\")[0];
				String fileName = currentLine.split("\\\\")[currentLine.split("\\\\").length - 1];
				
				if(!folderExists(folderName)){
					currentFolder = new PstFolder(folderName);
					folders.add(currentFolder);
					folderCount++;
				}
				if(!currentFolder.has(new PstFile(fileName).getPstType())){
					PstFile tempFile = new PstFile(fileName);
					currentFile = tempFile;
					currentFolder.add(currentFile);
				}
			}
			
			if(isPreset(currentLine)){
				currentFile.getPresets().add(new Preset(currentLine));
				currentFile.setPresetCount(currentFile.getPresetCount() + 1);
			}
			
			if(currentLine.equalsIgnoreCase("eof"))
				currentFile = null;
				
		}
	}
	
	/**
	 * Removes all the unnecessary lines contained in a pspi file
	 * @throws IOException*/
	public void removeGarbage() throws IOException{
		String currentLine,currentFile = null;
		
		while((currentLine = read.readLine()) != null){
			currentLine.trim();
			
			if(!currentLine.isEmpty()){
				if(isPreset(currentLine))
					write.println(currentLine);
				
				else if(isFileIdentifier(currentLine)){
					if(currentFile == null){
						currentFile = new File(currentLine).getName().replaceAll(".pst","");
						write.println("START OF FILE:" + currentFile);
					}
					else {
						write.println("EOF");
						currentFile = new File(currentLine).getName().replaceAll(".pst", "");
						write.println("START OF FILE:" + currentFile);
					}
				}
			}
		}
	}
		
//=======================================================================================================================================
//GETTERS
//=======================================================================================================================================
	
	public int getFolderCount(){
		return folderCount;
	}
	
	public ArrayList<PstFolder> getFolders(){
		return folders;
	}

//=======================================================================================================================================	
//SETTERS
//=======================================================================================================================================
	
	public void setFolderCount(int count){
		folderCount = count;
	}

//=======================================================================================================================================
// PRIVATE METHODS 
//=======================================================================================================================================
	
	/**
	 * Determines whether the given string is a file identifier. This method is only used inside the parseFile method
	 * @param line The string being checked
	 * @return A boolean telling whether the given line is a file identifier or not*/
	private boolean isFileIdentifier(String line){
		if(line.charAt(0) == '#')
			if(line.replaceAll("#","").replaceAll("\\s+","").contains("RMDS"))
				return true;			
		return false;
	}
	
	/**
	 * Determines whether the given line is the beginning of a pst file. This method is only used inside the parseFile method.
	 * @param line The line being examined
	 * @return A boolean telling if the given line is the beginning of a pst file*/
	private boolean isStartOfFile(String line){
		if(line.startsWith("START OF FILE")){
			return true;
		}
		return false;
	}
	
	/**
	 * Determines whether given folder exists inside this pspi file object. This method is only used in the parseFile method.
	 * @param folderName The name of the folder
	 * @return A boolean telling whether the folder object has been created inside this pspi file object*/
	private boolean folderExists(String folderName){
		if(folders.isEmpty())
			return false;
		for(PstFolder folder : folders){
			if(folder.getName().equalsIgnoreCase(folderName))
				return true;
		}
		return false;
	}
}
