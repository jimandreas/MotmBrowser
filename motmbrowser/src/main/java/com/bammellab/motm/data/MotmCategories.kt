/*
 * Copyright (C) 2016-2018 James Andreas
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
 * limitations under the License
 */

package com.bammellab.motm.data

import com.bammellab.motm.MotmSection
import java.util.*

class MotmCategories {

    private val categoryList: MutableMap<MotmSection, String> = EnumMap(com.bammellab.motm.MotmSection::class.java)

    init {
        categoryList[MotmSection.FRAG_SECTION_HEALTH] = "Health and Disease"
        categoryList[MotmSection.FRAG_SECTION_LIFE] = "Life"
        categoryList[MotmSection.FRAG_SECTION_BIOTEC] = "Biotech/Nanotech"
        categoryList[MotmSection.FRAG_SECTION_STRUCTURES] = "Structures"
    }

}
