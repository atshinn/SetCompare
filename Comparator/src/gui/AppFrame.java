package gui;
import javax.swing.*;

import Control.Controller;

import java.awt.*;

public class AppFrame extends JFrame {
	final int MIN_X = 650, MIN_Y = 750,MAX_X = 1000,MAX_Y = 150;
	
	private Dimension minimum,maximum;
	private BasicPanel currentView;
	private String homeDirectory;
	
	public AppFrame(String homeDirectory){
		super("Preset Comparator");
		
		this.homeDirectory = homeDirectory;
		buildComponents();
		editComponents();
		addComponents();
		
		
		this.setVisible(true);
		
		Controller.getInstance().setApp(this);
		currentView.setVisible(true);
	}
	
	public BasicPanel getCurrentView(){
		return currentView;
	}
	
	public void setCurrentView(BasicPanel panel){
		currentView = panel;		
	}
	
	private void buildComponents(){
		minimum = new Dimension(MIN_X,MIN_Y);
		maximum = new Dimension(MAX_X,MAX_Y);
		
		currentView = new FileSelection(homeDirectory);
	}
	
	private void editComponents(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(minimum);
		this.setMaximumSize(maximum);
	}
	
	private void addComponents(){
		this.add(currentView);
	}
	
}
