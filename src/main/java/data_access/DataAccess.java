package data_access;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;


import configuration.ConfigXML;
import configuration.UtilDate;
import domain.*;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess {
	private EntityManager db;
	
	static final String REJECTED = "Rejected";

	ConfigXML c = ConfigXML.getInstance();
	
	private String adminPass="admin";
	
	Logger logger = Logger.getLogger(getClass().getName());

	public DataAccess() {
		
		if (c.isDatabaseInitialized()) {
			String fileName = c.getDbFilename();
			Path fileToDelete = Paths.get(fileName);
            try {
                // Delete the main file
                Files.delete(fileToDelete);
                // Delete the temporary file if it exists
                Path fileToDeleteTemp = Paths.get(fileName + "$");
                Files.deleteIfExists(fileToDeleteTemp);

                logger.info("File deleted");
            } catch (IOException e) {
            	logger.info("Operation failed: "+e.getMessage());
            }
		}
		open();
		if (c.isDatabaseInitialized()) {
			initializeDB();
		}

		logger.info("DataAccess created => isDatabaseLocal: " + c.isDatabaseLocal() + " isDatabaseInitialized: "
				+ c.isDatabaseInitialized());

		close();

	}
	
	
	public DataAccess(boolean isInitialized) {
		
		if (isInitialized) {
			String fileName = c.getDbFilename();
			Path fileToDelete = Paths.get(fileName);
            try {
                // Delete the main file
                Files.delete(fileToDelete);
                // Delete the temporary file if it exists
                Path fileToDeleteTemp = Paths.get(fileName + "$");
                Files.deleteIfExists(fileToDeleteTemp);

                logger.info("File deleted");
            } catch (IOException e) {
            	logger.info("Operation failed: "+e.getMessage());
            }
		}
		open();
		if (!c.isDatabaseInitialized()) {
			initializeDB();
		}

		logger.info("DataAccess created => isDatabaseLocal: " + c.isDatabaseLocal() + " isDatabaseInitialized: "
				+ c.isDatabaseInitialized());

		close();

	}
	
	
	//This constructor is used to mock the DB
	public DataAccess(EntityManager db) {
		this.db = db;
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {
		db.getTransaction().begin();
		try {
			Driver driver1 = new Driver("Urtzi", "123");
			driver1.setMoney(15);
			driver1.setBalorazioa(14);
			driver1.setBalkop(3);
			Driver driver2 = new Driver("Zuri", "456");
			driver2.setBalorazioa(10);
			driver2.setBalkop(3);
			db.persist(driver1);
			db.persist(driver2);

			Traveler traveler1 = new Traveler("Unax", "789");
			traveler1.setIzoztatutakoDirua(68);
			traveler1.setMoney(100);
			traveler1.setBalorazioa(14);
			traveler1.setBalkop(4);
			Traveler traveler2 = new Traveler("Luken", "abc");
			traveler2.setBalorazioa(4);
			traveler2.setBalkop(3);
			db.persist(traveler1);
			db.persist(traveler2);

			Calendar cal = Calendar.getInstance();
			cal.set(2024, Calendar.MAY, 20);
			Date date1 = UtilDate.trim(cal.getTime());

			cal.set(2024, Calendar.MAY, 30);
			Date date2 = UtilDate.trim(cal.getTime());

			cal.set(2024, Calendar.MAY, 10);
			Date date3 = UtilDate.trim(cal.getTime());

			cal.set(2024, Calendar.APRIL, 20);
			Date date4 = UtilDate.trim(cal.getTime());
			
			final String DONOSTIA = "Donostia";
			final String MADRID = "Madrid";
			driver1.addRide(DONOSTIA, MADRID, date2, 5, 20); //ride1
			driver1.addRide("Irun", DONOSTIA, date2, 5, 2); //ride2
			driver1.addRide(MADRID, DONOSTIA, date3, 5, 5); //ride3
			driver1.addRide("Barcelona", MADRID, date4, 0, 10); //ride4
			driver2.addRide(DONOSTIA, "Hondarribi", date1, 5, 3); //ride5

			Ride ride1 = driver1.getCreatedRides().get(0);
			Ride ride2 = driver1.getCreatedRides().get(1);
			Ride ride3 = driver1.getCreatedRides().get(2);
			Ride ride4 = driver1.getCreatedRides().get(3);
			Ride ride5 = driver2.getCreatedRides().get(0);

			Booking book1 = new Booking(ride4, traveler1, 2);
			Booking book2 = new Booking(ride1, traveler1, 2);
			Booking book4 = new Booking(ride3, traveler1, 1);
			Booking book3 = new Booking(ride2, traveler2, 2);
			Booking book5 = new Booking(ride5, traveler1, 1);

			final String ACCEPTED = "Accepted";
			
			
			book1.setStatus(ACCEPTED);
			book2.setStatus(REJECTED);
			book3.setStatus(ACCEPTED);
			book4.setStatus(ACCEPTED);
			book5.setStatus(ACCEPTED);

			db.persist(book1);
			db.persist(book2);
			db.persist(book3);
			db.persist(book4);
			db.persist(book5);

			final String BOOKFREEZE = "BookFreeze";
			Movement m1 = new Movement(traveler1, BOOKFREEZE, 20);
			Movement m2 = new Movement(traveler1, BOOKFREEZE, 40);
			Movement m3 = new Movement(traveler1, BOOKFREEZE, 5);
			Movement m4 = new Movement(traveler2, BOOKFREEZE, 4);
			Movement m5 = new Movement(traveler1, BOOKFREEZE, 3);
			Movement m6 = new Movement(driver1, "Deposit", 15);
			Movement m7 = new Movement(traveler1, "Deposit", 168);
			
			db.persist(m6);
			db.persist(m7);
			db.persist(m1);
			db.persist(m2);
			db.persist(m3);
			db.persist(m4);
			db.persist(m5);
			
			traveler1.addBookedRide(book1);
			traveler1.addBookedRide(book2);
			traveler2.addBookedRide(book3);
			traveler1.addBookedRide(book4);
			traveler1.addBookedRide(book5);
			db.merge(traveler1);

			Car c1 = new Car("1234ABC", "Renault", 5);
			Car c2 = new Car("5678DEF", "Citroen", 3);
			Car c3 = new Car("9101GHI", "Audi", 5);
			driver1.addCar(c1);
			driver1.addCar(c2);
			driver2.addCar(c3);
			db.persist(c1);
			db.persist(c2);
			db.persist(c3);

			Discount dis = new Discount("Uda24", 0.2, true);
			db.persist(dis);

			db.getTransaction().commit();
			logger.info("Db initialized");

	    } catch (Exception e) {
	        /*e.printStackTrace();
	        if (db.getTransaction().isActive()) {
	            db.getTransaction().rollback();
	        }*/
	        logger.severe("Error al inicializar la base de datos");
	    }
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
		return query.getResultList();
	}
	
	
	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",
				String.class);
		query.setParameter(1, from);
		return query.getResultList();
	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(int nPlaces, float price, Ride rRide)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		String driverName=rRide.getDriver().getUsername();
		String from= rRide.getFrom();
		String to= rRide.getTo();
		Date date= rRide.getDate();
		logger.info(
				">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverName + " date " + date);
		if (driverName==null) return null;
		try {
			if (new Date().compareTo(date) > 0) {
				logger.info("ppppp");
				throw new RideMustBeLaterThanTodayException(
						ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}

			db.getTransaction().begin();
			Driver driver = db.find(Driver.class, driverName);
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			// next instruction can be obviated
			db.persist(driver);
			db.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			return null;
		}
		

	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		logger.info(">> DataAccess: getActiveRides=> from= " + from + " to= " + to + " date " + date);

		List<Ride> res = new ArrayList<>();
		TypedQuery<Ride> query = db.createQuery(
				"SELECT r FROM Ride r WHERE r.from = ?1 AND r.to = ?2 AND r.date = ?3 AND r.active = true", Ride.class);
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		logger.info(">> DataAccess: getThisMonthActiveRideDates");

		List<Date> res = new ArrayList<>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Date> query = db.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4 AND r.active = true",
				Date.class);

		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
		res.addAll(dates);

		return res;
	}

	public void open() {
		EntityManagerFactory emf;

		String fileName = c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory(
					"objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);
			db = emf.createEntityManager();
		}
		logger.info("DataAccess opened => isDatabaseLocal: " + c.isDatabaseLocal());

	}

	public void close() {
		db.close();
		logger.info("DataAcess closed");
	}

	public User getUser(String erab) {
		TypedQuery<User> query = db.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
		query.setParameter("username", erab);
		return query.getSingleResult();
	}

	public double getActualMoney(String erab) {
		TypedQuery<Double> query = db.createQuery("SELECT u.money FROM User u WHERE u.username = :username",
				Double.class);
		query.setParameter("username", erab);
		Double money = query.getSingleResult();
		if (money != null) {
			return money;
		} else {
			return 0;
		}
	}

	public boolean isRegistered(String erab, String passwd) {
		TypedQuery<Long> travelerQuery = db.createQuery(
				"SELECT COUNT(t) FROM Traveler t WHERE t.username = :username AND t.passwd = :passwd", Long.class);
		travelerQuery.setParameter("username", erab);
		travelerQuery.setParameter("passwd", passwd);
		Long travelerCount = travelerQuery.getSingleResult();

		TypedQuery<Long> driverQuery = db.createQuery(
				"SELECT COUNT(d) FROM Driver d WHERE d.username = :username AND d.passwd = :passwd", Long.class);
		driverQuery.setParameter("username", erab);
		driverQuery.setParameter("passwd", passwd);
		Long driverCount = driverQuery.getSingleResult();

		/*TypedQuery<Long> adminQuery = db.createQuery(
				"SELECT COUNT(a) FROM Admin a WHERE a.username = :username AND a.passwd = :passwd", Long.class);
		adminQuery.setParameter("username", erab);
		adminQuery.setParameter("passwd", passwd);
		Long adminCount = adminQuery.getSingleResult();*/

		boolean isAdmin=((erab.compareTo("admin")==0) && (passwd.compareTo(adminPass)==0));
		return travelerCount > 0 || driverCount > 0 || isAdmin;
	}

	public Driver getDriver(String erab) {
		TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE d.username = :username", Driver.class);
		query.setParameter("username", erab);
		List<Driver> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	public Traveler getTraveler(String erab) {
		TypedQuery<Traveler> query = db.createQuery("SELECT t FROM Traveler t WHERE t.username = :username",
				Traveler.class);
		query.setParameter("username", erab);
		List<Traveler> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	/*public Admin getAdmin(String erab) {
		TypedQuery<Admin> query = db.createQuery("SELECT a FROM Admin a WHERE t.username = :username", Admin.class);
		query.setParameter("username", erab);
		List<Admin> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}*/

	public String getMotabyUsername(String erab) {
		TypedQuery<String> driverQuery = db.createQuery("SELECT d.mota FROM Driver d WHERE d.username = :username",
				String.class);
		driverQuery.setParameter("username", erab);
		List<String> driverResultList = driverQuery.getResultList();

		TypedQuery<String> travelerQuery = db.createQuery("SELECT t.mota FROM Traveler t WHERE t.username = :username",
				String.class);
		travelerQuery.setParameter("username", erab);
		List<String> travelerResultList = travelerQuery.getResultList();

		/*TypedQuery<String> adminQuery = db.createQuery("SELECT a.mota FROM Admin a WHERE a.username = :username",
				String.class);
		adminQuery.setParameter("username", erab);
		List<String> adminResultList = adminQuery.getResultList();*/

		if (!driverResultList.isEmpty()) {
			return driverResultList.get(0);
		} else if (!travelerResultList.isEmpty()) {
			return travelerResultList.get(0);
		} else  {
			return "Admin";
		} 
	}

	public boolean addDriver(String username, String password) {
		try {
			db.getTransaction().begin();
			Driver existingDriver = getDriver(username);
			Traveler existingTraveler = getTraveler(username);
			if (existingDriver != null || existingTraveler != null) {
				return false;
			}

			Driver driver = new Driver(username, password);
			db.persist(driver);
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public boolean addTraveler(String username, String password) {
		try {
			db.getTransaction().begin();

			Driver existingDriver = getDriver(username);
			Traveler existingTraveler = getTraveler(username);
			if (existingDriver != null || existingTraveler != null) {
				return false;
			}

			Traveler traveler = new Traveler(username, password);
			db.persist(traveler);
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public boolean gauzatuEragiketa(String username, double amount, boolean deposit) {
		try {
			db.getTransaction().begin();
			User user = getUser(username);
			if (user != null) {
				double currentMoney = user.getMoney();
				if (deposit) {
					user.setMoney(currentMoney + amount);
				} else {
					if ((currentMoney - amount) < 0)
						user.setMoney(0);
					else
						user.setMoney(currentMoney - amount);
				}
				db.merge(user);
				db.getTransaction().commit();
				return true;
			}
			db.getTransaction().commit();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public void addMovement(User user, String eragiketa, double amount) {
		try {
			db.getTransaction().begin();

			Movement movement = new Movement(user, eragiketa, amount);
			db.persist(movement);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public boolean bookRide(String username, Ride ride, int seats, double desk) {
		try {
			db.getTransaction().begin();

			Traveler traveler = getTraveler(username);
			if (traveler == null) {
				return false;
			}

			if (ride.getnPlaces() < seats) {
				return false;
			}

			double ridePriceDesk = (ride.getPrice() - desk) * seats;
			double availableBalance = traveler.getMoney();
			if (availableBalance < ridePriceDesk) {
				return false;
			}

			Booking booking = new Booking(ride, traveler, seats);
			booking.setTraveler(traveler);
			booking.setDeskontua(desk);
			db.persist(booking);

			ride.setnPlaces(ride.getnPlaces() - seats);
			traveler.addBookedRide(booking);
			traveler.setMoney(availableBalance - ridePriceDesk);
			traveler.setIzoztatutakoDirua(traveler.getIzoztatutakoDirua() + ridePriceDesk);
			db.merge(ride);
			db.merge(traveler);
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public List<Movement> getAllMovements(User user) {
		TypedQuery<Movement> query = db.createQuery("SELECT m FROM Movement m WHERE m.user = :user", Movement.class);
		query.setParameter("user", user);
		return query.getResultList();
	}

	public List<Booking> getBookedRides(String username) {
		db.getTransaction().begin();
		Traveler trav = getTraveler(username);
		db.getTransaction().commit();
		return trav.getBookedRides();
	}

	public void updateTraveler(Traveler traveler) {
		try {
			db.getTransaction().begin();
			db.merge(traveler);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public void updateDriver(Driver driver) {
		try {
			db.getTransaction().begin();
			db.merge(driver);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public void updateUser(User user) {
		try {
			db.getTransaction().begin();
			db.merge(user);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public List<Booking> getPastBookedRides(String username) {
		TypedQuery<Booking> query = db.createQuery(
				"SELECT b FROM Booking b WHERE b.traveler.username = :username AND b.ride.date <= CURRENT_DATE",
				Booking.class);
		query.setParameter("username", username);
		return query.getResultList();
	}

	public void updateBooking(Booking booking) {
		try {
			db.getTransaction().begin();
			db.merge(booking);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public List<Booking> getBookingFromDriver(String username) {
		try {
			db.getTransaction().begin();
			TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE d.username = :username",
					Driver.class);
			query.setParameter("username", username);
			Driver driver = query.getSingleResult();

			List<Ride> rides = driver.getCreatedRides();
			List<Booking> bookings = new ArrayList<>();

			for (Ride ride : rides) {
				if (ride.isActive()) {
					bookings.addAll(ride.getBookings());
				}
			}

			db.getTransaction().commit();
			return bookings;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public void cancelRide(Ride ride) throws Exception {
		try {
			db.getTransaction().begin();

			for (Booking booking : ride.getBookings()) {
				if (booking.getStatus().equals("Accepted") || booking.getStatus().equals("NotDefined")) {
					double price = booking.prezioaKalkulatu();
					Traveler traveler = booking.getTraveler();
					double frozenMoney = traveler.getIzoztatutakoDirua();
					traveler.setIzoztatutakoDirua(frozenMoney - price);

					double money = traveler.getMoney();
					traveler.setMoney(money + price);
					db.merge(traveler);
					db.getTransaction().commit();
					addMovement(traveler, "BookDeny", price);
					db.getTransaction().begin();
				}
				booking.setStatus("Rejected");
				db.merge(booking);
			}
			ride.setActive(false);
			db.merge(ride);

			//db.getTransaction().commit();
		}
		catch (Exception e) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			//e.printStackTrace()
			throw(e);
		}
	}

	public List<Ride> getRidesByDriver(String username) {
		try {
			db.getTransaction().begin();
			TypedQuery<Driver> query = db.createQuery("SELECT d FROM Driver d WHERE d.username = :username",
					Driver.class);
			query.setParameter("username", username);
			Driver driver = query.getSingleResult();

			List<Ride> rides = driver.getCreatedRides();
			List<Ride> activeRides = new ArrayList<>();

			for (Ride ride : rides) {
				if (ride.isActive()) {
					activeRides.add(ride);
				}
			}

			db.getTransaction().commit();
			return activeRides;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public boolean addCar(String username, Car kotxe) {
		try {
			boolean b = isAdded(username, kotxe.getMatrikula());
			if (!b) {
				db.getTransaction().begin();
				Driver dri = getDriver(username);
				dri.addCar(kotxe);
				db.persist(dri);
				db.getTransaction().commit();
			}
			return !b;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public boolean isAdded(String username, String matr) {
		boolean era = false;
		for (Car kotxe : getDriver(username).getCars()) {
			if (kotxe.getMatrikula().equals(matr)) {
				era = true;
			}
		}
		return era;
	}
	

	public boolean erreklamazioaBidali(Complaint complaint) {
		try {
			db.getTransaction().begin();

			Complaint erreklamazioa = complaint;
			db.persist(erreklamazioa);
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public void updateComplaint(Complaint erreklamazioa) {
		try {
			db.getTransaction().begin();
			db.merge(erreklamazioa);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public Car getKotxeByMatrikula(String matrikula) {
		TypedQuery<Car> query = db.createQuery("SELECT k FROM Car k WHERE k.matrikula = :matrikula", Car.class);
		query.setParameter("matrikula", matrikula);
		List<Car> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	public void createDiscount(Discount di) {
		try {
			db.getTransaction().begin();
			db.persist(di);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public List<Discount> getAllDiscounts() {
		try {
			db.getTransaction().begin();
			TypedQuery<Discount> query = db.createQuery("SELECT d FROM Discount d ", Discount.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public void deleteDiscount(Discount dis) {
		try {
			db.getTransaction().begin();
			db.remove(dis);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public void updateDiscount(Discount dis) {
		try {
			db.getTransaction().begin();
			db.merge(dis);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public Discount getDiscount(String kodea) {
		TypedQuery<Discount> query = db.createQuery("SELECT d FROM Discount d WHERE d.kodea = :kodea", Discount.class);
		query.setParameter("kodea", kodea);
		List<Discount> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	public void deleteCar(Car car) {
		try {
			db.getTransaction().begin();

			Car managedCar = db.merge(car);
			db.remove(managedCar);
			Driver driver = managedCar.getDriver();
			driver.removeCar(managedCar);
			db.merge(driver);

			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}

	public List<User> getUserList() {
		TypedQuery<User> query = db.createQuery("SELECT u FROM User u", User.class);
		return query.getResultList();
	}

	public void deleteUser(User us) {
		try {
			if (us.getMota().equals("Driver")) {
				exDeleteDriver(us);
			} else {
				exDeleteNonUser(us);
			}
			db.getTransaction().begin();
			us = db.merge(us);
			db.remove(us);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void exDeleteDriver(User us) throws Exception {
		List<Ride> rl = getRidesByDriver(us.getUsername());
		if (rl != null) {
			for (Ride ri : rl) {
				cancelRide(ri);
			}
		}
		Driver d = getDriver(us.getUsername());
		List<Car> cl = d.getCars();
		if (cl != null) {
			for (int i = cl.size() - 1; i >= 0; i--) {
				Car ci = cl.get(i);
				deleteCar(ci);
			}
		}
	}
	
	private void exDeleteNonUser(User us) {
		List<Booking> lb = getBookedRides(us.getUsername());
		if (lb != null) {
			for (Booking li : lb) {
				li.setStatus(REJECTED);
				li.getRide().setnPlaces(li.getRide().getnPlaces() + li.getSeats());
			}
		}
		List<Alert> la = getAlertsByUsername(us.getUsername());
		if (la != null) {
			for (Alert lx : la) {
				deleteAlert(lx.getAlertNumber());
			}
		}
	}

	public List<Alert> getAlertsByUsername(String username) {
		try {
			db.getTransaction().begin();

			TypedQuery<Alert> query = this.getAlertaQuery();
			query.setParameter("username", username);
			List<Alert> alerts = query.getResultList();

			db.getTransaction().commit();

			return alerts;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public Alert getAlert(int alertNumber) {
		try {
			db.getTransaction().begin();
			TypedQuery<Alert> query = db.createQuery("SELECT a FROM Alert a WHERE a.alertNumber = :alertNumber",
					Alert.class);
			query.setParameter("alertNumber", alertNumber);
			Alert alert = query.getSingleResult();
			db.getTransaction().commit();
			return alert;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return null;
		}
	}

	public void updateAlert(Alert alert) {
		try {
			db.getTransaction().begin();
			db.merge(alert);
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
		}
	}
	
	public TypedQuery<Alert> getAlertaQuery() {
		return db.createQuery("SELECT a FROM Alert a WHERE a.traveler.username = :username",Alert.class);
	}
	
	public boolean updateAlertaAurkituak(String username) {
		try {
			db.getTransaction().begin();

			boolean alertFound = false;
			TypedQuery<Alert> alertQuery = this.getAlertaQuery();
			alertQuery.setParameter("username", username);
			List<Alert> alerts = alertQuery.getResultList();

			TypedQuery<Ride> rideQuery = db
					.createQuery("SELECT r FROM Ride r WHERE r.date > CURRENT_DATE AND r.active = true", Ride.class);
			List<Ride> rides = rideQuery.getResultList();

			alertFound = UpdateFoundAlert(alertFound, alerts, rides);

			db.getTransaction().commit();
			return alertFound;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}
	private boolean UpdateFoundAlert(boolean alertFound, List<Alert> alerts, List<Ride> rides) {
		for (Alert alert : alerts) {
			alertFound = findAlert(alertFound, rides, alert);
			db.merge(alert);
		}
		return alertFound;
	}
	private boolean findAlert(boolean alertFound, List<Ride> rides, Alert alert) {
		boolean found = false;
		for (Ride ride : rides) {
			if (UtilDate.datesAreEqualIgnoringTime(ride.getDate(), alert.getDate())
					&& ride.getFrom().equals(alert.getFrom()) && ride.getTo().equals(alert.getTo())
					&& ride.getnPlaces() > 0) {
				alert.setFound(true);
				found = true;
				if (alert.isActive())
					alertFound = true;
				break;
			}
		}
		if (!found) {
			alert.setFound(false);
		}
		return alertFound;
	}

	public boolean createAlert(Alert alert) {
		try {
			db.getTransaction().begin();
			db.persist(alert);
			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public boolean deleteAlert(int alertNumber) {
		try {
			db.getTransaction().begin();

			TypedQuery<Alert> query = db.createQuery("SELECT a FROM Alert a WHERE a.alertNumber = :alertNumber",
					Alert.class);
			query.setParameter("alertNumber", alertNumber);
			Alert alert = query.getSingleResult();

			Traveler traveler = alert.getTraveler();
			traveler.removeAlert(alert);
			db.merge(traveler);

			db.remove(alert);

			db.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			db.getTransaction().rollback();
			return false;
		}
	}

	public Complaint getComplaintsByBook(Booking book) {
		TypedQuery<Complaint> query = db.createQuery("SELECT DISTINCT c FROM Complaint c WHERE c.booking = :book",
				Complaint.class);
		query.setParameter("book", book);

		List<Complaint> erreklamazioa = query.getResultList();
		if (!erreklamazioa.isEmpty()) {
			return erreklamazioa.get(0);
		} else {
			return null;
		}
	}

}

