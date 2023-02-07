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

import android.surfaceflinger.Layerstrace
import com.google.common.truth.Truth
import java.io.File
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

/**
 * Contains [LayersTraceMonitor] tests. To run this test: `atest
 * FlickerLibTest:LayersTraceMonitorTest`
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LayersTraceMonitorTest : TraceMonitorTest<LayersTraceMonitor>() {
    override fun getMonitor(outputDir: File) = LayersTraceMonitor(outputDir)

    override fun assertTrace(traceData: ByteArray) {
        val trace = Layerstrace.LayersTraceFileProto.parseFrom(traceData)
        Truth.assertThat(trace.magicNumber)
            .isEqualTo(
                Layerstrace.LayersTraceFileProto.MagicNumber.MAGIC_NUMBER_H.number.toLong() shl
                    32 or
                    Layerstrace.LayersTraceFileProto.MagicNumber.MAGIC_NUMBER_L.number.toLong()
            )
    }

    @Test
    fun withSFTracing() {
        val trace = withSFTracing {
            device.pressHome()
            device.pressRecentApps()
        }

        Truth.assertWithMessage("Could not obtain SF trace").that(trace.entries).isNotEmpty()
    }
}
