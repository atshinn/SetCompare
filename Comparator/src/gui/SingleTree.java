package gui;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import Control.BackListener;
import Enumerations.ProgramState;
import FileModels.PspiFile;
import FileModels.PstFile;
import FileModels.PstFolder;

import java.awt.*;

public class SingleTree extends BasicPanel {
	private JTree tree;
	private PspiFile file;
	private JScrollPane treePane;
	private JButton back;
	
	public SingleTree(PspiFile file){
		this.file = file;
		
		buildTree();
		
		this.treePane = new JScrollPane(tree);
		
		back = new JButton("back");
		back.addActionListener(new BackListener());
		
		super.buttonPanel.add(back);
		super.add(treePane);
		super.setVisible(false);
	}
	
	public JTree getTree(){
		return tree;
	}

	
	private void buildTree(){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(file.getName());
				
		for(PstFolder folder : file.getFolders()){
			DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(folder.getName());
			root.add(folderNode);
			
			for(PstFile pstFile : folder.getFiles()){
				DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(pstFile.getName());
				folderNode.add(fileNode);
			}
		}		
		tree = new JTree(root);
	}
}
