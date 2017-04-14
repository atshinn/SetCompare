package Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import gui.*;
import FileModels.*;

public class SelectionListener implements ActionListener {
	PresetFile selected;
	
	@Override
	public void actionPerformed(ActionEvent event) {
		selected = new PresetFile(select(Controller.getInstance().getCurrentView()));
		Controller.getInstance().addFile(selected).updateUI();
	}
	
	private File select(BasicPanel view) {
		File result;
		if(view instanceof FileSelection) {
			result = select((FileSelection) view);
		}
		else if(view instanceof DoubleTree) {
			result = select((DoubleTree) view);
		}
		else
			result = select((SingleTree) view);
		return result;
	}
	
	private File select(FileSelection view) {
		return view.getFileChooser().getSelectedFile();
	}	
	
	private File select(DoubleTree view){
		return new File("here");
	}
	
	private File select(SingleTree view){
		return new File("");
	}
		
}
