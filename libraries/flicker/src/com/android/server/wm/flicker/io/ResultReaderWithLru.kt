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

package com.android.server.wm.flicker.io

import androidx.collection.LruCache
import com.android.server.wm.flicker.TraceConfigs
import com.android.server.wm.traces.common.Timestamp
import com.android.server.wm.traces.common.events.EventLog
import com.android.server.wm.traces.common.io.IReader
import com.android.server.wm.traces.common.io.ResultArtifactDescriptor
import com.android.server.wm.traces.common.io.TraceType
import com.android.server.wm.traces.common.io.TransitionTimeRange
import com.android.server.wm.traces.common.layers.LayersTrace
import com.android.server.wm.traces.common.windowmanager.WindowManagerTrace
import java.io.IOException

/**
 * Helper class to read results from a flicker artifact using a LRU
 *
 * @param result to read from
 * @param traceConfig
 */
open class ResultReaderWithLru(
    result: IResultData,
    traceConfig: TraceConfigs,
    private val reader: ResultReader = ResultReader(result, traceConfig)
) : IReader by reader {
    /** {@inheritDoc} */
    @Throws(IOException::class)
    override fun readWmTrace(): WindowManagerTrace? {
        val descriptor = ResultArtifactDescriptor(TraceType.SF)
        val key = CacheKey(reader.artifactPath, descriptor, reader.transitionTimeRange)
        val trace = wmTraceCache[key] ?: reader.readWmTrace()
        return trace?.also { wmTraceCache.put(key, trace) }
    }

    /** {@inheritDoc} */
    @Throws(IOException::class)
    override fun readLayersTrace(): LayersTrace? {
        val descriptor = ResultArtifactDescriptor(TraceType.SF)
        val key = CacheKey(reader.artifactPath, descriptor, reader.transitionTimeRange)
        val trace = layersTraceCache[key] ?: reader.readLayersTrace()
        return trace?.also { layersTraceCache.put(key, trace) }
    }

    /** {@inheritDoc} */
    @Throws(IOException::class)
    override fun readEventLogTrace(): EventLog? {
        val descriptor = ResultArtifactDescriptor(TraceType.EVENT_LOG)
        val key = CacheKey(reader.artifactPath, descriptor, reader.transitionTimeRange)
        val trace = eventLogCache[key] ?: reader.readEventLogTrace()
        return trace?.also { eventLogCache.put(key, trace) }
    }

    /** {@inheritDoc} */
    override fun slice(startTimestamp: Timestamp, endTimestamp: Timestamp): ResultReaderWithLru {
        val slicedReader = reader.slice(startTimestamp, endTimestamp)
        return ResultReaderWithLru(slicedReader.result, slicedReader.traceConfig, slicedReader)
    }

    companion object {
        data class CacheKey(
            private val artifact: String,
            private val descriptor: ResultArtifactDescriptor,
            private val transitionTimeRange: TransitionTimeRange
        )

        private val wmTraceCache = LruCache<CacheKey, WindowManagerTrace>(1)
        private val layersTraceCache = LruCache<CacheKey, LayersTrace>(1)
        private val eventLogCache = LruCache<CacheKey, EventLog>(1)
    }
}
