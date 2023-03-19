import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class A02MiddleTier {

	public ResultSet rs;
	public Statement st;
	public int count, listener;
	public int num; // random id number
	public String evName, evDate, jourName; // evDate Format = YYYY-MM-DD
	public A02MiddleTier(){
		
		try{
			Connection connection = DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/a02schema",
				"root", "root1234"
					);

			// Create Statement
			st = connection.createStatement();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void execQuery() {
		String query = "";
		String insert_query = "";
		String insert_query2 = "";
		Boolean retBoolean;
		
		if(evName.equals("") && evDate.equals("") && jourName.equals("")) { // User has empty text fields
			return;
		}
		else if (!evName.equals("")){ // User must have eventname and selected an event type
			insert_query = "INSERT INTO Event (ID, Name) VALUES ("+num+", '"+evName+"')";
			if (listener == 1 && !evDate.equals("")) { // EventConference
				query = "SELECT * FROM Event LEFT JOIN EventConference ON Event.ID=EventConference.EventID";
				insert_query2 = "INSERT INTO EventConference (EventID, EvDate) VALUES ("+num+", '"+evDate+"')";

			}
			else if(listener == 2 && !jourName.equals("")) { // EventJournal
				query = "SELECT * FROM Event LEFT JOIN EventJournal ON Event.ID=EventJournal.EventID";
				insert_query2 = "INSERT INTO EventJournal (EventID, JournalName) VALUES ("+num+", '"+jourName+"')";
			}
			else if(listener == 3) { // EventBook
				query = "SELECT * FROM Event LEFT JOIN EventBook ON Event.ID=EventBook.EventID";
				insert_query2 = "INSERT INTO EventBook (EventID) VALUES ("+num+")";
			}
		}
		
		try {
			retBoolean = st.execute(insert_query); // Insert into Event Table
			retBoolean = st.execute(insert_query2); // Insert into Selected Event Type Table

			// Execute SELECT query
			rs = st.executeQuery(query);
			
			// Gets the number of columns
			count = rs.getMetaData().getColumnCount();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
