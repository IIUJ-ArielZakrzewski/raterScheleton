/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

import Dialogs.ErrorDialog;
import java.beans.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Karolina
 */
public class LocalDatabaseConnectorPostgre extends LocalDatabaseConnector 
{
                java.sql.Connection con = null;
                Statement st = null;
                ResultSet rs = null;

                PreparedStatement pst = null;
                String url = "jdbc:postgresql://localhost/";

    public LocalDatabaseConnectorPostgre(String dataBaseName, String login, String password)
    {
        url += dataBaseName;
        this.login = login;
        this.password = password;
    }
    
    @Override
    public boolean checkTable(String nazwa) 
    {
                    try 
                    {
                        con = DriverManager.getConnection(url, login, password);
                        String stm;
                        stm = "SELECT * FROM " + nazwa;

                        pst = con.prepareStatement(stm);
                        pst.executeUpdate();
                        return true;
                    } 
                    catch (SQLException ex) 
                    {
                        return false;
                    }
    }

    @Override
    public boolean insert(DataRow row) 
    {
        Boolean flaga = false;
             try 
             {
                                        
                con = DriverManager.getConnection(url, login, password);
                String stm;
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

                pst = con.prepareStatement(stm);
                pst.executeUpdate();
                flaga = true;
                

            } 
             catch (SQLException ex) 
                 {
                    ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w lokalnej bazie danych: \n" +  ex.getMessage(), "LocalDatabaseConnectorPostgre", "insert(DataRow row)", "con, stm, pst");
                    errorDialog.setVisible(true);
                    
                 } 
            finally 
             {

                      try {
                              if (pst != null) {
                                pst.close();
                              }
                               if (con != null) {
                                  con.close();
                              }

                           }
           catch (SQLException ex) 
            {
             ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w lokalnej bazie danych: \n" +  ex.getMessage(), "LocalDatabaseConnectorPostgre", "insert(DataRow row)", "con, stm, pst");
             errorDialog.setVisible(true);
             }
             }
             return flaga;
    }

    @Override
    public boolean update(DataRow parameters, DataRow row) 
    {
        Boolean flaga = false;
        try
        {
                con = DriverManager.getConnection(url, login, password);
                String stm;
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
                pst = con.prepareStatement(stm);
                pst.executeUpdate();
                flaga = true;
        }
        catch (Exception ex)
        {
             ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w lokalnej bazie danych: \n" +  ex.getMessage(), "LocalDatabaseConnectorPostgre", "update(DataRow parameters, DataRow row)", "con, stm, pst");
             errorDialog.setVisible(true);
        }
        return flaga;
    }

    @Override
    public boolean remove(DataRow row) 
    {
        Boolean flaga = false;
        try
        {
                con = DriverManager.getConnection(url, login, password);
                String stm;
                String tabelName = row.getTableName();
                List<DataCell> toDelete = row.row;
                String temp = "";
                for (DataCell x : toDelete)
                {
                    temp += x.name + " = " + x.value + " AND ";
                }
                temp = temp.substring(0, temp.length() - 5);
                stm = "DELETE FROM " + tabelName + " WHERE " + temp;
                pst = con.prepareStatement(stm);
                pst.executeUpdate();
                flaga = true;
        }
        catch (Exception ex)
        {
          ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w lokalnej bazie danych: \n" +  ex.getMessage(), "LocalDatabaseConnectorPostgre", "remove( DataRow row)", "con, stm, pst");
          errorDialog.setVisible(true);
        }
        return flaga;
    }

    @Override
    public List<DataRow> select(DataRow parameters) 
    {
        List<DataRow> wynik = new ArrayList<>();
        try
        {
                con = DriverManager.getConnection(url, login, password);
                String stm;
                String tabelName = parameters.getTableName();
                List<DataCell> select = parameters.row;
                String kolumny = "";
                for (DataCell x : select)
                {
                    kolumny += x.name + " = " + x.value + " AND ";
                }
                kolumny = kolumny.substring(0, kolumny.length() - 5);
                stm = "SELECT * FROM " + tabelName + " WHERE " + kolumny;
                pst = con.prepareStatement(stm);
                rs = pst.executeQuery();
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
            ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w lokalnej bazie danych: \n" +  ex.getMessage(), "LocalDatabaseConnectorPostgre", "select(DataRow parameters)", "con, stm, rs, pst");
            errorDialog.setVisible(true);
        }
        return wynik;
    }
    
}
