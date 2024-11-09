package businessLogic;

public class BLFactory {
	public BLFacade getBusinessLogicFactory(boolean b) {
		return new BLFacadeImplementation();
	}
}
