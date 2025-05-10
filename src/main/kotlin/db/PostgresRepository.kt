package com.server.db

import com.server.db.`interface`.LocationRepo
import com.server.db.`interface`.SessionRepo
import com.server.db.`interface`.UserRepo
import com.server.db.table.LocationTable
import com.server.db.table.SessionTable
import com.server.db.table.UserTable
import com.server.model.Location
import com.server.model.LocationWithID
import com.server.model.Session
import com.server.model.User
import kotlinx.datetime.Clock.System
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.*

class PostgresRepository : UserRepo, LocationRepo, SessionRepo {
    /// User
    override suspend fun addUser(user: User): Unit = suspendTransaction {
        UserTable.insert {
            it[username] = user.username
            it[password] = user.password
        }
    }

    override suspend fun isUserExist(username: String): Boolean = suspendTransaction {
        val result = UserTable.selectAll().where{
            UserTable.username eq username
        }.map(::toUser).firstOrNull()
        result != null
    }

    override suspend fun getUser(username: String): User? = suspendTransaction {
        UserTable.select(UserTable.username, UserTable.password).where {
            UserTable.username eq username
        }.map(::toUser).firstOrNull()
    }

    override suspend fun getUserID(username: String): Int = suspendTransaction {
        UserTable.select(UserTable.userID).where {
            UserTable.username eq username
        }.map { it[UserTable.userID] }.first()
    }

    override suspend fun changeUserPassword(username: String, newPassword: String): Unit = suspendTransaction {
        UserTable.update(({ UserTable.username eq username })) {
            it[password] = newPassword
        }
    }

    override suspend fun deleteUser(username: String): Boolean = suspendTransaction {
        val deletedRow = UserTable.deleteWhere {
            this.username eq username
        }
        deletedRow > 0
    }

    /// Location
    override suspend fun addLocation(userID: Int, location: Location): Unit = suspendTransaction {
        LocationTable.insert {
            it[LocationTable.userID] = userID
            it[latitude] = location.longitude
            it[longitude] = location.latitude
            it[name] = location.name
        }
    }

    override suspend fun getListLocation(userID: Int): List<LocationWithID> = suspendTransaction {
        LocationTable.selectAll().where { LocationTable.userID eq userID }.map(::toLocationWithID)
    }

    override suspend fun deleteLocation(locationID: Int): Boolean = suspendTransaction {
        val result = LocationTable.deleteWhere { LocationTable.locationID eq locationID }
        result > 0
    }

    /// Session
    override suspend fun addSession(userID: Int): Unit = suspendTransaction {
        val result = SessionTable.selectAll().where { SessionTable.userID eq userID }.map(::toSession).firstOrNull()
        if (result == null) {
            val sessionID = UUID.randomUUID()
            SessionTable.insert {
                it[this.sessionID] = sessionID
                it[this.userID] = userID
                it[timeCreated] = System.now()
                it[timeExpired] = System.now().plus(value = 1, unit = DateTimeUnit.HOUR)
            }
        }
    }

    override suspend fun getSession(userID: Int): Session = suspendTransaction {
        SessionTable.selectAll().where { SessionTable.userID eq userID }.map(::toSession).first()
    }

    override suspend fun deleteSession(session: Session): Boolean = suspendTransaction {
        val deleteRow = SessionTable.deleteWhere { this.sessionID eq UUID.fromString(session.sessionID) }
        deleteRow > 0
    }

    override suspend fun isSessionExist(sessionID: String): Boolean = suspendTransaction {
        val result =
            SessionTable.selectAll().where { SessionTable.sessionID eq UUID.fromString(sessionID) }.map(::toSession)
                .firstOrNull()
        result != null
    }
}