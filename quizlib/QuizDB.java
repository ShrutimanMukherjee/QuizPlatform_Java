package quizlib;

import java.util.Collection;
import java.util.ArrayList;
import org.postgresql.*;
import java.sql.*;

public class QuizDB
{
	private static final String url = "jdbc:postgresql://heffalump.db.elephantsql.com:5432/qoagvwfc";
	private static final String username = "qoagvwfc";
	private static final String password = "QuS_0KbA1Nz7huCy2c8GyhurimfVg6Vi";
	
	private ArrayList<String> fields;
	private ArrayList<ArrayList<String>> table;
	private Connection db;
	
	public QuizDB()
	{
		try
		{
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e)
		{
            System.out.println("Cannot find : "+e.getMessage());
        }
		
		fields = new ArrayList<String>();
		table = new ArrayList<ArrayList<String>>();
	}
	
	public void runQuery(String query)
	{
		try
		{
			db = DriverManager.getConnection(url, username, password);
		}
		catch (java.sql.SQLException e)
		{
			System.out.println("SQL-Driver Exception : "+e.getMessage());
		}
		
		try 
		{
			Statement st = db.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			
			fields = new ArrayList<String>();
			table = new ArrayList<ArrayList<String>>();
			
			for(int field=1; field<=columns; field++)
			{
				fields.add(rsmd.getColumnName(field));
			}
			
			while (rs.next())
			{
				ArrayList<String> record = new ArrayList<String>();
				for(int field=1; field<=columns; field++)
				{
					record.add(rs.getString(field));
				}
				table.add(record);
			}
			rs.close();
			st.close();
		}
		catch (java.sql.SQLException e)
		{
			String mssg = e.getMessage();
			if(mssg.equals("No results were returned by the query."))
				System.out.println(" ");
			else
				System.out.println("SQL Exception : "+e.getMessage());
		}
		
		try
		{
			db.close();
		}
		catch(Exception e)
		{
			System.out.println("Connection closing exception : "+e.getMessage());
		}
	}
	
	public void resultDisplay()
	{
		System.out.println("\n---------------------------------------------------------------------------");
		for(String fieldName : fields)
			System.out.print("|\t"+fieldName+"\t|");
		System.out.println("\n---------------------------------------------------------------------------");
		
		for(ArrayList<String> row : table)
		{
			for(String fieldValue : row)
				System.out.print("|\t"+fieldValue+"\t|");
			System.out.println();
		}
	}
	
	public ArrayList<ArrayList<String>> getResult()
	{
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		result.add(new ArrayList<String>());
		for(String field : fields)
			result.get(0).add(field);
		int last = 0;
		for(ArrayList<String> row : table)
		{
			++last;
			result.add(new ArrayList<String>());
			for(String data : row)
				result.get(last).add(data);
		}
		return result;
	}
	
	public static void main(String args[])
	{
		QuizDB qobj = new QuizDB();
		qobj.runQuery("SELECT * FROM course");
		qobj.resultDisplay();
	}
}