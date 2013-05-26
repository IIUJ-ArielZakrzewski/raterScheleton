/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

import Dialogs.ErrorDialog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karolina
 */
public class LocalDatabaseConnectorSQLite extends LocalDatabaseConnector
{
     public static final String DRIVER = "org.sqlite.JDBC";
     public static final String DB_URL = "jdbc:sqlite:biblioteka.db";
     private Connection conn;
     private Statement stat;
     
     public LocalDatabaseConnectorSQLite()
     {
                 try {
            Class.forName(LocalDatabaseConnectorSQLite.DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }
 
        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }
 

     }

    @Override
    public boolean checkTable(String nazwa) 
    {
        Boolean flaga = false;
        
                        String stm = "";
                        stm = "SELECT * FROM " + nazwa;
                        try
                        {
                            PreparedStatement prepStmt = conn.prepareStatement(stm);
                            prepStmt.execute();
                            return true;
                        }
                        catch (Exception ex)
                        {
                            return false;
                        }
    }

    @Override
    public boolean insert(DataRow row) 
    {
                Boolean flaga = false;
                String stm = "";
                String tabelName = row.getTableName();
                List<DataCell> cells = row.row;
                
                String kolumny = "";
                String values = "";
                
                for(DataCell c : cells)
                {
                    kolumny += c.name + ", ";
                    values += c.value + ", ";
                    
                }
                kolumny = kolumny.substring(0, kolumny.length()-2);
                values = values.substring(0, values.length()-2);
                stm = "INSERT INTO " + tabelName + "(" + kolumny + ") VALUES (" + values + ")";
         try 
         {
             
             PreparedStatement prepStmt = conn.prepareStatement(stm);
             prepStmt.execute();
             flaga = true;
         } 
         catch (SQLException ex) 
         {
             ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w lokalnej bazie danych: \n" +  ex.getMessage(), "LocalDatabaseConnectorSQLite", "insert(DataRow row)", "prepStmt");
             errorDialog.setVisible(true);
         }

        
        return flaga;
    }

    @Override
    public boolean update(DataRow parameters, DataRow row) 
    {
     
                Boolean flaga = false;
                String stm = "";
                String tabelName = row.getTableName();
                List<DataCell> cells = row.row;
                List<DataCell> toUpdate = parameters.row;
                
                String kolumny = "";
                String doPodmiany = "";
                
                for(DataCell c : cells)
                {
                    kolumny += c.name + " = " + c.value + ", ";
                }
                
                for (DataCell x : toUpdate)
                {
                    doPodmiany += x.name + " = " + x.value + " AND ";
                }
                
                kolumny = kolumny.substring(0, kolumny.length() - 2);
                doPodmiany = doPodmiany.substring(0, doPodmiany.length() - 5);
                stm = "UPDATE " + tabelName + " SET " + kolumny + " WHERE " + doPodmiany;
                try
                {
                     PreparedStatement prepStmt = conn.prepareStatement(stm);
                    prepStmt.execute();
                    flaga = true;
                }
                catch (Exception ex)
                {
                    ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w lokalnej bazie danych: \n" +  ex.getMessage(), "LocalDatabaseConnectorSQLite", "update(DataRow parameters, DataRow row)", "prepStmt");
                    errorDialog.setVisible(true);
                }
                return flaga;
    }

    @Override
    public boolean remove(DataRow row) 
    {
        Boolean flaga = false;
                String stm = "";
                String tabelName = row.getTableName();
                List<DataCell> toDelete = row.row;
                String temp = "";
                for (DataCell x : toDelete)
                {
                    temp += x.name + " = " + x.value + " AND ";
                }
                temp = temp.substring(0, temp.length() - 5);
                stm = "DELETE FROM " + tabelName + " WHERE " + temp;
                try
                {
                     PreparedStatement prepStmt = conn.prepareStatement(stm);
                     prepStmt.execute();
                     flaga = true;
                }
                catch (Exception ex)
                {
                    ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w lokalnej bazie danych: \n" +  ex.getMessage(), "LocalDatabaseConnectorSQLite", "remove(DataRow row)", "prepStmt");
                    errorDialog.setVisible(true);
                }
        return flaga;
    }

    @Override
    public List<DataRow> select(DataRow parameters) 
    {
         List<DataRow> wynik = new ArrayList<>();
                String stm = "";
                String tabelName = parameters.getTableName();
                List<DataCell> select = parameters.row;
                String kolumny = "";
                for (DataCell x : select)
                {
                    kolumny += x.name + " = " + x.value + " AND ";
                }
                kolumny = kolumny.substring(0, kolumny.length() - 5);
                stm = "SELECT * FROM " + tabelName + " WHERE " + kolumny;
                try
                {
                    ResultSet rs = stat.executeQuery(stm);
                    while(rs.next())
                    {
                            DataRow x = new DataRow();
                            x.setTableName(tabelName);
                            ResultSetMetaData meta = rs.getMetaData();

                            for(int i=0; i<meta.getColumnCount(); i++)
                            {
                                String attrname = meta.getColumnName(i);
                                String attrvalue = rs.getString(i);
                                x.addAttribute(attrname, attrvalue);
                                if(attrname.equals("name"))
                                {
                                    x.setName(attrvalue);                        
                                }
                            }
                        wynik.add(x);
                    }
                }
                catch (Exception ex)
                {
                    ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w lokalnej bazie danych: \n" +  ex.getMessage(), "LocalDatabaseConnectorSQLite", "select(DataRow parameters) ", "result");
                    errorDialog.setVisible(true);
                }
                
         return wynik;
    }
    
}
