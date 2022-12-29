/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
* limitations under the License.
 */
package com.example.exoplayer

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import com.example.exoplayer.databinding.ActivityPlayerBinding

/**
 * A fullscreen activity to play audio or video streams.
 */
class PlayerActivity : AppCompatActivity() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private var player: ExoPlayer? = null
    private var playbackPosition = 0L
    private var currentItem = 0
    private var playWhenReady = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                viewBinding.videoView.player = exoPlayer

                val mediaItem = MediaItem.fromUri(getString(R.string.media_url_mp4))
                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.prepare() // acquire all the resources required for playback.
            }
    }

    override fun onStart() {
        super.onStart()
        // above API level 24, supports multiple windows
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        // Android API level 23 and lower requires you to wait as long as possible until
        // you grab resources, so you wait until onResume before initializing the player
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        // With API level 23 and lower, there is no guarantee of onStop being called,
        // so you have to release the player as early as possible in onPause
        if(Util.SDK_INT<=23){
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        // With API Level 24 and higher, onStop is guaranteed to be called.
        // In the paused state, your activity is still visible, so you wait to release the player until onStop.
        if(Util.SDK_INT > 23){
            releasePlayer()
        }
    }

    private fun releasePlayer(){
        player?.let{ exoPlayer ->
            playbackPosition = exoPlayer.currentPosition // current playback position
            currentItem = exoPlayer.currentMediaItemIndex // current media item index
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}