package com.ericlam.mc.multiconomy.sql;

import com.ericlam.mc.multiconomy.MultiConomy;
import com.ericlam.mc.multiconomy.UpdateResult;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.SQLDataSource;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Created by caxerx on 2016/6/28.
 */
public class MYSQLController {

    private final String getAccountStatement;
    private final String getBalanceStatement;
    private final String createAccountStatement;
    private final String updateBalanceStatement;
    private final String setBalanceStatement;
    private final String createUserTableStmt;
    private final String lockerRowStatement;
    private final double defaultBalance;

    private final SQLDataSource sql;
    private final String serverId;

    public MYSQLController(String userdataTable, int defaultBalance) {
        this.sql = HyperNiteMC.getAPI().getSQLDataSource();
        this.serverId = Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort();
        this.defaultBalance = defaultBalance;
        createUserTableStmt = "CREATE TABLE IF NOT EXISTS `" + userdataTable + "` ( `uuid` TEXT NOT NULL , `name` TINYTEXT, `money` DOUBLE NOT NULL DEFAULT '0', `lock` TEXT DEFAULT NULL, PRIMARY KEY (`uuid`(36)))";
        getAccountStatement = "SELECT * FROM `" + userdataTable + "` WHERE `uuid`=?";
        getBalanceStatement = "SELECT `money`,`lock` FROM `" + userdataTable + "` WHERE `uuid` = ?";
        createAccountStatement = "INSERT IGNORE INTO `" + userdataTable + "` VALUES (?,?,?,null)";
        updateBalanceStatement = "INSERT INTO " + userdataTable + " VALUES(?,?,?,null) ON DUPLICATE KEY UPDATE `name` = ?, `money` = money + ?, `lock` = null";
        setBalanceStatement = "INSERT INTO " + userdataTable + " VALUES(?,?,?,null) ON DUPLICATE KEY UPDATE `name` = ?, `money` = ?, `lock` = null";
        lockerRowStatement = "UPDATE IGNORE " + userdataTable + " SET `lock`=? WHERE `uuid`=?";
    }

    private String getServerId() {
        return serverId;
    }

    public void createTable() {
        try (Connection connection = sql.getConnection();
             PreparedStatement createTable = connection.prepareStatement(createUserTableStmt);
        ) {
            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void locker(OfflinePlayer player, boolean lock) {
        String uuid = player.getUniqueId().toString();
        try (Connection connection = sql.getConnection();
             PreparedStatement statement = connection.prepareStatement(lockerRowStatement)) {
            statement.setString(1, lock ? getServerId() : null);
            statement.setString(2, uuid);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(OfflinePlayer player, boolean forceUnlock) throws TableLockedException {
        double result = defaultBalance;
        String uuid = player.getUniqueId().toString();
        try (Connection connection = sql.getConnection();
             PreparedStatement statement = connection.prepareStatement(getBalanceStatement)) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                if (!forceUnlock) checkLock(resultSet);
                result = resultSet.getDouble("money");
            } else {
                createAccount(player);
            }
        } catch (SQLException e) {
            MultiConomy.getPlugin().getLogger().log(Level.SEVERE, e.getSQLState(), e);
        }
        return result;
    }


    public boolean hasAccount(OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        try (Connection connection = sql.getConnection();
             PreparedStatement statement = connection.prepareStatement(getAccountStatement)) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {

            MultiConomy.getPlugin().getLogger().log(Level.SEVERE, e.getSQLState(), e);
        }
        return false;
    }

    public void createAccount(OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        try (Connection connection = sql.getConnection();
             PreparedStatement statement = connection.prepareStatement(createAccountStatement)
        ) {
            statement.setString(1, uuid);
            statement.setString(2, player.getName());
            statement.setDouble(3, defaultBalance);
            statement.execute();
        } catch (SQLException e) {
            MultiConomy.getPlugin().getLogger().log(Level.SEVERE, e.getSQLState(), e);
        }
    }

    public void checkLock(ResultSet set) throws TableLockedException, SQLException {
        var lock = set.getString("lock");
        if (lock != null && !lock.equals(getServerId())){
            throw new TableLockedException();
        }
    }


    public UpdateResult updatePlayer(OfflinePlayer player, double value, boolean set, boolean forceUnlock) throws TableLockedException {
        String uuid = player.getUniqueId().toString();
        UpdateResult result = UpdateResult.UNKNOWN;
        try (Connection connection = sql.getConnection();
             PreparedStatement statement = connection.prepareStatement(getBalanceStatement);
             PreparedStatement statement2 = connection.prepareStatement(set ? setBalanceStatement : updateBalanceStatement)) {
            if (!set && value < 0) {
                statement.setString(1, uuid);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    if (!forceUnlock) checkLock(resultSet);
                    if (resultSet.getDouble("money") < Math.abs(value)) {
                        return UpdateResult.BALANCE_INSUFFICIENT;
                    }
                }
            }
            statement2.setString(1, uuid);
            statement2.setString(2, player.getName());
            statement2.setDouble(3, set ? value : defaultBalance + value);
            statement2.setString(4, player.getName());
            statement2.setDouble(5, value);
            if (statement2.executeUpdate() > 0) {
                result = UpdateResult.SUCCESS;
            }
        } catch (SQLException e) {
            MultiConomy.getPlugin().getLogger().log(Level.SEVERE, e.getSQLState(), e);
        }
        return result;
    }



    /*
    public boolean logTransition(String uuid, String operator, String action, double value, long time) {
        return logTransition(uuid, operator, action, value, time, getConnection(), true);
    }

    public boolean logTransition(String uuid, String operator, String action, double value, long time, Connection connection) {
        return logTransition(uuid, operator, action, value, time, connection, false);
    }

    public boolean logTransition(String uuid, String operator, String action, double value, long time, Connection connection, boolean closeConnection) {
        PreparedStatement statement = null;
        boolean result = false;
        try {
            statement = connection.prepareStatement(logStatement);
            statement.setString(1, uuid);
            statement.setString(2, operator);
            statement.setString(3, action);
            statement.setDouble(4, value);
            statement.setLong(5, time);
            if (statement.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (closeConnection) {
                closeConnection(connection);
            }
            closeStatement(statement);
            return result;
        }
    }
*/
}
