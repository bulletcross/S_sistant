package com.enterprise.bulletcross.assistant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

/**
 * Service class constantly seeks for importance detection
 * Once importance is detected, service changes state
 * and constanly seeks for vicinity detection.
 * Vicinity is detected until interacted.
 * State 0 -> importance detection state (default)
 * State 1 -> Vicinity detection state
 * State 2 -> No detection state
 * State 3 -> Notify state
 *
 * State Change Flow
 * State 0 -> [interaction] -> State 2
 * State 0 -> [no interaction + importance is true] -> State 1
 * State 1 -> [interaction] -> State 2
 * State 1 -> [no interaction + vicinity is true] -> State 3
 * State 2 -> [no interaction] -> State 0
 * State 3 -> [interaction] -> State 0
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
