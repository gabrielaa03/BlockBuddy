package com.gabrielaangebrandt.blockbuddy.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import android.util.Patterns
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.processing.CallLogModel
import com.gabrielaangebrandt.blockbuddy.model.processing.CallState

private const val SUSPICIOUS_CALL_MOCK = "425-950-1212"
private const val SCAM_CALL_MOCK = "253-950=1212"

open class ProcessingManager(
    val context: Context,
    private val sharedPrefsHelper: SharedPrefsHelper
) {

    private val callLogProjection = arrayOf(
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.NUMBER,
        CallLog.Calls.TYPE,
        CallLog.Calls.DATE
    )

    fun isNumberOnContactList(number: String): Boolean {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val projection = arrayOf(
            ContactsContract.PhoneLookup._ID,
            ContactsContract.PhoneLookup.NUMBER,
            ContactsContract.PhoneLookup.DISPLAY_NAME
        )

        // if cursor exists, check its content.
        // Otherwise, there are no contacts
        getCursor(uri, projection)?.let {
            // if cursor has data, contact is found
            val hasData = it.moveToFirst()
            it.close()
            return hasData
        } ?: kotlin.run {
            return false
        }
    }

    fun isNumberBlocked(number: String): Boolean =
        sharedPrefsHelper.blockedNumbers.find {
            PhoneNumberUtils.compare(
                number,
                it
            )
        } != null

    // retrieves all call logs wrapped with cursor
    fun getCallLogs(): List<CallLogModel> {
        val uri = CallLog.Calls.CONTENT_URI
        getCursor(uri, callLogProjection)?.let {
            val callLogs = filterCallLogs(it)
            it.close()
            return callLogs
        } ?: return emptyList()
    }

    // filtering all incoming calls
    private fun filterCallLogs(cursor: Cursor): List<CallLogModel> =
        generateSequence(cursor)
            .map { getCallLogsFromCursor(it) }
            .filter { it.type != CallLog.Calls.OUTGOING_TYPE }
            .sortedByDescending { it.time }
            .toList()

    // extension for getting data of specific a column
    private fun Cursor.getCallLogData(columnName: String) =
        this.getString(callLogProjection.indexOf(columnName))

    // modeling
    private fun getCallLogsFromCursor(cursor: Cursor): CallLogModel {
        val number: String = cursor.getCallLogData(CallLog.Calls.NUMBER)
        var name: String = cursor.getCallLogData(CallLog.Calls.CACHED_NAME)
        if (name.isEmpty()) {
            name = context.getString(R.string.unknown)
        }
        val time: String = cursor.getCallLogData(CallLog.Calls.DATE)
        val type: Int = cursor.getCallLogData(CallLog.Calls.TYPE).toInt()

        return CallLogModel(
            type = type,
            number = number,
            name = name,
            time = time.toLong().convertToDate(),
            state = getState(number),
        )
    }

    private fun getState(number: String): CallState {
        val numberBlocked = isNumberBlocked(number)
        val numberIsScam = isNumberPotentialScam(number)
        val numberSuspicious = isNumberSuspicious(number)
        val numberIsAContact = isNumberOnContactList(number)

        return when {
            numberBlocked || numberIsScam -> CallState.SCAM
            numberSuspicious -> CallState.SUSPICIOUS
            numberIsAContact -> CallState.CONTACT
            else -> CallState.UNDEFINED
        }
    }

    // MARK: generic

    private fun getCursor(uri: Uri, projection: Array<String>): Cursor? =
        context.contentResolver?.query(
            uri,
            projection,
            null,
            null,
            null
        )

    private fun generateSequence(cursor: Cursor): Sequence<Cursor> =
        generateSequence {
            if (cursor.moveToNext()) {
                cursor
            } else {
                null
            }
        }

    fun isNumberPotentialScam(number: String): Boolean =
        PhoneNumberUtils.compare(
            SCAM_CALL_MOCK,
            number
        )

    fun isNumberSuspicious(number: String): Boolean =
        PhoneNumberUtils.compare(
            SUSPICIOUS_CALL_MOCK,
            number
        )

    // check if entered number matches phone number regex
    fun isNumberValid(number: String) =
        Patterns.PHONE.matcher(number).matches()

}