/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

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
    
    public void login(String name, String password)
    {
        DataRow row = new  DataRow();
        row.setTableName("users");
        row.addAttribute("userName", name);
        
        if(local.select(row).isEmpty())
        {
            //Wyświetl error o braku usera w bazie danych
        }
        DataRow user = local.select(row).get(0);
        for(DataCell cell : user.row)
        {
            if(cell.name.equals("password"))
            {
                if(cell.value.equals(password))
                {
                    userName = name;
                    //Wyświetl komunikat o udanym zalogowaniu
                } else {
                    //Wyświetl error o błędnym haśle
                }
            }
        }
        
    }
    
    public boolean insert(DataRow row)
    {
        if(!checkUser())
        {
            return false;
        }
        if(!select(row).isEmpty())
        {
            //Wyświetl error o istniejącym obiekcie w bazie danych
            return false;
        }
        boolean result;
        row = addIdentifyingParameters(row);
        DataRow removed = new DataRow();
        removed.setTableName("removedData");
        removed.addAttribute("tableName", row.tableName);
        removed.addAttribute("name", row.name);
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
            //Wyświetl okno o błędzie insertu do bazy danych
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
            //Wyświetl okno o błędzie update do bazy danych
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
        DataRow removed = new DataRow();
        removed.setTableName("removedData");
        removed.addAttribute("tableName", row.tableName);
        removed.addAttribute("name", row.name);
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
            //Wyświetl okno o błędzie remove do bazy danych
            result = false;
        }
        return result;
    }
    public List<DataRow> select(DataRow parameters)
    {
        synchronize();
        return local.select(parameters);
    }
    
    public boolean createUser(DataRow row)
    {
        if(!select(row).isEmpty())
        {
            //Wyświetl error o istniejącym obiekcie w bazie danych
            return false;
        }
        boolean result;
        DataRow removed = new DataRow();
        removed.setTableName("removedData");
        removed.addAttribute("tableName", row.tableName);
        removed.addAttribute("name", row.name);
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
            //Wyświetl okno o błędzie insertu do bazy danych
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
                    DataRow removed = new DataRow();
                    removed.setTableName("removedData");
                    removed.addAttribute("tableName", serv.tableName);
                    removed.addAttribute("name", serv.name);
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
                    DataRow removed = new DataRow();
                    removed.setTableName("removedData");
                    removed.addAttribute("tableName", loc.tableName);
                    removed.addAttribute("name", loc.name);
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
        row.addAttribute("userName", userName);
        
        if(local.select(row).isEmpty())
        {
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
}
