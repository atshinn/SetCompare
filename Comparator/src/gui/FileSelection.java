package gui;
import javax.swing.*;

import Control.Controller;
import Enumerations.ProgramState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


@SuppressWarnings("serial")
public class FileSelection extends BasicPanel {
	//file chooser gui members
	private JFileChooser fileChooser;
	
	public FileSelection(String homeDirectory){
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setControlButtonsAreShown(false);
		fileChooser.setCurrentDirectory(new File(homeDirectory));
		
		super.add(fileChooser);
		super.setVisible(false);
	}
	
	public JFileChooser getFileChooser(){
		return fileChooser;
	}

	

	
}
