/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.server.wm.flicker.monitor

import com.android.server.wm.nano.WindowManagerTraceFileProto
import com.google.common.truth.Truth
import java.io.File
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

/**
 * Contains [WindowManagerTraceMonitor] tests. To run this test: `atest
 * FlickerLibTest:LayersTraceMonitorTest`
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class WindowManagerTraceMonitorTest : TraceMonitorTest<WindowManagerTraceMonitor>() {
    override fun getMonitor(outputDir: File) = WindowManagerTraceMonitor(outputDir)

    override fun assertTrace(traceData: ByteArray) {
        val trace = WindowManagerTraceFileProto.parseFrom(traceData)
        Truth.assertThat(trace.magicNumber)
            .isEqualTo(
                WindowManagerTraceFileProto.MAGIC_NUMBER_H.toLong() shl
                    32 or
                    WindowManagerTraceFileProto.MAGIC_NUMBER_L.toLong()
            )
    }

    @Test
    fun withWMTracing() {
        val trace = withWMTracing {
            device.pressHome()
            device.pressRecentApps()
        }

        Truth.assertWithMessage("Could not obtain WM trace").that(trace.entries).isNotEmpty()
    }
}
