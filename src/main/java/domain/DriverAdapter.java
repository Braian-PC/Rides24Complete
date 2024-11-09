package domain;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DriverAdapter extends AbstractTableModel {
    private Driver driver;
    private List<Ride> rides;
    private final String[] columnNames = {"from", "to", "date", "places", "price"};

    public DriverAdapter(Driver driver) {
        this.driver = driver;
        this.rides = driver.getCreatedRides();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public int getRowCount() {
        return rides.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ride ride = rides.get(rowIndex);
        switch (columnIndex) {
            case 0: return ride.getFrom();
            case 1: return ride.getTo();
            case 2: return ride.getDate();
            case 3: return ride.getNPlaces();
            case 4: return ride.getPrice();
            default: return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
