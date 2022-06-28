package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Food> getVertici(int porzioni, Map<Integer, Food> idMap) {
		String sql = "SELECT food_code, COUNT(DISTINCT portion_id) AS cnt "
				+ "FROM food_pyramid_mod.portion "
				+ "GROUP BY food_code "
				+ "HAVING COUNT(DISTINCT portion_id) = ?";
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			List<Food> list = new ArrayList<>() ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(idMap.get(res.getInt("food_code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}
	
	
	public List<Adiacenza> getArchi(int porzioni, Map<Integer, Food> idMap) {
		String sql = "SELECT f1.food_code AS fc1, f2.food_code AS fc2, AVG(c.condiment_calories) AS peso "
				+ "FROM food_condiment AS f1, food_condiment AS f2, condiment AS c "
				+ "WHERE f1.food_code > f2.food_code "
				+ "AND f1.condiment_code = f2.condiment_code "
				+ "AND f1.condiment_code = c.condiment_code "
				+ "AND f1.food_code IN (SELECT food_code "
				+ "							FROM food_pyramid_mod.portion "
				+ "							GROUP BY food_code "
				+ "							HAVING COUNT(DISTINCT portion_id) = ?) "
				+ "AND f2.food_code IN (SELECT food_code "
				+ "							FROM food_pyramid_mod.portion "
				+ "							GROUP BY food_code "
				+ "							HAVING COUNT(DISTINCT portion_id) = ?) "
				+ "GROUP BY f1.food_code, f2.food_code";
		
		List<Adiacenza> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, porzioni);
			st.setInt(2, porzioni);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(new Adiacenza(idMap.get(res.getInt("fc1")), idMap.get(res.getInt("fc2")), res.getDouble("peso")));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}
