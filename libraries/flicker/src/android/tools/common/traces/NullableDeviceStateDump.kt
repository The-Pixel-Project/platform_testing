/*
 * Copyright (C) 2023 The Android Open Source Project
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

package android.tools.common.traces

import android.tools.common.traces.surfaceflinger.LayerTraceEntry
import android.tools.common.traces.wm.WindowManagerState

/**
 * Represents a state dump optionally containing the [WindowManagerState] and the [LayerTraceEntry]
 * parsed.
 */
open class NullableDeviceStateDump(
    /** Parsed [WindowManagerState] */
    open val wmState: WindowManagerState?,

    /** Parsed [LayerTraceEntry] */
    open val layerState: LayerTraceEntry?
)
