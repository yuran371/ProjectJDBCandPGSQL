package jdbcFirst;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jdbcFirst.util.ConnectionManager;

public class BlobRunner {

	public static void main(String[] args) throws SQLException, IOException {
		getImage();
	}

	private static void getImage() throws SQLException, IOException {
		
		String sql = """
				UPDATE aircraft
				SET image = ?
				WHERE id = 1
				""";
		
		try(Connection connect = ConnectionManager.open();
			PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
			preparedStatement.setBytes(1, Files.readAllBytes(Path.of("src\\main\\resources", "Boeing_777.jpg")));
			preparedStatement.executeUpdate();
		}
	}
}
