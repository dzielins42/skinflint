package pl.dzielins42.skinflint.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: Long,
    val name: String,
    val currency: String,
    val description: String?,
    val date: Date,
    // Amount is stored without decimal point, calculate it accordingly ;)
    val value: Long
)