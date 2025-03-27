package com.lambao.base.extension

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import java.io.Serializable

/**
 * Extension function to retrieve a Serializable from a Bundle with type safety and
 * backward compatibility.
 *
 * @param key The key associated with the Serializable in the Bundle.
 * @return The Serializable object if found, null otherwise.
 * @throws ClassCastException if the object under the key is not an instance of [T].
 */
inline fun <reified T : Serializable> Bundle.getSerializableCompat(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

/**
 * Extension function to retrieve a Parcelable from a Bundle with type safety and
 * backward compatibility.
 *
 * @param key The key associated with the Parcelable in the Bundle.
 * @return The Parcelable object if found, null otherwise.
 * @throws ClassCastException if the object under the key is not an instance of [T].
 */
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

/**
 * Extension function to retrieve a List of Parcelables from a Bundle with type safety and
 * backward compatibility.
 *
 * @param key The key associated with the List in the Bundle.
 * @return The List of Parcelables if found, null otherwise.
 * @throws ClassCastException if any object in the List is not an instance of [T].
 */
inline fun <reified T : Parcelable> Bundle.getParcelableListCompat(key: String): List<T>? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArrayList(
        key,
        T::class.java
    )

    else -> @Suppress("DEPRECATION") getParcelableArrayList<T>(key)
}

/**
 * Extension function to retrieve a SparseArray of Parcelables from a Bundle with
 * type safety and backward compatibility.
 *
 * @param key The key associated with the SparseArray in the Bundle.
 * @return The SparseArray of Parcelables if found, null otherwise.
 * @throws ClassCastException if any object in the SparseArray is not an instance of [T].
 */
inline fun <reified T : Parcelable> Bundle.getSparseParcelableArrayCompat(key: String): SparseArray<T>? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSparseParcelableArray(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getSparseParcelableArray<T>(key)
    }