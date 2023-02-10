/*
 * Copyright (C) 2022 The Android Open Source Project
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

package com.android.server.wm.flicker.assertions

import com.android.server.wm.traces.common.AssertionTag
import kotlin.reflect.KClass

/**
 * Helper class to create assertions to execute on a state
 *
 * @param stateSubject Type of subject used for state assertions
 */
open class AssertionStateDataFactory(private val stateSubject: KClass<out FlickerSubject>) {
    /**
     * Creates an [assertion] to be executed on the initial state of a trace
     *
     * @param assertion Assertion predicate
     */
    fun createStartStateAssertion(assertion: FlickerSubject.() -> Unit) =
        AssertionData(
            tag = AssertionTag.START,
            expectedSubjectClass = stateSubject,
            assertion = assertion
        )

    /**
     * Creates an [assertion] to be executed on the final state of a trace
     *
     * @param assertion Assertion predicate
     */
    fun createEndStateAssertion(assertion: FlickerSubject.() -> Unit) =
        AssertionData(
            tag = AssertionTag.END,
            expectedSubjectClass = stateSubject,
            assertion = assertion
        )

    /**
     * Creates an [assertion] to be executed on a user defined moment ([tag]) of a trace
     *
     * @param assertion Assertion predicate
     */
    fun createTagAssertion(tag: String, assertion: FlickerSubject.() -> Unit) =
        AssertionData(tag = tag, expectedSubjectClass = stateSubject, assertion = assertion)
}
