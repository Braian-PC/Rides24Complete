package businessLogic;

import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import domain.Alert;
import domain.Booking;
import domain.Car;
import domain.Complaint;
import domain.Discount;
import domain.Driver;
import domain.Movement;
import domain.Ride;
import domain.Traveler;
import domain.User;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class BLFacadeRemote implements BLFacade{

	private BLFacade remoteFacade;
	
	public BLFacadeRemote(ConfigXML c) {
		try {
	        String serviceName = "http://" + c.getBusinessLogicNode() + ":" + c.getBusinessLogicPort() + "/ws/"
	                + c.getBusinessLogicName() + "?wsdl";
	        URL url = new URL(serviceName);
	        QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
	        Service service = Service.create(url, qname);
	        this.remoteFacade=service.getPort(BLFacade.class);
		}
		catch(Exception e) {
			System.out.println("Error creating remote BLFacade: " + e.toString());
		}
	}

    @Override
    public List<String> getDepartCities() {
        return remoteFacade.getDepartCities();
    }

    @Override
    public ExtendedIterator<String> getDepartingCitiesIterator() {
        return remoteFacade.getDepartingCitiesIterator();
    }

    @Override
    public List<String> getDestinationCities(String from) {
        return remoteFacade.getDestinationCities(from);
    }

    @Override
    public Ride createRide(int nPlaces, float price, Ride rRide)
            throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
        return remoteFacade.createRide(nPlaces, price, rRide);
    }

    @Override
    public List<Ride> getRides(String from, String to, Date date) {
        return remoteFacade.getRides(from, to, date);
    }

    @Override
    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
        return remoteFacade.getThisMonthDatesWithRides(from, to, date);
    }

    @Override
    public void initializeBD() {
        remoteFacade.initializeBD();
    }

    @Override
    public User getUser(String erab) {
        return remoteFacade.getUser(erab);
    }

    @Override
    public double getActualMoney(String erab) {
        return remoteFacade.getActualMoney(erab);
    }

    @Override
    public boolean isRegistered(String erab, String passwd) {
        return remoteFacade.isRegistered(erab, passwd);
    }

    @Override
    public Driver getDriver(String erab) {
        return remoteFacade.getDriver(erab);
    }

    @Override
    public Traveler getTraveler(String erab) {
        return remoteFacade.getTraveler(erab);
    }

    @Override
    public String getMotaByUsername(String erab) {
        return remoteFacade.getMotaByUsername(erab);
    }

    @Override
    public boolean addDriver(String username, String password) {
        return remoteFacade.addDriver(username, password);
    }

    @Override
    public boolean addTraveler(String username, String password) {
        return remoteFacade.addTraveler(username, password);
    }

    @Override
    public boolean gauzatuEragiketa(String username, double amount, boolean b) {
        return remoteFacade.gauzatuEragiketa(username, amount, b);
    }

    @Override
    public boolean bookRide(String username, Ride ride, int seats, double desk) {
        return remoteFacade.bookRide(username, ride, seats, desk);
    }

    @Override
    public List<Movement> getAllMovements(User user) {
        return remoteFacade.getAllMovements(user);
    }

    @Override
    public void addMovement(User user, String eragiketa, double amount) {
        remoteFacade.addMovement(user, eragiketa, amount);
    }

    @Override
    public List<Booking> getBookedRides(String username) {
        return remoteFacade.getBookedRides(username);
    }

    @Override
    public void updateTraveler(Traveler traveler) {
        remoteFacade.updateTraveler(traveler);
    }

    @Override
    public void updateDriver(Driver driver) {
        remoteFacade.updateDriver(driver);
    }

    @Override
    public void updateUser(User user) {
        remoteFacade.updateUser(user);
    }

    @Override
    public List<Booking> getPastBookedRides(String username) {
        return remoteFacade.getPastBookedRides(username);
    }

    @Override
    public void updateBooking(Booking booking) {
        remoteFacade.updateBooking(booking);
    }

    @Override
    public List<Booking> getBookingFromDriver(String username) {
        return remoteFacade.getBookingFromDriver(username);
    }

    @Override
    public List<Ride> getRidesByDriver(String username) {
        return remoteFacade.getRidesByDriver(username);
    }

    @Override
    public void cancelRide(Ride ride) {
        remoteFacade.cancelRide(ride);
    }

    @Override
    public boolean addCar(String username, Car kotxe) {
        return remoteFacade.addCar(username, kotxe);
    }

    @Override
    public boolean isAdded(String username, String matr) {
        return remoteFacade.isAdded(username, matr);
    }

    @Override
    public Car getKotxeByMatrikula(String matr) {
        return remoteFacade.getKotxeByMatrikula(matr);
    }

    @Override
    public void deleteCar(Car car) {
        remoteFacade.deleteCar(car);
    }

    @Override
    public boolean erreklamazioaBidali(Complaint complaint) {
        return remoteFacade.erreklamazioaBidali(complaint);
    }

    @Override
    public void updateComplaint(Complaint erreklamazioa) {
        remoteFacade.updateComplaint(erreklamazioa);
    }

    @Override
    public void createDiscount(Discount di) {
        remoteFacade.createDiscount(di);
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return remoteFacade.getAllDiscounts();
    }

    @Override
    public void deleteDiscount(Discount dis) {
        remoteFacade.deleteDiscount(dis);
    }

    @Override
    public void updateDiscount(Discount dis) {
        remoteFacade.updateDiscount(dis);
    }

    @Override
    public Discount getDiscount(String desk) {
        return remoteFacade.getDiscount(desk);
    }

    @Override
    public List<User> getUserList() {
        return remoteFacade.getUserList();
    }

    @Override
    public void deleteUser(User us) {
        remoteFacade.deleteUser(us);
    }

    @Override
    public List<Alert> getAlertsByUsername(String username) {
        return remoteFacade.getAlertsByUsername(username);
    }

    @Override
    public Alert getAlert(int alertNumber) {
        return remoteFacade.getAlert(alertNumber);
    }

    @Override
    public void updateAlert(Alert alert) {
        remoteFacade.updateAlert(alert);
    }

    @Override
    public boolean updateAlertaAurkituak(String username) {
        return remoteFacade.updateAlertaAurkituak(username);
    }

    @Override
    public boolean createAlert(Alert newAlert) {
        return remoteFacade.createAlert(newAlert);
    }

    @Override
    public boolean deleteAlert(int alertNumber) {
        return remoteFacade.deleteAlert(alertNumber);
    }

    @Override
    public Complaint getComplaintsByBook(Booking bo) {
        return remoteFacade.getComplaintsByBook(bo);
    }
}
