package domain;

import businessLogic.BLFacade;
import businessLogic.BLFacadeFactory;

public class MainAdapter {

	public static void main(String[] args) {
		// the BL is local
		boolean isLocal = true;
		BLFacade blFacade = new BLFacadeFactory().createBLFacade();
		Driver d= blFacade.getDriver("Urtzi");
		DriverTable dt=new DriverTable(d);
		dt.setVisible(true);
		}

}
