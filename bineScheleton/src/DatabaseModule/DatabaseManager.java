/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

import Dialogs.ErrorDialog;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ariel
 */
public class DatabaseManager {
    LocalDatabaseConnector local;
    ServerDatabaseConnector server;
    List<String> tableNames;
    String userName;
    
    public DatabaseManager(LocalDatabaseConnector loc, ServerDatabaseConnector serv, String tabNames)
    {
        local = loc;
        server = serv;
        tableNames = new LinkedList<>();
        String[] names = tabNames.split(";");
        tableNames.addAll(Arrays.asList(names));
        userName = "";
    }
    
    public boolean login(String name, String password)
    {
        DataRow row = new  DataRow();
        row.setTableName("users");
        row.addAttribute("name", name);
        
        if(local.select(row).isEmpty())
        {
            
            if(server.select(row).isEmpty())
            {
                ErrorDialog errorDialog = new ErrorDialog(true, "User o nazwie: " + name + " nie istnieje.", "DatabaseManager", "login(String name, String password)", "name");
                errorDialog.setVisible(true);
                return false;
            } else {
                DataRow user = local.select(row).get(0);
                local.insert(user);
                synchronize();
                for(DataCell cell : user.row)
                {
                    if(cell.name.equals("password"))
                    {
                        if(cell.value.equals(password))
                        {
                            userName = name;
                            //Wyświetl komunikat o udanym zalogowaniu
                            return true;
                        } else {
                            ErrorDialog errorDialog = new ErrorDialog(true, "Hasło dla usera o nazwie: " + name + " jest nieprawidłowe.", "DatabaseManager", "login(String name, String password)", "password");
                            errorDialog.setVisible(true);
                            return false;
                        }
                    }
                }
            }
        } else {
            DataRow user = local.select(row).get(0);
            for(DataCell cell : user.row)
            {
                if(cell.name.equals("userPassword"))
                {
                    if(cell.value.equals(password))
                    {
                        userName = name;
                        //Wyświetl komunikat o udanym zalogowaniu
                        return true;
                    } else {
                        ErrorDialog errorDialog = new ErrorDialog(true, "Hasło dla usera o nazwie: " + name + " jest nieprawidłowe.", "DatabaseManager", "login(String name, String password)", "password");
                        errorDialog.setVisible(true);
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean insert(DataRow row)
    {
        if(!checkUser())
        {
            return false;
        }
        DataRow parameter = new DataRow(row.getAttribute("name").getValue());
        parameter.setTableName(row.getTableName());
        parameter.addAttribute("userName", getUserName());
        if(!select(parameter).isEmpty())
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "W bazie danych istnieje wpis o podanych parametrach.", "DatabaseManager", "insert(DataRow row)", "row");
            errorDialog.setVisible(true);
            return false;
        }
        boolean result;
        row = addIdentifyingParameters(row);
        DataRow removed = new DataRow(row.getTableName() + row.getAttribute("name").getValue());
        removed.setTableName("removedData");
        removed.addAttribute("tableName", row.tableName);
        removed.addAttribute("objectName", row.getAttribute("name").getValue());
        removed.addAttribute("userName", userName);
        try
        {
            result = local.insert(row);
            if(!local.select(removed).isEmpty())
            {
                local.remove(removed);
            }
            if(server.checkConnection())
            {
                result = result && server.insert(row);
                if(!server.select(removed).isEmpty())
                {
                    server.remove(removed);
                }
            }
        } catch(Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w bazie danych: \n" +  e.getMessage(), "DatabaseManager", "insert(DataRow row)", "row");
            errorDialog.setVisible(true);
            result = false;
        }
        return result;
    }
    
    public boolean update(DataRow parameters, DataRow row)
    {
        if(!checkUser())
        {
            return false;
        }
        boolean result;
        row = addIdentifyingParameters(row);
        try
        {
            result = local.update(parameters, row);
            if(server.checkConnection())
            {
                result = result && server.update(parameters, row);
            }
        } catch(Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w bazie danych: \n" +  e.getMessage(), "DatabaseManager", "update(DataRow parameters, DataRow row)", "row");
            errorDialog.setVisible(true);
            result = false;
        }
        return result;
    }
    public boolean remove(DataRow row)
    {
        if(!checkUser())
        {
            return false;
        }
        boolean result;
        row = addIdentifyingParameters(row);
        DataRow removed = new DataRow(row.getTableName() + row.getName());
        removed.setTableName("removedData");
        removed.addAttribute("tableName", row.tableName);
        removed.addAttribute("objectName", row.getAttribute("name").getValue());
        removed = addIdentifyingParameters(removed);
        try
        {
            result = local.remove(row);
            local.insert(removed);
            if(server.checkConnection())
            {
                result = result && server.remove(row);
                server.insert(removed);
            }
        } catch(Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w bazie danych: \n" +  e.getMessage(), "DatabaseManager", "remove(DataRow row)", "row");
            errorDialog.setVisible(true);
            result = false;
        }
        return result;
    }
    public List<DataRow> select(DataRow parameters)
    {
        synchronize();
        List<DataRow> result = local.select(parameters);
        for(DataRow row : result)
        {
            if(row.containsAttribute("date"))
            {
                row.removeAttribute("date");
            }
            if(row.containsAttribute("userName"))
            {
                row.removeAttribute("userName");
            }
        }
        return result;
    }
    
    public boolean createUser(DataRow row)
    {
        if(!select(row).isEmpty())
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "W bazie danych istnieje wpis o podanych parametrach.", "DatabaseManager", "createUser(DataRow row)", "row");
            errorDialog.setVisible(true);
            return false;
        }
        boolean result;
        DataRow removed = new DataRow(row.getTableName() + row.getName());
        removed.setTableName("removedData");
        removed.addAttribute("tableName", row.tableName);
        removed.addAttribute("objectName", row.name);
        try
        {
            result = local.insert(row);
            if(!local.select(removed).isEmpty())
            {
                local.remove(removed);
            }
            if(server.checkConnection())
            {
                result = result && server.insert(row);
                if(!server.select(removed).isEmpty())
                {
                    server.remove(removed);
                }
            }
        } catch(Exception e) {
            ErrorDialog errorDialog = new ErrorDialog(true, "Błąd operacji w bazie danych: \n" +  e.getMessage(), "DatabaseManager", "createUser(DataRow row)", "row");
            errorDialog.setVisible(true);
            result = false;
        }
        return result;
    }
    
    public boolean removeUser(DataRow row)
    {
        boolean result = remove(row);
        userName = "";
        return result;
    }
    
    public void synchronize()
    {
        if(!server.checkConnection())
        {
            return;
        }
        for(String table : tableNames)
        {
            DataRow row = new DataRow();
            row.setTableName(table);
            row.addAttribute("userName", userName);
            List<DataRow> locals = local.select(row);
            List<DataRow> servers = server.select(row);
            for(DataRow serv : servers)
            {
                boolean updated = false;
                for(DataRow loc : locals)
                {
                    if(serv.name.equals(loc.name))
                    {
                        Date dataLocal = null;
                        for(DataCell cell : loc.row)
                        {
                            if(cell.name.equals("date"))
                            {
                                dataLocal = new Date(cell.value);
                            }
                        }
                        Date dataServer = null;
                        for(DataCell cell : serv.row)
                        {
                            if(cell.name.equals("date"))
                            {
                                dataServer = new Date(cell.value);
                            }
                        }
                        if(dataLocal.before(dataServer))
                        {
                            local.update(loc, serv);
                        } else {
                            server.update(serv, loc);
                        }
                        updated = true;
                        locals.remove(loc);
                    }
                }
                if(!updated)
                {
                    DataRow removed = new DataRow(serv.getTableName() + serv.getName());
                    removed.setTableName("removedData");
                    removed.addAttribute("tableName", serv.tableName);
                    removed.addAttribute("objectName", serv.name);
                    removed.addAttribute("userName", userName);
                    if(local.select(removed).isEmpty())
                    {
                        local.insert(serv);
                    } else {
                        DataRow rem = local.select(removed).get(0);
                        Date dataLocal = null;
                        for(DataCell cell : rem.row)
                        {
                            if(cell.name.equals("date"))
                            {
                                dataLocal = new Date(cell.value);
                            }
                        }
                        Date dataServer = null;
                        for(DataCell cell : serv.row)
                        {
                            if(cell.name.equals("date"))
                            {
                                dataServer = new Date(cell.value);
                            }
                        }
                        if(dataServer.before(dataLocal))
                        {
                            server.remove(serv);
                        } else {
                            local.insert(serv);
                        }
                    }
                    
                }
            }
            
            if(!locals.isEmpty())
            {
                for(DataRow loc : locals)
                {
                    DataRow removed = new DataRow(loc.getTableName() + loc.getName());
                    removed.setTableName("removedData");
                    removed.addAttribute("tableName", loc.tableName);
                    removed.addAttribute("objectName", loc.name);
                    removed.addAttribute("userName", userName);
                    if(server.select(removed).isEmpty())
                    {
                        server.insert(loc);
                    } else {
                        DataRow rem = server.select(removed).get(0);
                        Date dataLocal = null;
                        for(DataCell cell : loc.row)
                        {
                            if(cell.name.equals("date"))
                            {
                                dataLocal = new Date(cell.value);
                            }
                        }
                        Date dataServer = null;
                        for(DataCell cell : rem.row)
                        {
                            if(cell.name.equals("date"))
                            {
                                dataServer = new Date(cell.value);
                            }
                        }
                        if(dataServer.before(dataLocal))
                        {
                            local.remove(loc);
                        } else {
                            server.insert(loc);
                        }
                    }
                }
            }
        }
    }
    
    private boolean checkUser()
    {
        DataRow row = new  DataRow();
        row.setTableName("users");
        row.addAttribute("name", userName);
        
        if(local.select(row).isEmpty())
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "User nie istnieje lub nie jest zalogowany.", "DatabaseManager", "checkUser()", "local variable userName");
            errorDialog.setVisible(true);
            return false;
        }
        return true;
    }
    
    
    private DataRow addIdentifyingParameters(DataRow row)
    {
        Date data = new Date();
        row.addAttribute("date", data.toString());
        row.addAttribute("userName", userName);
        return row;
    }
    
    public boolean isAtrribute(String typ)
    {
        return local.checkTable(typ);
    }
    
    public String getUserName()
    {
        return userName;
    }
}
