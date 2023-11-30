package jdbcFirst.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
 * 003 Подключение к базам данных
 */

public final class ConnectionManager {
	
	private static final String PASSWORD_KEY = "db.password";
	private static final String USERNAME_KEY = "db.username";
	private static final String URL_KEY = "db.url";
	private static final String POOL_SIZE_KEY= "db.pool.size";
	private static Integer DEFAULT_POOL_SIZE = 10;
	private static BlockingQueue<Connection> pool;		// 006 Пул соединений (JDBC Аdvanced); потокобезопасная коллекция
	private static List<Connection> sourceConnections;

	static {
		loadDriver(); 
		initConnectionPool();  // 006 Пул соединений (JDBC Аdvanced)
	}
	
	private ConnectionManager() {
	}
	
	// 006 Пул соединений (JDBC Аdvanced)
	private static void initConnectionPool() {
		String poolSize = PropertiesUtil.get(POOL_SIZE_KEY);
		int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
		pool = new ArrayBlockingQueue<>(size);
		sourceConnections = new ArrayList<>(size);
		for (int i=0; i<size; i++) {
			Connection connection = open();
			Connection proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(), new Class[] {Connection.class}, 
					(proxy, method, args) -> method.getName().equals("close")
						? pool.add((Connection) proxy)
					: method.invoke(connection, args));
			pool.add(proxyConnection);
			sourceConnections.add(connection);	// Чтобы вызвать именно метод close (не перезаписанный в proxyConnection
		}
	}
	
	public static Connection get() {
		try {
			return pool.take();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private static Connection open() {
		try {
			return DriverManager.getConnection(
					PropertiesUtil.get(URL_KEY),
					PropertiesUtil.get(USERNAME_KEY),
					PropertiesUtil.get(PASSWORD_KEY)
					);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void loadDriver() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void closePool() {
		for (Connection sourceConnection : sourceConnections) {
			try {
				sourceConnection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}