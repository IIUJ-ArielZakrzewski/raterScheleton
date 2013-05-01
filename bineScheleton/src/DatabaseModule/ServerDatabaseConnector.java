/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Ariel
 */
public abstract class ServerDatabaseConnector extends DatabaseConnector{
    public boolean internetConnection;
    public String hostName;
    
    public boolean checkConnection()
    {
        internetConnection = true;
        try
        {
        URL url=new URL("http://www.google.pl");
        URLConnection con=url.openConnection();
        } catch (Exception e) {
            internetConnection = false;
        }
        return internetConnection;
    }
}
