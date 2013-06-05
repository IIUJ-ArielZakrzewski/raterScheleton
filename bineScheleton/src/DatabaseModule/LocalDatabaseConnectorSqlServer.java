/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

import Dialogs.ErrorDialog;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ariel
 */
public class LocalDatabaseConnectorSqlServer extends LocalDatabaseConnector{

    
    Connection dbConnection;
    String dbDriverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    
    public LocalDatabaseConnectorSqlServer(String connection_port, String instancja, String dbName, String connection_login, String connection_password)
    {
        port = connection_port;
        login = connection_login;
        password = connection_password;
        int pr;
        try
        {
            pr = Integer.parseInt(port);
        } catch(Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Wystąpił błąd parsera numeru portu:\n" + e.getMessage(), "SqlServerLocalDatabaseConnector", "constructor", "port");
            errorDialog.setVisible(true);
            return;
        }
        
        if(pr < 1)
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Podano nieprawidłowy port.", "SqlServerLocalDatabaseConnector", "constructor", "port");
            errorDialog.setVisible(true);
            return;
        }
        if(dbName == null || dbName.trim().equals(""))
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Podano pustą nazwę bazy danych.", "SqlServerLocalDatabaseConnector", "constructor", "dbName");
            errorDialog.setVisible(true);
            return;
        }
        if(login == null || login.trim().equals(""))
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Podano pusty login.", "SqlServerLocalDatabaseConnector", "constructor", "login");
            errorDialog.setVisible(true);
            return;
        }
        if(password == null || password.trim().equals(""))
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Podano puste hasło.", "SqlServerLocalDatabaseConnector", "constructor", "password");
            errorDialog.setVisible(true);
            return;
        }
        if(instancja != null || !instancja.trim().equals(""))
        {
            instancja = "\\" + instancja;
        } else {
            instancja = "";
        }
        String connectionUrl = "jdbc:sqlserver://localhost" + instancja + ":" + port + "; databaseName=" + dbName + "; username=" + login + "; password=" + password;
        try {
            Class.forName(dbDriverClass);
            dbConnection = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException | SQLException ex) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage(), "SqlServerLocalDatabaseConnector", "constructor", "connectionUrl");
            errorDialog.setVisible(true);
            dbConnection = null;
        }
    }
    
    public LocalDatabaseConnectorSqlServer(String instancja, String dbName, String connection_login, String connection_password)
    {
        port = "1433";
        login = connection_login;
        password = connection_password;
        
        if(dbName == null || dbName.trim().equals(""))
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Podano pustą nazwę bazy danych.", "SqlServerLocalDatabaseConnector", "constructor", "dbName");
            errorDialog.setVisible(true);
            return;
        }
        if(login == null || login.trim().equals(""))
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Podano pusty login.", "SqlServerLocalDatabaseConnector", "constructor", "login");
            errorDialog.setVisible(true);
            return;
        }
        if(password == null || password.trim().equals(""))
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Podano puste hasło.", "SqlServerLocalDatabaseConnector", "constructor", "password");
            errorDialog.setVisible(true);
            return;
        }
        if(instancja != null || !instancja.trim().equals(""))
        {
            instancja = "\\" + instancja;
        } else {
            instancja = "";
        }
        String connectionUrl = "jdbc:sqlserver://localhost" + instancja + ":" + port + "; databaseName=" + dbName + "; username=" + login + "; password=" + password;
        try {
            Class.forName(dbDriverClass);
            dbConnection = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException | SQLException ex) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd podczas połączenia z bazą danych:\n" + ex.getMessage(), "SqlServerLocalDatabaseConnector", "constructor", "connectionUrl");
            errorDialog.setVisible(true);
            dbConnection = null;
        }
    }
    
    
    @Override
    public boolean checkTable(String nazwa) {
        try
        {
            Statement stat = dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stat.executeQuery("SELECT * FROM " + nazwa);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public boolean insert(DataRow row) {
        String statement = "INSERT INTO " + row.getTableName();
        String args = "";
        String values = "";
        boolean isFirst = true;
        for(DataCell cell : row.row)
        {
            if(isFirst)
            {
                isFirst = false;
                args += cell.getName();
                values += "'" + cell.getValue() + "'";
                
            } else {
                args += ", " + cell.getName();
                values += ", " + "'" + cell.getValue() + "'";
            }
            
        }
        //System.out.println(args);
        //System.out.println(values);
        statement += "(" + args + ")" + " VALUES(" + values + ")";
        try
        {
            Statement stat = dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stat.executeUpdate(statement);
            return true;
        } catch(Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd podczas wykonywania insert do bazy danych:\n" + e.getMessage(), "SqlServerLocalDatabaseConnector", "insert", "row, statement");
            errorDialog.setVisible(true);
            return false;
        }
    }

    @Override
    public boolean update(DataRow parameters, DataRow row) {
        if(!parameters.getName().equals(row.getName()))
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nazwy tabel w parameters i row są różne!", "SqlServerLocalDatabaseConnector", "update", "row, parameters");
            errorDialog.setVisible(true);
            return false;
        }
        String statement = "UPDATE " + row.getTableName() + " SET ";
        boolean isFirst = true;
        for(DataCell cell : row.row)
        {
            if(!cell.getName().equals("name"))
            {
                if(isFirst)
                {
                    isFirst = false;
                    statement += cell.getName() + " = '" + cell.getValue() + "'";
                } else {
                    statement += ", " + cell.getName() + " = '" + cell.getValue() + "'";
                }
            }
        }
        statement += " WHERE name = '" + parameters.getName() + "'";
        try
        {
            Statement stat = dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stat.executeUpdate(statement);
            return true;
        } catch(Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd podczas wykonywania update w bazie danych:\n" + e.getMessage(), "SqlServerLocalDatabaseConnector", "update", "row, parameters, statement");
            errorDialog.setVisible(true);
            return false;
        }
    }

    @Override
    public boolean remove(DataRow row) {
        
        String statement = "DELETE FROM " + row.getTableName();
        statement += " WHERE name = '" + row.getAttribute("name").getValue() + "'";
        try
        {
            Statement stat = dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stat.executeUpdate(statement);
            return true;
        } catch(Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd podczas wykonywania delete w bazie danych:\n" + e.getMessage(), "SqlServerLocalDatabaseConnector", "remove", "row, statement");
            errorDialog.setVisible(true);
            return false;
        }
    }

    @Override
    public List<DataRow> select(DataRow parameters) {
        String statement = "SELECT * FROM " + parameters.getTableName();
        if(!parameters.isEmpty())
        {
            statement += " WHERE ";
        }
        boolean isFirst = true;
        for(DataCell cell : parameters.row)
        {
            if(isFirst)
            {
                isFirst = false;
                statement += cell.getName() + " = '" + cell.getValue() + "'";
            } else {
                statement += " AND " + cell.getName() + " = '" + cell.getValue() + "'";
            }
        }
        List<DataRow> result = new LinkedList<>();
        try
        {
            Statement stat = dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stat.executeQuery(statement);
            rs.beforeFirst();
            while(rs.next())
            {
                DataRow newRow = new DataRow();
                newRow.setTableName(parameters.getTableName());
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                
                for(int i = 1; i <= columnCount; i++)
                {
                    newRow.addAttribute(rsmd.getColumnName(i), rs.getString(i));
                }
                newRow.setName(newRow.getAttribute("name").getValue());
                result.add(newRow);
            }
            return result;
        } catch(Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd podczas wykonywania select w bazie danych:\n" + e.getMessage(), "SqlServerLocalDatabaseConnector", "select", "parameters, statement");
            errorDialog.setVisible(true);
            return null;
        }
    }
    
}
