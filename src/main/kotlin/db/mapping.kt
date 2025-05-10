package com.server.db

import com.server.db.table.LocationTable
import com.server.db.table.SessionTable
import com.server.db.table.UserTable
import com.server.model.LocationWithID
import com.server.model.Session
import com.server.model.User
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: suspend Transaction.() -> T,): T {
    return newSuspendedTransaction(Dispatchers.IO, statement = block)
}

fun toUser(row: ResultRow) = User(row[UserTable.username], row[UserTable.password])
fun toLocationWithID(row: ResultRow) = LocationWithID(
    row[LocationTable.locationID],
    row[LocationTable.userID],
    row[LocationTable.latitude],
    row[LocationTable.longitude],
    row[LocationTable.name]
)
fun toSession(row: ResultRow) = Session(
    row[SessionTable.sessionID].toString(),
    row[SessionTable.userID],
    row[SessionTable.timeCreated].toString(),
    row[SessionTable.timeExpired].toString(),
)