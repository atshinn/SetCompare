package gui;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import Control.BackListener;
import FileModels.PspiFile;
import FileModels.PstFile;
import FileModels.PstFolder;


public class DoubleTree extends BasicPanel {
	private JTree tree1,tree2;
	private JScrollPane treePane1,treePane2;
	private JSplitPane treeView;
	private JButton back;
	
	public DoubleTree(PspiFile file1,PspiFile file2){
		tree1 = buildTree(file1);
		tree2 = buildTree(file2);
		
		treePane1 = new JScrollPane(tree1);
		treePane2 = new JScrollPane(tree2);
		
		treeView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treePane1,treePane2);
		//treeSplit.setDividerLocation(.500);
		
		back = new JButton("back");
		back.addActionListener(new BackListener());
		
		super.buttonPanel.add(back);
		super.add(treeView);
		super.setVisible(false);
	}
	
	private JTree buildTree(PspiFile file){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(file.getName());
		
		for(PstFolder folder : file.getFolders()){
			DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(folder.getName());
			root.add(folderNode);
			
			for(PstFile pstFile : folder.getFiles()){
				DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(pstFile.getName());
				folderNode.add(fileNode);
			}
		}
		return new JTree(root);
	}

	
}
