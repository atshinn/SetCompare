package Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UndoListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(Controller.getInstance().getComparee() != null)
			Controller.getInstance().setComparee(null);
		else 
			Controller.getInstance().setComparer(null);

		Controller.getInstance().updateUI();
	}

}
