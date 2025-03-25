package com.lambao.base.extension

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray

/**
 * Extension function to retrieve a Parcelable from a Bundle with type safety and
 * backward compatibility.
 *
 * @param key The key associated with the Parcelable in the Bundle.
 * @param clazz The class of the expected Parcelable.
 * @return The Parcelable object if found, null otherwise.
 * @throws ClassCastException if the object under the key is not an instance of [clazz].
 */
fun <T : Parcelable> Bundle.getParcelableCompat(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getParcelable(key) as? T
    }
}

/**
 * Extension function to retrieve a List of Parcelables from a Bundle with type safety and
 * backward compatibility.
 *
 * @param key The key associated with the List in the Bundle.
 * @param clazz The class of the expected Parcelables in the List.
 * @return The List of Parcelables if found, null otherwise.
 * @throws ClassCastException if any object in the List is not an instance of [clazz].
 */
fun <T : Parcelable> Bundle.getParcelableListCompat(
    key: String,
    clazz: Class<T>
): List<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayList(key, clazz)
    } else {
        getParcelableArrayList<T>(key)
    }
}

/**
 * Extension function to retrieve a SparseArray of Parcelables from a Bundle with
 * type safety and backward compatibility.
 *
 * @param key The key associated with the SparseArray in the Bundle.
 * @param clazz The class of the expected Parcelables in the SparseArray.
 * @return The SparseArray of Parcelables if found, null otherwise.
 * @throws ClassCastException if any object in the SparseArray is not an instance of [clazz].
 */
fun <T : Parcelable> Bundle.getSparseParcelableArrayCompat(
    key: String,
    clazz: Class<T>
): SparseArray<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSparseParcelableArray(key, clazz)
    } else {
        getSparseParcelableArray<T>(key)
    }
}