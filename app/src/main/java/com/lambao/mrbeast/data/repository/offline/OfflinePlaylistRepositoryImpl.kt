package com.lambao.mrbeast.data.repository.offline

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.lambao.base.data.Resource
import com.lambao.base.data.local.BaseLocalDataSource
import com.lambao.base.data.local.LocalDataException
import com.lambao.base.data.local.LocalErrorType
import com.lambao.mrbeast.data.local.model.SongLocalDto
import com.lambao.mrbeast.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflinePlaylistRepositoryImpl @Inject constructor(
    private val context: Context,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseLocalDataSource(ioDispatcher), OfflinePlaylistRepository {

    override fun getPlaylist(): Flow<Resource<List<SongLocalDto>>> = safeCall {
        fetchLocalSongs()
    }

    private fun fetchLocalSongs(): List<SongLocalDto> {
        val songList = mutableListOf<SongLocalDto>()
        val contentResolver = context.contentResolver

        val audioProjection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            audioProjection,
            MediaStore.Audio.Media.IS_MUSIC + " != 0",
            null,
            MediaStore.Audio.Media.TITLE + " ASC"
        ) ?: throw LocalDataException(
            type = LocalErrorType.STORAGE_UNAVAILABLE,
            message = "Unable to access media store"
        )

        cursor.use { c ->
            val idColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val pathColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val sizeColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val albumIdColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (c.moveToNext()) {
                val albumId = c.getLong(albumIdColumn)
                val thumbnailUri = getAlbumArtUri(albumId)

                val song = SongLocalDto(
                    id = c.getLong(idColumn).toString(),
                    title = c.getString(titleColumn),
                    artist = c.getString(artistColumn),
                    album = c.getString(albumColumn),
                    duration = c.getLong(durationColumn).takeIf { it > 0 },
                    filePath = c.getString(pathColumn),
                    fileSize = c.getLong(sizeColumn).takeIf { it > 0 },
                    thumbnail = thumbnailUri
                )
                songList.add(song)
            }
        }

        return songList
    }

    private fun getAlbumArtUri(albumId: Long): Uri? {
        return try {
            val albumUri = ContentUris.withAppendedId(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                albumId
            )
            // Không cần load thumbnail ngay, chỉ trả về Uri
            // ContentResolver.loadThumbnail() sẽ được gọi khi cần hiển thị
            albumUri
        } catch (e: Exception) {
            e.printStackTrace()
            null // Trả về null nếu không lấy được Uri
        }
    }
}