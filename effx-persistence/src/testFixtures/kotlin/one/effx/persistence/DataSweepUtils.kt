package one.effx.persistence

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait

class DataSweepUtils(
    private val databaseClient: DatabaseClient,
    private val transactionalOperator: TransactionalOperator
) {

    suspend fun sweep() {
        transactionalOperator.executeAndAwait {
            val tableNames = getAllTableNames()
            if (tableNames.isNotEmpty()) {
                truncate(tableNames)
            }
        }
    }

    private suspend fun truncate(tables: Set<String>) {
        for (table in tables) {
            databaseClient.sql("DELETE FROM \"$table\"").then().awaitSingleOrNull()
        }
    }

    private suspend fun getAllTableNames(): Set<String> {
        val dbName = databaseClient.connectionFactory.metadata.name.lowercase()
        val schema = when {
            dbName.contains("postgresql") -> "effx"
            dbName.contains("h2") -> "PUBLIC"
            else -> null
        } ?: return emptySet()

        val query = when {
            dbName.contains("postgresql") -> """
                SELECT table_name FROM information_schema.tables 
                WHERE table_schema = '$schema' AND table_type = 'BASE TABLE'
            """
            dbName.contains("h2") -> """
                SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES 
                WHERE TABLE_SCHEMA = '$schema' AND TABLE_TYPE = 'BASE TABLE'
            """
            else -> return emptySet()
        }

        return databaseClient.sql(query)
            .map { row, _ ->
                row.get("table_name", String::class.java)
                    ?: row.get("TABLE_NAME", String::class.java)
                    ?: throw IllegalStateException("table_name 컬럼이 없음")
            }
            .all()
            .collectList()
            .awaitSingle()
            .toSet()
    }
}
