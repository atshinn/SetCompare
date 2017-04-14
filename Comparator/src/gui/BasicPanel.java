package gui;
import javax.swing.*;

import Control.*;
import Enumerations.ProgramState;

import java.awt.*;

public class BasicPanel extends JPanel{
	protected JButton select,next,undo,back;
	protected JPanel buttonPanel,labelPanel,bottomPanel;
	protected JLabel file1,file2,directions;
		
	public BasicPanel(){
		buildComponents();
		editComponents();
		addListeners();
		addComponents();		
	}
	
	public BasicPanel setFileLabel1(String text){
		file1.setText(text);
		return this;
	}
	
	public BasicPanel setFileLabel2(String text){
		file2.setText(text);
		return this;
	}
	
	public JLabel getFileLabel1(){
		return file1;
	}
	
	public JLabel getFileLabel2(){
		return file2;
	}
	
	public void setFileLabel1(JLabel label) {
		file1 = label;
	}
	
	public void setFileLabel2(JLabel label) {
		file2 = label;
	}
	
	public void updateButtons(ProgramState state){
		switch(state){
			case INITIAL:
				select.setEnabled(true);
				next.setEnabled(false);
				undo.setEnabled(false);
				break;
			
			case PSPI_SELECTION:
				select.setEnabled(true);
				next.setEnabled(true);
				undo.setEnabled(true);
				break;
			
			case OTHER_SELECTION:
				select.setEnabled(true);
				next.setEnabled(false);
				undo.setEnabled(true);
				break;
				
			case COMPARABLE:
				select.setEnabled(false);
				next.setEnabled(true);
				undo.setEnabled(true);
				break;
			
			case ERRONEOUS:
				select.setEnabled(true);
				next.setEnabled(false);
				undo.setEnabled(false);
				break;
			
			default:
				select.setEnabled(true);
				next.setEnabled(false);
				undo.setEnabled(false);
				break;				
		}
	}
	
	private void buildComponents(){
		//buttons
		select = new JButton("Select");
		next = new JButton("Next");
		undo = new JButton("Undo");
		
		//panels
		buttonPanel = new JPanel();
		labelPanel = new JPanel();
		bottomPanel = new JPanel();
		
		//labels
		file1 = new JLabel(" ");
		file2 = new JLabel(" ");
		directions = new JLabel("Select 2 preset directories or pst files compare",JLabel.CENTER);		
	}
	
	private void editComponents(){
		next.setEnabled(false);
		undo.setEnabled(false);		
						
		//edit panels
		this.setLayout(new BorderLayout());
		bottomPanel.setLayout(new BorderLayout());		
	}
	
	private void addListeners(){
		select.addActionListener(new SelectionListener());
		next.addActionListener(new ComparisonListener());
		undo.addActionListener(new UndoListener());
	}
	
	private void addComponents(){
		//button panel
		buttonPanel.add(select);
		buttonPanel.add(next);
		buttonPanel.add(undo);
				
		//label panel
		labelPanel.add(file1);
		labelPanel.add(file2);
		
		//bottom panel
		bottomPanel.add(buttonPanel,BorderLayout.EAST);
		bottomPanel.add(labelPanel, BorderLayout.SOUTH);
		
		//file chooser panel
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.add(directions, BorderLayout.NORTH);		
	}

}
