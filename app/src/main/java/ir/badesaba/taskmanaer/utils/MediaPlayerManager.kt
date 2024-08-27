package ir.badesaba.taskmanaer.utils

import android.media.MediaPlayer

object MediaPlayerManager {
    private val mediaPlayers = mutableListOf<MediaPlayer>()

    fun addMediaPlayer(player: MediaPlayer) {
        mediaPlayers.add(player)
    }

    fun stopAndReleaseAll() {
        for (player in mediaPlayers) {
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        mediaPlayers.clear()
    }
}