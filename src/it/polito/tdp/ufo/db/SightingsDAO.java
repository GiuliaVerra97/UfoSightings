package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"),
							res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), 
							res.getString("state"), 
							res.getString("country"),
							res.getString("shape"),
							res.getInt("duration"),
							res.getString("duration_hm"),
							res.getString("comments"),
							res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), 
							res.getDouble("longitude"))) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	
	
	
	/**
	 * Metodo che mi permette di ricavare l'anno e i numeri di ufi visti in quell'anno
	 * @return lista di {@link AnnoCount}
	 */
	
	public List<AnnoCount> getAnni(){
		 String sql="SELECT DISTINCT YEAR(DATETIME) AS anno, COUNT(id) AS cnt " + 
		 		"FROM sighting " + 
		 		"WHERE country='us' " + 
		 		"GROUP BY YEAR(datetime)";
		
		try {
			
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<AnnoCount> anni = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				anni.add(new AnnoCount(Year.of(res.getInt("anno")), res.getInt("cnt")));		//si prende l'anno con Year.of
			}
			
			conn.close();
			return anni;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
	
	
	
	
	/**
	 * Metodo che mi permette di ricavare tutti gli stati
	 * @param anno
	 * @return lista di {@link String}
	 */
	
	public List<String> getStati(Year anno){
		String sql="SELECT DISTINCT state "+
					"from sighting "+
					"where country='us' "+
					"and Year(datetime)=?";
		
		
		try {
			
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			
			List<String> stati = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				stati.add(res.getString("state"));
			}
			
			conn.close();
			return stati;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
			
		}
		
		
		
	}
	
	
	
	
	/**
	 * Verifico che esista una connessione diretta tra due stati. 
	 * Condizioni: stesso anno, il country deve essere us e non devonno avvenire nello stesso giorno 
	 * @param s1 stato 1
	 * @param s2 stato 2
	 * @param anno
	 * @return boolean
	 */
	
	public boolean esisteArco(String s1, String s2, Year anno) {
		
		String sql="SELECT COUNT(*) AS cnt " + 
				"FROM sighting s1, sighting s2 " + 
				"WHERE YEAR(s1.DATETIME)=YEAR(s2.DATETIME) AND YEAR(s1.DATETIME)=? " + 
				"     AND s1.state=? AND s2.state=? AND s1.country='us' AND s2.country='us' AND s2.DATETIME>s1.datetime";
	
		
		try {
			
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			st.setString(2, s1);
			st.setString(3, s2);
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				if(res.getInt("cnt")>0) {
					conn.close();
					return true;
				}else {
					conn.close();			
					return false;
				}
			}
			
			//se metto solo il conn.close() qui non va bene, non funziona il DB
						
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
			
		}
		
		return false;

	
	}
	
	
	
	
}



	
	
	


