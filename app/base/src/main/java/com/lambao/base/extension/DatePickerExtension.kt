package com.lambao.base.extension

import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

/**
 * Displays a single date picker dialog using MaterialDatePicker with Material 3 styling.
 *
 * @param title The title of the date picker dialog. Defaults to null (no title).
 * @param theme The resource ID of a custom theme for the picker, e.g., R.style.CustomDatePickerTheme.
 *             Must inherit from Theme.Material3.* or Theme.MaterialComponents.*. Defaults to 0 (uses app theme).
 * @param minDate The minimum selectable date in milliseconds since epoch. Defaults to null (no minimum).
 * @param maxDate The maximum selectable date in milliseconds since epoch. Defaults to null (no maximum).
 * @param openAt The date to open the calendar at, in milliseconds since epoch. Defaults to null (uses initial selection or today).
 * @param firstDayOfWeek The first day of the week (e.g., Calendar.SUNDAY, Calendar.MONDAY). Defaults to null (uses system default).
 * @param initialSelection The initially selected date in milliseconds since epoch. Defaults to today.
 * @param validator A custom validator for selectable dates. Defaults to null (no validator).
 * @param positiveButtonText The text for the positive (OK) button. Defaults to null (uses default text).
 * @param negativeButtonText The text for the negative (Cancel) button. Defaults to null (uses default text).
 * @param tag The fragment tag for the picker dialog. Defaults to "SINGLE_DATE_PICKER".
 * @param onDateSelected A callback invoked with the selected date in milliseconds since epoch.
 *
 * **Example**:
 * ```kotlin
 * supportFragmentManager.showSingleDatePicker(
 *     title = "Select a Date",
 *     theme = R.style.CustomDatePickerTheme,
 *     initialSelection = System.currentTimeMillis(),
 *     positiveButtonText = "Confirm"
 * ) { selectedDate ->
 *     val dateString = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date(selectedDate))
 *     Toast.makeText(this, "Selected: $dateString", Toast.LENGTH_SHORT).show()
 * }
 * ```
 */
fun FragmentManager.showSingleDatePicker(
    title: String? = null,
    theme: Int = 0,
    minDate: Long? = null,
    maxDate: Long? = null,
    openAt: Long? = null,
    firstDayOfWeek: Int? = null,
    initialSelection: Long = MaterialDatePicker.todayInUtcMilliseconds(),
    validator: CalendarConstraints.DateValidator? = null,
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    tag: String = "SINGLE_DATE_PICKER",
    onDateSelected: (Long) -> Unit
) {
    val builder = MaterialDatePicker.Builder.datePicker().apply {
        setSelection(initialSelection)
        title?.let { setTitleText(it) }
        if (theme != 0) {
            setTheme(theme)
        }

        val constraintsBuilder = CalendarConstraints.Builder().apply {
            minDate?.let { setStart(it) }
            maxDate?.let { setEnd(it) }
            openAt?.let { setOpenAt(it) }
            firstDayOfWeek?.let { setFirstDayOfWeek(it) }
            validator?.let { setValidator(it) }
        }
        setCalendarConstraints(constraintsBuilder.build())

        positiveButtonText?.let { setPositiveButtonText(it) }
        negativeButtonText?.let { setNegativeButtonText(it) }
    }

    val picker = builder.build()
    picker.addOnPositiveButtonClickListener { selection ->
        onDateSelected(selection)
    }
    picker.show(this, tag)
}

/**
 * Displays a date range picker dialog using MaterialDatePicker with Material 3 styling.
 *
 * @param title The title of the date range picker dialog. Defaults to null (no title).
 * @param theme The resource ID of a custom theme for the picker, e.g., R.style.CustomDatePickerTheme.
 *             Must inherit from Theme.Material3.* or Theme.MaterialComponents.*. Defaults to 0 (uses app theme).
 * @param minDate The minimum selectable date in milliseconds since epoch. Defaults to null (no minimum).
 * @param maxDate The maximum selectable date in milliseconds since epoch. Defaults to null (no maximum).
 * @param openAt The date to open the calendar at, in milliseconds since epoch. Defaults to null (uses initial selection or today).
 * @param firstDayOfWeek The first day of the week (e.g., Calendar.SUNDAY, Calendar.MONDAY). Defaults to null (uses system default).
 * @param initialSelection A pair of start and end dates in milliseconds since epoch. Defaults to the current month start to today.
 * @param validator A custom validator for selectable dates. Defaults to null (no validator).
 * @param positiveButtonText The text for the positive (OK) button. Defaults to null (uses default text).
 * @param negativeButtonText The text for the negative (Cancel) button. Defaults to null (uses default text).
 * @param tag The fragment tag for the picker dialog. Defaults to "DATE_RANGE_PICKER".
 * @param onDateRangeSelected A callback invoked with the selected start and end dates in milliseconds since epoch.
 *
 * **Example**:
 * ```kotlin
 * supportFragmentManager.showDateRangePicker(
 *     title = "Select Date Range",
 *     theme = R.style.CustomDatePickerTheme,
 *     initialSelection = Pair(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L, System.currentTimeMillis()),
 *     positiveButtonText = "Confirm"
 * ) { startDate, endDate ->
 *     val start = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date(startDate))
 *     val end = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date(endDate))
 *     Toast.makeText(this, "Range: $start to $end", Toast.LENGTH_SHORT).show()
 * }
 * ```
 */
fun FragmentManager.showDateRangePicker(
    title: String? = null,
    theme: Int = 0,
    minDate: Long? = null,
    maxDate: Long? = null,
    openAt: Long? = null,
    firstDayOfWeek: Int? = null,
    initialSelection: Pair<Long, Long> = Pair(
        MaterialDatePicker.thisMonthInUtcMilliseconds(),
        MaterialDatePicker.todayInUtcMilliseconds()
    ),
    validator: CalendarConstraints.DateValidator? = null,
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    tag: String = "DATE_RANGE_PICKER",
    onDateRangeSelected: (startDate: Long, endDate: Long) -> Unit
) {
    val builder = MaterialDatePicker.Builder.dateRangePicker().setSelection(initialSelection)

    title?.let { builder.setTitleText(it) }
    if (theme != 0) {
        builder.setTheme(theme)
    }

    val constraintsBuilder = CalendarConstraints.Builder().apply {
        minDate?.let { setStart(it) }
        maxDate?.let { setEnd(it) }
        openAt?.let { setOpenAt(it) }
        firstDayOfWeek?.let { setFirstDayOfWeek(it) }
        validator?.let { setValidator(it) }
        positiveButtonText?.let { builder.setPositiveButtonText(it) }
        negativeButtonText?.let { builder.setNegativeButtonText(it) }
    }
    builder.setCalendarConstraints(constraintsBuilder.build())

    val picker = builder.build()
    picker.addOnPositiveButtonClickListener { selection ->
        onDateRangeSelected(selection.first, selection.second)
    }
    picker.show(this, tag)
}

/**
 * Displays a time picker dialog using MaterialTimePicker with Material 3 styling.
 *
 * @param title The title of the time picker dialog. Defaults to null (no title).
 * @param theme The resource ID of a custom theme for the picker, e.g., R.style.CustomTimePickerTheme.
 *             Must inherit from Theme.Material3.* or Theme.MaterialComponents.*. Defaults to 0 (uses app theme).
 * @param hour The initial hour (0-23). Defaults to the current hour.
 * @param minute The initial minute (0-59). Defaults to the current minute.
 * @param is24Hour Whether to use 24-hour format. Defaults to false (12-hour format with AM/PM).
 * @param positiveButtonText The text for the positive (OK) button. Defaults to null (uses default text).
 * @param negativeButtonText The text for the negative (Cancel) button. Defaults to null (uses default text).
 * @param tag The fragment tag for the picker dialog. Defaults to "TIME_PICKER".
 * @param onTimeSelected A callback invoked with the selected hour (0-23) and minute (0-59).
 *
 * **Example**:
 * ```kotlin
 * supportFragmentManager.showTimePicker(
 *     title = "Select Time",
 *     theme = R.style.CustomTimePickerTheme,
 *     hour = 14,
 *     minute = 30,
 *     is24Hour = true,
 *     positiveButtonText = "Confirm"
 * ) { hour, minute ->
 *     Toast.makeText(this, "Selected: $hour:$minute", Toast.LENGTH_SHORT).show()
 * }
 * ```
 */
fun FragmentManager.showTimePicker(
    title: String? = null,
    theme: Int = 0,
    hour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    minute: Int = Calendar.getInstance().get(Calendar.MINUTE),
    is24Hour: Boolean = true,
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    tag: String = "TIME_PICKER",
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    val builder = MaterialTimePicker.Builder()
        .setTimeFormat(if (is24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
        .setHour(hour)
        .setMinute(minute)

    title?.let { builder.setTitleText(it) }
    if (theme != 0) {
        builder.setTheme(theme)
    }

    positiveButtonText?.let { builder.setPositiveButtonText(it) }
    negativeButtonText?.let { builder.setNegativeButtonText(it) }

    val picker = builder.build()
    picker.addOnPositiveButtonClickListener {
        onTimeSelected(picker.hour, picker.minute)
    }
    picker.show(this, tag)
}