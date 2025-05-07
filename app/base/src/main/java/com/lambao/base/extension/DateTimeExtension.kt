package com.lambao.base.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.ConcurrentHashMap

/**
 * Cache for SimpleDateFormat to improve performance.
 *
 * **Note**: SimpleDateFormat is not thread-safe. In multi-threaded environments, ensure thread-local
 * instances or synchronization when using cached instances.
 */
private val simpleDateFormatCache = ConcurrentHashMap<Pair<String, Locale>, SimpleDateFormat>()

/**
 * Gets a cached SimpleDateFormat or creates a new one.
 *
 * @param pattern The date-time pattern (e.g., "dd/MM/yyyy").
 * @param locale The locale for formatting/parsing.
 * @return A cached or new SimpleDateFormat instance.
 *
 * **Note**: Use with caution in multi-threaded environments due to SimpleDateFormat's thread-safety issues.
 */
private fun getSimpleDateFormat(pattern: String, locale: Locale): SimpleDateFormat {
    return simpleDateFormatCache.getOrPut(Pair(pattern, locale)) {
        SimpleDateFormat(pattern, locale)
    }
}

/**
 * Formats the Date to a string with the given pattern.
 *
 * @param pattern The date-time pattern (e.g., "dd/MM/yyyy HH:mm").
 * @param locale The locale for formatting (default: Locale.getDefault()).
 * @return Formatted string or empty string if an error occurs.
 *
 * **Example**:
 * ```kotlin
 * val date = Date()
 * val formatted = date.format("dd/MM/yyyy") // e.g., "06/05/2025"
 * ```
 */
fun Date.format(pattern: String, locale: Locale = Locale.getDefault()): String {
    return try {
        getSimpleDateFormat(pattern, locale).format(this)
    } catch (e: Exception) {
        ""
    }
}

/**
 * Checks if the Date is before another Date.
 *
 * @param other The Date to compare with.
 * @return True if this Date is before the other.
 *
 * **Example**:
 * ```kotlin
 * val date1 = createDate(2025, 5, 6)
 * val date2 = createDate(2025, 5, 7)
 * val isBefore = date1.isBefore(date2) // true
 * ```
 */
fun Date.isBefore(other: Date): Boolean {
    return time < other.time
}

/**
 * Checks if the Date is after another Date.
 *
 * @param other The Date to compare with.
 * @return True if this Date is after the other.
 *
 * **Example**:
 * ```kotlin
 * val date1 = createDate(2025, 5, 6)
 * val date2 = createDate(2025, 5, 7)
 * val isAfter = date2.isAfter(date1) // true
 * ```
 */
fun Date.isAfter(other: Date): Boolean {
    return time > other.time
}

/**
 * Checks if the Date represents the same day as another Date.
 *
 * @param other The Date to compare with.
 * @return True if both Dates represent the same day.
 *
 * **Note**: For slightly better performance, consider [isSameDayEfficient].
 *
 * **Example**:
 * ```kotlin
 * val date1 = createDate(2025, 5, 6)
 * val date2 = createDate(2025, 5, 6)
 * val isSameDay = date1.isSameDay(date2) // true
 * ```
 */
fun Date.isSameDay(other: Date): Boolean {
    val thisCalendar = Calendar.getInstance().apply { time = this@isSameDay }
    val otherCalendar = Calendar.getInstance().apply { time = other }
    return thisCalendar.isSameDay(otherCalendar)
}

/**
 * More efficient implementation for checking if two Dates represent the same day.
 *
 * @param other The Date to compare with.
 * @return True if both Dates represent the same day.
 *
 * **Example**:
 * ```kotlin
 * val date1 = createDate(2025, 5, 6)
 * val date2 = createDate(2025, 5, 6)
 * val isSameDay = date1.isSameDayEfficient(date2) // true
 * ```
 */
fun Date.isSameDayEfficient(other: Date): Boolean {
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = this
    cal2.time = other
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
}

/**
 * Checks if the Date is today.
 *
 * @return True if the Date is today.
 *
 * **Example**:
 * ```kotlin
 * val today = Date()
 * val isToday = today.isToday() // true
 * val pastDate = createDate(2025, 5, 5)
 * val isNotToday = pastDate.isToday() // false
 * ```
 */
fun Date.isToday(): Boolean {
    return isSameDay(Date())
}

/**
 * Checks if the Date is in the past.
 *
 * @return True if the Date is before today.
 *
 * **Example**:
 * ```kotlin
 * val pastDate = createDate(2025, 5, 5)
 * val isPast = pastDate.isPast() // true (if today is after May 5, 2025)
 * ```
 */
fun Date.isPast(): Boolean {
    return isBefore(Date())
}

/**
 * Checks if the Date is in the future.
 *
 * @return True if the Date is after today.
 *
 * **Example**:
 * ```kotlin
 * val futureDate = createDate(2025, 5, 7)
 * val isFuture = futureDate.isFuture() // true (if today is before May 7, 2025)
 * ```
 */
fun Date.isFuture(): Boolean {
    return isAfter(Date())
}

/**
 * Gets the day of the week as a string.
 *
 * @param locale The locale for the day name (default: Locale.getDefault()).
 * @return The full name of the day (e.g., "Monday").
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val dayOfWeek = date.getDayOfWeek(Locale.US) // "Tuesday"
 * ```
 */
fun Date.getDayOfWeek(locale: Locale = Locale.getDefault()): String {
    return getSimpleDateFormat("EEEE", locale).format(this)
}

/**
 * Gets the day of the week as a number (1-7, Sunday=1).
 *
 * @return The day of week (1-7, Sunday=1).
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val dayNumber = date.getDayOfWeekNumber() // 3 (Tuesday)
 * ```
 */
fun Date.getDayOfWeekNumber(): Int {
    val cal = Calendar.getInstance().apply { time = this@getDayOfWeekNumber }
    return cal.get(Calendar.DAY_OF_WEEK)
}

/**
 * Adds days to the Date.
 *
 * @param days The days to add (can be negative to subtract).
 * @return New Date with added days.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val newDate = date.plusDays(2) // May 8, 2025
 * ```
 */
fun Date.plusDays(days: Int): Date {
    return add(Calendar.DAY_OF_MONTH, days)
}

/**
 * Adds months to the Date.
 *
 * @param months The months to add (can be negative to subtract).
 * @return New Date with added months.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val newDate = date.plusMonths(1) // June 6, 2025
 * ```
 */
fun Date.plusMonths(months: Int): Date {
    return add(Calendar.MONTH, months)
}

/**
 * Adds years to the Date.
 *
 * @param years The years to add (can be negative to subtract).
 * @return New Date with added years.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val newDate = date.plusYears(1) // May 6, 2026
 * ```
 */
fun Date.plusYears(years: Int): Date {
    return add(Calendar.YEAR, years)
}

/**
 * Adds hours to the Date.
 *
 * @param hours The hours to add (can be negative to subtract).
 * @return New Date with added hours.
 *
 * **Example**:
 * ```kotlin
 * val date = createDateTime(2025, 5, 6, 14, 30)
 * val newDate = date.plusHours(2) // 16:30 on May 6, 2025
 * ```
 */
fun Date.plusHours(hours: Int): Date {
    return add(Calendar.HOUR_OF_DAY, hours)
}

/**
 * Adds minutes to the Date.
 *
 * @param minutes The minutes to add (can be negative to subtract).
 * @return New Date with added minutes.
 *
 * **Example**:
 * ```kotlin
 * val date = createDateTime(2025, 5, 6, 14, 30)
 * val newDate = date.plusMinutes(15) // 14:45 on May 6, 2025
 * ```
 */
fun Date.plusMinutes(minutes: Int): Date {
    return add(Calendar.MINUTE, minutes)
}

/**
 * Adds or subtracts a specified amount to the Date.
 *
 * @param field The Calendar field (e.g., Calendar.DAY_OF_MONTH).
 * @param amount The amount to add (positive) or subtract (negative).
 * @return New Date instance with the modified time.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val newDate = date.add(Calendar.DAY_OF_MONTH, 3) // May 9, 2025
 * ```
 */
fun Date.add(field: Int, amount: Int): Date {
    return Calendar.getInstance().apply {
        time = this@add
        add(field, amount)
    }.time
}

/**
 * Converts Date to Calendar.
 *
 * @return Calendar representation of the Date.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val calendar = date.toCalendar()
 * println(calendar.format("dd/MM/yyyy")) // "06/05/2025"
 * ```
 */
fun Date.toCalendar(): Calendar {
    return Calendar.getInstance().apply { time = this@toCalendar }
}

/**
 * Gets the start of the day (00:00:00) for the given Date.
 *
 * @return Date representing the start of the day.
 *
 * **Example**:
 * ```kotlin
 * val date = createDateTime(2025, 5, 6, 14, 30)
 * val start = date.startOfDay() // 00:00:00 on May 6, 2025
 * ```
 */
fun Date.startOfDay(): Date {
    return Calendar.getInstance().apply {
        time = this@startOfDay
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
}

/**
 * Gets the end of the day (23:59:59.999) for the given Date.
 *
 * @return Date representing the end of the day.
 *
 * **Example**:
 * ```kotlin
 * val date = createDateTime(2025, 5, 6, 14, 30)
 * val end = date.endOfDay() // 23:59:59.999 on May 6, 2025
 * ```
 */
fun Date.endOfDay(): Date {
    return Calendar.getInstance().apply {
        time = this@endOfDay
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.time
}

/**
 * Calculates age based on the birthdate.
 *
 * @return Age in years, or 0 if the birthdate is invalid or in the future.
 *
 * **Example**:
 * ```kotlin
 * val birthDate = createDate(1990, 5, 6)
 * val age = birthDate.getAge() // 35 (if today is May 6, 2025)
 * ```
 */
fun Date.getAge(): Int {
    val birthCalendar = Calendar.getInstance().apply {
        time = this@getAge
    }
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
    if (today.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
        (today.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))) {
        age--
    }
    return age
}

/**
 * Formats the Calendar to a string with the given pattern.
 *
 * @param pattern The date-time pattern (e.g., "dd/MM/yyyy HH:mm").
 * @param locale The locale for formatting (default: Locale.getDefault()).
 * @return Formatted string or empty string if an error occurs.
 *
 * **Example**:
 * ```kotlin
 * val calendar = Calendar.getInstance()
 * val formatted = calendar.format("dd/MM/yyyy HH:mm") // e.g., "06/05/2025 14:30"
 * ```
 */
fun Calendar.format(pattern: String, locale: Locale = Locale.getDefault()): String {
    return try {
        getSimpleDateFormat(pattern, locale).format(time)
    } catch (e: Exception) {
        ""
    }
}

/**
 * Checks if the Calendar represents a date/time before another Calendar.
 *
 * @param other The Calendar to compare with.
 * @return True if this Calendar is before the other.
 *
 * **Example**:
 * ```kotlin
 * val cal1 = createDate(2025, 5, 6).toCalendar()
 * val cal2 = createDate(2025, 5, 7).toCalendar()
 * val isBefore = cal1.isBefore(cal2) // true
 * ```
 */
fun Calendar.isBefore(other: Calendar): Boolean {
    return timeInMillis < other.timeInMillis
}

/**
 * Checks if the Calendar represents a date/time after another Calendar.
 *
 * @param other The Calendar to compare with.
 * @return True if this Calendar is after the other.
 *
 * **Example**:
 * ```kotlin
 * val cal1 = createDate(2025, 5, 6).toCalendar()
 * val cal2 = createDate(2025, 5, 7).toCalendar()
 * val isAfter = cal2.isAfter(cal1) // true
 * ```
 */
fun Calendar.isAfter(other: Calendar): Boolean {
    return timeInMillis > other.timeInMillis
}

/**
 * Checks if the Calendar represents the same day as another Calendar.
 *
 * @param other The Calendar to compare with.
 * @return True if both Calendars represent the same day.
 *
 * **Example**:
 * ```kotlin
 * val cal1 = createDate(2025, 5, 6).toCalendar()
 * val cal2 = createDate(2025, 5, 6).toCalendar()
 * val isSameDay = cal1.isSameDay(cal2) // true
 * ```
 */
fun Calendar.isSameDay(other: Calendar): Boolean {
    return get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
            get(Calendar.MONTH) == other.get(Calendar.MONTH) &&
            get(Calendar.DAY_OF_MONTH) == other.get(Calendar.DAY_OF_MONTH)
}

/**
 * Checks if the Calendar represents today.
 *
 * @return True if the Calendar is today.
 *
 * **Example**:
 * ```kotlin
 * val today = Calendar.getInstance()
 * val isToday = today.isToday() // true
 * ```
 */
fun Calendar.isToday(): Boolean {
    return isSameDay(Calendar.getInstance())
}

/**
 * Checks if the Calendar is in the past.
 *
 * @return True if the Calendar is before today.
 *
 * **Example**:
 * ```kotlin
 * val past = createDate(2025, 5, 5).toCalendar()
 * val isPast = past.isPast() // true (if today is after May 5, 2025)
 * ```
 */
fun Calendar.isPast(): Boolean {
    return isBefore(Calendar.getInstance())
}

/**
 * Checks if the Calendar is in the future.
 *
 * @return True if the Calendar is after today.
 *
 * **Example**:
 * ```kotlin
 * val future = createDate(2025, 5, 7).toCalendar()
 * val isFuture = future.isFuture() // true (if today is before May 7, 2025)
 * ```
 */
fun Calendar.isFuture(): Boolean {
    return isAfter(Calendar.getInstance())
}

/**
 * Gets the day of the week as a string.
 *
 * @param locale The locale for the day name (default: Locale.getDefault()).
 * @return The full name of the day (e.g., "Monday").
 *
 * **Example**:
 * ```kotlin
 * val calendar = createDate(2025, 5, 6).toCalendar()
 * val dayOfWeek = calendar.getDayOfWeek(Locale.US) // "Tuesday"
 * ```
 */
fun Calendar.getDayOfWeek(locale: Locale = Locale.getDefault()): String {
    return getSimpleDateFormat("EEEE", locale).format(time)
}

/**
 * Gets the AM/PM status of the Calendar.
 *
 * @return "AM" or "PM" based on the hour.
 *
 * **Example**:
 * ```kotlin
 * val calendar = createDateTime(2025, 5, 6, 14, 30).toCalendar()
 * val amPm = calendar.getAmPm() // "PM"
 * ```
 */
fun Calendar.getAmPm(): String {
    return if (get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
}

/**
 * Gets the number of days between this Calendar and another.
 *
 * @param other The Calendar to compare with.
 * @return The number of days between the two Calendars (positive if other is after, negative if before).
 *
 * **Example**:
 * ```kotlin
 * val cal1 = createDate(2025, 5, 6).toCalendar()
 * val cal2 = createDate(2025, 5, 8).toCalendar()
 * val days = cal1.daysUntil(cal2) // 2
 * ```
 */
fun Calendar.daysUntil(other: Calendar): Int {
    val thisDate = Calendar.getInstance().apply {
        timeInMillis = this@daysUntil.timeInMillis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val otherDate = Calendar.getInstance().apply {
        timeInMillis = other.timeInMillis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val diff = otherDate.timeInMillis - thisDate.timeInMillis
    return (diff / (1000 * 60 * 60 * 24)).toInt()
}

/**
 * Converts Calendar to Date.
 *
 * @return Date representation of the Calendar.
 *
 * **Example**:
 * ```kotlin
 * val calendar = Calendar.getInstance()
 * val date = calendar.toDate()
 * println(date.format("dd/MM/yyyy")) // e.g., "06/05/2025"
 * ```
 */
fun Calendar.toDate(): Date {
    return time
}

/**
 * Adds days to the Calendar.
 *
 * @param days Days to add (can be negative to subtract).
 * @return A new Calendar instance with days added.
 *
 * **Example**:
 * ```kotlin
 * val calendar = createDate(2025, 5, 6).toCalendar()
 * val newCalendar = calendar.plusDays(2) // May 8, 2025
 * ```
 */
fun Calendar.plusDays(days: Int): Calendar {
    return (clone() as Calendar).apply {
        add(Calendar.DAY_OF_MONTH, days)
    }
}

/**
 * Adds months to the Calendar.
 *
 * @param months Months to add (can be negative to subtract).
 * @return A new Calendar instance with months added.
 *
 * **Example**:
 * ```kotlin
 * val calendar = createDate(2025, 5, 6).toCalendar()
 * val newCalendar = calendar.plusMonths(1) // June 6, 2025
 * ```
 */
fun Calendar.plusMonths(months: Int): Calendar {
    return (clone() as Calendar).apply {
        add(Calendar.MONTH, months)
    }
}

/**
 * Adds years to the Calendar.
 *
 * @param years Years to add (can be negative to subtract).
 * @return A new Calendar instance with years added.
 *
 * **Example**:
 * ```kotlin
 * val calendar = createDate(2025, 5, 6).toCalendar()
 * val newCalendar = calendar.plusYears(1) // May 6, 2026
 * ```
 */
fun Calendar.plusYears(years: Int): Calendar {
    return (clone() as Calendar).apply {
        add(Calendar.YEAR, years)
    }
}

/**
 * Converts a String to a Date based on the given pattern.
 *
 * @param pattern The date-time pattern (e.g., "dd/MM/yyyy HH:mm").
 * @param locale The locale for parsing (default: Locale.getDefault()).
 * @return Date object or null if parsing fails.
 *
 * **Example**:
 * ```kotlin
 * val date = "06/05/2025 14:30".toDate("dd/MM/yyyy HH:mm", Locale.US)
 * println(date?.format("yyyy-MM-dd")) // "2025-05-06"
 * ```
 */
fun String.toDate(pattern: String, locale: Locale = Locale.getDefault()): Date? {
    return try {
        getSimpleDateFormat(pattern, locale).parse(this)
    } catch (e: Exception) {
        null
    }
}

/**
 * Converts a String to a Calendar based on the given pattern.
 *
 * @param pattern The date-time pattern (e.g., "dd/MM/yyyy HH:mm").
 * @param locale The locale for parsing (default: Locale.getDefault()).
 * @return Calendar object or null if parsing fails.
 *
 * **Example**:
 * ```kotlin
 * val calendar = "06/05/2025 14:30".toCalendar("dd/MM/yyyy HH:mm", Locale.US)
 * println(calendar?.format("yyyy-MM-dd")) // "2025-05-06"
 * ```
 */
fun String.toCalendar(pattern: String, locale: Locale = Locale.getDefault()): Calendar? {
    return toDate(pattern, locale)?.toCalendar()
}

/**
 * Reformats a date string from an old pattern to a new pattern.
 *
 * @param oldPattern The pattern of the input string (e.g., "dd/MM/yyyy").
 * @param newPattern The desired pattern for the output string (e.g., "yyyy-MM-dd").
 * @param locale The locale for parsing and formatting (default: Locale.getDefault()).
 * @return Reformatted string or null if the input string is invalid.
 *
 * **Example**:
 * ```kotlin
 * val reformatted = "06/05/2025".reformatDate("dd/MM/yyyy", "yyyy-MM-dd", Locale.US)
 * println(reformatted) // "2025-05-06"
 * ```
 */
fun String.reformatDate(
    oldPattern: String,
    newPattern: String,
    locale: Locale = Locale.getDefault()
): String? {
    return toDate(oldPattern, locale)?.format(newPattern, locale)
}

/**
 * Checks if the string is a valid date with the given pattern.
 *
 * @param pattern The date-time pattern (e.g., "dd/MM/yyyy").
 * @param locale The locale for parsing (default: Locale.getDefault()).
 * @return True if the string is a valid date for the given pattern, false otherwise.
 *
 * **Note**: Uses strict parsing (non-lenient) to ensure exact match with the pattern.
 *
 * **Example**:
 * ```kotlin
 * val valid = "06/05/2025".isValidDate("dd/MM/yyyy", Locale.US) // true
 * val invalid = "2025-05-06".isValidDate("dd/MM/yyyy", Locale.US) // false
 * ```
 */
fun String.isValidDate(pattern: String, locale: Locale = Locale.getDefault()): Boolean {
    return try {
        getSimpleDateFormat(pattern, locale).apply {
            isLenient = false // Strict parsing
        }.parse(this)
        true
    } catch (e: ParseException) {
        false
    }
}

/**
 * Gets the number of days in the month of the given Date.
 *
 * @return The number of days in the month (e.g., 31 for May).
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val days = date.getDaysInMonth() // 31
 * ```
 */
fun Date.getDaysInMonth(): Int {
    val calendar = Calendar.getInstance().apply { time = this@getDaysInMonth }
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

/**
 * Gets the first day of the month for the given Date.
 *
 * @return Date representing the first day of the month at 00:00:00.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val firstDay = date.firstDayOfMonth() // May 1, 2025 00:00:00
 * ```
 */
fun Date.firstDayOfMonth(): Date {
    return Calendar.getInstance().apply {
        time = this@firstDayOfMonth
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
}

/**
 * Gets the last day of the month for the given Date.
 *
 * @return Date representing the last day of the month at 23:59:59.999.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val lastDay = date.lastDayOfMonth() // May 31, 2025 23:59:59.999
 * ```
 */
fun Date.lastDayOfMonth(): Date {
    return Calendar.getInstance().apply {
        time = this@lastDayOfMonth
        set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.time
}

/**
 * Gets the first day of the week (Sunday) for the given Date.
 *
 * @return Date representing the first day of the week at 00:00:00.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6) // Tuesday
 * val firstDay = date.firstDayOfWeek() // May 4, 2025 (Sunday)
 * ```
 */
fun Date.firstDayOfWeek(): Date {
    val calendar = Calendar.getInstance().apply { time = this@firstDayOfWeek }
    return calendar.apply {
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
}

/**
 * Gets the last day of the week (Saturday) for the given Date.
 *
 * @return Date representing the last day of the week at 23:59:59.999.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6) // Tuesday
 * val lastDay = date.lastDayOfWeek() // May 10, 2025 (Saturday)
 * ```
 */
fun Date.lastDayOfWeek(): Date {
    val calendar = Calendar.getInstance().apply { time = this@lastDayOfWeek }
    return calendar.apply {
        set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.time
}

/**
 * Gets the first day of the year for the given Date.
 *
 * @return Date representing the first day of the year at 00:00:00.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val firstDay = date.firstDayOfYear() // January 1, 2025 00:00:00
 * ```
 */
fun Date.firstDayOfYear(): Date {
    return Calendar.getInstance().apply {
        time = this@firstDayOfYear
        set(Calendar.MONTH, Calendar.JANUARY)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
}

/**
 * Gets the last day of the year for the given Date.
 *
 * @return Date representing the last day of the year at 23:59:59.999.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val lastDay = date.lastDayOfYear() // December 31, 2025 23:59:59.999
 * ```
 */
fun Date.lastDayOfYear(): Date {
    return Calendar.getInstance().apply {
        time = this@lastDayOfYear
        set(Calendar.MONTH, Calendar.DECEMBER)
        set(Calendar.DAY_OF_MONTH, 31)
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.time
}

/**
 * Determines if the year of the Calendar is a leap year.
 *
 * @return True if the year is a leap year, false otherwise.
 *
 * **Example**:
 * ```kotlin
 * val calendar = createDate(2024, 5, 6).toCalendar()
 * val isLeap = calendar.isLeapYear() // true
 * ```
 */
fun Calendar.isLeapYear(): Boolean {
    val year = get(Calendar.YEAR)
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

/**
 * Gets the quarter (1-4) of the year for the given Date.
 *
 * @return The quarter number (1 for Jan-Mar, 2 for Apr-Jun, etc.).
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val quarter = date.getQuarter() // 2 (Q2)
 * ```
 */
fun Date.getQuarter(): Int {
    val calendar = Calendar.getInstance().apply { time = this@getQuarter }
    return calendar.get(Calendar.MONTH) / 3 + 1
}

/**
 * Returns the week number within the year (1-52/53).
 *
 * @return The week number in the year.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val week = date.getWeekOfYear() // e.g., 19
 * ```
 */
fun Date.getWeekOfYear(): Int {
    val calendar = Calendar.getInstance().apply { time = this@getWeekOfYear }
    return calendar.get(Calendar.WEEK_OF_YEAR)
}

/**
 * Formats the Date according to the specified timezone.
 *
 * @param pattern The date-time pattern (e.g., "dd/MM/yyyy HH:mm").
 * @param timeZone The timezone for formatting.
 * @param locale The locale for formatting (default: Locale.getDefault()).
 * @return Formatted string or empty string if an error occurs.
 *
 * **Example**:
 * ```kotlin
 * val date = createDateTime(2025, 5, 6, 14, 30)
 * val formatted = date.formatWithTimeZone("dd/MM/yyyy HH:mm", TimeZone.getTimeZone("UTC"))
 * // e.g., "06/05/2025 14:30" in UTC
 * ```
 */
fun Date.formatWithTimeZone(
    pattern: String,
    timeZone: TimeZone,
    locale: Locale = Locale.getDefault()
): String {
    return try {
        SimpleDateFormat(pattern, locale).apply {
            this.timeZone = timeZone
        }.format(this)
    } catch (e: Exception) {
        ""
    }
}

/**
 * Calculates the difference in milliseconds between two Dates.
 *
 * @param other The Date to compare with.
 * @return The difference in milliseconds (positive if other is after, negative if before).
 *
 * **Example**:
 * ```kotlin
 * val date1 = createDateTime(2025, 5, 6, 14, 30)
 * val date2 = createDateTime(2025, 5, 6, 14, 31)
 * val diff = date1.millisUntil(date2) // 60000 (1 minute)
 * ```
 */
fun Date.millisUntil(other: Date): Long {
    return other.time - this.time
}

/**
 * Calculates the difference in seconds between two Dates.
 *
 * @param other The Date to compare with.
 * @return The difference in seconds (positive if other is after, negative if before).
 *
 * **Example**:
 * ```kotlin
 * val date1 = createDateTime(2025, 5, 6, 14, 30)
 * val date2 = createDateTime(2025, 5, 6, 14, 31)
 * val diff = date1.secondsUntil(date2) // 60
 * ```
 */
fun Date.secondsUntil(other: Date): Long {
    return millisUntil(other) / 1000
}

/**
 * Calculates the difference in minutes between two Dates.
 *
 * @param other The Date to compare with.
 * @return The difference in minutes (positive if other is after, negative if before).
 *
 * **Example**:
 * ```kotlin
 * val date1 = createDateTime(2025, 5, 6, 14, 30)
 * val date2 = createDateTime(2025, 5, 6, 15, 30)
 * val diff = date1.minutesUntil(date2) // 60
 * ```
 */
fun Date.minutesUntil(other: Date): Long {
    return secondsUntil(other) / 60
}

/**
 * Calculates the difference in hours between two Dates.
 *
 * @param other The Date to compare with.
 * @return The difference in hours (positive if other is after, negative if before).
 *
 * **Example**:
 * ```kotlin
 * val date1 = createDateTime(2025, 5, 6, 14, 30)
 * val date2 = createDateTime(2025, 5, 6, 16, 30)
 * val diff = date1.hoursUntil(date2) // 2
 * ```
 */
fun Date.hoursUntil(other: Date): Long {
    return minutesUntil(other) / 60
}

/**
 * Calculates the difference in days between two Dates.
 *
 * @param other The Date to compare with.
 * @return The number of days between the two Dates (positive if other is after, negative if before).
 *
 * **Example**:
 * ```kotlin
 * val date1 = createDate(2025, 5, 6)
 * val date2 = createDate(2025, 5, 8)
 * val diff = date1.daysUntil(date2) // 2
 * ```
 */
fun Date.daysUntil(other: Date): Int {
    return toCalendar().daysUntil(other.toCalendar())
}

/**
 * Checks if the Date is a weekend (Saturday or Sunday).
 *
 * @return True if the Date is a Saturday or Sunday.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 10) // Saturday
 * val isWeekend = date.isWeekend() // true
 * ```
 */
fun Date.isWeekend(): Boolean {
    val calendar = Calendar.getInstance().apply { time = this@isWeekend }
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
}

/**
 * Checks if the Date is a weekday (Monday to Friday).
 *
 * @return True if the Date is a Monday to Friday.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6) // Tuesday
 * val isWeekday = date.isWeekday() // true
 * ```
 */
fun Date.isWeekday(): Boolean {
    return !isWeekend()
}

/**
 * Creates a Date for a specific day.
 *
 * @param year The year (e.g., 2025).
 * @param month The month (1-12).
 * @param day The day of the month (1-31).
 * @return Date representing the specified day at 00:00:00.
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * println(date.format("dd/MM/yyyy")) // "06/05/2025"
 * ```
 */
fun createDate(year: Int, month: Int, day: Int): Date {
    return Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1) // Calendar months are 0-based
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
}

/**
 * Creates a Date for a specific date and time.
 *
 * @param year The year (e.g., 2025).
 * @param month The month (1-12).
 * @param day The day of the month (1-31).
 * @param hour The hour of the day (0-23).
 * @param minute The minute (0-59).
 * @param second The second (0-59, default: 0).
 * @return Date representing the specified date and time.
 *
 * **Example**:
 * ```kotlin
 * val date = createDateTime(2025, 5, 6, 14, 30)
 * println(date.format("dd/MM/yyyy HH:mm")) // "06/05/2025 14:30"
 * ```
 */
fun createDateTime(
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int,
    second: Int = 0
): Date {
    return Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month - 1) // Calendar months are 0-based
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, second)
        set(Calendar.MILLISECOND, 0)
    }.time
}

/**
 * Gets the full month name from the Date.
 *
 * @param locale The locale for the month name (default: Locale.getDefault()).
 * @return The full month name (e.g., "May").
 *
 * **Example**:
 * ```kotlin
 * val date = createDate(2025, 5, 6)
 * val monthName = date.getMonthName(Locale.US) // "May"
 * ```
 */
fun Date.getMonthName(locale: Locale = Locale.getDefault()): String {
    return getSimpleDateFormat("MMMM", locale).format(this)
}

/**
 * Converts a Long timestamp to a Date object.
 *
 * @return Date object representing the timestamp, or null if the timestamp is invalid.
 *
 * **Example**:
 * ```kotlin
 * val timestamp = 1743964200000L // 06/05/2025 14:30 UTC
 * val date = timestamp.toDate()
 * println(date?.format("dd/MM/yyyy HH:mm", Locale.US)) // "06/05/2025 14:30"
 * ```
 */
fun Long.toDate(): Date? {
    return try {
        Date(this)
    } catch (e: Exception) {
        null
    }
}