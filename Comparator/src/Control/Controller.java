package Control;
import Enumerations.FileType;
import Enumerations.ProgramState;
import Exceptions.InvalidComparisonException;
import Exceptions.InvalidFileTypeException;
import FileModels.*;
import gui.*;

/**Control.java is a singleton class that tracks the state of the program at given time. The purpose of this class is to handle:
 * 	file comparison
 *  global access to files being compared or that are selected
 * 	global access AppFrame.java
 *  Exception handling
 *  
 * @author Alec Shinn p68370
 * */
public class Controller {
	//The only Controller object in the program
	private static Controller controller = new Controller();
	
	//The frame for the programs UI
	private AppFrame app = null;
	
	//The two files being compared
	private PresetFile comparer = null,comparee = null;
	
	//Used to track the state the program is in at any given time
	private ProgramState state = ProgramState.INITIAL;
	
	//Holds the latest exception caught so the controller can react accordingly
	public Exception caughtException;
	
//=======================================================================================================================================
//CONSTRUCTOR (Since this class is a singleton class the constructor is private. Use getInstance() to access the only controller object)
//=======================================================================================================================================
	
	public static Controller getInstance(){
		return controller;
	}	
	
//==================================================================================================
//CLASS BEHAVIOR METHODS
//==================================================================================================
	
	public Controller addFile(PresetFile file){
		if(comparer == null){
			comparer = file;
		}
		else{
			comparee = file;
		}
		return this;
	}

	public Controller handleErrorState(){
		if(state == ProgramState.ERRONEOUS) {
			if(caughtException instanceof InvalidFileTypeException){
				if(comparee != null && comparee.getType() == FileType.INVALID){
					comparee = null;
				}
				if(comparer != null && comparer.getType() == FileType.INVALID){
					comparer = null;
				}
			}
			else if(caughtException instanceof InvalidComparisonException){
				comparee = null;
			}
			else
				comparer = null;
				comparee = null;
		}
		caughtException = null;
		return this;
	}
		
	public ProgramState updateState() throws Exception{
		ProgramState result;
		
		if(comparer == null && comparee == null){
			result = ProgramState.INITIAL;
		}
		else if(comparer != null && comparee != null){
			result = ProgramState.COMPARABLE;
		}
		else if(comparer != null && comparee == null){
			if(comparer.getType() == FileType.PSPI){
				result = ProgramState.PSPI_SELECTION;
			}
			else
				result = ProgramState.OTHER_SELECTION;
		}
		else{
			result = ProgramState.ERRONEOUS;
			new Exception("[ERROR]:CRITICAL ERROR RESTARTING");
		}
		
		return result;
	}
	
	public Controller updateUI(){
			
			if(state == ProgramState.ERRONEOUS){
				handleErrorState();
			}
	
			try {
				setState(updateState());
			} catch (Exception e) {
				e.printStackTrace();
				setState(ProgramState.INITIAL);
				comparer = null;
				comparee = null;
			} finally{			
				updateLabels();
				getCurrentView().updateButtons(state);
			}
			return this;
		}
		
//==================================================================================================
// GETTERS
//==================================================================================================
	
	public PresetFile getComparee(){
		return comparee;
	}
	
	public PresetFile getComparer(){
		return comparer;
	}
	
	public BasicPanel getCurrentView(){
		return app.getCurrentView();
	}
		
	public ProgramState getState(){
		return state;
	}
	
//=================================================================================================
//SETTERS ( All setters return this so they can be chained like controller.setFoo1().setFoo2() etc)
//=================================================================================================
	
	public Controller setApp(AppFrame appl){
		if( this.app == null && appl != null)	
			this.app = appl;
		
		return this;
	}	
	
	public Controller setComparee(PresetFile file){
		comparee = file;
		return this;
	}
	
	public Controller setComparer(PresetFile file){
		comparer = file;
		return this;
	}
	
	public Controller setCurrentView(BasicPanel panel){
		app.setCurrentView(panel);
		return this;
	}
	
	public Controller setState(ProgramState state){
		this.state = state;
		return this;
	}
	
//=============================================================================
// PRIVATE METHODS (Since this is a singleton class the Constructor is private)
//=============================================================================
	private void updateLabels(){
		switch(state){
			case PSPI_SELECTION:
				//SAME AS OTHER SELECTION BUT STILL NEED TO CHECK FOR THAT STATE
			case OTHER_SELECTION:
				app.getCurrentView().getFileLabel1().setText("Selected File: " + comparer.getName());
				app.getCurrentView().getFileLabel2().setText(", ");
				break;
			
			case COMPARABLE:
				app.getCurrentView().getFileLabel1().setText("Selected Files: " + comparer.getName());
				app.getCurrentView().getFileLabel2().setText(", " + comparee.getName());
				break;
			
			default:
				app.getCurrentView().getFileLabel1().setText("Selected Files: NONE");
				app.getCurrentView().getFileLabel2().setText(" ");
		}
	}	
	
	private Controller(){}
}