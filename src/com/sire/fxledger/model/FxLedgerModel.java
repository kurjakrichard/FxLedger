/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template excelFilePath, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sire.fxledger.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/**
 *
 * @author sire
 */
public class FxLedgerModel {

//<editor-fold defaultstate="collapsed" desc="Class variables">
    private final String FILENAME = "fxledger.db";
    private final String URL = "jdbc:sqlite:" + FILENAME;
    private Connection conn = null;
    private final String POSTALCODETABLE = "CREATE TABLE IF NOT EXISTS zipcode (\n"
            + "    id integer PRIMARY KEY,\n"
            + "    zipcode text NOT NULL,\n"
            + "    city text NOT NULL\n"
            + ");";
    private final String CUSTOMERTABLE = "CREATE TABLE IF NOT EXISTS customer (\n"
            + "    id integer PRIMARY KEY,\n"
            + "    name text NOT NULL,\n"
            + "    city text NOT NULL,\n"
            + "    address1 text NOT NULL,\n"
            + "    address2 text\n"
            + "    country text\n"
            + "    phone text\n"
            + "    fax text\n"
            + "    email text\n"
            + "    country text\n"
            + "    contact text\n"
            + "    taxnumber text\n"
            + "    bankaccountnumber text\n"
            + "    employee_id integer\n"
            + ");";
    private final String EMPLOYEETABLE = "CREATE TABLE IF NOT EXISTS employee (\n"
            + "    id integer PRIMARY KEY,\n"
            + "    name text NOT NULL,\n"
            + "    city text NOT NULL,\n"
            + "    address1 text NOT NULL,\n"
            + "    address2 text\n"
            + "    country text\n"
            + "    workphone text\n"
            + "    homephone text\n"
            + "    fax text\n"
            + "    email text\n"
            + "    country text\n"
            + "    contact text\n"
            + "    bankaccountnumber text\n"
            + ");";
    String excelFilePath = "iranyitoszam.xlsx";

//</editor-fold>
    
    public FxLedgerModel() {
        connect(URL);
        createNewTableIfNotExist(POSTALCODETABLE);
        createNewTableIfNotExist(CUSTOMERTABLE);
        createNewTableIfNotExist(EMPLOYEETABLE);
        excelToDatabase(excelFilePath);
    }

    /**
     * Create connection to the database
     */
    private void connect(String url) {
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println("Hiba történt a kapcsolat létrehozásakor!");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create a new table in the fxledger database
     *
     */
    public final void createNewTableIfNotExist(String sql) {
        if (conn != null) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Hiba történt az adatbázis tábla létrehozásakor!");
        }
    }

    /**
     * Import Hungarian zipcodes from excel file once.
     *
     * @param excelFilePath
     */
    private void excelToDatabase(String excelFilePath) {

        String sqlzipcode = "SELECT * FROM zipcode";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlzipcode)) {
            if (!rs.next()) {
                try {
                    FileInputStream inputStream = new FileInputStream(excelFilePath);
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet firstSheet = workbook.getSheetAt(0);
                    Iterator<Row> rowIterator = firstSheet.iterator();

                    String sql = "INSERT INTO zipcode (zipcode, city) VALUES(?,?)";

                    int batchSize = 40; // Hogy ne teljen meg a memória

                    if (conn != null) {
                        conn.setAutoCommit(false);
                        try (PreparedStatement statement = conn.prepareStatement(sql)) {
                            rowIterator.next(); // skip the header row
                            while (rowIterator.hasNext()) {
                                Row nextRow = rowIterator.next();
                                Iterator<Cell> cellIterator = nextRow.cellIterator();
                                String city = "";
                                int count = 0;
                                while (cellIterator.hasNext()) {
                                    Cell nextCell = cellIterator.next();
                                    int columnIndex = nextCell.getColumnIndex();
                                    switch (columnIndex) {
                                        case 0:
                                            String zipcode = String.valueOf((int) nextCell.getNumericCellValue());
                                            statement.setString(1, zipcode);
                                            System.out.print(zipcode + " ");
                                            break;
                                        case 1:
                                            city = nextCell.getStringCellValue();
                                            System.out.println(city);
                                            statement.setString(2, city);
                                            break;
                                    }
                                }

                                if (city.length() == 0) {
                                    break;
                                }
                                statement.addBatch();

                                if (count % batchSize == 0) {
                                    statement.executeBatch();
                                }
                                count++;
                            }
                            workbook.close();
                            statement.executeBatch();
                            conn.commit();
                            conn.close();
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Hiba történt az adat felvitelekor!");
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FxLedgerModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FxLedgerModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(FxLedgerModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FxLedgerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
