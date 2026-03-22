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

import java.io.InputStream

/**
 * Auto-detecting factory that routes an [InputStream] to the correct parser.
 *
 * Uses [MmCifFormatDetector] to peek at the first bytes of the stream.
 * If the content looks like an mmCIF file (`data_` at the start of a line),
 * [ParserMmCifFile] is used; otherwise [ParserPdbFile] is used.
 *
 * The stream is reconstituted after detection so the parser receives the full content.
 *
 * Example:
 * ```kotlin
 * MoleculeParserFactory.parse(mol, inputStream, "4HHB")
 * ```
 */
object MoleculeParserFactory {

    fun parse(
        mol: Molecule,
        stream: InputStream,
        name: String = "",
        messages: MutableList<String> = mutableListOf()
    ) {
        val (isMmCif, reconstituted) = MmCifFormatDetector.detect(stream)

        if (isMmCif) {
            ParserMmCifFile
                .Builder(mol)
                .setMoleculeName(name)
                .setMessageStrings(messages)
                .loadMmCifFromStream(reconstituted)
                .doBondProcessing(true)
                .parse()
        } else {
            ParserPdbFile
                .Builder(mol)
                .setMoleculeName(name)
                .setMessageStrings(messages)
                .loadPdbFromStream(reconstituted)
                .doBondProcessing(true)
                .parse()
        }

        reconstituted.close()
    }
}
