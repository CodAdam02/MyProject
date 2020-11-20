package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("Armies/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Armies{


    @GET
    @Path("list")
    public String ArmiesList() {
        System.out.println("Invoked Armies.ArmiesList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT ArmyName, Units, UserID, ArmyId, DOC  FROM Armies");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject row = new JSONObject();
                row.put("ArmyName", results.getString(1));
                row.put("Units", results.getString(2));
                row.put("UserID", results.getInt(3));
                row.put("ArmyId", results.getInt(4));
                row.put("DOC", results.getString(5));
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
    public String ArmiesAdd(@FormDataParam("ArmyName") String ArmyName, @FormDataParam("Units") String Units, @FormDataParam("UserID") Integer UserID, @FormDataParam("ArmyId") Integer ArmyId, @FormDataParam("DOC") String DOC ) {
        System.out.println("Invoked Armies.ArmiesAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Armies (ArmyName,Units,UserID,ArmyId,DOC) VALUES (?,?,?,?,?)");
            ps.setString(1, ArmyName);
            ps.setString(2, Units);
            ps.setInt(3, UserID);
            ps.setInt(4, ArmyId);
            ps.setString(5, DOC);

            ps.execute();
            return "{\"OK\": \"Added Army.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    public String updateArmies(@FormDataParam("ArmyName") String ArmyName, @FormDataParam("Units") String Units, @FormDataParam("UserID") Integer UserID, @FormDataParam("ArmyId") Integer ArmyId, @FormDataParam("DOC") String DOC ) {
        try {
            System.out.println("Invoked Armies.UpdateArmies/update UserID=" + UserID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET UserName = ?,Units = ?,ArmyId = ?,DOC = ? WHERE UserID = ?");
            ps.setString(1, ArmyName);
            ps.setString(2, Units);
            ps.setInt(3, ArmyId);
            ps.setString(4, DOC);
            ps.setInt(5, UserID);
            ps.execute();
            return "{\"OK\": \"Armies updated\"}";
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