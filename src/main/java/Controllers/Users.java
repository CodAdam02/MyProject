package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Users/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)


public class Users{


    @GET
    @Path("list")
    public String UsersList() {
        System.out.println("Invoked Users.UsersList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserName, Email, Password, UserID, Admin FROM Users");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject row = new JSONObject();
                row.put("UserName", results.getString(1));
                row.put("Email", results.getString(2));
                row.put("Password", results.getString(3));
                row.put("UserID", results.getInt(4));
                row.put("Admin", results.getBoolean(5));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }




    @POST
    @Path("add")
    public String UsersAdd(@FormDataParam("UserName") String UserName, @FormDataParam("Email") String Email, @FormDataParam("Password") String Password, @FormDataParam("UserID") Integer UserID, @FormDataParam("Admin") Boolean Admin) {
        System.out.println("Invoked Users.UsersAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserName, Email, Password, UserID, Admin ) VALUES (?,?,?,?,?)");

            ps.setString(1, UserName);
            ps.setString(2, Email);
            ps.setString(3, Password);
            ps.setInt(4, UserID);
            ps.setBoolean(5, Admin);
            ps.execute();
            return "{\"OK\": \"Added user.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }




    @POST
    @Path("update")
    public String UsersUpdate(@FormDataParam("UserName") String UserName, @FormDataParam("Email") String Email, @FormDataParam("Password") String Password, @FormDataParam("UserID") Integer UserID, @FormDataParam("Admin") Boolean Admin) {
        try {
            System.out.println("Invoked Users.UpdateUsers/update UserID=" + UserID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET UserName = ?,Email = ?, Password = ?, Admin = ?   WHERE UserID = ?");

            ps.setString(1, UserName);
            ps.setString(2, Email);
            ps.setString(3, Password);
            ps.setBoolean(4, Admin);
            ps.setInt(5, UserID);


            ps.execute();

            return "{\"OK\": \"Users updated\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to update item, please see server console for more info.\"}";
        }
    }




    @POST
    @Path("delete/{UserID}")
    public String DeleteUser(@PathParam("UserID") Integer UserID) throws Exception {
        System.out.println("Invoked Users.DeleteUser()");
        if (UserID == null) {
            throw new Exception("UserID is missing in the HTTP request's URL.");
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.execute();
            return "{\"OK\": \"User deleted\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }
}

