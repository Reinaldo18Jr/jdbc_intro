package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;


public class Program {

	public static void main(String[] args) {

		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DB.getConnection();
			
			// Transações serão 100% executadas ou nenhuma executada quando estiver entre commit
			conn.setAutoCommit(false);
			
			st = conn.createStatement();
			
			int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");
			
			int x = 1;
			if (x < 2) {
				throw new SQLException("FAKE ERROR");
			}
			
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");
			
			conn.commit();
			
			System.out.println("Rows1: " + rows1);
			System.out.println("Rows2: " + rows2);
		}
		
		catch (SQLException e) {
			// PARA VOLTAR AO ESTADO INICIAL DAS TRANSAÇÕES quando houver algum erro, não executando as suas ações
			try {
				conn.rollback();
				throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());	
			} 
			catch (SQLException e1) {
				throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
			}
		}
		
		finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
