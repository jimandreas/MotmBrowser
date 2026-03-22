/*
 *  Copyright 2021 James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.kotmol.pdbParser

/**
 * Implemented by each mmCIF category handler.
 *
 * When [MmCifParser] encounters a `loop_` block whose first column belongs
 * to a registered category, it delegates to the matching handler via this interface.
 *
 * @param columnHeaders ordered list of (category, attribute) pairs from the loop_ header
 * @param tokenizer the shared tokenizer — the handler must consume all value rows and
 *   push back any non-value sentinel token it discovers at the end of the loop
 */
interface MmCifLoopHandler {
    fun handleLoop(
        columnHeaders: List<Pair<String, String>>,
        tokenizer: MmCifTokenizer
    )
}
