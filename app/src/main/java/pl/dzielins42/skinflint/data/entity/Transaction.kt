package pl.dzielins42.skinflint.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "transactions")
@Parcelize
data class Transaction(
    @PrimaryKey val id: Long,
    val name: String,
    val currency: String,
    val description: String?,
    val date: Date,
    // Amount is stored without decimal point, calculate it accordingly ;)
    val value: Long
) : Parcelable